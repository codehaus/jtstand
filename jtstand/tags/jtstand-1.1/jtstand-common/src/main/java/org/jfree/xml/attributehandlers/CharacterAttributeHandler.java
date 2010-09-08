/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, CharacterAttributeHandler.java is part of JTStand.
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
 * A class that handles the conversion of {@link Character} attributes to and from an appropriate
 * {@link String} representation.
 */
public class CharacterAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public CharacterAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     * 
     * @param o  the attribute ({@link Character} expected).
     * 
     * @return A string representing the {@link Character} value.
     */
    public String toAttributeValue(final Object o) {
        final Character in = (Character) o;
        return in.toString();
    }

    /**
     * Converts a string to a {@link Character}.
     * 
     * @param s  the string.
     * 
     * @return a {@link Character}.
     */
    public Object toPropertyValue(final String s) {
        if (s.length() == 0) {
            throw new RuntimeException("Ugly, no char set!");
        }
        return new Character(s.charAt(0));
    }
}
