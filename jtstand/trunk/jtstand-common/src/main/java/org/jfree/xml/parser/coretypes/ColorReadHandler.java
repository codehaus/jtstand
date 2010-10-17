/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ColorReadHandler.java is part of JTStand.
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

import java.awt.Color;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A SAX handler for reading {@link Color} objects from an XML element.
 */
public class ColorReadHandler extends AbstractXmlReadHandler  {

    /** The color under construction. */
    private Color color;
    
    /**
     * Creates a new handler.
     */
    public ColorReadHandler() {
        super();
    }

    /**
     * Called at the start of parsing a {@link Color} element, this method reads the attributes and
     * constructs the {@link Color}. 
     * 
     * @param attrs  the attributes.
     * 
     * @throws SAXException to indicate a parsing error.
     */
    protected void startParsing(final Attributes attrs) throws SAXException {
        final String encodedValue = attrs.getValue("value");
        this.color = Color.decode(encodedValue);
        if (attrs.getValue("alpha") != null) {
            this.color = new Color(this.color.getRed(), this.color.getGreen(),
                                   this.color.getBlue(), 
                                   Integer.parseInt(attrs.getValue("alpha")));
        }
    }

    /**
     * Returns the color under construction.
     * 
     * @return the color.
     */
    public Object getObject() {
        return this.color;
    }
    
}
