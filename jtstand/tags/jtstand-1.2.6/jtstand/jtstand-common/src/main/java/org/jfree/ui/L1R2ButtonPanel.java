/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, L1R2ButtonPanel.java is part of JTStand.
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
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A 'ready-made' panel that has one button on the left and two buttons on the right - nested
 * panels and layout managers take care of resizing.
 *
 * @author David Gilbert
 */
public class L1R2ButtonPanel extends JPanel {

    /** The left button. */
    private JButton left;

    /** The first button on the right of the panel. */
    private JButton right1;

    /** The second button on the right of the panel. */
    private JButton right2;

    /**
     * Standard constructor - creates a three button panel with the specified button labels.
     *
     * @param label1  the label for button 1.
     * @param label2  the label for button 2.
     * @param label3  the label for button 3.
     */
    public L1R2ButtonPanel(final String label1, final String label2, final String label3) {

        setLayout(new BorderLayout());

        // create the pieces...
        this.left = new JButton(label1);

        final JPanel rightButtonPanel = new JPanel(new GridLayout(1, 2));
        this.right1 = new JButton(label2);
        this.right2 = new JButton(label3);
        rightButtonPanel.add(this.right1);
        rightButtonPanel.add(this.right2);

        // ...and put them together
        add(this.left, BorderLayout.WEST);
        add(rightButtonPanel, BorderLayout.EAST);

    }

    /**
     * Returns a reference to button 1, allowing the caller to set labels, action-listeners etc.
     *
     * @return the left button.
     */
    public JButton getLeftButton() {
        return this.left;
    }

    /**
     * Returns a reference to button 2, allowing the caller to set labels, action-listeners etc.
     *
     * @return the right button 1.
     */
    public JButton getRightButton1() {
        return this.right1;
    }

    /**
     * Returns a reference to button 3, allowing the caller to set labels, action-listeners etc.
     *
     * @return  the right button 2.
     */
    public JButton getRightButton2() {
        return this.right2;
    }

}
