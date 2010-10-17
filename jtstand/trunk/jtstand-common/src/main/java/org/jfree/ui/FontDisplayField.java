/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FontDisplayField.java is part of JTStand.
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

import java.awt.Font;
import java.util.ResourceBundle;

import javax.swing.JTextField;

import org.jfree.util.ResourceBundleWrapper;

/**
 * A field for displaying a font selection.  The display field itself is
 * read-only, to the developer must provide another mechanism to allow the
 * user to change the font.
 *
 * @author David Gilbert
 */
public class FontDisplayField extends JTextField {

    /** The current font. */
    private Font displayFont;

    /** The resourceBundle for the localization. */
    protected static final ResourceBundle localizationResources =
            ResourceBundleWrapper.getBundle("org.jfree.ui.LocalizationBundle");

    /**
     * Standard constructor - builds a FontDescriptionField initialised with
     * the specified font.
     *
     * @param font  the font.
     */
    public FontDisplayField(final Font font) {
        super("");
        setDisplayFont(font);
        setEnabled(false);
    }

    /**
     * Returns the current font.
     *
     * @return the font.
     */
    public Font getDisplayFont() {
        return this.displayFont;
    }

    /**
     * Sets the font.
     *
     * @param font  the font.
     */
    public void setDisplayFont(final Font font) {
        this.displayFont = font;
        setText(fontToString(this.displayFont));
    }

    /**
     * Returns a string representation of the specified font.
     *
     * @param font  the font.
     *
     * @return a string describing the font.
     */
    private String fontToString(final Font font) {
        if (font != null) {
            return font.getFontName() + ", " + font.getSize();
        }
        else {
            return localizationResources.getString("No_Font_Selected");
        }
    }

}
