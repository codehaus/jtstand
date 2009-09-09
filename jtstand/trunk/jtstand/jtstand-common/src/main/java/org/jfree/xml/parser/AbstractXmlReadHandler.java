/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AbstractXmlReadHandler.java is part of JTStand.
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
import org.jfree.util.Log;

/**
 * A base class for implementing an {@link XmlReadHandler}.
 */
public abstract class AbstractXmlReadHandler implements XmlReadHandler {
    /** The root handler. */
    private RootXmlReadHandler rootHandler;

    /** The tag name. */
    private String tagName;

    /** A flag indicating the first call. */
    private boolean firstCall = true;

    /**
     * Creates a new handler.
     */
    public AbstractXmlReadHandler() {
    }

    /**
     * Initialises the handler.
     *
     * @param rootHandler  the root handler.
     * @param tagName  the tag name.
     */
    public void init(final RootXmlReadHandler rootHandler, final String tagName) {
        if (rootHandler == null) {
            throw new NullPointerException("Root handler must not be null");
        }
        if (tagName == null) {
            throw new NullPointerException("Tag name must not be null");
        }
        this.rootHandler = rootHandler;
        this.tagName = tagName;
    }

    /**
     * This method is called at the start of an element.
     *
     * @param tagName  the tag name.
     * @param attrs  the attributes.
     *
     * @throws SAXException if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    public final void startElement(final String tagName, final Attributes attrs)
        throws XmlReaderException, SAXException {
        if (this.firstCall) {
            if (!this.tagName.equals(tagName)) {
                throw new SAXException("Expected <" + this.tagName + ">, found <" + tagName + ">");
            }
            this.firstCall = false;
            startParsing(attrs);
        }
        else {
            final XmlReadHandler childHandler = getHandlerForChild(tagName, attrs);
            if (childHandler == null) {
                Log.warn ("Unknown tag <" + tagName + ">");
                return;
            }
            childHandler.init(getRootHandler(), tagName);
            this.rootHandler.recurse(childHandler, tagName, attrs);
        }
    }

    /**
     * This method is called to process the character data between element tags.
     *
     * @param ch  the character buffer.
     * @param start  the start index.
     * @param length  the length.
     *
     * @throws SAXException if there is a parsing error.
     */
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        // nothing required
    }

    /**
     * This method is called at the end of an element.
     *
     * @param tagName  the tag name.
     *
     * @throws SAXException if there is a parsing error.
     */
    public final void endElement(final String tagName) throws SAXException {
        if (this.tagName.equals(tagName)) {
            try {
                doneParsing();
                this.rootHandler.unwind(tagName);
            }
            catch (XmlReaderException xre) {
                throw new SAXException(xre);
            }
        }
    }

    /**
     * Starts parsing.
     *
     * @param attrs  the attributes.
     *
     * @throws SAXException if there is a parsing error.
     * @throws XmlReaderException ?
     */
    protected void startParsing(final Attributes attrs)
        throws SAXException, XmlReaderException {
        // nothing required
    }

    /**
     * Done parsing.
     *
     * @throws SAXException if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    protected void doneParsing() throws SAXException, XmlReaderException {
        // nothing required
    }

    /**
     * Returns the handler for a child element.
     *
     * @param tagName  the tag name.
     * @param atts  the attributes.
     *
     * @return the handler or null, if the tagname is invalid.
     *
     * @throws SAXException  if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    protected XmlReadHandler getHandlerForChild(final String tagName, final Attributes atts)
        throws XmlReaderException, SAXException {
        return null;
    }

    /**
     * Returns the tag name.
     *
     * @return the tag name.
     */
    public String getTagName() {
        return this.tagName;
    }

    /**
     * Returns the root handler for the parsing.
     *
     * @return the root handler.
     */
    public RootXmlReadHandler getRootHandler() {
        return this.rootHandler;
    }

}

