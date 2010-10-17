/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, XmlWriteHandler.java is part of JTStand.
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

import java.io.IOException;

/**
 * The interface that must be supported by all XML write handlers.
 */
public interface XmlWriteHandler {

    /**
     * Returns the root handler for this write handler. The root handler
     * will be used to resolve the child handlers.
     * 
     * @return the root handler.
     */
    public RootXmlWriteHandler getRootHandler();

    /**
     * Sets the root handler.
     * 
     * @param rootHandler  the root handler.
     */
    public void setRootHandler(RootXmlWriteHandler rootHandler);

    /**
     * Performs the writing of a single object.
     *
     * @param tagName  the tag name for the generated xml element.
     * @param object  the object to be written.
     * @param writer  the writer.
     * @param mPlexAttribute  the multiplexer selector attribute name.
     * @param mPlexValue the multiplexers attribute value corresponding to this
     * object type.
     * 
     * @throws IOException if an IOError occured.
     * @throws XMLWriterException if an XmlDefinition error occured.
     */
    public void write(String tagName, Object object, XMLWriter writer,
                      String mPlexAttribute, String mPlexValue)
        throws IOException, XMLWriterException;

}
