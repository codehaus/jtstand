/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, RenderingHintValueReadHandler.java is part of JTStand.
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.jfree.util.Log;
import org.jfree.xml.parser.AbstractXmlReadHandler;
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A read handler for a rendering hint value.
 */
public class RenderingHintValueReadHandler extends AbstractXmlReadHandler {

    /** The key under construction. */
    private Object key;
    
    /** The value under construction. */
    private Object value;

    /**
     * Creates a new read handler.
     */
    public RenderingHintValueReadHandler() {
        super();
    }

    /**
     * Starts parsing.
     *
     * @param attrs  the attributes.
     *
     * @throws SAXException if there is a parsing error.
     */
    protected void startParsing(final Attributes attrs) throws SAXException {
        final String keyText = attrs.getValue("key");
        final String valueText = attrs.getValue("value");
        this.key = stringToHintField(keyText);
        this.value = stringToHintField(valueText);
    }

    private Object stringToHintField (final String name) {
        final Field[] fields = RenderingHints.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            final Field f = fields[i];
            if (Modifier.isFinal(f.getModifiers()) 
                && Modifier.isPublic(f.getModifiers()) 
                && Modifier.isStatic(f.getModifiers())) {
                try {
                    final String fieldName = f.getName();
                    if (fieldName.equals(name)) {
                        return f.get(null);
                    }
                }
                catch (Exception e) {
                    Log.info ("Unable to write RenderingHint", e);
                }
            }
        }
        throw new IllegalArgumentException("Invalid value given");
    }

    /**
     * Returns the object for this element.
     *
     * @return the object.
     *
     * @throws XmlReaderException if there is a parsing error.
     */
    public Object getObject() throws XmlReaderException {
        return new Object[] {this.key, this.value};
    }

    /**
     * Returns the key.
     * 
     * @return the key.
     */
    public Object getKey() {
        return this.key;
    }

    /**
     * Returns the value.
     * 
     * @return the value.
     */
    public Object getValue() {
        return this.value;
    }
    
}
