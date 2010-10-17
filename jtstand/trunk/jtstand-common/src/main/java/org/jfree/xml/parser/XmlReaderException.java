/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, XmlReaderException.java is part of JTStand.
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

package org.jfree.xml.parser;

import org.jfree.xml.util.ObjectDescriptionException;

/**
 * An XML reader exception.
 */
public class XmlReaderException extends ObjectDescriptionException {
    /**
     * Creates a StackableRuntimeException with no message and no parent.
     */
    public XmlReaderException() {
        super();
    }

    /**
     * Creates an exception.
     *
     * @param message  the exception message.
     */
    public XmlReaderException(final String message) {
        super(message);
    }

    /**
     * Creates an exception.
     *
     * @param message  the exception message.
     * @param ex  the parent exception.
     */
    public XmlReaderException(final String message, final Exception ex) {
        super(message, ex);
    }
}
