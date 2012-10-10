/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AbstractListTableModel.java is part of JTStand.
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

import java.io.Serializable;
import java.util.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author albert_kurucz
 */
abstract public class AbstractListTableModel<E> extends ArrayList<E> implements TableModel, List<E>, Set<E>, Serializable {
//
// Instance Variables
//

    /** List of listeners */
    protected EventListenerList listenerList = new EventListenerList();

    public int getRowCount() {
        return size();
    }

    public String getColumnName(int column) {
        String result = "";
        for (; column >= 0; column = column / 26 - 1) {
            result = (char) ((char) (column % 26) + 'A') + result;
        }
        return result;
    }

    public int findColumn(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnName.equals(getColumnName(i))) {
                return i;
            }
        }
        return -1;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public TableModelListener[] getTableModelListeners() {
        return listenerList.getListeners(TableModelListener.class);
    }

//
//  Fire methods
//
    /**
     * Notifies all listeners that all cell values in the table's
     * rows may have changed. The number of rows may also have changed
     * and the <code>JTable</code> should redraw the
     * table from scratch. The structure of the table (as in the order of the
     * columns) is assumed to be the same.
     *
     * @see TableModelEvent
     * @see EventListenerList
     * @see javax.swing.JTable#tableChanged(TableModelEvent)
     */
    public void fireTableDataChanged() {
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Notifies all listeners that the table's structure has changed.
     * The number of columns in the table, and the names and types of
     * the new columns may be different from the previous state.
     * If the <code>JTable</code> receives this event and its
     * <code>autoCreateColumnsFromModel</code>
     * flag is set it discards any table columns that it had and reallocates
     * default columns in the order they appear in the model. This is the
     * same as calling <code>setModel(TableModel)</code> on the
     * <code>JTable</code>.
     *
     * @see TableModelEvent
     * @see EventListenerList
     */
    public void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    /**
     * Notifies all listeners that rows in the range
     * <code>[firstRow, lastRow]</code>, inclusive, have been inserted.
     *
     * @param  firstRow  the first row
     * @param  lastRow   the last row
     *
     * @see TableModelEvent
     * @see EventListenerList
     *
     */
    public void fireTableRowsInserted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
     * Notifies all listeners that rows in the range
     * <code>[firstRow, lastRow]</code>, inclusive, have been updated.
     *
     * @param firstRow  the first row
     * @param lastRow   the last row
     *
     * @see TableModelEvent
     * @see EventListenerList
     */
    public void fireTableRowsUpdated(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
    }

    /**
     * Notifies all listeners that rows in the range
     * <code>[firstRow, lastRow]</code>, inclusive, have been deleted.
     *
     * @param firstRow  the first row
     * @param lastRow   the last row
     *
     * @see TableModelEvent
     * @see EventListenerList
     */
    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    /**
     * Notifies all listeners that the value of the cell at
     * <code>[row, column]</code> has been updated.
     *
     * @param row  row of cell which has been updated
     * @param column  column of cell which has been updated
     * @see TableModelEvent
     * @see EventListenerList
     */
    public void fireTableCellUpdated(int row, int column) {
        fireTableChanged(new TableModelEvent(this, row, row, column));
    }

    /**
     * Forwards the given notification event to all
     * <code>TableModelListeners</code> that registered
     * themselves as listeners for this table model.
     *
     * @param e  the event to be forwarded
     *
     * @see #addTableModelListener
     * @see TableModelEvent
     * @see EventListenerList
     */
    public void fireTableChanged(TableModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener) listeners[i + 1]).tableChanged(e);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        } else {
            super.add(e);
            int index = size() - 1;
            this.fireTableRowsInserted(index, index);
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i < 0) {
            return false;
        }
        remove(i);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        List<E> fc = new ArrayList<E>();
        for (E e : c) {
            if (!contains(e)) {
                fc.add(e);
            }
        }
        if (fc.size() == 0) {
            return false;
        } else {
            int index = size();
            super.addAll(fc);
            fireTableRowsInserted(index, index + fc.size() - 1);
            return true;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        List<E> fc = new ArrayList<E>();
        for (E e : c) {
            if (!contains(e)) {
                fc.add(e);
            }
        }
        if (fc.size() == 0) {
            return false;
        } else {
            super.addAll(fc);
            fireTableRowsInserted(index, index + fc.size() - 1);
            return true;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (super.removeAll(c)) {
            fireTableDataChanged();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (super.retainAll(c)) {
            fireTableDataChanged();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void clear() {
        super.clear();
        fireTableDataChanged();
    }

    @Override
    public E set(int index, E element) {
        int i = indexOf(element);
        if (i == index) {
            return element;
        }
        if (i < 0) {
            E oldElement = super.set(index, element);
            fireTableRowsUpdated(index, index);
            return oldElement;
        } else {
            remove(i);
            add(index, element);
            this.fireTableRowsUpdated(index, size() - 1);
            return element;
        }
    }

    @Override
    public void add(int index, E element) {
        if (!this.contains(element)) {
            super.add(index, element);
            fireTableRowsInserted(index, index);
        }
    }

    @Override
    public E remove(int index) {
        E o = super.remove(index);
        if (o != null) {
            fireTableRowsDeleted(index, index);
        }
        return o;
    }
}