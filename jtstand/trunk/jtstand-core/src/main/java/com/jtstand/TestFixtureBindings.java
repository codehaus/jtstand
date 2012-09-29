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
public class TestFixtureBindings implements Bindings {

    private transient TestFixture testFixture;
    private transient Map<String, Object> localVariablesMap = new HashMap<String, Object>();

    public TestFixtureBindings() {
    }

    public TestFixtureBindings(TestFixture testFixture) {
        this.testFixture = testFixture;
    }

    @Override
    public Object put(String name, Object value) {
//        System.out.println("putting to fixture variable: '" + name + "' value: " + value);
        return testFixture.put(name, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        for (Entry<? extends String, ? extends Object> variableEntry : toMerge.entrySet()) {
            put(variableEntry.getKey(), variableEntry.getValue());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return testFixture.containsKey(key.toString())
                || "fixture".equals(key)
                || localVariablesMap.containsKey(key.toString())
                || testFixture.containsProperty(key.toString());
    }

    @Override
    public Object get(Object key) {
        if ("$type$".equals(key)) {
            return getClass().getName();
        }
        if ("context".equals(key)) {
            return ScriptContext.ENGINE_SCOPE;
        }
        if ("fixture".equals(key)) {
            return testFixture;
        }
        if ("station".equals(key)) {
            return testFixture.getTestStation();
        }

        if (localVariablesMap.containsKey((String) key)) {
            return localVariablesMap.get((String) key);
        }
        try {
            return testFixture.getVariable((String) key);
        } catch (ScriptException ex) {
            Logger.getLogger(TestFixture.class.getName()).log(Logger.Level.WARN, null, ex);
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
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (String key : keySet()) {
            if (value.equals(get(key))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        testFixture.clear();
        localVariablesMap.clear();
    }

    @Override
    public Set<String> keySet() {
        System.out.println("TestFixture keySet() is called.");
        Set<String> keys = new HashSet<String>();
        keys.addAll(testFixture.keySet());
        TestStation station = testFixture.getTestStation();
        if (station != null) {
            keys.addAll(station.keySet());
        }
        keys.add("fixture");
        keys.add("station");
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
