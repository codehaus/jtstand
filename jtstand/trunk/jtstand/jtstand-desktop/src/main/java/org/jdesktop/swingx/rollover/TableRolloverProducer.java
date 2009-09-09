/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TableRolloverProducer.java is part of JTStand.
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

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 * Table-specific implementation of RolloverProducer. 
 * 
 * @author Jeanette Winzenburg
 */
public class TableRolloverProducer extends RolloverProducer {

    @Override
    protected void updateRolloverPoint(JComponent component, Point mousePoint) {
        JTable table = (JTable) component;
        int col = table.columnAtPoint(mousePoint);
        int row = table.rowAtPoint(mousePoint);
        if ((col < 0) || (row < 0)) {
            row = -1;
            col = -1;
        }
        rollover.x = col;
        rollover.y = row;
    }

}
