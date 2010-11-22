/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, XMLWriter.java is part of JTStand.
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

package org.jfree.xml.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * A class for writing XML to a character stream.
 */
public class XMLWriter extends XMLWriterSupport {

    /**
     * The character stream.
     */
    private Writer writer;

    /**
     * Creates a new XML writer for the specified character stream.  By 
     * default, four spaces are used for indentation.
     *
     * @param writer the character stream.
     */
    public XMLWriter(final Writer writer) {
        this(writer, "    ");
    }

    /**
     * Creates a new XML writer for the specified character stream.
     *
     * @param writer       the character stream.
     * @param indentString the string used for indentation (should contain 
     *                     white space, for example four spaces).
     */
    public XMLWriter(final Writer writer, final String indentString) {
        super(new SafeTagList(), 0, indentString);
        if (writer == null) {
            throw new NullPointerException("Writer must not be null.");
        }

        this.writer = writer;
    }

    /**
     * Writes the XML declaration that usually appears at the top of every XML 
     * file.
     * 
     * @throws IOException if there is a problem writing to the character 
     *                     stream.               
     */
    public void writeXmlDeclaration() throws IOException {
        this.writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        this.writer.write(getLineSeparator());
    }

    /**
     * Writes an opening XML tag that has no attributes.
     *
     * @param name  the tag name.
     * @param close a flag that controls whether or not the tag is closed 
     *              immediately.
     * @throws java.io.IOException if there is an I/O problem.
     */
    public void writeTag(final String name, final boolean close) 
            throws IOException {
        if (close) {
            writeTag(this.writer, name, new AttributeList(), close);
        }
        else {
            writeTag(this.writer, name);
        }
    }

    /**
     * Writes a closing XML tag.
     *
     * @param tag the tag name.
     * @throws java.io.IOException if there is an I/O problem.
     */
    public void writeCloseTag(final String tag) throws IOException {
        writeCloseTag(this.writer, tag);
    }

    /**
     * Writes an opening XML tag with an attribute/value pair.
     *
     * @param name           the tag name.
     * @param attributeName  the attribute name.
     * @param attributeValue the attribute value.
     * @param close          controls whether the tag is closed.
     * @throws java.io.IOException if there is an I/O problem.
     */
    public void writeTag(final String name, final String attributeName, 
                         final String attributeValue, final boolean close) 
            throws IOException {
        writeTag(this.writer, name, attributeName, attributeValue, close);
    }

    /**
     * Writes an opening XML tag along with a list of attribute/value pairs.
     *
     * @param name       the tag name.
     * @param attributes the attributes.
     * @param close      controls whether the tag is closed.
     * @throws java.io.IOException if there is an I/O problem.
     */
    public void writeTag(final String name, final AttributeList attributes, 
            final boolean close) throws IOException {
        writeTag(this.writer, name, attributes, close);
    }

    /**
     * Writes an opening XML tag along with a list of attribute/value pairs.
     *
     * @param name       the tag name.
     * @param attributes the attributes.
     * @param close      controls whether the tag is closed.
     * @throws java.io.IOException if there is an I/O problem.
     * @deprecated use the attribute list instead ...
     */
    public void writeTag(final String name, final Properties attributes, 
            final boolean close) throws IOException {
        writeTag(this.writer, name, attributes, close);
    }

    /**
     * Writes some text to the character stream.
     *
     * @param text the text.
     * @throws IOException if there is a problem writing to the character 
     *                     stream.
     */
    public void writeText(final String text) throws IOException {
        this.writer.write(text);
    }

    /**
     * Closes the underlying character stream.
     * 
     * @throws IOException if there is a problem closing the character stream.
     */
    public void close() throws IOException {
        this.writer.close();
    }

}
