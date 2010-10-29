/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TestStepScript.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.script.ScriptException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import org.tmatesoft.svn.core.SVNException;

/**
 *
 * @author albert_kurucz
 */
@Entity
public class TestStepScript extends FileRevisionReference {

    @OneToOne
    private TestStep testStep;
    private String interpreter;

    @XmlTransient
    public TestStep getTestStep() {
        return testStep;
    }

    public void setTestStep(TestStep testStep) {
        this.testStep = testStep;
        if (testStep != null) {
            setCreator(testStep.getCreator());
        }
    }

    @XmlAttribute
    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testStep != null ? testStep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStepScript)) {
            return false;
        }
        TestStepScript other = (TestStepScript) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testStep == null && other.getTestStep() != null) || (this.testStep != null && !this.testStep.equals(other.getTestStep()))) {
            return false;
        }
        return true;
    }
    public static final Class<?>[] EVAL_VOID = null;
    public static final Class<?>[] EVAL_STRING = {String.class};
    public static final Class<?>[] EVAL_STRING_BINDINGS = {String.class, Bindings.class};

    public Object execute(TestStepInstance step) throws ScriptException, URISyntaxException, SVNException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        if (null == getInterpreter()) {
            /* interpreter is not defined, use the default interpreter, which is groovy in our case! */
            return TestProject.getScriptEngineManager().getEngineByName("groovy").eval(getFileContent(), step);
        }
        Object myInterpreterObject = null;
        Class<?> classOfMyInterpreterObjects = null;
        try {
            myInterpreterObject = step.getVariable(getInterpreter());
            classOfMyInterpreterObjects = myInterpreterObject.getClass();
        } catch (Exception ex) {
            try {
                /* no variable with this name, lets try if it is a class */
                classOfMyInterpreterObjects = Thread.currentThread().getContextClassLoader().loadClass(getInterpreter());
            } catch (ClassNotFoundException ex1) {
                //System.out.println("Getting engine by name: " + getInterpreter() + "...");
                return TestProject.getScriptEngineManager().getEngineByName(getInterpreter()).eval(getFileContent(), step);
            }
        }
        if (ScriptEngine.class.isAssignableFrom(classOfMyInterpreterObjects)) {
            if (myInterpreterObject == null) {
                //System.out.println("Instantiating the engine...");
                myInterpreterObject = classOfMyInterpreterObjects.newInstance();
            }
            //System.out.println("Calling eval on engine...");
            return ((ScriptEngine) myInterpreterObject).eval(getFileContent(), step);
        }
        try {
            //System.out.println("try1...");
            return classOfMyInterpreterObjects.getMethod("eval", EVAL_STRING_BINDINGS).invoke(myInterpreterObject, getFileContent(), step);
        } catch (NoSuchMethodException ex) {
            try {
                //System.out.println("try2...");
                return classOfMyInterpreterObjects.getMethod("eval", EVAL_STRING).invoke(myInterpreterObject, getFileContent());
            } catch (NoSuchMethodException ex1) {
                //System.out.println("try3 (last one)...");
                return classOfMyInterpreterObjects.getMethod("eval", EVAL_VOID).invoke(myInterpreterObject);
            }
        }
    }

//    public void executeOld(TestStepInstance step) throws Exception {
//        if (getInterpreter() == null) {
////            System.out.println("executing groovy script:\n" + getCode());
//            step.runGroovyScript("def propertyMissing(String name){step.getVariable(name)};def setVariable={String name,value->delegate.step.setVariable(name,value)};def setValue={delegate.step.setValue(it)};" + getFileContent());
//
//        } else {
//            Object o = null;
//            try {
//                o = step.getVariable(getInterpreter());
//            } catch (Exception ex) {
//                Class<?> stepClass = Class.forName(step.getPropertyString(getInterpreter(), getInterpreter()));
//                Constructor<?> stepObjectContructor = stepClass.getConstructor(TestStepInstance.NULL_CONSTRUCTOR);
//                o = stepObjectContructor.newInstance();
//                ((Interpreter) o).eval(getFileContent(), step);
//                disposeOrClose(o);
//            }
//            ((Interpreter) o).eval(getFileContent(), step);
//        }
//    }
    public static boolean disposeOrClose(Object o) {
        return call(o, "dispose") ? true : call(o, "close");
    }

    public static boolean abort(Object o) {
        return call(o, "abort");
    }

    private static boolean call(Object o, String methodName) {
        Method[] allMethods = o.getClass().getMethods();
        for (Method m : allMethods) {
            String mname = m.getName();
            if (!mname.equals(methodName)) {
                continue;
            }
            Type[] pType = m.getGenericParameterTypes();
            if ((pType.length != 0)) {
                continue;
            }
            int mod = m.getModifiers();
            if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {
                try {
                    m.invoke(o, (Object[]) null);
                    return true;
                } catch (Exception ex) {
                    return false;
                }
            }
        }
        return false;
    }
}
