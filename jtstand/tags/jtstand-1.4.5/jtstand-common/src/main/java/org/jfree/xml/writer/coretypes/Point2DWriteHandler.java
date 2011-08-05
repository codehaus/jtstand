/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Point2DWriteHandler.java is part of JTStand.
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

import java.awt.geom.Point2D;
import java.io.IOException;

import org.jfree.xml.writer.AbstractXmlWriteHandler;
import org.jfree.xml.writer.AttributeList;
import org.jfree.xml.writer.XMLWriter;
import org.jfree.xml.writer.XMLWriterException;

/**
 * A handler for writing a {@link Point2D} object.
 */
public class Point2DWriteHandler extends AbstractXmlWriteHandler {
    
    /**
     * Default constructor.
     */
    public Point2DWriteHandler() {
        super();
    }

    /**
     * Performs the writing of a {@link Point2D} object.
     *
     * @param tagName  the tag name.
     * @param object  the {@link Point2D} object.
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
        final Point2D point = (Point2D) object;
        final double x = point.getX();
        final double y = point.getY();
        final AttributeList attribs = new AttributeList();
        if (mPlexAttribute != null) {
            attribs.setAttribute(mPlexAttribute, mPlexValue);
        }
        attribs.setAttribute("x", String.valueOf(x));
        attribs.setAttribute("y", String.valueOf(y));
        writer.writeTag(tagName, attribs, true);
    }
}
