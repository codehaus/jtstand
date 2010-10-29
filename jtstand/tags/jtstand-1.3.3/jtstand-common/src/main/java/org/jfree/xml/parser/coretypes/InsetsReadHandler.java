/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, InsetsReadHandler.java is part of JTStand.
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

package org.jfree.xml.parser.coretypes;

import java.awt.Insets;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A reader that can parse the XML element for an {@link Insets} object.
 */
public class InsetsReadHandler extends AbstractXmlReadHandler {

    /** The insets under construction. */
    private Insets insets;

    /**
     * Creates a new read handler.
     */
    public InsetsReadHandler() {
        super();
    }

    /**
     * Begins parsing.
     * 
     * @param attrs  the attributes.
     * 
     * @throws SAXException if there is a parsing problem.
     */
    protected void startParsing(final Attributes attrs) throws SAXException {

        final String top = attrs.getValue("top");
        final String left = attrs.getValue("left");
        final String bottom = attrs.getValue("bottom");
        final String right = attrs.getValue("right");

        this.insets = new Insets(
            Integer.parseInt(top), Integer.parseInt(left),
            Integer.parseInt(bottom), Integer.parseInt(right)
        );
        
    }

    /**
     * Returns the object for this element.
     *
     * @return the object.
     *
     * @throws XmlReaderException never.
     */
    public Object getObject() throws XmlReaderException {
        return this.insets;
    }
}
