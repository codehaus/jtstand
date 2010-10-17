/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, GradientPaintReadHandler.java is part of JTStand.
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
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A SAX handler for reading a {@link GradientPaint} from an XML element.
 */
public class GradientPaintReadHandler extends AbstractXmlReadHandler  {

    /** The gradient paint under construction. */
    private GradientPaint gradient;

    /** The handler for color 1. */
    private XmlReadHandler color1Handler;

    /** The handler for color 2. */
    private XmlReadHandler color2Handler;

    /** The handler for point 1. */
    private XmlReadHandler point1Handler;

    /** The handler for point 2. */
    private XmlReadHandler point2Handler;

    /**
     * Creates a new handler.
     */
    public GradientPaintReadHandler() {
        super();
    }

    /**
     * Returns the gradient paint under construction.
     * 
     * @return the gradient paint.
     */
    public Object getObject() {
        return this.gradient;
    }

    /**
     * Returns the handler for a child element.
     * 
     * @param tagName  the tag name.
     * @param atts  the attributes.
     * 
     * @return the handler.
     * @throws SAXException to indicate a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    protected XmlReadHandler getHandlerForChild(final String tagName, final Attributes atts)
        throws SAXException, XmlReaderException {
        if ("color1".equals(tagName)) {
            this.color1Handler = getRootHandler().createHandler(Color.class, tagName, atts);
            return this.color1Handler;
        }
        else if ("color2".equals(tagName)) {
            this.color2Handler = getRootHandler().createHandler(Color.class, tagName, atts);
            return this.color2Handler;
        }
        else if ("point1".equals(tagName)) {
            this.point1Handler = getRootHandler().createHandler(Point2D.class, tagName, atts);
            return this.point1Handler;
        }
        else if ("point2".equals(tagName)) {
            this.point2Handler = getRootHandler().createHandler(Point2D.class, tagName, atts);
            return this.point2Handler;
        }
        return null;
    }

    /**
     * At the end of parsing the element, the gradient paint is constructed.
     * 
     * @throws XmlReaderException if there is a parsing error.
     */
    protected void doneParsing() throws XmlReaderException {
        if (this.point1Handler == null || this.point2Handler == null 
            || this.color1Handler == null || this.color2Handler == null) {
            throw new XmlReaderException("Not all required subelements are defined.");
        }
        this.gradient = new GradientPaint
            ((Point2D) this.point1Handler.getObject(),
            (Color) this.color1Handler.getObject(),
            (Point2D) this.point2Handler.getObject(),
            (Color) this.color2Handler.getObject());
    }

}
