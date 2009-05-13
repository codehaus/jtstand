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

import com.jtstand.TestSequenceInstance;
import com.jtstand.TestStepInstance;
import com.jtstand.TestStepNamePath;
import com.jtstand.statistics.Stat;
import com.jtstand.statistics.Stats;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 **/
public class TestStepStatisticsModel extends AbstractTableModel implements TableModel {

    public static final long serialVersionUID = 20080507L;

    public static enum StatisticsColumn {

        ROW("", Integer.class),
        STEPNUMBER("#", Integer.class),
        PATH("Step Path", String.class),
        N("Count", Integer.class),
        AVERAGE("Average", Double.class),
        SIGMA("Sigma", Double.class),
        MINIMUM("Minimum", Double.class),
        MAXIMUM("Maximum", Double.class),
        CP("CP", Double.class),
        //        CPL("CPL", Double.class),
        //        CPU("CPU", Double.class),
        CPK("CPK", Double.class);
        public final String columnName;
        public final Class columnClass;

        StatisticsColumn(String columnName, Class columnClass) {
            this.columnName = columnName;
            this.columnClass = columnClass;
        }
    }
    public static StatisticsColumn[] statisticsColumns = StatisticsColumn.values();
    public static int StatisticsColumnCount = statisticsColumns.length;
    private Stats stats;
    private TestSequenceInstance baseSequence;
    private TestStepStatistics testStepStatistics;

    public TestStepStatisticsModel(TestStepStatistics testStepStatistics, Stats stats, TestSequenceInstance baseSequence) {
        this.testStepStatistics = testStepStatistics;
        this.stats = stats;
        this.baseSequence = baseSequence;
    }

    public Stats getStats() {
        return stats;
    }

    @Override
    public int getColumnCount() {
        return StatisticsColumnCount;
    }

    @Override
    public int getRowCount() {
        return stats.size();
    }

    @Override
    public String getColumnName(int column) {
        return statisticsColumns[column].columnName;
    }

    @Override
    public Class getColumnClass(int column) {
        return statisticsColumns[column].columnClass;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row >= stats.size()) {
            return "";
        }
        String name = stats.getStats().keySet().toArray()[row].toString();
        if (name == null) {
            return "";
        }
        Stat stat = stats.getStats().get(name);
        if (stat == null) {
            return "";
        }
        TestStepNamePath tsnp;
        switch (statisticsColumns[column]) {
            case ROW:
                return 1 + testStepStatistics.getJTable().convertRowIndexToView(row);
            case STEPNUMBER:
                TestStepInstance step = baseSequence.getChild(Util.getPathList(name));
                if (step != null && step.getTestStepNamePath() != null) {
                    return step.getTestStepNamePath().getStepNumber();
                } else {
                    return null;
                }
            case PATH:
                return name;
            case N:
                return stat.getN();
            case AVERAGE:
                return stat.getAverage();
            case SIGMA:
                return stat.getStandardDeviation();
            case MINIMUM:
                return stat.getMin();
            case MAXIMUM:
                return stat.getMax();
            case CP:
                tsnp = baseSequence.getTestSequence().getNames().get(name);
                if (tsnp != null && tsnp.getTestLimit() != null) {
                    return stat.getCP(tsnp.getTestLimit().getLowerSpecifiedLimit(), tsnp.getTestLimit().getUpperSpeficiedLimit());
                }
                return null;
//            case CPL:
//                tsnp = baseSequence.getTestSequence().getNames().get(name);
//                if (tsnp != null && tsnp.getTestLimit() != null) {
//                    return stat.getCPL(tsnp.getTestLimit().getLowerSpecifiedLimit());
//                }
//                return null;
//            case CPU:
//                tsnp = baseSequence.getTestSequence().getNames().get(name);
//                if (tsnp != null && tsnp.getTestLimit() != null) {
//                    return stat.getCPU(tsnp.getTestLimit().getUpperSpeficiedLimit());
//                }
//                return null;
            case CPK:
                tsnp = baseSequence.getTestSequence().getNames().get(name);
                if (tsnp != null && tsnp.getTestLimit() != null) {
                    return stat.getCPK(tsnp.getTestLimit().getLowerSpecifiedLimit(), tsnp.getTestLimit().getUpperSpeficiedLimit());
                }
                return null;
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
