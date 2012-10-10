/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ColorWriteHandler.java is part of JTStand.
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
import java.io.IOException;

import org.jfree.xml.writer.AbstractXmlWriteHandler;
import org.jfree.xml.writer.AttributeList;
import org.jfree.xml.writer.XMLWriter;
import org.jfree.xml.writer.XMLWriterException;

/**
 * A handler for writing a {@link Color} object.
 */
public class ColorWriteHandler extends AbstractXmlWriteHandler  {

    /**
     * Default constructor.
     */
    public ColorWriteHandler() {
        super();
    }

    /**
     * Performs the writing of a {@link Color} object.
     *
     * @param tagName  the tag name.
     * @param object  the {@link Color} object.
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
        final Color color = (Color) object;
        final AttributeList attribs = new AttributeList();
        if (mPlexAttribute != null) {
            attribs.setAttribute(mPlexAttribute, mPlexValue);
        }
        attribs.setAttribute("value", encodeColor(color));
        if (color.getAlpha() != 255) {
            attribs.setAttribute("alpha", String.valueOf(color.getAlpha()));
        }
        writer.writeTag(tagName, attribs, true);
    }

    private String encodeColor(final Color color) {
        return "#" + encodeInt(color.getRed()) 
            + encodeInt(color.getGreen()) + encodeInt(color.getBlue());
    }

    private String encodeInt(final int i) {
        final String retVal = Integer.toHexString(i);
        if (retVal.length() == 1) {
            return "0" + retVal;
        } 
        else {
            return retVal;
        }
    }
}
