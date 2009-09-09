/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, InsetsTextField.java is part of JTStand.
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

import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JTextField;

import org.jfree.util.ResourceBundleWrapper;

/**
 * A JTextField for displaying insets.
 *
 * @author Andrzej Porebski
 */
public class InsetsTextField extends JTextField {

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.ui.LocalizationBundle");

    /**
     * Default constructor. Initializes this text field with formatted string
     * describing provided insets.
     *
     * @param insets  the insets.
     */
    public InsetsTextField(final Insets insets) {
        super();
        setInsets(insets);
        setEnabled(false);
    }

    /**
     * Returns a formatted string describing provided insets.
     *
     * @param insets  the insets.
     *
     * @return the string.
     */
    public String formatInsetsString(Insets insets) {
        insets = (insets == null) ? new Insets(0, 0, 0, 0) : insets;
        return
            localizationResources.getString("T") + insets.top + ", "
             + localizationResources.getString("L") + insets.left + ", "
             + localizationResources.getString("B") + insets.bottom + ", "
             + localizationResources.getString("R") + insets.right;
    }

    /**
     * Sets the text of this text field to the formatted string
     * describing provided insets. If insets is null, empty insets
     * (0,0,0,0) are used.
     *
     * @param insets  the insets.
     */
    public void setInsets(final Insets insets) {
        setText(formatInsetsString(insets));
    }

}
