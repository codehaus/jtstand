/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import javax.script.ScriptException;
import junit.framework.TestCase;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

public class TestStepScriptTest extends TestCase {

    public static final String FOO = "class Foo  { int getResult() { return 1 } }";
    public static final String SCRIPT = "Foo f = new Foo(); f.getResult()";
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
        assertEquals(1, (new GroovyShell(gcl)).parse(SCRIPT).run());
    }    

    public void testClassLoaderBSF() throws BSFException {
        BSFManager manager = new BSFManager();
        manager.setClassLoader(gcl);
        assertEquals(1, manager.eval("groovy", null, 0, 0, SCRIPT));
    }
}
