/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepStatisticsModel.java is part of JTStand.
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
        String name = stats.keySet().toArray()[row].toString();
        if (name == null) {
            return "";
        }
        Stat stat = stats.get(name);
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
