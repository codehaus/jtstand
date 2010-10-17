/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, L1R1ButtonPanel.java is part of JTStand.
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
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A 'ready-made' panel that has one button on the left and another button on the right - a layout
 * manager takes care of resizing.
 *
 * @author David Gilbert
 */
public class L1R1ButtonPanel extends JPanel {

    /** The button on the left. */
    private JButton left;

    /** The button on the right. */
    private JButton right;

    /**
     * Standard constructor - creates a two-button panel with the specified labels.
     *
     * @param leftLabel  the label for the left button.
     * @param rightLabel  the label for the right button.
     */
    public L1R1ButtonPanel(final String leftLabel, final String rightLabel) {

        setLayout(new BorderLayout());
        this.left = new JButton(leftLabel);
        this.right = new JButton(rightLabel);
        add(this.left, BorderLayout.WEST);
        add(this.right, BorderLayout.EAST);

    }

    /**
     * Returns a reference to button 1, allowing the caller to set labels, action-listeners etc.
     *
     * @return the button.
     */
    public JButton getLeftButton() {
        return this.left;
    }

    /**
     * Returns a reference to button 2, allowing the caller to set labels, action-listeners etc.
     *
     * @return the button.
     */
    public JButton getRightButton() {
        return this.right;
    }

}
