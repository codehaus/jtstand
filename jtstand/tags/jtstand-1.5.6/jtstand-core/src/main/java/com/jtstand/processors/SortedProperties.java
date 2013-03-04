/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.processors;

/**
 *
 * @author albert_kurucz
 */
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SortedProperties extends Properties {

    private Vector mykeys = new Vector();

    /**
     * Overrides, called by the store method.
     */
    @Override
    public synchronized Enumeration keys() {
        return mykeys.elements();
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        if (!mykeys.contains(key)) {
            mykeys.add(key);
        }
        return super.setProperty(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        mykeys.remove(key);
        return super.remove(key);
    }       
}