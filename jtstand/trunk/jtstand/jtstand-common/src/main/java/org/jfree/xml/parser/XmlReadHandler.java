/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, XmlReadHandler.java is part of JTStand.
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

package org.jfree.xml.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A handler for reading an XML element.
 */
public interface XmlReadHandler {
    
    /**
     * This method is called at the start of an element.
     * 
     * @param tagName  the tag name.
     * @param attrs  the attributes.
     * 
     * @throws SAXException if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    public void startElement(String tagName, Attributes attrs)
        throws SAXException, XmlReaderException;
    
    /**
     * This method is called to process the character data between element tags.
     * 
     * @param ch  the character buffer.
     * @param start  the start index.
     * @param length  the length.
     * 
     * @throws SAXException if there is a parsing error.
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException;
    
    /**
     * This method is called at the end of an element.
     * 
     * @param tagName  the tag name.
     * 
     * @throws SAXException if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    public void endElement(String tagName)
        throws SAXException, XmlReaderException;
    
    /**
     * Returns the object for this element or null, if this element does
     * not create an object.
     * 
     * @return the object.
     * 
     * @throws XmlReaderException if there is a parsing error.
     */
    public Object getObject() throws XmlReaderException;
    
    /**
     * Initialise.
     * 
     * @param rootHandler  the root handler.
     * @param tagName  the tag name.
     */
    public void init(RootXmlReadHandler rootHandler, String tagName);

}
