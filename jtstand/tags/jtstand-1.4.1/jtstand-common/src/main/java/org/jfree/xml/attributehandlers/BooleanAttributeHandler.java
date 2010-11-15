/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, BooleanAttributeHandler.java is part of JTStand.
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

package org.jfree.xml.attributehandlers;

/**
 * A class that handles the conversion of {@link Boolean} attributes to and from their
 * {@link String} representation.
 */
public class BooleanAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public BooleanAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     * 
     * @param o  the attribute ({@link Boolean} expected).
     * 
     * @return A string representing the {@link Boolean} value.
     */
    public String toAttributeValue(final Object o) {
        if (o instanceof Boolean) {
            return o.toString();
        }
        throw new ClassCastException("Give me a real type.");
    }

    /**
     * Converts a string to a {@link Boolean}.
     * 
     * @param s  the string.
     * 
     * @return a {@link Boolean}.
     */
    public Object toPropertyValue(final String s) {
        return new Boolean (s);
    }
}
