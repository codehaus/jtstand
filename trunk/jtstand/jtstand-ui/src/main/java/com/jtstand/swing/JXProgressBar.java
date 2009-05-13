/*
 * Copyright 2009 Albert Kurucz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jtstand.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
