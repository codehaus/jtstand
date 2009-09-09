/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ModuleInitializeException.java is part of JTStand.
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

package org.jfree.base.modules;

import org.jfree.util.StackableException;

/**
 * This exception is thrown when the module initialization encountered an
 * unrecoverable error which prevents the module from being used.
 *
 * @author Thomas Morgner
 */
public class ModuleInitializeException extends StackableException {

    /**
     * Creates a ModuleInitializeException with no message and no base
     * exception.
     */
    public ModuleInitializeException() {
        // nothing required
    }

    /**
     * Creates a ModuleInitializeException with the given message and base
     * exception.
     *
     * @param s the message
     * @param e the root exception
     */
    public ModuleInitializeException(final String s, final Exception e) {
        super(s, e);
    }

    /**
     * Creates a ModuleInitializeException with the given message and no base
     * exception.
     *
     * @param s the exception message
     */
    public ModuleInitializeException(final String s) {
        super(s);
    }
    
}
