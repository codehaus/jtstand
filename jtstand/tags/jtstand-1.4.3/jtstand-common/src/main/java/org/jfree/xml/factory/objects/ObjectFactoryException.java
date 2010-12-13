/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectFactoryException.java is part of JTStand.
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

package org.jfree.xml.factory.objects;

import org.jfree.util.StackableException;

/**
 * An exception that is thrown, if the creation of an Object failed in the
 * ObjectFactory implementation.
 *
 * @author Thomas Morgner.
 */
public class ObjectFactoryException extends StackableException {

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public ObjectFactoryException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *        later retrieval by the {@link #getMessage()} method.
     */
    public ObjectFactoryException(final String message) {
        super(message);
    }

    /**
     * Creates a new exception.
     *
     * @param message  the message.
     * @param cause  the cause of the exception.
     */
    public ObjectFactoryException(final String message, final Exception cause) {
        super(message, cause);
    }
}
