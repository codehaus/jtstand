/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ManualMappingDefinition.java is part of JTStand.
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
 * Maps a class to a read handler and a write handler.
 */
public class ManualMappingDefinition {
    
    /** The class. */
    private Class baseClass;

    /** The read handler. */
    private String readHandler;
    
    /** The write handler. */
    private String writeHandler;
    
    /**
     * Creates a mapping between the class and the read and write handlers.
     * 
     * @param baseClass  the class (<code>null</code> not permitted).
     * @param readHandler  the name of the read handler.
     * @param writeHandler  the name of the write handler.
     */
    public ManualMappingDefinition(final Class baseClass, final String readHandler, final String writeHandler) {
        if (baseClass == null) {
            throw new NullPointerException("BaseClass must not be null");
        }
        if (readHandler == null && writeHandler == null) {
            throw new NullPointerException
                ("At least one of readHandler or writeHandler must be defined.");
        }
        this.baseClass = baseClass;
        this.readHandler = readHandler;
        this.writeHandler = writeHandler;
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
     * Returns the name of the read handler.
     * 
     * @return The name of the read handler.
     */
    public String getReadHandler() {
        return this.readHandler;
    }

    /**
     * Returns the name of the write handler.
     * 
     * @return The name of the write handler.
     */
    public String getWriteHandler() {
        return this.writeHandler;
    }
    
}
