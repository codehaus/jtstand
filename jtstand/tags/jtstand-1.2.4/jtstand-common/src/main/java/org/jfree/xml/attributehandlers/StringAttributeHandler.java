/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StringAttributeHandler.java is part of JTStand.
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
 * A {@link String} attribute handler.
 */
public class StringAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public StringAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     * 
     * @param o  the attribute ({@link String} expected).
     * 
     * @return A string.
     */
    public String toAttributeValue(final Object o) {
        final String in = (String) o;
        if (in != null) {
            return in; 
        }
        return null;
    }

    /**
     * Converts a copy of the string.
     * 
     * @param s  the string.
     * 
     * @return a {@link Short}.
     */
    public Object toPropertyValue(final String s) {
        return s; 
    }
    
}
