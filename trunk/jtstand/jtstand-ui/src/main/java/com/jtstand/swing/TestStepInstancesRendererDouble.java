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

import com.jtstand.TestLimit;
import com.jtstand.TestStepInstance;
import com.jtstand.swing.TestStepInstancesModel.StepsColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

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
                valueWithUnit += testLimit.getMeasurementUnit();
            }
        }
        return valueWithUnit;
    }
}
