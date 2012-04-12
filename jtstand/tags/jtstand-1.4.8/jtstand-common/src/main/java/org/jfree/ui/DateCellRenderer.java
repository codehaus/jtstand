/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DateCellRenderer.java is part of JTStand.
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
import java.text.DateFormat;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A table cell renderer that formats dates.
 *
 * @author David Gilbert
 */
public class DateCellRenderer extends DefaultTableCellRenderer {

    /** The formatter. */
    private DateFormat formatter;
    
    /**
     * Default constructor.
     */
    public DateCellRenderer() {
        this(DateFormat.getDateTimeInstance());
    }
    
    /**
     * Creates a new renderer.
     * 
     * @param formatter  the formatter.
     */
    public DateCellRenderer(final DateFormat formatter) {
        super();
        this.formatter = formatter;
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Returns itself as the renderer. Supports the TableCellRenderer interface.
     *
     * @param table  the table.
     * @param value  the data to be rendered.
     * @param isSelected  a boolean that indicates whether or not the cell is 
     *                    selected.
     * @param hasFocus  a boolean that indicates whether or not the cell has 
     *                  the focus.
     * @param row  the (zero-based) row index.
     * @param column  the (zero-based) column index.
     *
     * @return the component that can render the contents of the cell.
     */
    public Component getTableCellRendererComponent(final JTable table, 
            final Object value, final boolean isSelected, 
            final boolean hasFocus, final int row, final int column) {

        setFont(null);
        if (value != null) {
          setText(this.formatter.format(value));
        }
        else {
          setText("");
        }
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        }
        else {
            setBackground(null);
        }
        return this;
    }

}
