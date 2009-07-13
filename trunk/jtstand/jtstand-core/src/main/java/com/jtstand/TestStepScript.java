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

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 *
 * @author albert_kurucz
 */
@Entity
public class TestStepScript extends FileRevisionReference implements Serializable {

    private static final long serialVersionUID = 1L;
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

    public void execute(TestStepInstance step) throws Exception {
        if (getInterpreter() == null) {
//            System.out.println("executing groovy script:\n" + getCode());
            step.runGroovyScript("def propertyMissing(String name){step.getVariable(name)};def setVariable={String name,value->delegate.step.setVariable(name,value)};def setValue={delegate.step.setValue(it)};"+getFileContent());
        } else {
            Object o = null;
            try {
                o = step.getVariable(getInterpreter());
            } catch (Exception ex) {
                Class<?> stepClass = Class.forName(step.getPropertyString(getInterpreter(), getInterpreter()));
                Constructor<?> stepObjectContructor = stepClass.getConstructor(TestStepInstance.NULL_CONSTRUCTOR);
                o = stepObjectContructor.newInstance();
                ((Interpreter) o).eval(getFileContent(), step);
                disposeOrClose(o);
            }
            ((Interpreter) o).eval(getFileContent(), step);
        }
    }

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

//    public static void main(String[] args) {
//        ClassLoader cl = TestStepScript.class.getClassLoader();
//        String bar = "package com.bar;public class Bar { void hello(){println \"hello Bar\"}}";
//        GroovyClassLoader gcl = new GroovyClassLoader(cl);
//        gcl.parseClass(bar);
//        cl = gcl;
//        String foo = "public class Foo extends com.bar.Bar { void fello(){println \"hello Foo\"}}";
//        GroovyClassLoader gcl2 = new GroovyClassLoader(cl);
//        gcl2.parseClass(foo);
//        cl = gcl2;
//        String scriptText = "(new Foo()).hello()\n(new Foo()).fello()";
//        (new GroovyShell(new GroovyClassLoader(cl))).parse(scriptText).run();
//    }
}
