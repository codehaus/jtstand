/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepInstancesRendererDouble.java is part of JTStand.
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

import com.jtstand.TestLimit;
import com.jtstand.TestStepInstance;
import com.jtstand.swing.TestStepInstancesModel.StepsColumn;
import java.awt.Component;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author albert_kurucz
 */
public class TestStepInstancesRendererDouble extends DefaultTableCellRenderer {

    public static final long serialVersionUID = 20081114L;
    private TestStepInstances steps;

    public TestStepInstancesRendererDouble(TestStepInstances steps) {
        this.steps = steps;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == StepsColumn.VALUE.ordinal()) {
            if (steps != null) {
                TestStepInstance step = steps.getTestStepInstance(row);
                if (step != null) {
                    ((JLabel) component).setText(computeValueWithUnit(step));
                }
            }
        }
        //((JLabel) component).setHorizontalAlignment(JLabel.CENTER);
        return component;
    }

    private String computeValueWithUnit(TestStepInstance step) {
        TestStepInstance refStep = steps.getReferenceStep();
        if (refStep == null) {
            return step.getValueWithUnit();
        }
        String valueWithUnit = step.getValueString();
        if (valueWithUnit != null) {
            return valueWithUnit;
        }
        if (step.getValueNumber() != null) {
            String decFormatStr = refStep.getPropertyString(TestStepInstance.STR_DECIMAL_FORMAT, null);
            if (decFormatStr != null) {
                valueWithUnit = (new DecimalFormat(decFormatStr)).format(step.getValueNumber());
            } else {
                valueWithUnit = step.getValueNumber().toString();
            }
        } else {
            valueWithUnit = "";
        }
        if (valueWithUnit.length() > 0) {
            TestLimit testLimit = refStep.getTestLimit();
            if (testLimit != null && testLimit.getMeasurementUnit() != null) {
                valueWithUnit += "[" + testLimit.getMeasurementUnit() + "]";
            }
        }
        return valueWithUnit;
    }
}
