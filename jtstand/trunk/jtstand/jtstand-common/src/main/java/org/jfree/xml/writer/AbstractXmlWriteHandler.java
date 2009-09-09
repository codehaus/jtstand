/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AbstractXmlWriteHandler.java is part of JTStand.
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

package org.jfree.xml.writer;

/**
 * A base class for implementing a handler that writes the XML for an object of a particular
 * class.
 */
public abstract class AbstractXmlWriteHandler implements XmlWriteHandler {
    
    /** The root handler. */
    private RootXmlWriteHandler rootHandler;

    /**
     * Creates a new handler.
     */
    public AbstractXmlWriteHandler() {
        super();
    }

    /**
     * Returns the root handler.
     * 
     * @return the root handler.
     */
    public RootXmlWriteHandler getRootHandler() {
        return this.rootHandler;
    }
    
    /**
     * Sets the root handler.
     * 
     * @param rootHandler  the root handler.
     */
    public void setRootHandler(final RootXmlWriteHandler rootHandler) {
        this.rootHandler = rootHandler;
    }

}
