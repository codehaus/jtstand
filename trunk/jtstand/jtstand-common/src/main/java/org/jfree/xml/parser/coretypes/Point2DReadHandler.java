/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Point2DReadHandler.java is part of JTStand.
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


import java.awt.geom.Point2D;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A SAX handler for reading {@link Point2D} objects from an XML element.
 */
public class Point2DReadHandler extends AbstractXmlReadHandler  {

    /** The point under construction. */
    private Point2D point;

    /**
     * Creates a new handler.
     */
    public Point2DReadHandler() {
        super();
    }

    /** 
     * At the start of parsing, the attributes are read and used to construct the point.
     * 
     * @param attrs  the attributes.
     * 
     * @throws SAXException if there is a parsing error.
     */
    protected void startParsing(final Attributes attrs) throws SAXException {
        final String x = attrs.getValue("x");
        final String y = attrs.getValue("y");
        this.point = new Point2D.Double(Double.parseDouble(x),
                                        Double.parseDouble(y));
    }

    /**
     * Returns the point under construction.
     * 
     * @return the point.
     */
    public Point2D getPoint2D() {
        return this.point;
    }

    /**
     * Returns the point under construction.
     * 
     * @return the point.
     */
    public Object getObject() {
        return this.point;
    }
    
}
