/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SortableTable.java is part of JTStand.
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

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * A simple extension of JTable that supports the use of a SortableTableModel.
 *
 * @author David Gilbert
 */
public class SortableTable extends JTable {

    /** A listener for sorting. */
    private SortableTableHeaderListener headerListener;

    /**
     * Standard constructor - builds a table for the specified model.
     *
     * @param model  the data.
     */
    public SortableTable(final SortableTableModel model) {

        super(model);

        final SortButtonRenderer renderer = new SortButtonRenderer();
        final TableColumnModel cm = getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setHeaderRenderer(renderer);
        }

        final JTableHeader header = getTableHeader();
        this.headerListener = new SortableTableHeaderListener(model, renderer);
        header.addMouseListener(this.headerListener);
        header.addMouseMotionListener(this.headerListener);

        model.sortByColumn(0, true);

    }

    /**
     * Changes the model for the table.  Takes care of updating the header listener at the
     * same time.
     *
     * @param model  the table model.
     *
     */
    public void setSortableModel(final SortableTableModel model) {

        super.setModel(model);
        this.headerListener.setTableModel(model);
        final SortButtonRenderer renderer = new SortButtonRenderer();
        final TableColumnModel cm = getColumnModel();
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setHeaderRenderer(renderer);
        }
        model.sortByColumn(0, true);

    }

}
