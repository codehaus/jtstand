/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TreeRolloverProducer.java is part of JTStand.
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
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jdesktop.swingx.rollover;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXTree;

/**
 * Tree-specific implementation of RolloverProducer.
 * <p>
 * This implementation assumes a "hit" for rollover if the mouse is anywhere in
 * the total width of the tree. Additionally, a pressed to the right (but
 * outside of the label bounds) is re-dispatched as a pressed just inside the
 * label bounds. This is a first go for #166-swingx.
 * <p>
 * 
 * PENDING JW: bidi-compliance of pressed?
 * 
 * @author Jeanette Winzenburg
 */
public class TreeRolloverProducer extends RolloverProducer {

    @Override
    public void mousePressed(MouseEvent e) {
        JXTree tree = (JXTree) e.getComponent();
        Point mousePoint = e.getPoint();
        int labelRow = tree.getRowForLocation(mousePoint.x, mousePoint.y);
        // default selection
        if (labelRow >= 0)
            return;
        int row = tree.getClosestRowForLocation(mousePoint.x, mousePoint.y);
        Rectangle bounds = tree.getRowBounds(row);
        if (bounds == null) {
            row = -1;
        } else {
            if ((bounds.y + bounds.height < mousePoint.y)
                    || bounds.x > mousePoint.x) {
                row = -1;
            }
        }
        // no hit
        if (row < 0)
            return;
        tree.dispatchEvent(new MouseEvent(tree, e.getID(), e.getWhen(), e
                .getModifiers(), bounds.x + bounds.width - 2, mousePoint.y, e
                .getClickCount(), e.isPopupTrigger(), e.getButton()));
    }

    @Override
    protected void updateRolloverPoint(JComponent component, Point mousePoint) {
        JXTree tree = (JXTree) component;
        int row = tree.getClosestRowForLocation(mousePoint.x, mousePoint.y);
        Rectangle bounds = tree.getRowBounds(row);
        if (bounds == null) {
            row = -1;
        } else {
            if ((bounds.y + bounds.height < mousePoint.y)
                    || bounds.x > mousePoint.x) {
                row = -1;
            }
        }
        int col = row < 0 ? -1 : 0;
        rollover.x = col;
        rollover.y = row;
    }

}
