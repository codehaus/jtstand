/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StringReadHandler.java is part of JTStand.
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
import org.jfree.xml.parser.XmlReaderException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Required for list contents ...
 */
public class StringReadHandler extends AbstractXmlReadHandler
{

  /**
   * A string buffer.
   */
  private StringBuffer buffer;

  /**
   * The string under construction.
   */
  private String result;

  /**
   * Creates a new handler.
   */
  public StringReadHandler ()
  {
    super();
  }

  /**
   * Starts parsing.
   *
   * @param attrs the attributes.
   * @throws SAXException if there is a parsing error.
   */
  protected void startParsing (final Attributes attrs)
          throws SAXException
  {
    this.buffer = new StringBuffer();
  }

  /**
   * This method is called to process the character data between element tags.
   *
   * @param ch     the character buffer.
   * @param start  the start index.
   * @param length the length.
   * @throws SAXException if there is a parsing error.
   */
  public void characters (final char[] ch, final int start, final int length)
          throws SAXException
  {
    this.buffer.append(ch, start, length);
  }

  /**
   * Done parsing.
   *
   * @throws SAXException       if there is a parsing error.
   * @throws XmlReaderException if there is a reader error.
   */
  protected void doneParsing ()
          throws SAXException, XmlReaderException
  {
    this.result = this.buffer.toString();
    this.buffer = null;
  }

  /**
   * Returns the result.
   *
   * @return The result.
   */
  public String getResult ()
  {
    return this.result;
  }

  /**
   * Returns the object for this element.
   *
   * @return the object.
   */
  public Object getObject ()
  {
    return this.result;
  }
}
