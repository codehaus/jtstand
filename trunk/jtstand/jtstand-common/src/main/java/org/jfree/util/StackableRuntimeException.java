/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StackableRuntimeException.java is part of JTStand.
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

package org.jfree.util;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A baseclass for RuntimeExceptions, which could have parent exceptions. These parent exceptions
 * are raised in a subclass and are now wrapped into a subclass of this Exception.
 * <p>
 * The parents are printed when this exception is printed. This class exists mainly for
 * debugging reasons, as with them it is easier to detect the root cause of an error.
 *
 * @author Thomas Morgner
 */
public class StackableRuntimeException extends RuntimeException {

    /** The parent exception. */
    private Exception parent;

    /**
     * Creates a StackableRuntimeException with no message and no parent.
     */
    public StackableRuntimeException() {
        super();
    }

    /**
     * Creates an exception.
     *
     * @param message  the exception message.
     * @param ex  the parent exception.
     */
    public StackableRuntimeException(final String message, final Exception ex) {
        super(message);
        this.parent = ex;
    }

    /**
     * Creates an exception.
     *
     * @param message  the exception message.
     */
    public StackableRuntimeException(final String message) {
        super(message);
    }

    /**
     * Returns the parent exception (possibly null).
     *
     * @return the parent exception.
     */
    public Exception getParent() {
        return this.parent;
    }

    /**
     * Prints the stack trace to the specified stream.
     *
     * @param stream  the output stream.
     */
    public void printStackTrace(final PrintStream stream) {
        super.printStackTrace(stream);
        if (getParent() != null) {
            stream.println("ParentException: ");
            getParent().printStackTrace(stream);
        }
    }

    /**
     * Prints the stack trace to the specified writer.
     *
     * @param writer  the writer.
     */
    public void printStackTrace(final PrintWriter writer) {
        super.printStackTrace(writer);
        if (getParent() != null) {
            writer.println("ParentException: ");
            getParent().printStackTrace(writer);
        }
    }

}
