/*
 * Copyright 2009 Albert Kurucz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jtstand;

import groovy.lang.Binding;

import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
abstract public class AbstractProperties implements PropertiesInterface, Serializable {

    @Override
    public Boolean getPropertyBoolean(String keyString, boolean defaultValue) {
        try {
            return getPropertyBoolean(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Boolean getPropertyBoolean(String keyString) {
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
//            System.out.println("Required Boolean type property is missing: " + keyString);
            throw new IllegalArgumentException("Required Boolean type property is missing: " + keyString);
        }
//        System.out.println("Found boolean: '" + keyString + "': " + prop);
        if (Boolean.class.isAssignableFrom(prop.getClass())) {
            return (Boolean) prop;
        }
//        System.out.println("Found boolean: '" + keyString + "': " + prop);
        return Boolean.valueOf(prop.toString().trim());
    }

    @Override
    public Byte getPropertyByte(String keyString, Byte defaultValue) {
        try {
            return getPropertyByte(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Byte getPropertyByte(String keyString) {
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
            throw new IllegalArgumentException("Required Byte type property is missing: " + keyString);
        }
        if (Byte.class.isAssignableFrom(prop.getClass())) {
            return (Byte) prop;
        }
        String propString = prop.toString();
        if (propString.startsWith("0x") || propString.startsWith("0X")) {
            return Byte.parseByte(propString.substring(2), 16);
        }
        return Byte.parseByte(propString);
    }

    @Override
    public Integer getPropertyInteger(String keyString, Integer defaultValue) {
        try {
            return getPropertyInteger(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Integer getPropertyInteger(String keyString) {
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
            throw new IllegalArgumentException("Required Integer type property is missing: " + keyString);
        }
        if (Integer.class.isAssignableFrom(prop.getClass())) {
            return (Integer) prop;
        }
        String propString = prop.toString();
        if (propString.startsWith("0x") || propString.startsWith("0X")) {
            return Integer.parseInt(propString.substring(2), 16);
        }
        return Integer.parseInt(propString);
    }

    @Override
    public Short getPropertyShort(String keyString, Short defaultValue) {
        try {
            return getPropertyShort(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Short getPropertyShort(String keyString) {
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
            throw new IllegalArgumentException("Required Integer type property is missing: " + keyString);
        }
        if (Short.class.isAssignableFrom(prop.getClass())) {
            return (Short) prop;
        }
        String propString = prop.toString();
        if (propString.startsWith("0x") || propString.startsWith("0X")) {
            return Short.parseShort(propString.substring(2), 16);
        }
        return Short.parseShort(propString);
    }

    @Override
    public Long getPropertyLong(String keyString, Long defaultValue) {
        try {
            return getPropertyLong(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Long getPropertyLong(String keyString) {
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
            throw new IllegalArgumentException("Required Long type property is missing: " + keyString);
        }
        if (Long.class.isAssignableFrom(prop.getClass())) {
            return (Long) prop;
        }
        String propString = prop.toString();
        if (propString.startsWith("0x") || propString.startsWith("0X")) {
            return Long.parseLong(propString.substring(2), 16);
        }
        return Long.parseLong(propString);
    }

    @Override
    public Double getPropertyDouble(String keyString, Double defaultValue) {
        try {
            return getPropertyDouble(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Double getPropertyDouble(String keyString) {
//        System.out.println("Getting Double property: '" + keyString + "'");
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
            throw new IllegalArgumentException("Required Double type property is missing: " + keyString);
        }
        if (Double.class.isAssignableFrom(prop.getClass())) {
            return (Double) prop;
        }
        return Double.parseDouble(prop.toString());
    }

    @Override
    public Character getPropertyCharacter(String keyString, Character defaultValue) {
        try {
            return getPropertyCharacter(keyString);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    @Override
    public Character getPropertyCharacter(String keyString) {
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
            throw new IllegalArgumentException("Required Character type property is missing: " + keyString);
        }
        if (Character.class.isAssignableFrom(prop.getClass())) {
            return (Character) prop;
        }
        String propString = prop.toString();
        if (propString.length() == 0) {
            throw new IllegalArgumentException("Required Character type property is empty: " + keyString);
        }
        if (propString.length() == 1) {
            return propString.charAt(0);
        }
        throw new IllegalArgumentException("Cannot evaluate the property:'" + keyString + "' as a single Character:'" + propString + "'");
    }

    @Override
    public String getPropertyString(String keyString, String defaultValue) {
        try {
            return getPropertyString(keyString);
        } catch (Exception ex) {
//            System.out.println("Ex: " + ex.getMessage());
//            ex.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public String getPropertyString(String keyString) {
//        System.out.println("Finding String property : '" + keyString + "'...");
        Object prop = getPropertyObject(keyString);
        if (prop == null) {
//            System.out.println("Not found String property : '" + keyString + "'");
            throw new IllegalArgumentException("Required String type property is missing: " + keyString);
        }
//        System.out.println("Found String property : '" + keyString + "' : '" + prop + "'");
        return prop.toString();
    }

    @Override
    public Object getPropertyObject(String keyString) {
        return getPropertyObject(keyString, getBinding());
    }
    protected transient Binding binding;

    abstract public Binding getBinding();

    abstract public Object getPropertyObject(String keyString, Binding binding);
}
