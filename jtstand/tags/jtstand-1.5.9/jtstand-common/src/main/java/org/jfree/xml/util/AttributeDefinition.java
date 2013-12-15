/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AttributeDefinition.java is part of JTStand.
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

import org.jfree.xml.attributehandlers.AttributeHandler;

/**
 * An attribute definition.
 */
public class AttributeDefinition {
    
    /** The attribute name. */
    private String attributeName;
    
    /** The attribute handler. */
    private AttributeHandler handler;
    
    /** The property name. */
    private String propertyName;

    /**
     * Creates a new attribute definition.
     * 
     * @param propertyName  the property name.
     * @param attributeName  the attribute name.
     * @param handler  the handler.
     */
    public AttributeDefinition(final String propertyName,
                               final String attributeName,
                               final AttributeHandler handler) {
        this.propertyName = propertyName;
        this.attributeName = attributeName;
        this.handler = handler;
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
     * Returns the attribute name.
     * 
     * @return the attribute name.
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * Returns the handler.
     * 
     * @return the handler.
     */
    public AttributeHandler getHandler() {
        return this.handler;
    }
    
}
