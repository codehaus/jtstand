/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectDescriptionException.java is part of JTStand.
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

import org.jfree.util.StackableException;

/**
 * An exception that indicates a problem with an object description.
 */
public class ObjectDescriptionException extends StackableException {

    /**
     * Creates a StackableRuntimeException with no message and no parent.
     */
    public ObjectDescriptionException() {
        super();
    }

    /**
     * Creates an exception.
     *
     * @param message  the exception message.
     * @param ex  the parent exception.
     */
    public ObjectDescriptionException(final String message, final Exception ex) {
        super(message, ex);
    }

    /**
     * Creates an exception.
     *
     * @param message  the exception message.
     */
    public ObjectDescriptionException(final String message) {
        super(message);
    }

}
