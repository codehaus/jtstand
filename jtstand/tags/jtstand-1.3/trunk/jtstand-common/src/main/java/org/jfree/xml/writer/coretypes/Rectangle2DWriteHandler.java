/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Rectangle2DWriteHandler.java is part of JTStand.
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

import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.jfree.xml.writer.AbstractXmlWriteHandler;
import org.jfree.xml.writer.AttributeList;
import org.jfree.xml.writer.XMLWriter;
import org.jfree.xml.writer.XMLWriterException;

/**
 * A handler for writing a {@link Rectangle2D} object.
 */
public class Rectangle2DWriteHandler extends AbstractXmlWriteHandler {

    /**
     * Default constructor.
     */
    public Rectangle2DWriteHandler() {
        super();
    }

    /**
     * Performs the writing of a single object.
     *
     * @param tagName  the tag name.
     * @param object  the {@link Rectangle2D} object.
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
        final Rectangle2D rect = (Rectangle2D) object;
        final double x = rect.getX();
        final double y = rect.getY();
        final double w = rect.getWidth();
        final double h = rect.getHeight();
        final AttributeList attribs = new AttributeList();
        if (mPlexAttribute != null) {
            attribs.setAttribute(mPlexAttribute, mPlexValue);
        }
        attribs.setAttribute("x", String.valueOf(x));
        attribs.setAttribute("y", String.valueOf(y));
        attribs.setAttribute("width", String.valueOf(w));
        attribs.setAttribute("height", String.valueOf(h));
        writer.writeTag(tagName, attribs, true);
    }
    
}
