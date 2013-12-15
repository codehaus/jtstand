/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, LongAttributeHandler.java is part of JTStand.
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
 * A class that handles the conversion of {@link Long} attributes to and from an appropriate
 * {@link String} representation.
 */
public class LongAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public LongAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     * 
     * @param o  the attribute ({@link Long} expected).
     * 
     * @return A string representing the {@link Long} value.
     */
    public String toAttributeValue(final Object o) {
        final Long in = (Long) o;
        return in.toString();
    }

    /**
     * Converts a string to a {@link Long}.
     * 
     * @param s  the string.
     * 
     * @return a {@link Long}.
     */
    public Object toPropertyValue(final String s) {
        return new Long(s);
    }
    
}
