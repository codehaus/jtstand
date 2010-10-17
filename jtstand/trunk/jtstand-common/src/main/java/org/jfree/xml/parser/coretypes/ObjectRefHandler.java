/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectRefHandler.java is part of JTStand.
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

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A SAX handler for an object reference.
 */
public class ObjectRefHandler extends AbstractXmlReadHandler {

    /** The object. */
    private Object object;
  
    /** The property name. */
    private String propertyName;

    /**
     * Creates a new handler.
     */
    public ObjectRefHandler() {
        super();
    }

    /**
     * Starts parsing.
     * 
     * @param attrs  the attributes.
     * 
     * @throws SAXException ???.
     */
    protected void startParsing(final Attributes attrs) throws SAXException {
        final String tagName = getTagName();
        if (tagName.equals("objectRef")) {
            final String sourceName = attrs.getValue("source");
            if (sourceName == null) {
                throw new SAXException("Source name is not defined.");
            }
            this.propertyName = attrs.getValue("property");
            if (this.propertyName == null) {
                throw new SAXException("Property name is not defined.");
            }

            this.object = getRootHandler().getHelperObject(sourceName);
            if (this.object == null) {
                throw new SAXException("Referenced object is undefined.");
            }
        }
    }

    /**
     * Returns the property name.
     * 
     * @return the property name.
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Returns the value.
     * 
     * @return the value.
     */
    public Object getObject() {
        return this.object;
    }

}
