/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AttributeHandler.java is part of JTStand.
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
 * An attribute handler is an object that can transform an object into a string or vice
 * versa.
 */
public interface AttributeHandler {
    
    /**
     * Converts an object to an attribute value.
     * 
     * @param o  the object.
     * 
     * @return the attribute value.
     */
    public String toAttributeValue (Object o);
    
    /**
     * Converts a string to a property value.
     * 
     * @param s  the string.
     * 
     * @return a property value.
     */    
    public Object toPropertyValue (String s);

}
