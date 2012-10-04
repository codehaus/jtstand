/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import org.jboss.logging.Logger;

/**
 *
 * @author albert
 */
class TestStepInstanceBindings implements Bindings {

    private static final Logger log = Logger.getLogger(TestStepInstanceBindings.class.getName());
    private transient TestStepInstance testStepInstance;
    private transient Map<String, Object> localVariablesMap = new HashMap<String, Object>();

    public TestStepInstanceBindings() {
        //testStepInstance = null;
    }

    public TestStepInstanceBindings(TestStepInstance testStepInstance) {
        this.testStepInstance = testStepInstance;
    }

    @Override
    public Object put(String key, Object variableValue) {
        System.err.println("put of Bindings is called with key: '" + key + "', value: '" + variableValue + "'");
        if ("value".equals(key)) {
            Object retval = testStepInstance.getValue();
            testStepInstance.setValue(variableValue);
            return retval;
        }
        for (TestStepProperty tsp : testStepInstance.getTestStep().getProperties()) {
            if (tsp.getName().equals(key)) {
                if ((tsp.isFinal() == null || tsp.isFinal()) && testStepInstance.containsKey(key)) {
                    throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                }
                return testStepInstance.put(key, variableValue);
            }
        }
        if (testStepInstance.getCalledTestStep() != null) {
            for (TestStepProperty tsp : testStepInstance.getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(key)) {
                    if ((tsp.isFinal() == null || tsp.isFinal()) && testStepInstance.containsKey(key)) {
                        throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                    }
                    return testStepInstance.put(key, variableValue);
                }
            }
        }
        if (testStepInstance.getParent() != null) {
            return testStepInstance.getParent().getBindings().put(key, variableValue);
        }
        TestSequenceInstance seq = testStepInstance.getTestSequenceInstance();
        if (seq != null) {
            if (seq.getTestFixture() != null) {
                for (TestFixtureProperty tsp : seq.getTestFixture().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.getTestFixture().containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.getTestFixture().put(key, variableValue);
                    }
                }
            }
            if (seq.getTestStation() != null) {
                for (TestStationProperty tsp : seq.getTestStation().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.getTestStation().containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.getTestStation().put(key, variableValue);
                    }
                }
            }
            if (seq.getTestProject() != null) {
                for (TestProjectProperty tsp : seq.getTestProject().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.getTestStation().containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.getTestStation().put(key, variableValue);
                    }
                }
            }
        }
        return localVariablesMap.put(key, variableValue);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        for (Entry<? extends String, ? extends Object> variableEntry : toMerge.entrySet()) {
            put(variableEntry.getKey(), variableEntry.getValue());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        System.err.println("containsKey of Bindings is called with key: '" + key + "'");

        return testStepInstance.containsKey(key.toString())
                || "value".equals(key)
                || "step".equals(key)
                || localVariablesMap.containsKey(key.toString())
                || testStepInstance.containsProperty(key.toString());
    }

    @Override
    public Object get(Object key) {
        Object retval = get1(key);
        if ("VAR1".equals(key)) {
            System.err.println("get of Bindings is returning value:" + retval);
        }
        return retval;
    }

    //@Override
    public Object get1(Object key) {
        System.err.println("get of Bindings is called with key: '" + key + "'...");
        if ("context".equals(key)) {
            return ScriptContext.ENGINE_SCOPE;
        }
        if ("step".equals(key)) {
            return testStepInstance;
        }
        if ("fixture".equals(key)) {
            return testStepInstance.getTestSequenceInstance().getTestFixture();
        }
        if ("station".equals(key)) {
            return testStepInstance.getTestSequenceInstance().getTestStation();
        }
        if (localVariablesMap.containsKey((String) key)) {
            return localVariablesMap.get((String) key);
        }
        try {
            return testStepInstance.getVariable((String) key);
        } catch (ScriptException ex) {
            log.warn(null, ex);
            throw new IllegalStateException(ex.getMessage());
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    @Override
    public Object remove(Object key) {
        return localVariablesMap.remove((String) key);
    }

    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        /* 'value' is always there, and cannot be removed */
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        System.err.println("containsValue of Bindings is called");
        for (String key : keySet()) {
            if (value.equals(get(key))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        testStepInstance.clear();
        localVariablesMap.clear();
    }

    @Override
    public Set<String> keySet() {
        System.err.println("keySet of Bindings is called.");
        Set<String> keys = testStepInstance.keySetPublic();
        keys.add("value");
        keys.add("step");
        keys.addAll(localVariablesMap.keySet());
        return keys;
    }

    @Override
    public Collection<Object> values() {
        Collection<Object> values = new ArrayList<Object>();
        for (String key : keySet()) {
            values.add(get(key));
        }
        return values;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();
        for (String key : keySet()) {
            entries.add(new HashMap.SimpleEntry(key, get(key)));
        }
        return entries;
    }
}
