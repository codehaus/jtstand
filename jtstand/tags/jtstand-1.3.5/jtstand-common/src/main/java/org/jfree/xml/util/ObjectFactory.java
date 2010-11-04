/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectFactory.java is part of JTStand.
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
 * An object factory.
 */
public interface ObjectFactory {

    /**
     * Returns the generic factory for the given class or null, if there is
     * no handler for this class. The factory will be reinitialized using
     * getInstance().
     *
     * @param c the class for which we need a producer
     * @return the factory for this class
     */
    public GenericObjectFactory getFactoryForClass(Class c);

    /**
     * Checks, whether a generic handler exists for the given class. This does
     * not check, whether an mapping exists.
     *
     * @param c the class for which to check
     * @return true, if an generic handler is defined, false otherwise.
     */
    public boolean isGenericHandler(Class c);

    /**
     * Returns the multiplex definition for the given class, or null, if no
     * such definition exists.
     *
     * @param c the class for which to check the existence of the multiplexer
     * @return the multiplexer for the class, or null if no multiplexer exists.
     */
    public MultiplexMappingDefinition getMultiplexDefinition(Class c);

    /**
     * Returns the manual mapping definition for the given class, or null, if
     * not manual definition exists.
     *
     * @param c the class for which to check the existence of the definition
     * @return the manual mapping definition or null.
     */
    public ManualMappingDefinition getManualMappingDefinition(Class c);

}
