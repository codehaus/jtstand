/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TabbedFrame.java is part of JTStand.
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
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A JFrame implementation that uses a tabbed UI as backend.
 *
 * @author Thomas Morgner
 */
public class TabbedFrame extends JFrame {

    /** The backend. */
    private AbstractTabbedUI tabbedUI;

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

    /**
     * Default constructor.
     */
    public TabbedFrame() {
    }

    /**
     * Creates a new tabbed frame with the specified title.
     *
     * @param title  the frame title.
     */
    public TabbedFrame(final String title) {
        super(title);
    }

    /**
     * Returns the UI implementation for the frame.
     *
     * @return Returns the tabbedUI.
     */
    protected final AbstractTabbedUI getTabbedUI()
    {
      return this.tabbedUI;
    }

    /**
     * Initialises the dialog.
     *
     * @param tabbedUI  the UI that controls the dialog.
     */
    public void init(final AbstractTabbedUI tabbedUI) {

        this.tabbedUI = tabbedUI;
        this.tabbedUI.addPropertyChangeListener(
            AbstractTabbedUI.JMENUBAR_PROPERTY, new MenuBarChangeListener()
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                getTabbedUI().getCloseAction().actionPerformed
                    (new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, 0));
            }
        });

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(tabbedUI, BorderLayout.CENTER);
        setContentPane(panel);
        setJMenuBar(tabbedUI.getJMenuBar());
    }

}
