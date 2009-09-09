/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, WizardPanel.java is part of JTStand.
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

import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * A panel that provides the user interface for a single step in a WizardDialog.
 *
 * @author David Gilbert
 */
public abstract class WizardPanel extends JPanel {

    /** The owner. */
    private WizardDialog owner;

    /**
     * Creates a new panel.
     *
     * @param layout  the layout manager.
     */
    protected WizardPanel(final LayoutManager layout) {
        super(layout);
    }

    /**
     * Returns a reference to the dialog that owns the panel.
     *
     * @return the owner.
     */
    public WizardDialog getOwner() {
        return this.owner;
    }

    /**
     * Sets the reference to the dialog that owns the panel (this is called automatically by
     * the dialog when the panel is added to the dialog).
     *
     * @param owner  the owner.
     */
    public void setOwner(final WizardDialog owner) {
        this.owner = owner;
    }

    /**
     * Returns the result.
     *
     * @return the result.
     */
    public Object getResult() {
        return null;
    }

    /**
     * This method is called when the dialog redisplays this panel as a result of the user clicking
     * the "Previous" button.  Inside this method, subclasses should make a note of their current
     * state, so that they can decide what to do when the user hits "Next".
     */
    public abstract void returnFromLaterStep();

    /**
     * Returns true if it is OK to redisplay the last version of the next panel, or false if a new
     * version is required.
     *
     * @return boolean.
     */
    public abstract boolean canRedisplayNextPanel();

    /**
     * Returns true if there is a next panel.
     *
     * @return boolean.
     */
    public abstract boolean hasNextPanel();

    /**
     * Returns true if it is possible to finish from this panel.
     *
     * @return boolean.
     */
    public abstract boolean canFinish();

    /**
     * Returns the next panel in the sequence, given the current user input.  Returns null if this
     * panel is the last one in the sequence.
     *
     * @return the next panel in the sequence.
     */
    public abstract WizardPanel getNextPanel();

}
