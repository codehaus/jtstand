/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PropertyType.java is part of JTStand.
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
 
package org.jfree.xml.generator.model;

/**
 * An enumeration over the defined property types.
 * <p>
 * Attribute types are mapped into xml attributes - this is used for
 * primitive data or enumeration classes.
 * <p>
 * Element types are used to define complex classes, a new xml tag will
 * be introduced for every element type.
 * <p>
 * The lookup properties are defined elsewhere and are referenced using
 * the defined name during the class building process. 
 */
public final class PropertyType {
    
    /** A property that is described using an attribute in the XML. */
    public static final PropertyType ATTRIBUTE = new PropertyType("ATTRIBUTE");
    
    /** A property that is described using an XML element. */
    public static final PropertyType ELEMENT = new PropertyType("ELEMENT");
    
    /** A property that is... */
    public static final PropertyType LOOKUP = new PropertyType("LOOKUP");

    /** The property type name. */
    private final String myName; // for debug only

    /**
     * Private constructor prevents new types being created.
     * 
     * @param name  the type name.
     */
    private PropertyType(final String name) {
        this.myName = name;
    }

    /**
     * Returns a string representing the type.
     * 
     * @return a string.
     */
    public String toString() {
        return this.myName;
    }
    
}
