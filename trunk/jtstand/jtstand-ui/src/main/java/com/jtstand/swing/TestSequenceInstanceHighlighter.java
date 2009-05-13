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
import com.jtstand.swing.TestSequenceInstanceModel.SequenceColumn;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

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
