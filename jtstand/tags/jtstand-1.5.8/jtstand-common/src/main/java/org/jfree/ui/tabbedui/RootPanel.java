/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, RootPanel.java is part of JTStand.
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

package org.jfree.ui.tabbedui;

import javax.swing.JComponent;

/**
 * A root panel is a panel implementation of an root editor.
 *
 * @author Thomas Morgner
 */
public abstract class RootPanel extends JComponent implements RootEditor {

    /**a flag indicating whether this editor is the currently active editor. */
    private boolean active;

    /**
     * Default constructor.
     */
    public RootPanel() {
        // nothing required.
    }

    /**
     * Returns a flag that indicates whether the panel is active or not.
     * 
     * @return A flag.
     */
    public final boolean isActive() {
        return this.active;
    }

    /**
     * Called when the panel is activated.
     */
    protected void panelActivated()
    {
    }

    /**
     * Called when the panel is deactivated.
     */
    protected void panelDeactivated()
    {
    }

    /**
     * Sets the status of the panel to active or inactive.
     * 
     * @param active  the flag.
     */
    public final void setActive(final boolean active) {
        if (this.active == active) {
            return;
        }
        this.active = active;
        if (active) {
            panelActivated();
        } 
        else {
            panelDeactivated();
        }
    }

    /**
     * Returns the main panel. Returns the self reference, as this panel
     * implements all necessary methods.
     *
     * @return The main panel.
     */
    public JComponent getMainPanel() {
        return this;
    }

    /**
     * Returns the toolbar. This default implementation return null, to indicate
     * that no toolbar is used.
     *
     * @return The toolbar.
     */
    public JComponent getToolbar() {
        return null;
    }


}
