/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PropertyDefinition.java is part of JTStand.
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
 * A property definition.
 */
public class PropertyDefinition {
    
    /** The property name. */
    private String propertyName;
    
    /** The element name. */
    private String elementName;

    /**
     * Creates a new property definition.
     * 
     * @param propertyName  the property name.
     * @param elementName  the element name.
     */
    public PropertyDefinition(final String propertyName, final String elementName) {
        this.elementName = elementName;
        this.propertyName = propertyName;
    }

    /**
     * Returns the property name.
     * 
     * @return the property name.
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Returns the element name.
     * 
     * @return the element name.
     */
    public String getElementName() {
        return this.elementName;
    }
    
}
