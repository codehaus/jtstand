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

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.List;

/**
 *
 **/
public class TestStepInstancesModel extends AbstractTableModel implements TableModel {

    public static final long serialVersionUID = 20080507L;

    public static enum StepsColumn {

        ROW("", Integer.class),
        LOCATION("Location", String.class),
        OPERATOR("Operator", String.class),
        PN("Product Type", String.class),
        TESTTYPE("Test Type", String.class),
        SN("Serial Number", String.class),
        STARTTIME("Sequence Started", Long.class),
        ELAPSED("Elapsed", Long.class),
        STATUS("State", String.class),
        PATH("Path", String.class),
        NAME("Name", String.class),
        VALUE("Value", Double.class),
        LSL("LSL", String.class),
        USL("USL", String.class),
        LOOP("Loop", String.class),
        STEPSTARTTIME("Step Started", Long.class),
        STEPELAPSED("Elapsed", Long.class),
        STEPSTATUS("State", String.class);
        public final String columnName;
        public final Class columnClass;

        StepsColumn(String columnName, Class columnClass) {
            this.columnName = columnName;
            this.columnClass = columnClass;
        }
    }
    public static StepsColumn[] sequencesColumns = StepsColumn.values();
    public static int SequencesColumnCount = sequencesColumns.length;
    private TestStepInstances instances;

    public TestStepInstancesModel(TestStepInstances instances) {
        this.instances = instances;
    }

    @Override
    public int getColumnCount() {
        return SequencesColumnCount;
    }

    @Override
    public int getRowCount() {
        return instances.size();
    }

    @Override
    public String getColumnName(
            int column) {
        return sequencesColumns[column].columnName;
    }

    @Override
    public Class getColumnClass(
            int column) {
        return sequencesColumns[column].columnClass;
    }

    @Override
    public Object getValueAt(
            int row, int column) {
        List<TestStepInstance> steps = instances;
        if (steps == null) {
            return null;
        }
        if (row >= steps.size()) {
            return null;
        }
        TestStepInstance step = steps.get(row);
        if (step == null) {
            return null;
        }
//        System.out.println("sequencesColumns[column]" + sequencesColumns[column]);
        switch (sequencesColumns[column]) {
            case ROW:
                return 1 + instances.getJTable().convertRowIndexToView(row);
            case SN:
                return step.getTestSequenceInstance().getSerialNumber();
            case PN:
                return step.getTestSequenceInstance().getProductName();
            case TESTTYPE:
                return step.getTestSequenceInstance().getTestTypeName();
            case LOCATION:
                if (step.getTestSequenceInstance().getTestStation() != null) {
                    if (step.getTestSequenceInstance().getTestFixture() != null) {
                        return step.getTestSequenceInstance().getTestStation().getHostName() + "@" + step.getTestSequenceInstance().getTestFixture().getFixtureName();
                    } else {
                        return step.getTestSequenceInstance().getTestStation().getHostName();
                    }
                } else {
                    return "";
                }
            case OPERATOR:
                return step.getTestSequenceInstance().getEmployeeNumber() != null ? step.getTestSequenceInstance().getEmployeeNumber() : "";
            case STARTTIME:
                return new Long(step.getTestSequenceInstance().getCreateTime());
            case ELAPSED:
                return step.getTestSequenceInstance().getElapsed();//.getElapsedString();
            case STATUS:
                return step.getTestSequenceInstance().getStatusString();
            case PATH:
                return step.getTestStepInstancePath();
            case NAME:
                return step;
            case VALUE:
                return step.getValueNumber();
            case LSL:
                return step.getLslWithUnit();
            case USL:
                return step.getUslWithUnit();
            case LOOP:
                return step.getLoopsString();
            case STEPSTARTTIME:
                return step.getStartTime();
            case STEPELAPSED:
                return step.getElapsed();
            case STEPSTATUS:
                return step.getStatusString();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
