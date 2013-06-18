/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, RenderingHintsReadHandler.java is part of JTStand.
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

import java.awt.RenderingHints;
import java.util.ArrayList;

import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A read handler that can parse the XML element for a {@link RenderingHints} collection.
 */
public class RenderingHintsReadHandler extends AbstractXmlReadHandler {

    /** The subhandlers. */
    private ArrayList handlers;
    
    /** The rendering hints under construction. */
    private RenderingHints renderingHints;

    /**
     * Creates a new read handler.
     */
    public RenderingHintsReadHandler() {
        super();
    }

    /**
     * Starts parsing.
     *
     * @param attrs  the attributes.
     *
     * @throws SAXException never.
     */
    protected void startParsing(final Attributes attrs) throws SAXException {
        this.handlers = new ArrayList();
    }

    /**
     * Returns the handler for a child element.
     *
     * @param tagName  the tag name.
     * @param atts  the attributes.
     *
     * @return the handler.
     *
     * @throws SAXException  if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    protected XmlReadHandler getHandlerForChild(final String tagName, final Attributes atts)
        throws XmlReaderException, SAXException {

        if (!tagName.equals("entry")) {
            throw new SAXException("Expected 'entry' tag.");
        }

        final XmlReadHandler handler = new RenderingHintValueReadHandler();
        this.handlers.add(handler);
        return handler;
    }

    /**
     * Done parsing.
     *
     * @throws SAXException if there is a parsing error.
     * @throws XmlReaderException if there is a reader error.
     */
    protected void doneParsing() throws SAXException, XmlReaderException {
        this.renderingHints = new RenderingHints(null);

        for (int i = 0; i < this.handlers.size(); i++) {
            final RenderingHintValueReadHandler rh =
                (RenderingHintValueReadHandler) this.handlers.get(i);
            this.renderingHints.put(rh.getKey(), rh.getValue());
        }
    }

    /**
     * Returns the object for this element.
     *
     * @return the object.
     *
     * @throws XmlReaderException if there is a parsing error.
     */
    public Object getObject() throws XmlReaderException {
        return this.renderingHints;
    }
}
