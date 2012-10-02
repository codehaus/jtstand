/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestSequenceInstanceHighlighter.java is part of JTStand.
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
import com.jtstand.swing.TestSequenceInstanceModel.SequenceColumn;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstanceHighlighter implements Highlighter {

    private static Object bgc = UIManager.get("Panel.background");
    private JXTreeTable jXTreeTable;

    TestSequenceInstanceHighlighter(JXTreeTable jXTreeTable) {
        this.jXTreeTable = jXTreeTable;
    }

    @Override
    public Component highlight(
            Component c, ComponentAdapter ca) {
        if (JLabel.class.isAssignableFrom(c.getClass())) {
            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            int column = jXTreeTable.convertColumnIndexToModel(ca.column);
            if (column == SequenceColumn.STEPSTATUS.ordinal()) {
                c.setForeground(MainFrame.getColor(((JLabel) c).getText()));
            } else if (column == SequenceColumn.VALUE.ordinal()) {
                Object step = jXTreeTable.getValueAt(ca.row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal());
                if (step != null && TestStepInstance.class.isAssignableFrom(step.getClass())) {
                    if (!((TestStepInstance) step).isValuePassed(((TestStepInstance) step).getValue())) {
                        c.setForeground(Color.RED);
                    }
                }
            } else if (column == SequenceColumn.NAME.ordinal()) {
                Object step = jXTreeTable.getValueAt(ca.row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal());
                if (step != null && TestStepInstance.class.isAssignableFrom(step.getClass())) {
                    if (((TestStepInstance) step).isParallel()) {
                        c.setForeground(Color.MAGENTA);
                    }
                }
            } else if (column == SequenceColumn.STEPNUMBER.ordinal() && bgc != null && Color.class.isAssignableFrom(bgc.getClass())) {
                c.setForeground(Color.black);
                c.setBackground((Color) bgc);
            }
        }
        return c;
    }

    @Override
    public void addChangeListener(ChangeListener arg0) {
    }

    @Override
    public void removeChangeListener(ChangeListener arg0) {
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        return null;
    }
}
