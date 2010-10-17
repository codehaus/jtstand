/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Base64ReadHandler.java is part of JTStand.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jfree.xml.util.Base64;
import org.xml.sax.SAXException;

/**
 * A read handler for Base64 encoded elements.
 * 
 * @deprecated base64 encoded elements are no longer supported ...
 */
public class Base64ReadHandler extends AbstractXmlReadHandler {
    
    /** The encoded object. */
    private String encodedObject;
    
    /**
     * Creates a new handler.
     */
    public Base64ReadHandler() {
        super();
    }

    /**
     * Process character data.
     * 
     * @param ch  the character buffer.
     * @param start  the start index.
     * @param length  the number of characters.
     * 
     * @throws SAXException ???.
     */
    public void characters(final char[] ch, final int start, final int length)
        throws SAXException {
        this.encodedObject = new String(ch, start, length);
    }

    /**
     * Returns the object under construction.
     * 
     * @return the object
     * 
     * @throws XmlReaderException ???.
     */
    public Object getObject() throws XmlReaderException {
        try {
            final byte[] bytes = Base64.decode(this.encodedObject.toCharArray());
            final ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(bytes));
            return in.readObject();
        } 
        catch (IOException e) {
            throw new XmlReaderException("Can't read class for <" + getTagName() + ">", e);
        } 
        catch (ClassNotFoundException e) {
            throw new XmlReaderException("Class not found for <" + getTagName() + ">", e);
        }
    }
}
