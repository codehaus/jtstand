/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, ListRolloverProducer.java is part of JTStand.
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

import javax.swing.JComponent;
import javax.swing.JList;

/**
 * List-specific implementation of RolloverProducer.
 * 
 * @author Jeanette Winzenburg
 */
public class ListRolloverProducer extends RolloverProducer {

    @Override
    protected void updateRolloverPoint(JComponent component, Point mousePoint) {
        JList list = (JList) component;
        int row = list.locationToIndex(mousePoint);
        if (row >= 0) {
            Rectangle cellBounds = list.getCellBounds(row, row);
            if (!cellBounds.contains(mousePoint)) {
                row = -1;
            }
        }
        int col = row < 0 ? -1 : 0;
        rollover.x = col;
        rollover.y = row;
    }

}
