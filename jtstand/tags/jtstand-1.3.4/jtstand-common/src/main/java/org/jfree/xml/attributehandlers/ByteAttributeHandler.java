/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ByteAttributeHandler.java is part of JTStand.
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
 * A class that handles the conversion of {@link Byte} attributes to and from an appropriate
 * {@link String} representation.
 */
public class ByteAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public ByteAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     * 
     * @param o  the attribute ({@link Byte} expected).
     * 
     * @return A string representing the {@link Byte} value.
     */
    public String toAttributeValue(final Object o) {
        final Byte in = (Byte) o;
        return in.toString();
    }

    /**
     * Converts a string to a {@link Byte}.
     * 
     * @param s  the string.
     * 
     * @return a {@link Byte}.
     */
    public Object toPropertyValue(final String s) {
        return new Byte(s);
    }
    
}
