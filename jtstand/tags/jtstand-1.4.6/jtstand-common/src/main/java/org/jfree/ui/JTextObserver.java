/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JTextObserver.java is part of JTStand.
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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

/**
 * An observer that selects all the text when a field gains the focus.
 *
 * @author Thomas Morgner
 */
public final class JTextObserver implements FocusListener {

    /** The singleton instance. */
    private static JTextObserver singleton;

    /**
     * Creates a new instance.
     */
    private JTextObserver() {
        // nothing required
    }

    /**
     * Returns the single instance.
     * 
     * @return The single instance.
     */
    public static JTextObserver getInstance() {
        if (singleton == null) {
           singleton = new JTextObserver();
        }
        return singleton;
    }

    /**
     * Selects all the text when a field gains the focus.
     * 
     * @param e  the focus event.
     */
    public void focusGained(final FocusEvent e) {
        if (e.getSource() instanceof JTextComponent) {
            final JTextComponent tex = (JTextComponent) e.getSource();
            tex.selectAll();
        }
    }

    /**
     * Deselects the text when a field loses the focus.
     * 
     * @param e  the event.
     */
    public void focusLost(final FocusEvent e) {
        if (e.getSource() instanceof JTextComponent) {
            final JTextComponent tex = (JTextComponent) e.getSource();
            tex.select(0, 0);
        }
    }

    /**
     * Adds this instance as a listener for the specified text component.
     * 
     * @param t  the text component.
     */
    public static void addTextComponent(final JTextComponent t) {
        if (singleton == null) {
            singleton = new JTextObserver();
        }
        t.addFocusListener(singleton);
    }

    /**
     * Removes this instance as a listener for the specified text component.
     * 
     * @param t  the text component.
     */
   public static void removeTextComponent(final JTextComponent t) {
        if (singleton == null) {
            singleton = new JTextObserver();
        }
        t.removeFocusListener(singleton);
    }
    
}
