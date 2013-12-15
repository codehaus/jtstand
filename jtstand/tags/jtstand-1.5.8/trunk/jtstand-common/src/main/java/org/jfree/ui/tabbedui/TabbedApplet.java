/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TabbedApplet.java is part of JTStand.
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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * An applet implementation that uses a tabbed GUI as backend.
 *
 * @author Thomas Morgner
 */
public class TabbedApplet extends JApplet {

    /**
     * A property change listener that waits for the menubar to change.
     */
    private class MenuBarChangeListener implements PropertyChangeListener {
        /**
         * Creates a new change listener.
         */
        public MenuBarChangeListener() {
        }

        /**
         * This method gets called when a bound property is changed.
         *
         * @param evt A PropertyChangeEvent object describing the event source
         *            and the property that has changed.
         */
        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(AbstractTabbedUI.JMENUBAR_PROPERTY)) {
                setJMenuBar(getTabbedUI().getJMenuBar());
            }
        }
    }

    /** The UI for the applet. */
    private AbstractTabbedUI tabbedUI;

    /**
     * Default constructor.
     */
    public TabbedApplet() {
    }


    /**
     * Returns the UI implementation for the applet.
     *
     * @return Returns the tabbedUI.
     */
    protected final AbstractTabbedUI getTabbedUI()
    {
      return this.tabbedUI;
    }
    /**
     * Initialises the applet.
     *
     * @param tabbedUI  the UI that controls the applet.
     */
    public void init(final AbstractTabbedUI tabbedUI) {

        this.tabbedUI = tabbedUI;
        this.tabbedUI.addPropertyChangeListener
            (AbstractTabbedUI.JMENUBAR_PROPERTY, new MenuBarChangeListener());

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(tabbedUI, BorderLayout.CENTER);
        setContentPane(panel);
        setJMenuBar(tabbedUI.getJMenuBar());
    }

}
