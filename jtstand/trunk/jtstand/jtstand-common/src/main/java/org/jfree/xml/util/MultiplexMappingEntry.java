/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MultiplexMappingEntry.java is part of JTStand.
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
 * A multiplex mapping entry.
 */
public class MultiplexMappingEntry {
    
    /** The attribute value. */
    private String attributeValue;
    
    /** The target class. */
    private String targetClass;

    /**
     * Creates a new instance.
     * 
     * @param attributeValue  the attribute value.
     * @param targetClass  the target class.
     */
    public MultiplexMappingEntry(final String attributeValue, final String targetClass) {
        this.attributeValue = attributeValue;
        this.targetClass = targetClass;
    }

    /**
     * Returns the attribute value.
     * 
     * @return The attribute value.
     */
    public String getAttributeValue() {
        return this.attributeValue;
    }

    /**
     * Returns the target class.
     * 
     * @return The target class.
     */
    public String getTargetClass() {
        return this.targetClass;
    }
    
}
