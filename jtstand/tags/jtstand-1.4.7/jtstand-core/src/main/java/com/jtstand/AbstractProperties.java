/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AbstractProperties.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.ScriptException;

/**
 *
 * @author albert_kurucz
 */
abstract public class AbstractProperties {

    public Boolean getPropertyBoolean(String keyString, boolean defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            if (Boolean.class.isAssignableFrom(prop.getClass())) {
                return (Boolean) prop;
            }
            return Boolean.valueOf(prop.toString().trim());
        }
        return defaultValue;
    }

    public Boolean getPropertyBoolean(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            if (Boolean.class.isAssignableFrom(prop.getClass())) {
                return (Boolean) prop;
            }
            return Boolean.valueOf(prop.toString().trim());
        }
        throw new IllegalArgumentException("Required Boolean type property is missing: " + keyString);
    }

    public Byte getPropertyByte(String keyString, Byte defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            if (Byte.class.isAssignableFrom(prop.getClass())) {
                return (Byte) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Byte.parseByte(propString.substring(2), 16);
            }
            return Byte.parseByte(propString);
        }
        return defaultValue;
    }

    public Byte getPropertyByte(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            if (Byte.class.isAssignableFrom(prop.getClass())) {
                return (Byte) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Byte.parseByte(propString.substring(2), 16);
            }
            return Byte.parseByte(propString);
        }
        throw new IllegalArgumentException("Required Byte type property is missing: " + keyString);
    }

    public Integer getPropertyInteger(String keyString, Integer defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            if (Integer.class.isAssignableFrom(prop.getClass())) {
                return (Integer) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Integer.parseInt(propString.substring(2), 16);
            }
            return Integer.parseInt(propString);
        }
        return defaultValue;
    }

    public Integer getPropertyInteger(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            if (Integer.class.isAssignableFrom(prop.getClass())) {
                return (Integer) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Integer.parseInt(propString.substring(2), 16);
            }
            return Integer.parseInt(propString);
        }
        throw new IllegalArgumentException("Required Integer type property is missing: " + keyString);
    }

    public Short getPropertyShort(String keyString, Short defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            if (Short.class.isAssignableFrom(prop.getClass())) {
                return (Short) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Short.parseShort(propString.substring(2), 16);
            }
            return Short.parseShort(propString);
        }
        return defaultValue;
    }

    public Short getPropertyShort(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            if (Short.class.isAssignableFrom(prop.getClass())) {
                return (Short) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Short.parseShort(propString.substring(2), 16);
            }
            return Short.parseShort(propString);
        }
        throw new IllegalArgumentException("Required Integer type property is missing: " + keyString);
    }

    public Long getPropertyLong(String keyString, Long defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            if (Long.class.isAssignableFrom(prop.getClass())) {
                return (Long) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Long.parseLong(propString.substring(2), 16);
            }
            return Long.parseLong(propString);
        }
        return defaultValue;
    }

    public Long getPropertyLong(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            if (Long.class.isAssignableFrom(prop.getClass())) {
                return (Long) prop;
            }
            String propString = prop.toString();
            if (propString.startsWith("0x") || propString.startsWith("0X")) {
                return Long.parseLong(propString.substring(2), 16);
            }
            return Long.parseLong(propString);
        }
        throw new IllegalArgumentException("Required Long type property is missing: " + keyString);
    }

    public Double getPropertyDouble(String keyString, Double defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            if (Double.class.isAssignableFrom(prop.getClass())) {
                return (Double) prop;
            }
            return Double.parseDouble(prop.toString());
        }
        return defaultValue;
    }

    public Double getPropertyDouble(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            if (Double.class.isAssignableFrom(prop.getClass())) {
                return (Double) prop;
            }
            return Double.parseDouble(prop.toString());
        }
        throw new IllegalArgumentException("Required Double type property is missing: " + keyString);
    }

    public Character getPropertyCharacter(String keyString, Character defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
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
        return defaultValue;
    }

    public Character getPropertyCharacter(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
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
        throw new IllegalArgumentException("Required Character type property is missing: " + keyString);
    }

    public String getPropertyString(String keyString, String defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            return prop.toString();
        }
        return defaultValue;
    }

    public String getPropertyString(String keyString) throws ScriptException {
        Object prop = getPropertyObject(keyString);
        if (prop != null) {
            return prop.toString();
        }
        throw new IllegalArgumentException("Required String type property is missing: " + keyString);
    }

    public Object getPropertyObject(String keyString, String defaultValue) {
        Object prop;
        try {
            prop = getPropertyObject(keyString);
        } catch (ScriptException ex) {
            Logger.getLogger(AbstractProperties.class.getName()).log(Level.SEVERE, null, ex);
            return defaultValue;
        }
        if (prop != null) {
            return prop;
        }
        return defaultValue;
    }

    public Object getPropertyObject(String keyString) throws ScriptException {
        return getPropertyObjectUsingBindings(keyString, getBindings());
    }
//    protected transient Bindings bindings;

    abstract public Bindings getBindings();

    abstract public Object getPropertyObjectUsingBindings(String keyString, Bindings binding) throws ScriptException;

//    abstract public Set<String> getPropertyNames();
}
