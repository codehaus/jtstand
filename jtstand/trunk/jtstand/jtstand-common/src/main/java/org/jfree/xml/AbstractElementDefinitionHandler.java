/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AbstractElementDefinitionHandler.java is part of JTStand.
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
 * An abstract element definition handler.
 * 
 * @author Thomas Morgner
 */
public abstract class AbstractElementDefinitionHandler implements ElementDefinitionHandler {

    /** A parser. */
    private Parser parser;

    /**
     * Creates a new handler.
     * 
     * @param parser  the parser.
     */
    public AbstractElementDefinitionHandler(final Parser parser) {
        this.parser = parser;
    }

    /**
     * Callback to indicate that an XML element start tag has been read by the parser.
     *
     * @param tagName  the tag name.
     * @param attrs  the attributes.
     *
     * @throws SAXException if a parser error occurs or the validation failed.
     */
    public void startElement(final String tagName, final Attributes attrs) throws SAXException {
        // nothing required
    }

    /**
     * Callback to indicate that some character data has been read.
     *
     * @param ch  the character array.
     * @param start  the start index for the characters.
     * @param length  the length of the character sequence.
     * @throws SAXException if a parser error occurs or the validation failed.
     */
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        // nothing required
    }

    /**
     * Callback to indicate that an XML element end tag has been read by the parser.
     *
     * @param tagName  the tag name.
     *
     * @throws SAXException if a parser error occurs or the validation failed.
     */
    public void endElement(final String tagName) throws SAXException {
        // nothing required
    }

    /**
     * Returns the parser.
     *
     * @return The parser.
     */
    public Parser getParser() {
        return this.parser;
    }
}
