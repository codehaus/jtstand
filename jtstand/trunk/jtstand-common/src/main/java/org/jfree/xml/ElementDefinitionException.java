/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ElementDefinitionException.java is part of JTStand.
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
package org.jfree.xml;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.xml.sax.Locator;

/**
 * A reportdefinition exception is thrown when the parsing of the report definition
 * failed because invalid or missing attributes are encountered.
 *
 * @author Thomas Morgner
 */
public class ElementDefinitionException extends ParseException {

    /** The parent exception. */
    private Exception parent;

    /**
     * Creates a new ElementDefinitionException without an parent exception and with the given
     * message as explanation.
     *
     * @param message a detail message explaining the reasons for this exception.
     */
    public ElementDefinitionException(final String message) {
        super(message);
    }

    /**
     * Creates a new ElementDefinitionException with an parent exception and with the parents
     * message as explaination.
     *
     * @param e the parentException that caused this exception
     */
    public ElementDefinitionException(final Exception e) {
        this(e, e.getMessage());
    }

    /**
     * Creates a new ElementDefinitionException with an parent exception and with the given
     * message as explaination.
     *
     * @param e the parentException that caused this exception
     * @param message a detail message explaining the reasons for this exception
     */
    public ElementDefinitionException(final Exception e, final String message) {
        this(message);
        this.parent = e;
    }

    /**
     * Creates a new ParseException with the given root exception
     * and the locator.
     *
     * @param e       the exception
     * @param locator the locator of the parser
     */
    public ElementDefinitionException(final Exception e, final Locator locator) {
        super(e, locator);
        this.parent = e;
    }

    /**
     * Creates a new ParseException with the given message and the locator.
     *
     * @param message the message
     * @param locator the locator of the parser
     */
    public ElementDefinitionException(final String message, final Locator locator) {
        super(message, locator);
    }

    /**
     * Creates a new ParseException with the given message, root exception
     * and the locator.
     *
     * @param s       the message
     * @param e       the exception
     * @param locator the locator of the parser
     */
    public ElementDefinitionException(final String s, final Exception e, final Locator locator) {
        super(s, e, locator);
        this.parent = e;
    }

    /**
     * Returns the parent exception.
     *
     * @return the parent exception.
     */
    public Exception getParentException() {
        return this.parent;
    }

    /**
     * Prints the stack trace.  If an inner exception exists, use
     * its stack trace.
     *
     * @param s  the stream for writing to.
     */
    public void printStackTrace(final PrintStream s) {
        super.printStackTrace(s);
        if (this.parent != null) {
            s.print("ParentException:");
            this.parent.printStackTrace(s);
        }
        else {
            s.println("ParentException: <null>");
        }
    }

    /**
     * Prints the stack trace.  If an inner exception exists, use
     * its stack trace.
     *
     * @param s  the stream for writing to.
     */
    public void printStackTrace(final PrintWriter s) {
        super.printStackTrace(s);
        if (this.parent != null) {
            s.print("ParentException:");
            this.parent.printStackTrace(s);
        }
        else {
            s.println("ParentException: <null>");
        }
    }

}
