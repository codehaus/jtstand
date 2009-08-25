/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import junit.framework.TestCase;

public class TestStepScriptTest extends TestCase {

    public static final String FOO = "class Foo  { int getResult() { return 1 } }";
    public static final String GROOVY_SCRIPT = "Foo f = new Foo(); f.getResult() + r";
    public static final String JS_SCRIPT = "var f = new Packages.Foo(); f.getResult() + r";
    public static final String RUBY_SCRIPT = "require 'java' \n include_class 'Foo' \n f = Foo.new \n r + f.getResult()";

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
        assertEquals(2, (new GroovyShell(gcl, binding)).parse(GROOVY_SCRIPT).run());
    }

    public void testListEngines() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager(gcl);
        for (ScriptEngineFactory sef : factory.getEngineFactories()) {
            System.out.println("engine name: " + sef.getEngineName());
            for (String name : sef.getNames()) {
                System.out.println("name: " + name);
            }
        }
    }

    public void testClassLoaderJSR223Groovy() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        Thread.currentThread().setContextClassLoader(gcl);

        ScriptEngine engine = factory.getEngineByName("groovy");
        engine.put("r", 1);
        assertEquals(2, engine.eval(GROOVY_SCRIPT));
    }

    public void testClassLoaderJSR223Js() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        Thread.currentThread().setContextClassLoader(gcl);

        ScriptEngine engine = factory.getEngineByName("js");
        engine.put("r", 1);
        assertEquals(2.0, engine.eval(JS_SCRIPT));
    }

//    public void testClassLoaderJSR223Python() throws ScriptException {
//        ScriptEngineManager factory = new ScriptEngineManager();
//        Thread.currentThread().setContextClassLoader(gcl);
//
//        ScriptEngine engine = factory.getEngineByName("python");
//        engine.put("r", 1);
//        assertEquals(2, engine.eval(GROOVY_SCRIPT));
//    }
//
    public void testClassLoaderJSR223Jruby() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        Thread.currentThread().setContextClassLoader(gcl);

        ScriptEngine engine = factory.getEngineByName("jruby");
        engine.put("r", 1);
        assertEquals(2, engine.eval(RUBY_SCRIPT));
    }
}
