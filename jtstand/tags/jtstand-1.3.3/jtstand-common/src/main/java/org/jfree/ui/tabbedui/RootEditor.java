/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, RootEditor.java is part of JTStand.
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

import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * A root editor reprensents a tab in a TabbedUI.
 *
 * @author Thomas Morgner
 */
public interface RootEditor {

    /**
     * Sets the editor active or inactive.
     * 
     * @param b  a boolean.
     */
    public void setActive(boolean b);
  
    /**
     * Returns the active or inactive status of the editor.
     * 
     * @return A boolean.
     */
    public boolean isActive();

    /**
     * Returns the editor name.
     * 
     * @return The editor name.
     */
    public String getEditorName();

    /**
     * Returns the menus.
     * 
     * @return The menus.
     */
    public JMenu[] getMenus();
    
    /**
     * Returns the toolbar.
     * 
     * @return The toolbar.
     */
    public JComponent getToolbar();

    /**
     * Returns the main panel.
     * 
     * @return The main panel.
     */
    public JComponent getMainPanel();

    /**
     * Checks, whether this root editor is enabled.
     *
     * @return true, if the editor is enabled, false otherwise.
     */
    public boolean isEnabled();

    /**
     * Adds a property change listener.
     * 
     * @param property  the property name.
     * @param l  the listener.
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener l);
    
    /**
     * Removes a property change listener.
     * 
     * @param property  the property name.
     * @param l  the listener.
     */
    public void removePropertyChangeListener(String property, PropertyChangeListener l);

    /**
     * Adds a property change listener.
     * 
     * @param l  the listener.
     */
    public void addPropertyChangeListener(PropertyChangeListener l);
    
    /**
     * Removes a property change listener.
     * 
     * @param l  the listener.
     */
    public void removePropertyChangeListener(PropertyChangeListener l);

}
