/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StatsTableModel.java is part of JTStand.
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

import com.jtstand.statistics.Stat;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.TreeMap;

/**
 *
 **/
public class StatsTableModel extends AbstractTableModel implements TableModel {

    public static final long serialVersionUID = 20080507L;

    public static enum StatsTableColumn {

        CATEGORY("Category", Integer.class),
        N("Count", Integer.class),
        AVERAGE("Average", Double.class),
        SIGMA("Sigma", Double.class),
        MIN("Minimum", Double.class),
        MAX("Maximum", Double.class),
        CP("CP", Double.class),
        //        CPL("CPL", Double.class),
        //        CPU("CPU", Double.class),
        CPK("CPK", Double.class);
        public final String columnName;
        public final Class columnClass;

        StatsTableColumn(String columnName, Class columnClass) {
            this.columnName = columnName;
            this.columnClass = columnClass;
        }
    }
    public static StatsTableColumn[] statTableColumns = StatsTableColumn.values();
    public static int statTableColumnCount = statTableColumns.length;
    private Stat allstat;
    private TreeMap<String, Stat> catstats;
    private StatsPanel statsPanel;

    public StatsTableModel(StatsPanel statsPanel, Stat allstat, TreeMap<String, Stat> catstats) {
        this.statsPanel = statsPanel;
        this.allstat = allstat;
        this.catstats = catstats;
    }

    @Override
    public int getColumnCount() {
//        TestStepInstances testStepInstances = statsPanel.getTestStepInstances();
//        if (testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
//            if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
//                return statTableColumnCount;
//            }
//        }
        return statTableColumnCount;
    }

    @Override
    public int getRowCount() {
        return statsPanel.isMultipleCategorization() ? catstats.size() + 1 : 1;
    }

    @Override
    public String getColumnName(int column) {
        return statTableColumns[column].columnName;
    }

    @Override
    public Class getColumnClass(int column) {
        return statTableColumns[column].columnClass;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row == 0) {
            TestStepInstances testStepInstances;
            switch (statTableColumns[column]) {
                case CATEGORY:
                    if (statsPanel.isCategorization() && !statsPanel.isMultipleCategorization()) {
                        return catstats.firstKey();
                    }
                    return "All";
                case N:
                    return allstat != null ? allstat.getN() : 0;
                case AVERAGE:
                    return allstat != null ? allstat.getAverage() : null;
                case SIGMA:
                    return allstat != null ? allstat.getStandardDeviation() : null;
                case MIN:
                    return allstat != null ? allstat.getMin() : null;
                case MAX:
                    return allstat != null ? allstat.getMax() : null;
                case CP:
                    testStepInstances = statsPanel.getTestStepInstances();
                    if (allstat != null && testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
                        if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null && testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
                            return allstat.getCP(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit(), testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
                        }
                    }
                    return null;
//                case CPL:
//                    testStepInstances = statsPanel.getTestStepInstances();
//                    if (allstat != null && testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
//                        if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
//                            return allstat.getCPL(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit());
//                        }
//                    }
//                    return null;
//                case CPU:
//                    testStepInstances = statsPanel.getTestStepInstances();
//                    if (allstat != null && testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
//                        if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
//                            return allstat.getCPU(testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
//                        }
//                    }
//                    return null;
                case CPK:
                    testStepInstances = statsPanel.getTestStepInstances();
                    if (allstat != null && testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
                        if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
                            return allstat.getCPK(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit(), testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
                        }
                    }
                    return null;
            }
        } else {
            if (catstats != null && row <= catstats.size()) {
                Object k = catstats.keySet().toArray()[row - 1];
                TestStepInstances testStepInstances;
                switch (statTableColumns[column]) {
                    case CATEGORY:
                        return k;
                    case N:
                        return catstats.get(k).getN();
                    case AVERAGE:
                        return catstats.get(k).getAverage();
                    case SIGMA:
                        return catstats.get(k).getStandardDeviation();
                    case MIN:
                        return catstats.get(k).getMin();
                    case MAX:
                        return catstats.get(k).getMax();
                    case CP:
                        testStepInstances = statsPanel.getTestStepInstances();
                        if (testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
                            if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null && testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
                                return catstats.get(k).getCP(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit(), testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
                            }
                        }
                        return null;
//                    case CPL:
//                        testStepInstances = statsPanel.getTestStepInstances();
//                        if (testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
//                            if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
//                                return catstats.get(k).getCPL(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit());
//                            }
//                        }
//                        return null;
//                    case CPU:
//                        testStepInstances = statsPanel.getTestStepInstances();
//                        if (testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
//                            if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
//                                return catstats.get(k).getCPU(testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
//                            }
//                        }
//                        return null;
                    case CPK:
                        testStepInstances = statsPanel.getTestStepInstances();
                        if (testStepInstances != null && testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
                            if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null || testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
                                return catstats.get(k).getCPK(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit(), testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
                            }
                        }
                        return null;
                }
            }
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
