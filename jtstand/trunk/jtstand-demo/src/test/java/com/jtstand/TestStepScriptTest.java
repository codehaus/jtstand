/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import junit.framework.TestCase;

public class TestStepScriptTest extends TestCase {

    public static final String GROOVY_SCRIPT = "Foo f = new Foo(); f.getResult() + localvar + globalvar";
    public static final String JS_SCRIPT = "var f = new Packages.Foo(); f.getResult() + localvar + globalvar";
    public static final String FOO_CLASS = "class Foo { int getResult() { return 1; } } ; Foo.class";
    public static final String FOO = "class Foo { int getResult() { return 1; } }";
    public static final String I = "Integer i = 1";
    public static final String I1 = "i = new Integer(1)";
    public static final String FOOBAR = "class Foo { int getResult() { return 1; } } ; class Bar extends Foo { int getResult() { return super.getResult() + 1; } } ; Bar b = new Bar(); b.getResult()";
    public static final String MY_CLOSURE = "{it -> it + 1.0}";

    public void testBinding() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        Bindings bindings = new SimpleBindings();
        engine.eval(I1, bindings);
        assertEquals(new Integer(1), bindings.get("i"));
    }

    public void testMyClosure() throws ScriptException {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("groovy");
        assertEquals(2.0, ((Closure) engine.eval(MY_CLOSURE)).call(1.0));
    }

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
    public static ClassLoader cl = new ClassLoader() {
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
}
