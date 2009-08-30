/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.GroovyClassLoader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import junit.framework.TestCase;

public class TestStepScriptTest extends TestCase {

    public static final String GROOVY_SCRIPT = "Foo f = new Foo(); f.getResult() + localvar + globalvar";
    public static final String JS_SCRIPT = "var f = new Packages.Foo(); f.getResult() + localvar + globalvar";
    public static final String RUBY_SCRIPT = "require 'java' \n include_class 'Foo' \n f = Foo.new \n f.getResult() + localvar + globalvar";
    public static final String FOO_CLASS = "class Foo { int getResult() { return 1; } } ; Foo.class";
    public static final String FOO = "class Foo { int getResult() { return 1; } }";
    public static final String I = "Integer i = 1";
    public static final String FOOBAR = "class Foo { int getResult() { return 1; } } ; class Bar extends Foo { int getResult() { return super.getResult() + 1; } } ; Bar b = new Bar(); b.getResult()";
    public static final String BAR = "class Foo { int getResult() { return 1; } } ; class Bar extends Foo { int getResult() { return super.getResult() + 1; } }";
    public static final String MY_THREAD = "class MyThread extends Thread { void run() { System.out.println(\"Hi there!\") } } ";

    public void testMyThread() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine1 = factory.getEngineByName("groovy");
        ScriptEngine engine2 = factory.getEngineByName("groovy");
        assertNotSame(engine1, engine2);
    }

    public void testFooBar() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        assertEquals(2, engine.eval(FOOBAR));
    }

    public void testBar() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        //assertEquals("Bar", ((Class) engine.eval(BAR)).getCanonicalName());
    }

    public void testI() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        assertEquals(1, engine.eval(I));
    }

    public void testClassEvalVerbose() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        assertEquals("Foo", ((Class) engine.eval(FOO_CLASS)).getCanonicalName());
    }

    public void testClassEvalBrief() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        //assertEquals("Foo", ((Class) engine.eval(FOO)).getCanonicalName());
    }
    public static GroovyClassLoader gcl = new GroovyClassLoader(Thread.currentThread().getContextClassLoader()) {

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if ("Foo".equals(name)) {
                return parseClass(FOO);
            }
            return super.findClass(name);
        }
    };

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
        factory.put("globalvar", 1);
        Thread.currentThread().setContextClassLoader(gcl);
        ScriptEngine engine = factory.getEngineByName("groovy");
        engine.put("localvar", 1);
        assertEquals(3, engine.eval(GROOVY_SCRIPT));
    }

    public void testClassLoaderJSR223Js() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        factory.put("globalvar", 1);
        Thread.currentThread().setContextClassLoader(gcl);
        ScriptEngine engine = factory.getEngineByName("js");
        engine.put("localvar", 1);
        assertEquals(3.0, engine.eval(JS_SCRIPT));
    }
//    public void testClassLoaderJSR223Python() throws ScriptException {
//        ScriptEngineManager factory = new ScriptEngineManager();
//        factory.put("globalvar", 1);
//        Thread.currentThread().setContextClassLoader(gcl);
//        ScriptEngine engine = factory.getEngineByName("python");
//        engine.put("localvar", 1);
//        assertEquals(3, engine.eval(GROOVY_SCRIPT));
//    }
//
//    public void testClassLoaderJSR223Jruby() throws ScriptException {
//        ScriptEngineManager factory = new ScriptEngineManager();
//        factory.put("globalvar", 1);
//        Thread.currentThread().setContextClassLoader(gcl);
//        ScriptEngine engine = factory.getEngineByName("jruby");
//        engine.put("localvar", 1);
//        assertEquals(3, engine.eval(RUBY_SCRIPT));
//    }
}
