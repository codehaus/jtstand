/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SystemPropertiesTableModel.java is part of JTStand.
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.jfree.ui.SortableTableModel;
import org.jfree.util.ResourceBundleWrapper;

/**
 * A sortable table model containing the system properties.
 *
 * @author David Gilbert
 */
public class SystemPropertiesTableModel extends SortableTableModel {

    /**
     * Useful class for holding the name and value of a system property.
     *
     */
    protected static class SystemProperty {

        /** The property name. */
        private String name;

        /** The property value. */
        private String value;

        /**
         * Standard constructor - builds a new SystemProperty.
         *
         * @param name  the property name.
         * @param value  the property value.
         */
        public SystemProperty(final String name, final String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Returns the property name.
         *
         * @return the property name.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the property value.
         *
         * @return the property value.
         */
        public String getValue() {
            return this.value;
        }

    }

    /**
     * A class for comparing SystemProperty objects.
     *
     */
    protected static class SystemPropertyComparator implements Comparator {

        /** Indicates the sort order. */
        private boolean ascending;

        /**
         * Standard constructor.
         *
         * @param ascending  a flag that controls the sort order (ascending or
         *                   descending).
         */
        public SystemPropertyComparator(final boolean ascending) {
            this.ascending = ascending;
        }

        /**
         * Compares two objects.
         *
         * @param o1  the first object.
         * @param o2  the second object.
         *
         * @return an integer that indicates the relative order of the objects.
         */
        public int compare(final Object o1, final Object o2) {

            if ((o1 instanceof SystemProperty)
                    && (o2 instanceof SystemProperty)) {
                final SystemProperty sp1 = (SystemProperty) o1;
                final SystemProperty sp2 = (SystemProperty) o2;
                if (this.ascending) {
                    return sp1.getName().compareTo(sp2.getName());
                }
                else {
                    return sp2.getName().compareTo(sp1.getName());
                }
            }
            else {
                return 0;
            }

        }

        /**
         * Returns <code>true</code> if this object is equal to the specified
         * object, and <code>false</code> otherwise.
         *
         * @param o  the other object.
         *
         * @return A boolean.
         */
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SystemPropertyComparator)) {
                return false;
            }

            final SystemPropertyComparator systemPropertyComparator
                    = (SystemPropertyComparator) o;

            if (this.ascending != systemPropertyComparator.ascending) {
                return false;
            }

            return true;
        }

        /**
         * Returns a hash code value for the object.
         *
         * @return the hashcode
         */
        public int hashCode() {
            return (this.ascending ? 1 : 0);
        }
    }

    /** Storage for the properties. */
    private List properties;

    /** Localised name column label. */
    private String nameColumnLabel;

    /** Localised property column label. */
    private String valueColumnLabel;

    /**
     * Creates a new table model using the properties of the current Java
     * Virtual Machine.
     */
    public SystemPropertiesTableModel() {

        this.properties = new java.util.ArrayList();
        try {
            final Properties p = System.getProperties();
            final Iterator iterator = p.keySet().iterator();
            while (iterator.hasNext()) {
                final String name = (String) iterator.next();
                    final String value = System.getProperty(name);
                    final SystemProperty sp = new SystemProperty(name, value);
                    this.properties.add(sp);
            }
        }
        catch (SecurityException se) {
            // ignore SecurityExceptions
        }

        Collections.sort(this.properties, new SystemPropertyComparator(true));

        final String baseName = "org.jfree.ui.about.resources.AboutResources";
        final ResourceBundle resources = ResourceBundleWrapper.getBundle(
                baseName);

        this.nameColumnLabel = resources.getString(
                "system-properties-table.column.name");
        this.valueColumnLabel = resources.getString(
                "system-properties-table.column.value");

    }

    /**
     * Returns true for the first column, and false otherwise - sorting is only
     * allowed on the first column.
     *
     * @param column  the column index.
     *
     * @return true for column 0, and false for all other columns.
     */
    public boolean isSortable(final int column) {

        if (column == 0) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * Returns the number of rows in the table model (that is, the number of
     * system properties).
     *
     * @return the row count.
     */
    public int getRowCount() {
        return this.properties.size();
    }

    /**
     * Returns the number of columns in the table model.  In this case, there
     * are two columns: one for the property name, and one for the property
     * value.
     *
     * @return the column count (always 2 in this case).
     */
    public int getColumnCount() {
        return 2;
    }

    /**
     * Returns the name of the specified column.
     *
     * @param column  the column index.
     *
     * @return the column name.
     */
    public String getColumnName(final int column) {

        if (column == 0) {
            return this.nameColumnLabel;
        }
        else {
            return this.valueColumnLabel;
        }

    }

    /**
     * Returns the value at the specified row and column.  This method supports
     * the TableModel interface.
     *
     * @param row  the row index.
     * @param column  the column index.
     *
     * @return the value.
     */
    public Object getValueAt(final int row, final int column) {

        final SystemProperty sp = (SystemProperty) this.properties.get(row);
        if (column == 0) {
            return sp.getName();
        }
        else {
            if (column == 1) {
                return sp.getValue();
            }
            else {
                return null;
            }
        }

    }

    /**
     * Sorts on the specified column.
     *
     * @param column  the column index.
     * @param ascending  a flag that controls the sort order.
     *
     */
    public void sortByColumn(final int column, final boolean ascending) {

        if (isSortable(column)) {
            super.sortByColumn(column, ascending);
            Collections.sort(this.properties,
                    new SystemPropertyComparator(ascending));
        }

    }


}
