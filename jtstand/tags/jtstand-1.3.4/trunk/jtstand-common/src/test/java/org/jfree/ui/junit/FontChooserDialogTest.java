/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FontChooserDialogTest.java is part of JTStand.
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

package org.jfree.ui.junit;

import java.awt.Font;
import java.awt.Frame;

import junit.framework.TestCase;
import org.jfree.ui.FontChooserDialog;

/**
 * A test for the {@link FontChooserDialog} class.
 */
public class FontChooserDialogTest extends TestCase {

    /**
     * Creates a new test.
     * 
     * @param s  the test name.
     */
    public FontChooserDialogTest(final String s) {
        super(s);
    }

    /**
     * Checks that it is possible to create a dialog.
     */
    public void testCreateDialog () {
        try {
            new FontChooserDialog
                (new Frame(), "Title", false, new Font("Serif", Font.PLAIN, 10));
        }
        catch (UnsupportedOperationException use) {
            // Headless mode exception is instance of this ex.
        }
    }
}
