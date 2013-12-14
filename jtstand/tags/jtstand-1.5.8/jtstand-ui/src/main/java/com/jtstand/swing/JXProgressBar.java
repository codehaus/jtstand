/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JXProgressBar.java is part of JTStand.
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
package com.jtstand.swing;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;

/**
 *
 * @author albert_kurucz
 */
public class JXProgressBar extends JProgressBar {

    public static final long serialVersionUID = 20081114L;
    private boolean cancelled = false;

    public void reset() {
        cancelled = false;
    }

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public JXProgressBar() {
        super();
        addMenu(this);
    }

    private void addMenu(final JProgressBar bar) {
        bar.addMouseListener(new MouseAdapter() {

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && bar.isEnabled()) {

                    Point p = new Point(e.getX(), e.getY());
                    if (bar.contains(p)) {
                        /* create popup menu... */
                        JPopupMenu contextMenu = createContextMenu(bar);
                        /* ... and show it */
                        if (contextMenu != null && contextMenu.getComponentCount() > 0) {
                            contextMenu.show(bar, p.x, p.y);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
        });
    }

    private JPopupMenu createContextMenu(JProgressBar jLabel) {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem loginMenu = contextMenu.add("Cancel");
        loginMenu.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelled = true;
            }
        });
        return contextMenu;
    }
}
