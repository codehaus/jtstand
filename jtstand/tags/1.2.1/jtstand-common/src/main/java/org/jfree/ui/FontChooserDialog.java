/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FontChooserDialog.java is part of JTStand.
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

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A dialog for choosing a font from the available system fonts.
 *
 * @author David Gilbert
 */
public class FontChooserDialog extends StandardDialog {

    /** The panel within the dialog that contains the font selection controls. */
    private FontChooserPanel fontChooserPanel;

    /**
     * Standard constructor - builds a font chooser dialog owned by another dialog.
     *
     * @param owner  the dialog that 'owns' this dialog.
     * @param title  the title for the dialog.
     * @param modal  a boolean that indicates whether or not the dialog is modal.
     * @param font  the initial font displayed.
     */
    public FontChooserDialog(final Dialog owner, final String title, final boolean modal, final Font font) {
        super(owner, title, modal);
        setContentPane(createContent(font));
    }

    /**
     * Standard constructor - builds a font chooser dialog owned by a frame.
     *
     * @param owner  the frame that 'owns' this dialog.
     * @param title  the title for the dialog.
     * @param modal  a boolean that indicates whether or not the dialog is modal.
     * @param font  the initial font displayed.
     */
    public FontChooserDialog(final Frame owner, final String title, final boolean modal, final Font font) {
        super(owner, title, modal);
        setContentPane(createContent(font));
    }

    /**
     * Returns the selected font.
     *
     * @return the font.
     */
    public Font getSelectedFont() {
        return this.fontChooserPanel.getSelectedFont();
    }

    /**
     * Returns the panel that is the user interface.
     *
     * @param font  the font.
     *
     * @return the panel.
     */
    private JPanel createContent(Font font) {
        final JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        if (font == null) {
            font = new Font("Dialog", 10, Font.PLAIN);
        }
        this.fontChooserPanel = new FontChooserPanel(font);
        content.add(this.fontChooserPanel);

        final JPanel buttons = createButtonPanel();
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        content.add(buttons, BorderLayout.SOUTH);

        return content;
    }

}
