/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ConstructorDefinition.java is part of JTStand.
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

package org.jfree.xml.util;

/**
 * A constructor definition.
 */
public class ConstructorDefinition {
    
    /** isNull flag. */
    private boolean isNull;
    
    /** Property name. */
    private String propertyName;
    
    /** The type. */
    private Class type;

    /**
     * Creates a new constructor definition.
     * 
     * @param propertyName  the property name.
     * @param type  the type.
     */
    public ConstructorDefinition(final String propertyName, final Class type) {
        this.isNull = (propertyName == null);
        this.propertyName = propertyName;
        this.type = type;
    }

    /**
     * Returns the type.
     * 
     * @return the type.
     */
    public Class getType() {
        return this.type;
    }

    /**
     * Returns a flag.
     * 
     * @return a boolean.
     */
    public boolean isNull() {
        return this.isNull;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name.
     */
    public String getPropertyName() {
        return this.propertyName;
    }
    
}
