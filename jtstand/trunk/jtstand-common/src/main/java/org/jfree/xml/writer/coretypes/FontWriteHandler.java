/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FontWriteHandler.java is part of JTStand.
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

import java.awt.Font;
import java.io.IOException;

import org.jfree.xml.writer.AbstractXmlWriteHandler;
import org.jfree.xml.writer.AttributeList;
import org.jfree.xml.writer.XMLWriter;
import org.jfree.xml.writer.XMLWriterException;

/**
 * A handler for writing {@link Font} objects.
 */
public class FontWriteHandler extends AbstractXmlWriteHandler  {
    
    /**
     * Default constructor.
     */
    public FontWriteHandler() {
        super();
    }

    /**
     * Performs the writing of a {@link Font} object.
     *
     * @param tagName  the tag name.
     * @param object  the {@link Font} object.
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
        final Font font = (Font) object;
        final AttributeList attribs = new AttributeList();
        if (mPlexAttribute != null) {
            attribs.setAttribute(mPlexAttribute, mPlexValue);
        }
        attribs.setAttribute("family", font.getFamily());
        attribs.setAttribute("size", String.valueOf(font.getSize()));
        attribs.setAttribute("style", String.valueOf(getFontStyle(font)));
        writer.writeTag(tagName, attribs, true);
    }

    private String getFontStyle(final Font font) {
        if (font.isBold() && font.isItalic()) {
            return "bold-italic";
        }
        if (font.isBold()) {
            return "bold";
        }
        if (font.isItalic()) {
            return "italic";
        }
        return "plain";
    }
    
}
