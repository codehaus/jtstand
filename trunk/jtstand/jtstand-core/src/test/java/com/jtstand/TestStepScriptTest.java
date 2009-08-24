/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import javax.script.ScriptException;
import junit.framework.TestCase;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.ObjectRegistry;

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
        BSFManager manager = new BSFManager()
//        {
//
//            @Override
//            public Object lookupBean(String name) {
//                if ("r".equals(name)) {
//                    return 1;
//                }
//                return super.lookupBean(name);
//            }
//        }
        ;
        manager.setClassLoader(gcl);
        manager.declareBean("r", 1, Integer.class);
        assertEquals(1, manager.lookupBean("r"));
        assertEquals(2, manager.eval("groovy", null, 0, 0, SCRIPT));
    }
}
