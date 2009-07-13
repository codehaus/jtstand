/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StatsTableRendererDouble.java is part of JTStand.
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

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

/**
 *
 * @author albert_kurucz
 */
public class StatsTableRendererDouble extends DefaultTableCellRenderer {

    public static final long serialVersionUID = 20081114L;
    private DecimalFormat decimalFormat;
    public static DecimalFormat CPKformat = new DecimalFormat("0.0");

    public StatsTableRendererDouble(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null && Double.class.isAssignableFrom(value.getClass())) {
            if (column == StatsTableModel.StatsTableColumn.CP.ordinal() ||
                    //                    column == StatsTableModel.StatsTableColumn.CPL.ordinal() ||
                    //                    column == StatsTableModel.StatsTableColumn.CPU.ordinal() ||
                    column == StatsTableModel.StatsTableColumn.CPK.ordinal()) {
                if (((Double) value).equals(Double.MAX_VALUE)) {
                    ((JLabel) component).setText("\u221E");
                } else {
                    ((JLabel) component).setText(CPKformat.format((Double) value));
                }
            } else if (decimalFormat != null) {
                ((JLabel) component).setText(decimalFormat.format((Double) value));
            }
        }
        //((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
        return component;
    }
}
