/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DetailEditor.java is part of JTStand.
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
 * A detail editor.
 *
 * @author Thomas Morgner
 */
public abstract class DetailEditor extends JComponent {

    /** The object, that is edited. */
    private Object object;
    /** whether the edit process has been confirmed (user pressed OK). */
    private boolean confirmed;

    /**
     * Creates a new editor.
     */
    public DetailEditor() {
        // nothing required
    }

    /**
     * Updates the object.
     */
    public void update() {
        if (this.object == null) {
            throw new IllegalStateException();
        }
        else {
            updateObject(this.object);
        }
        setConfirmed(false);
    }

    /**
     * Returns the object.
     * 
     * @return The object.
     */
    public Object getObject() {
        return this.object;
    }

    /**
     * Sets the object to be edited.
     * 
     * @param object  the object.
     */
    public void setObject(final Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        this.object = object;
        setConfirmed(false);
        fillObject();
    }

    /**
     * Parses an integer.
     * 
     * @param text  the text.
     * @param def  the default value.
     * 
     * @return The parsed integer, or the default value if the string didn't contain a
     *         value.
     */
    protected static int parseInt(final String text, final int def) {
        try {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException fe) {
            return def;
        }
    }

    /**
     * Clears the editor.
     */
    public abstract void clear();

    /**
     * Edits the object. The object itself should not be modified, until
     * update or create was called.
     */
    protected abstract void fillObject();

    /**
     * Updates the object.
     * 
     * @param object  the object.
     */
    protected abstract void updateObject(Object object);

    /**
     * Returns the confirmed flag.
     * 
     * @return The confirmed flag.
     */
    public boolean isConfirmed() {
        return this.confirmed;
    }

    /**
     * Sets the confirmed flag.
     * 
     * @param confirmed  the confirmed flag.
     */
    protected void setConfirmed(final boolean confirmed) {
        final boolean oldConfirmed = this.confirmed;
        this.confirmed = confirmed;
        firePropertyChange("confirmed", oldConfirmed, confirmed);
    }

    
}
