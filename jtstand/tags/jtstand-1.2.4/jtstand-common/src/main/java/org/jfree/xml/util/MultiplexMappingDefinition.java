/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MultiplexMappingDefinition.java is part of JTStand.
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

import java.util.HashMap;

/**
 * Maps a class to ...
 */
public class MultiplexMappingDefinition {

    /** The class. */
    private Class baseClass;

    /** The attribute name. */
    private String attributeName;
    
    /** The forward mappings. */
    private HashMap forwardMappings;
    
    /** The reverse mappings. */
    private HashMap reverseMappings;

    /**
     * Creates a new mapping definition.
     * 
     * @param baseClass  the class.
     * @param attributeName  the attribute name.
     * @param entries  the entries.
     */
    public MultiplexMappingDefinition(final Class baseClass,
                                      final String attributeName,
                                      final MultiplexMappingEntry[] entries) {
        
        this.attributeName = attributeName;
        this.baseClass = baseClass;
        this.forwardMappings = new HashMap();
        this.reverseMappings = new HashMap();

        for (int i = 0; i < entries.length; i++) {
            final MultiplexMappingEntry entry = entries[i];
            this.forwardMappings.put(entry.getAttributeValue(), entry);
            this.reverseMappings.put(entry.getTargetClass(), entry);
        }
    }

    /**
     * Returns the attribute name.
     * 
     * @return The attribute name.
     */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * Returns the class.
     * 
     * @return The class.
     */
    public Class getBaseClass() {
        return this.baseClass;
    }

    /**
     * Returns a mapping entry for a type.
     * 
     * @param type  the type.
     * 
     * @return The mapping entry.
     */
    public MultiplexMappingEntry getEntryForType(final String type) {
        return (MultiplexMappingEntry) this.forwardMappings.get(type);
    }

    /**
     * Returns a mapping entry for a class.
     * 
     * @param clazz  the class.
     * 
     * @return The mapping entry.
     */
    public MultiplexMappingEntry getEntryForClass(final String clazz) {
        return (MultiplexMappingEntry) this.reverseMappings.get(clazz);
    }
}
