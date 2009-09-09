/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, OverlayLayout.java is part of JTStand.
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



import java.awt.Component;

import java.awt.Container;

import java.awt.Dimension;

import java.awt.Insets;

import java.awt.LayoutManager;

import java.awt.Rectangle;



/**

 * A simple layoutmanager to overlay all components of a parent.

 * <p/>

 * This layout manager acts similiar to the card layout, but all

 * childs of the parent band have the same size and all childs can

 * be visible at the same time.

 *

 * @author Thomas Morgner

 */

public final class OverlayLayout implements LayoutManager {



    /**

     * A flag that defines, whether invisible components should be ignored when

     * computing the layout.

     */

    private boolean ignoreInvisible;



    /**

     * Creates a new instance.

     * 

     * @param ignoreInvisible  whether to ignore invisible components when computing the layout.

     */

    public OverlayLayout(final boolean ignoreInvisible) {

        this.ignoreInvisible = ignoreInvisible;

    }



    /**

     * DefaultConstructor.

     */

    public OverlayLayout() {



    }



    /**

     * If the layout manager uses a per-component string,

     * adds the component <code>comp</code> to the layout,

     * associating it

     * with the string specified by <code>name</code>.

     *

     * @param name the string to be associated with the component

     * @param comp the component to be added

     */

    public void addLayoutComponent(final String name, final Component comp) {

    }



    /**

     * Removes the specified component from the layout.

     *

     * @param comp the component to be removed

     */

    public void removeLayoutComponent(final Component comp) {

    }



    /**

     * Lays out the specified container.

     *

     * @param parent the container to be laid out

     */

    public void layoutContainer(final Container parent) {

        synchronized (parent.getTreeLock()) {

            final Insets ins = parent.getInsets();



            final Rectangle bounds = parent.getBounds();

            final int width = bounds.width - ins.left - ins.right;

            final int height = bounds.height - ins.top - ins.bottom;



            final Component[] comps = parent.getComponents();



            for (int i = 0; i < comps.length; i++) {

                final Component c = comps[i];

                if ((comps[i].isVisible() == false) && this.ignoreInvisible) {

                    continue;

                }

                c.setBounds(ins.left, ins.top, width, height);

            }

        }

    }



    /**

     * Calculates the minimum size dimensions for the specified

     * container, given the components it contains.

     *

     * @param parent the component to be laid out

     * @return the minimum size computed for the parent.

     * @see #preferredLayoutSize

     */

    public Dimension minimumLayoutSize(final Container parent) {

        synchronized (parent.getTreeLock()) {

            final Insets ins = parent.getInsets();

            final Component[] comps = parent.getComponents();

            int height = 0;

            int width = 0;

            for (int i = 0; i < comps.length; i++) {

                if ((comps[i].isVisible() == false) && this.ignoreInvisible) {

                    continue;

                }



                final Dimension pref = comps[i].getMinimumSize();

                if (pref.height > height) {

                    height = pref.height;

                }

                if (pref.width > width) {

                    width = pref.width;

                }

            }

            return new Dimension(width + ins.left + ins.right,

                height + ins.top + ins.bottom);

        }

    }



    /**

     * Calculates the preferred size dimensions for the specified

     * container, given the components it contains.

     *

     * @param parent the container to be laid out

     * @return the preferred size computed for the parent.

     * @see #minimumLayoutSize

     */

    public Dimension preferredLayoutSize(final Container parent) {

        synchronized (parent.getTreeLock()) {

            final Insets ins = parent.getInsets();

            final Component[] comps = parent.getComponents();

            int height = 0;

            int width = 0;

            for (int i = 0; i < comps.length; i++) {

                if ((comps[i].isVisible() == false) && this.ignoreInvisible) {

                    continue;

                }



                final Dimension pref = comps[i].getPreferredSize();

                if (pref.height > height) {

                    height = pref.height;

                }

                if (pref.width > width) {

                    width = pref.width;

                }

            }

            return new Dimension(width + ins.left + ins.right,

                height + ins.top + ins.bottom);

        }

    }



}

