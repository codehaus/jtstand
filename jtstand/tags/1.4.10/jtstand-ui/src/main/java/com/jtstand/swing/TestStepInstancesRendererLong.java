/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepInstancesRendererLong.java is part of JTStand.
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

import com.jtstand.TestStepInstance;
import com.jtstand.swing.TestStepInstancesModel.StepsColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Calendar;

/**
 *
 * @author albert_kurucz
 */
public class TestStepInstancesRendererLong extends DefaultTableCellRenderer {

    public static final long serialVersionUID = 20081114L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null && Long.class.isAssignableFrom(value.getClass())) {
            if (column == StepsColumn.STARTTIME.ordinal()) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((Long) value);
                ((JLabel) component).setText(TestStepInstance.getDateWith24ClockMs(cal));
            } else if (column == StepsColumn.ELAPSED.ordinal()) {
                ((JLabel) component).setText(Util.getElapsedString((Long) value));
            } else if (column == StepsColumn.STEPSTARTTIME.ordinal()) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis((Long) value);
                ((JLabel) component).setText(TestStepInstance.getDateWith24ClockMs(cal));
            } else if (column == StepsColumn.STEPELAPSED.ordinal()) {
                ((JLabel) component).setText(Util.getElapsedString((Long) value));
            } else {
                ((JLabel) component).setText(String.valueOf(value));
            }
            //((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
            return component;
        }
        return component;
    }
}
