/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ApplicationFrame.java is part of JTStand.
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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 * A base class for creating the main frame for simple applications.  The frame listens for
 * window closing events, and responds by shutting down the JVM.  This is OK for small demo
 * applications...for more serious applications, you'll want to use something more robust.
 *
 * @author David Gilbert
 */
public class ApplicationFrame extends JFrame implements WindowListener {

    /**
     * Constructs a new application frame.
     *
     * @param title  the frame title.
     */
    public ApplicationFrame(final String title) {
        super(title);
        addWindowListener(this);
    }

    /**
     * Listens for the main window closing, and shuts down the application.
     *
     * @param event  information about the window event.
     */
    public void windowClosing(final WindowEvent event) {
        if (event.getWindow() == this) {
            dispose();
            System.exit(0);
        }
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     *
     * @param event  information about the window event.
     */
    public void windowClosed(final WindowEvent event) {
        // ignore
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     *
     * @param event  information about the window event.
     */
    public void windowActivated(final WindowEvent event) {
        // ignore
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     *
     * @param event  information about the window event.
     */
    public void windowDeactivated(final WindowEvent event) {
        // ignore
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     *
     * @param event  information about the window event.
     */
    public void windowDeiconified(final WindowEvent event) {
        // ignore
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     *
     * @param event  information about the window event.
     */
    public void windowIconified(final WindowEvent event) {
        // ignore
    }

    /**
     * Required for WindowListener interface, but not used by this class.
     *
     * @param event  information about the window event.
     */
    public void windowOpened(final WindowEvent event) {
        // ignore
    }

}
