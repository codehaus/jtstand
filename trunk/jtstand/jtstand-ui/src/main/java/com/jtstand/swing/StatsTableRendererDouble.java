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
