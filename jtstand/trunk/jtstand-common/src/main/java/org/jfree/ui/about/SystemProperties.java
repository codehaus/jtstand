/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SystemProperties.java is part of JTStand.
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

package org.jfree.ui.about;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jfree.ui.SortableTable;

/**
 * A utility class for working with system properties.
 *
 * @author David Gilbert
 */
public class SystemProperties {

    /**
     * Private constructor prevents object creation.
     */
    private SystemProperties () {
    }
    
    /**
     * Creates and returns a JTable containing all the system properties.  This method returns a
     * table that is configured so that the user can sort the properties by clicking on the table
     * header.
     *
     * @return a system properties table.
     */
    public static SortableTable createSystemPropertiesTable() {

        final SystemPropertiesTableModel properties = new SystemPropertiesTableModel();
        final SortableTable table = new SortableTable(properties);

        final TableColumnModel model = table.getColumnModel();
        TableColumn column = model.getColumn(0);
        column.setPreferredWidth(200);
        column = model.getColumn(1);
        column.setPreferredWidth(350);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        return table;

    }

}
