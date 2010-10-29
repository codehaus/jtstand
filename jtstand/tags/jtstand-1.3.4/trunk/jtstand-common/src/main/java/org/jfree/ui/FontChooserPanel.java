/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FontChooserPanel.java is part of JTStand.
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
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import org.jfree.util.ResourceBundleWrapper;

/**
 * A panel for choosing a font from the available system fonts - still a bit of
 * a hack at the moment, but good enough for demonstration applications.
 *
 * @author David Gilbert
 */
public class FontChooserPanel extends JPanel {

    /** The font sizes that can be selected. */
    public static final String[] SIZES = {"9", "10", "11", "12", "14", "16",
            "18", "20", "22", "24", "28", "36", "48", "72"};

    /** The list of fonts. */
    private JList fontlist;

    /** The list of sizes. */
    private JList sizelist;

    /** The checkbox that indicates whether the font is bold. */
    private JCheckBox bold;

    /** The checkbox that indicates whether or not the font is italic. */
    private JCheckBox italic;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources =
        ResourceBundleWrapper.getBundle("org.jfree.ui.LocalizationBundle");

    /**
     * Standard constructor - builds a FontChooserPanel initialised with the
     * specified font.
     *
     * @param font  the initial font to display.
     */
    public FontChooserPanel(final Font font) {

        final GraphicsEnvironment g
                = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final String[] fonts = g.getAvailableFontFamilyNames();

        setLayout(new BorderLayout());
        final JPanel right = new JPanel(new BorderLayout());

        final JPanel fontPanel = new JPanel(new BorderLayout());
        fontPanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            localizationResources.getString("Font")));
        this.fontlist = new JList(fonts);
        final JScrollPane fontpane = new JScrollPane(this.fontlist);
        fontpane.setBorder(BorderFactory.createEtchedBorder());
        fontPanel.add(fontpane);
        add(fontPanel);

        final JPanel sizePanel = new JPanel(new BorderLayout());
        sizePanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createEtchedBorder(),
                            localizationResources.getString("Size")));
        this.sizelist = new JList(SIZES);
        final JScrollPane sizepane = new JScrollPane(this.sizelist);
        sizepane.setBorder(BorderFactory.createEtchedBorder());
        sizePanel.add(sizepane);

        final JPanel attributes = new JPanel(new GridLayout(1, 2));
        this.bold = new JCheckBox(localizationResources.getString("Bold"));
        this.italic = new JCheckBox(localizationResources.getString("Italic"));
        attributes.add(this.bold);
        attributes.add(this.italic);
        attributes.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                localizationResources.getString("Attributes")));

        right.add(sizePanel, BorderLayout.CENTER);
        right.add(attributes, BorderLayout.SOUTH);

        add(right, BorderLayout.EAST);

        setSelectedFont(font);
    }

    /**
     * Returns a Font object representing the selection in the panel.
     *
     * @return the font.
     */
    public Font getSelectedFont() {
        return new Font(getSelectedName(), getSelectedStyle(),
                getSelectedSize());
    }

    /**
     * Returns the selected name.
     *
     * @return the name.
     */
    public String getSelectedName() {
        return (String) this.fontlist.getSelectedValue();
    }

    /**
     * Returns the selected style.
     *
     * @return the style.
     */
    public int getSelectedStyle() {
        if (this.bold.isSelected() && this.italic.isSelected()) {
            return Font.BOLD + Font.ITALIC;
        }
        if (this.bold.isSelected()) {
            return Font.BOLD;
        }
        if (this.italic.isSelected()) {
            return Font.ITALIC;
        }
        else {
            return Font.PLAIN;
        }
    }

    /**
     * Returns the selected size.
     *
     * @return the size.
     */
    public int getSelectedSize() {
        final String selected = (String) this.sizelist.getSelectedValue();
        if (selected != null) {
            return Integer.parseInt(selected);
        }
        else {
            return 10;
        }
    }

    /**
     * Initializes the contents of the dialog from the given font
     * object.
     *
     * @param font the font from which to read the properties.
     */
    public void setSelectedFont (final Font font) {
        if (font == null) {
            throw new NullPointerException();
        }
        this.bold.setSelected(font.isBold());
        this.italic.setSelected(font.isItalic());

        final String fontName = font.getName();
        ListModel model = this.fontlist.getModel();
        this.fontlist.clearSelection();
        for (int i = 0; i < model.getSize(); i++) {
            if (fontName.equals(model.getElementAt(i))) {
                this.fontlist.setSelectedIndex(i);
                break;
            }
        }

        final String fontSize = String.valueOf(font.getSize());
        model = this.sizelist.getModel();
        this.sizelist.clearSelection();
        for (int i = 0; i < model.getSize(); i++) {
            if (fontSize.equals(model.getElementAt(i))) {
                this.sizelist.setSelectedIndex(i);
                break;
            }
        }
    }
}
