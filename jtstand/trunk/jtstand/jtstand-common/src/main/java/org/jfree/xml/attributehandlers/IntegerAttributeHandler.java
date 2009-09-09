/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, IntegerAttributeHandler.java is part of JTStand.
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

import org.jfree.util.Log;

/**
 * A class that handles the conversion of {@link Integer} attributes to and from an appropriate
 * {@link String} representation.
 */
public class IntegerAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public IntegerAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     *
     * @param o  the attribute ({@link Integer} expected).
     * 
     * @return A string representing the integer value.
     */
    public String toAttributeValue(final Object o) {
        try {
            final Integer in = (Integer) o;
            return in.toString();
        }
        catch (ClassCastException cce) {
            if (o != null) {
                Log.debug("ClassCastException: Expected Integer, found " + o.getClass());
            }
            throw cce;
        }
    }

    /**
     * Converts a string to a {@link Integer}.
     *
     * @param s  the string.
     *
     * @return a {@link Integer}.
     */
    public Object toPropertyValue(final String s) {
        return new Integer(s);
    }
}
