/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ListWriteHandler.java is part of JTStand.
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

package org.jfree.xml.writer.coretypes;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jfree.xml.writer.AbstractXmlWriteHandler;
import org.jfree.xml.writer.XMLWriter;
import org.jfree.xml.writer.XMLWriterException;
import org.jfree.xml.writer.XMLWriterSupport;

/**
 * A handler for writing a {@link List} object.
 */
public class ListWriteHandler extends AbstractXmlWriteHandler {

    /**
     * Performs the writing of a {@link List} object.
     *
     * @param tagName  the tag name.
     * @param object  the {@link List} object.
     * @param writer  the writer.
     * @param mPlexAttribute  ??.
     * @param mPlexValue  ??.
     * 
     * @throws IOException if there is an I/O error.
     * @throws XMLWriterException if there is a writer error.
     */
    public void write(final String tagName, final Object object, final XMLWriter writer,
                      final String mPlexAttribute, final String mPlexValue)
        throws IOException, XMLWriterException {

        writer.writeTag(tagName, mPlexAttribute, mPlexValue, XMLWriterSupport.OPEN);

        final List list = (List) object;
        final Iterator it = list.iterator();
        while (it.hasNext()) {
            final Object value = it.next();
            if (value == null) {
                writer.writeTag("null", XMLWriterSupport.CLOSE);
            }
            else {
                getRootHandler().write("object", value, Object.class, writer);
            }
        }

        writer.writeCloseTag(tagName);
    }
    
}
