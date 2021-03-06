/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SortableTableModel.java is part of JTStand.
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

import javax.swing.table.AbstractTableModel;

/**
 * The base class for a sortable table model.
 *
 * @author David Gilbert
 */
public abstract class SortableTableModel extends AbstractTableModel {

    /** The column on which the data is sorted (-1 for no sorting). */
    private int sortingColumn;

    /** Indicates ascending (true) or descending (false) order. */
    private boolean ascending;

    /**
     * Constructs a sortable table model.
     */
    public SortableTableModel() {
        this.sortingColumn = -1;
        this.ascending = true;
    }

    /**
     * Returns the index of the sorting column, or -1 if the data is not sorted
     * on any column.
     *
     * @return the column used for sorting.
     */
    public int getSortingColumn() {
        return this.sortingColumn;
    }

    /**
     * Returns <code>true</code> if the data is sorted in ascending order, and
     * <code>false</code> otherwise.
     *
     * @return <code>true</code> if the data is sorted in ascending order, and
     *         <code>false</code> otherwise.
     */
    public boolean isAscending() {
        return this.ascending;
    }

    /**
     * Sets the flag that determines whether the sort order is ascending or
     * descending.
     *
     * @param flag  the flag.
     */
    public void setAscending(final boolean flag) {
        this.ascending = flag;
    }

    /**
     * Sorts the table.
     *
     * @param column  the column to sort on (zero-based index).
     * @param ascending  a flag to indicate ascending order or descending order.
     */
    public void sortByColumn(final int column, final boolean ascending) {
        if (isSortable(column)) {
            this.sortingColumn = column;
        }
    }

    /**
     * Returns a flag indicating whether or not a column is sortable.
     *
     * @param column  the column (zero-based index).
     *
     * @return boolean.
     */
    public boolean isSortable(final int column) {
        return false;
    }

}
