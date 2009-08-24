/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import junit.framework.TestCase;

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

    public void testClassLoaderJSR223() throws ScriptException{
        ScriptEngineManager factory = new ScriptEngineManager();
        Thread.currentThread().setContextClassLoader(gcl);
        ScriptEngine engine = factory.getEngineByName("groovy");
        engine.put("r", 1);
        assertEquals(2, engine.eval(SCRIPT));
    }
}
