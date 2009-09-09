/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, GradientPaintWriteHandler.java is part of JTStand.
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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.jfree.xml.writer.AbstractXmlWriteHandler;
import org.jfree.xml.writer.RootXmlWriteHandler;
import org.jfree.xml.writer.XMLWriter;
import org.jfree.xml.writer.XMLWriterException;

/**
 * A handler for writing {@link GradientPaint} objects.
 */
public class GradientPaintWriteHandler extends AbstractXmlWriteHandler  {

    /**
     * Default constructor.
     */
    public GradientPaintWriteHandler() {
        super();
    }

    /**
     * Performs the writing of a {@link GradientPaint} object.
     *
     * @param tagName  the tag name.
     * @param object  the {@link GradientPaint} object.
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
        final GradientPaint paint = (GradientPaint) object;
        writer.writeTag(tagName, mPlexAttribute, mPlexValue, false);
        writer.startBlock();
        final RootXmlWriteHandler rootHandler = getRootHandler();
        rootHandler.write("color1", paint.getColor1(), Color.class, writer);
        writer.allowLineBreak();
        rootHandler.write("color2", paint.getColor2(), Color.class, writer);
        writer.allowLineBreak();
        rootHandler.write("point1", paint.getPoint1(), Point2D.class, writer);
        writer.allowLineBreak();
        rootHandler.write("point2", paint.getPoint2(), Point2D.class, writer);
        writer.endBlock();
        writer.writeCloseTag(tagName);        
    }
}
