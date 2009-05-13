/*
 * Copyright 2009 Albert Kurucz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
