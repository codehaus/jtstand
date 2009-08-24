/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import java.lang.reflect.InvocationTargetException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import junit.framework.TestCase;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

public class TestStepScriptTest extends TestCase {

    public static final String FOO = "class Foo  { int getResult() { return 1 } }";
    public static final String SCRIPT = "Foo f = new Foo(); f.getResult() + r";
    public static GroovyClassLoader gcl = new GroovyClassLoader(Thread.currentThread().getContextClassLoader()) {

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if ("Foo".equals(name)) {
                return parseClass(FOO);
            }
            return super.findClass(name);
        }
    };

    public void testClassLoaderGroovyShell() throws ScriptException {
        Binding binding = new Binding();
        binding.setVariable("r", 1);
        assertEquals(2, (new GroovyShell(gcl, binding)).parse(SCRIPT).run());
    }

    public void testClassLoaderBSF() throws BSFException {
        BSFManager manager = new BSFManager();
        manager.setClassLoader(gcl);
        manager.declareBean("r", 1, Integer.class);
        assertEquals(1, manager.lookupBean("r"));
        assertEquals(2, manager.eval("groovy", null, 0, 0, SCRIPT));
    }

    public void testClassLoaderJSR223() throws ScriptException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ScriptEngineManager factory = new ScriptEngineManager();
        Thread.currentThread().setContextClassLoader(gcl);
        ScriptEngine engine = factory.getEngineByName("groovy");
        engine.put("r", 1);
        assertEquals(2, engine.eval(SCRIPT));
    }
}
