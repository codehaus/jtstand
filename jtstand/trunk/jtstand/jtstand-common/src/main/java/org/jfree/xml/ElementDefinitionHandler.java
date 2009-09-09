/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ElementDefinitionHandler.java is part of JTStand.
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

package org.jfree.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A element definition handler. The element definition handler is used to
 * represent a certain parser state. The current state is set in the parser
 * using the pushFactory() method. The parser forwards any incoming SAXEvent
 * to the current handler, until the handler is removed with popFactory().
 *
 * @author Thomas Morgner
 */
public interface ElementDefinitionHandler {

    /**
     * Callback to indicate that an XML element start tag has been read by the parser.
     *
     * @param tagName  the tag name.
     * @param attrs  the attributes.
     *
     * @throws SAXException if a parser error occurs or the validation failed.
     */
    public void startElement(String tagName, Attributes attrs) throws SAXException;

    /**
     * Callback to indicate that some character data has been read.
     *
     * @param ch  the character array.
     * @param start  the start index for the characters.
     * @param length  the length of the character sequence.
     * @throws SAXException if a parser error occurs or the validation failed.
     */
    public void characters(char[] ch, int start, int length) throws SAXException;

    /**
     * Callback to indicate that an XML element end tag has been read by the parser.
     *
     * @param tagName  the tag name.
     *
     * @throws SAXException if a parser error occurs or the validation failed.
     */
    public void endElement(String tagName) throws SAXException;

    /**
     * Returns the parser.
     *
     * @return The parser.
     */
    public Parser getParser();

}
