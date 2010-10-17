/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PadMessage.java is part of JTStand.
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

package org.jfree.base.log;

import java.util.Arrays;

/**
 * A message object that pads the output if the text is shorter than
 * the given length. This is usefull when concating multiple messages,
 * which should appear in a table like style.
 *
 * @author Thomas Morgner
 */
public class PadMessage {

    /**
     * The message.
     */
    private final Object text;
  
    /**
     * The padding size.
     */
    private final int length;

    /**
     * Creates a new message.
     *
     * @param message the message.
     * @param length  the padding size.
     */
    public PadMessage(final Object message, final int length) {
        this.text = message;
        this.length = length;
    }

    /**
     * Returns a string representation of the message.
     *
     * @return the string.
     */
    public String toString() {
        final StringBuffer b = new StringBuffer();
        b.append(this.text);
        if (b.length() < this.length) {
            final char[] pad = new char[this.length - b.length()];
            Arrays.fill(pad, ' ');
            b.append(pad);
        }
        return b.toString();
    }
    
}
