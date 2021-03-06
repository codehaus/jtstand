/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ContributorsTableModel.java is part of JTStand.
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

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.jfree.util.ResourceBundleWrapper;

/**
 * A table model containing a list of contributors to a project.
 * <P>
 * Used in the ContributorsPanel class.
 *
 * @author David Gilbert
 */
public class ContributorsTableModel extends AbstractTableModel {

    /** Storage for the contributors. */
    private List contributors;

    /** Localised version of the name column label. */
    private String nameColumnLabel;

    /** Localised version of the contact column label. */
    private String contactColumnLabel;

    /**
     * Constructs a ContributorsTableModel.
     *
     * @param contributors  the contributors.
     */
    public ContributorsTableModel(final List contributors) {

        this.contributors = contributors;

        final String baseName = "org.jfree.ui.about.resources.AboutResources";
        final ResourceBundle resources = ResourceBundleWrapper.getBundle(
                baseName);
        this.nameColumnLabel = resources.getString(
                "contributors-table.column.name");
        this.contactColumnLabel = resources.getString(
                "contributors-table.column.contact");

    }

    /**
     * Returns the number of rows in the table model.
     *
     * @return The number of rows.
     */
    public int getRowCount() {
        return this.contributors.size();
    }

    /**
     * Returns the number of columns in the table model.  In this case, there
     * are always two columns (name and e-mail address).
     *
     * @return The number of columns in the table model.
     */
    public int getColumnCount() {
        return 2;
    }

    /**
     * Returns the name of a column in the table model.
     *
     * @param column  the column index (zero-based).
     *
     * @return  the name of the specified column.
     */
    public String getColumnName(final int column) {

        String result = null;

        switch (column) {

            case 0:  result = this.nameColumnLabel;
                     break;

            case 1:  result = this.contactColumnLabel;
                     break;

        }

        return result;

    }

    /**
     * Returns the value for a cell in the table model.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return the value.
     */
    public Object getValueAt(final int row, final int column) {

        Object result = null;
        final Contributor contributor
                = (Contributor) this.contributors.get(row);

        if (column == 0) {
            result = contributor.getName();
        }
        else if (column == 1) {
            result = contributor.getEmail();
        }
        return result;

    }

}
