/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, IntegerDocument.java is part of JTStand.
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

package org.jfree.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A document for editing integers.
 *
 * @author Andrzej Porebski
 */
public class IntegerDocument extends PlainDocument {

    /**
     * Inserts a string.
     *
     * @param i  i.
     * @param s  s.
     * @param attributes  the attributes.
     *
     * @throws BadLocationException ??
     */
    public void insertString(final int i, final String s, final AttributeSet attributes)
        throws BadLocationException {

        super.insertString(i, s, attributes);
        if (s != null && (!s.equals("-") || i != 0 || s.length() >= 2)) {
            try {
                Integer.parseInt(getText(0, getLength()));
            }
            catch (NumberFormatException e) {
                remove(i, s.length());
            }
        }
    }

}
