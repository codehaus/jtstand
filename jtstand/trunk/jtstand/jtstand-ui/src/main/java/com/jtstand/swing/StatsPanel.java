/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StatsPanel.java is part of JTStand.
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
import com.jtstand.statistics.Gaussian;
import com.jtstand.statistics.Stat;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;
//import org.jdesktop.swingx.table.ColumnHeaderRenderer;
import org.jfree.chart.*;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 *
 * @author  albert_kurucz
 */
public class StatsPanel extends javax.swing.JPanel implements PropertyChangeListener, ChartMouseListener {

    public static final long serialVersionUID = 20081114L;
    public static final String VALUE_STR = "Value";
    public static final Stroke myStroke = new BasicStroke(3);
    public static final String NOT_GROUPING_DATA_NAME = VALUE_STR;
    public static final Color limitcolor = Color.DARK_GRAY;
    public static final int plots = 128; //number of plots
    private DecimalFormat format = new DecimalFormat("##0.####E0");
    //private DecimalFormat format=new DecimalFormat("##0.##E0");
    private Stat allstat = null;
    TreeMap<String, Stat> catstats = null;
    private ChartPanel cp = null;
    private int numberOfCategories = 15;
    private TestStepInstances testStepInstances;

    public TestStepInstances getTestStepInstances() {
        return testStepInstances;
    }

    public static enum ChartMode {

        STEP_TIME, SEQUENCE_TIME, LIST, DISTRIBUTION
    };
    private ChartMode chartMode = ChartMode.DISTRIBUTION;

    public ChartMode getChartMode() {
        return chartMode;
    }

    public void setChartMode(ChartMode chartMode) {
        if (this.chartMode == null || !this.chartMode.equals(chartMode)) {
            this.chartMode = chartMode;
            selectionChanged();
        }
    }

    /**
     * A custom renderer to provide mouseover highlights.
     */
    static class MyBarRenderer extends XYBarRenderer {

        public static final long serialVersionUID = 20081114L;
        /** The row to highlight (-1 for none). */
        private int highlightRow = -1;
        /** The column to highlight (-1 for none). */
        private int highlightColumn = -1;

        /**
         * Sets the item to be highlighted (use (-1, -1) for no highlight).
         *
         * @param r  the row index.
         * @param c  the column index.
         */
        public void setHighlightedItem(int r, int c) {
//            System.out.println("setting row:" + r + " column:" + c);
            if (this.highlightRow == r && this.highlightColumn == c) {
                return;  // nothing to do
            }
            this.highlightRow = r;
            this.highlightColumn = c;
            notifyListeners(new RendererChangeEvent(this));
        }

        @Override
        public boolean isDrawBarOutline() {
//            System.out.println("isDrawBarOutline");
            return true;
        }

        /**
         * Return a special colour for the highlighted item.
         *
         * @param row  the row index.
         * @param column  the column index.
         *
         * @return The outline paint.
         */
        @Override
        public Paint getItemOutlinePaint(int row, int column) {
//            System.out.println("paint row: " + row + " column: " + column);
            if (row == this.highlightRow && column == this.highlightColumn) {
                return Color.yellow;
            }
            return super.getItemOutlinePaint(row, column);
        }

        @Override
        public Stroke getItemOutlineStroke(int row, int column) {
//            System.out.println("stroke row:" + row + " column:" + column);
            if (row == this.highlightRow && column == this.highlightColumn) {
                return myStroke;
            }
            return super.getItemOutlineStroke(row, column);
        }
    }

    /** Creates new form GTParametricStats */
    public StatsPanel(TestStepInstances testStepInstances) {
        this.testStepInstances = testStepInstances;
        initComponents();

//        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        Rectangle bounds = env.getMaximumWindowBounds();
//        Dimension d = new Dimension(bounds.width, bounds.height);
//        setPreferredSize(d);
//        setSize(d);

        selectionChanged();
        jLabelLSL.setText("LSL: " + ((testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null && testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null) ? testStepInstances.getReferenceStep().getTestLimit().getLslStringWithUnit() : "N/A"));
        jLabelUSL.setText("USL: " + ((testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null && testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) ? testStepInstances.getReferenceStep().getTestLimit().getUslStringWithUnit() : "N/A"));
        jSplitPane.addPropertyChangeListener(this);
        setVisible(true);
    }

    public Number getNumber(TestStepInstance step) {
        if (step == null) {
            return null;
        }
        switch (testStepInstances.getMode()) {
            case PARAMETRIC:
                return step.getValueNumber();
            case RUNTIME:
                return step.getElapsed();
        }
        return null;
    }

    public boolean isGrouping() {
        return jCheckBoxStation.isSelected() ||
                jCheckBoxFixture.isSelected() ||
                jCheckBoxOperator.isSelected() ||
                jCheckBoxPartNumber.isSelected() ||
                jCheckBoxPartRevision.isSelected() ||
                jCheckBoxTestType.isSelected() ||
                jCheckBoxSerialNumber.isSelected();
    }

    public boolean isCategorization() {
        return isGrouping() && catstats != null && catstats.size() > 0;
    }

    public boolean isMultipleCategorization() {
        return isGrouping() && catstats != null && catstats.size() > 1;
    }

    public String getGroupName(TestStepInstance step) {
        if (!isGrouping()) {
            return NOT_GROUPING_DATA_NAME;
        }
        String groupName = "";
        if (jCheckBoxStation.isSelected()) {
            groupName += step.getTestSequenceInstance().getHostName();
        }
        if (jCheckBoxFixture.isSelected()) {
            if (groupName.length() > 0) {
                groupName += "@";
            }
            groupName += step.getTestSequenceInstance().getFixtureName();
        }
        if (jCheckBoxOperator.isSelected()) {
            if (groupName.length() > 0) {
                groupName += "@";
            }
            groupName += step.getTestSequenceInstance().getEmployeeNumber();
        }
        if (jCheckBoxPartNumber.isSelected()) {
            if (groupName.length() > 0) {
                groupName += "@";
            }
            groupName += step.getTestSequenceInstance().getPartNumber();
        }
        if (jCheckBoxPartRevision.isSelected()) {
            if (groupName.length() > 0) {
                groupName += "@";
            }
            groupName += step.getTestSequenceInstance().getPartRevision();
        }
        if (jCheckBoxTestType.isSelected()) {
            if (groupName.length() > 0) {
                groupName += "@";
            }
            groupName += step.getTestSequenceInstance().getTestTypeName();
        }
        if (jCheckBoxSerialNumber.isSelected()) {
            if (groupName.length() > 0) {
                groupName += "@";
            }
            groupName += step.getTestSequenceInstance().getSerialNumber();
        }
        return groupName;
    }

    public TreeMap<String, List<TestStepInstance>> getGroupedSteps(Iterator<TestStepInstance> it) {
        TreeMap<String, List<TestStepInstance>> groups = new TreeMap<String, List<TestStepInstance>>();
        if (it == null) {
            return null;
        }
        while (it.hasNext()) {
            TestStepInstance step = it.next();
            String groupName = getGroupName(step);
            List<TestStepInstance> steps = groups.get(groupName);
            if (steps == null) {
                steps = new ArrayList<TestStepInstance>();
                groups.put(groupName, steps);
            }
            steps.add(step);
        }
        return groups;
    }

    public Iterator<TestStepInstance> getFilteringIterator() {
        return new FilteringIterator(testStepInstances.iterator());
    }

    public double minValue() {
        return leftValue(0);
    }

    public double maxValue() {
        return rightValue(numberOfCategories - 1);
    }

    public double plotValue(int index, int plots) {
        //index goes 0..plots-1
        //return value goes min..max
        if (plots < 2) {
            throw new IllegalArgumentException("plots parameter needs to be minimum 2, received: " + plots);
        }
        double min = minValue();
        double max = maxValue();
        return min + (index * (max - min)) / (plots - 1);
    }

    public double leftValue(int i, int cat, int nrofcat) {
        if (nrofcat == 1) {
            return leftValue(i);
        }
        double l = leftValue(i);
        double r = rightValue(i);
        double w = r - l;
//        return l + (cat + 0.5) * (w / (nrofcat + 1));
        return l + cat * (w / nrofcat);
    }

    public double rightValue(int i, int cat, int nrofcat) {
        if (nrofcat == 1) {
            return rightValue(i);
        }
        double l = leftValue(i);
        double r = rightValue(i);
        double w = r - l;
//        return l + (cat + 1.5) * (w / (nrofcat + 1));
        return l + (cat + 1) * (w / nrofcat);
    }

    //private double numberOfSigmas = 7.5;
    public double getSpan(Stat stat) {
        return 2 * Math.max(stat.getAverage() - stat.getMin(), stat.getMax() - stat.getAverage());
    }

    public double getNumberOfSigmas(Stat stat) {
        return Math.max(getSpan(stat) / stat.getStandardDeviation() + 0.1, 7.0);
    }

    public double leftSigma(Stat stat, int i) {
        return (i - numberOfCategories / 2.0) * getNumberOfSigmas(stat) / numberOfCategories;
    }

    public double midSigma(Stat stat, int i) {
        return (i + 0.5 - numberOfCategories / 2.0) * getNumberOfSigmas(stat) / numberOfCategories;
    }

    public double rightSigma(Stat stat, int i) {
        return (i + 1 - numberOfCategories / 2.0) * getNumberOfSigmas(stat) / numberOfCategories;
    }

    public double leftValue(int i) {
        return allstat.getAverage() + allstat.getStandardDeviation() * leftSigma(allstat, i);
    }

    public double midValue(int i) {
        return allstat.getAverage() + allstat.getStandardDeviation() * midSigma(allstat, i);
    }

    public double rightValue(int i) {
        return allstat.getAverage() + allstat.getStandardDeviation() * rightSigma(allstat, i);
    }

    public void selectionChanged() {
        allstat = computeStatistics();
        //this.jRadioButtonDistribution.setEnabled(allstat.getStandardDeviation()>0.000001);
        separationChanged();
    }

    private void separationChanged() {
        catstats = computeCatStats();
        showStatisticsTable();
        chartChanged();
    }

    private void chartChanged() {
        showChart();
        tableResize();
    }

    private void tableResize() {
//        int rc = jTableSeries.getRowCount();
//        if (rc > 5) {
//            rc = 5;
//        }
//        int h = jTableSeries.getRowHeight() * rc;
//        javax.swing.table.JTableHeader jth = jTableSeries.getTableHeader();
//        h += jth.getHeight();
//        Insets i = jScrollPaneTable.getInsets();
//        h += i.bottom;
//        h += i.top;
//        int w = jScrollPaneTable.getWidth();
//        Dimension dim = new Dimension(w, h);
//        jScrollPaneTable.setPreferredSize(dim);
//        jScrollPaneTable.setMinimumSize(dim);

//        jSplitPane.setDividerLocation(1 + dim.height);
        jSplitPane.setDividerLocation(-1);
//        jScrollPaneTable.invalidate();
//        this.validateTree();
    }

    private TreeMap<String, Stat> computeCatStats() {
        //create the stats map
        Iterator<TestStepInstance> it = getFilteringIterator();
        if (it == null || !it.hasNext()) {
            return null;
        }
        catstats = new TreeMap<String, Stat>();
        while (it.hasNext()) {
            TestStepInstance step = it.next();
            String groupName = getGroupName(step);
            Stat stat = catstats.get(groupName);
            if (stat == null) {
                stat = new Stat();
                catstats.put(groupName, stat);
            }
            Number num = getNumber(step);
            if (num != null) {
                double val = num.doubleValue();
                stat.addValue(val);
            }
        }
        return catstats;
    }

    private void showStatisticsTable() {
        StatsTableModel tm = new StatsTableModel(this, allstat, catstats);
        jTable.setModel(tm);
        jTable.setBackground(Color.white);
        //...
//        resizeSeries();
        //((ColumnHeaderRenderer) jTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        jTable.addHighlighter(
                new Highlighter() {

                    public Component highlight(Component c, ComponentAdapter ca) {
                        if (JLabel.class.isAssignableFrom(c.getClass())) {
                            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                            int column = jTable.convertColumnIndexToModel(ca.column);
                            if (column == StatsTableModel.StatsTableColumn.CATEGORY.ordinal()) {
                                if (jTable.getRowCount() > 1) {
                                    int i = jTable.convertRowIndexToModel(ca.row);
                                    if (i > 0) {
                                        c.setForeground(ChartCategories.getColor(i - 1));
                                    }
                                } else {
                                    if (isCategorization() && !isMultipleCategorization()) {
                                        c.setForeground(ChartCategories.getColor(0));
                                    }
                                }
                            }
                        }
                        return c;
                    }

                    public void addChangeListener(ChangeListener arg0) {
                    }

                    public void removeChangeListener(ChangeListener arg0) {
                    }

                    public ChangeListener[] getChangeListeners() {
                        return new ChangeListener[0];
                    }
                });
        DecimalFormat df = null;
        if (testStepInstances.getReferenceStep() != null) {
            String decFormatStr = testStepInstances.getReferenceStep().getPropertyString(TestStepInstance.STR_DECIMAL_FORMAT, null);
            if (decFormatStr != null) {
                df = new DecimalFormat(decFormatStr);
            }
        }

        jTable.setDefaultRenderer(Double.class, new StatsTableRendererDouble(df));
        //this.jTableSeries.invalidate();
        Util.packColumns(jTable, 9);
//        jTableSeries.setVisibleRowCount(Math.min(3, jTableSeries.getRowCount()));
        Util.setVisibleRowCount(jTable, Math.min(3, jTable.getRowCount()), jSplitPane);
//        Util.getRowCount(jTable, jSplitPane);
//        Util.setDividerLocation(jSplitPane, jTable);
        jSplitPane.setDividerLocation(-1);
//        jScrollPaneTop.invalidate();
//        validateTree();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //JSplitPane sourceSplitPane = (JSplitPane) evt.getSource();
        String propertyName = evt.getPropertyName();
        if (propertyName.equals(JSplitPane.DIVIDER_LOCATION_PROPERTY) && !evt.getNewValue().equals((Integer) (-1))) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
//                    dividerChanged();
                    Util.dividerChanged(jTable, jSplitPane);
                }
            });
        }
    }

//    private void resizeSeries() {
//        Dimension minDim = new Dimension(640, Util.getHeight(jTable, Math.min(jTable.getRowCount(), 3), jScrollPaneTop, jSplitPane));
//        Dimension maxDim = new Dimension(Util.getMaximumWindowDimension().width, Util.getHeight(jTable, jTable.getRowCount(), jScrollPaneTop, jSplitPane));
//
//        jScrollPaneTop.setMinimumSize(minDim);
//        jScrollPaneTop.setMaximumSize(maxDim);
//
//        System.out.println("Setting jScrollPaneTable min:" + minDim.height + " max:" + maxDim.height);
//        dividerChanged();
//    }

//    public void dividerChanged() {
//        if (!jScrollPaneBottom.isVisible()) {
//            return;
//        }
//        int current = jSplitPane.getDividerLocation();
////        Insets i = jSplitPane.getInsets();
////        Insets j = jScrollPaneBottom.getInsets();
////        int rc = Util.getRowCount(jTable, current - i.bottom - i.top - j.bottom - j.top);
//        int rc = Util.getRowCount(jTable, current - jScrollPaneTop.getInsets().top);
//        if (rc == 0 && jTable.getRowCount() > 0) {
//            rc = 1;
//        }
//        System.out.println("computed row count:" + rc);
//        if (rc != jTable.getVisibleRowCount()) {
//            Util.setVisibleRowCount(jTable, rc);
//        }
//        Util.setDividerLocation(jSplitPane);
//    }
    private Stat computeStatistics() {
        Stat stat = new Stat();
        Iterator<TestStepInstance> it = getFilteringIterator();
        if (it == null) {
            return null;
        }
        while (it.hasNext()) {
            TestStepInstance step = it.next();
            Number num = getNumber(step);
            if (num != null) {
                stat.addValue(num.doubleValue());
            }
        }
        return stat;
    }

    public void showChart() {
        renderer = null;
        cp = new ChartPanel(getChart());
        cp.addChartMouseListener((ChartMouseListener) this);
        jScrollPaneBottom.setViewportView(cp);
    }

    private JFreeChart getChart() {
        switch (chartMode) {
            case STEP_TIME:
            case SEQUENCE_TIME:
                return getChartTime();
            case LIST:
                return getChartValuesAsListed();
            case DISTRIBUTION:
                return getChartDistribution(false);
        }
//        if (this.jRadioButtonStepTime.isSelected()) {
//            return getChartTime();
//        } else if (this.jRadioButtonSequenceTime.isSelected()) {
//            return getChartValuesByTime();
//        } else if (this.jRadioButtonList.isSelected()) {
//            return getChartValuesBySN();
//        } else if (this.jRadioButtonDistribution.isSelected()) {
//            return getChartDistribution(false);
//        }
        return null;
    }
//    private XYPlot plot;

    public JFreeChart getChartTime() {
        TreeMap<String, List<TestStepInstance>> s = getGroupedSteps(getFilteringIterator());
        if (s == null || s.size() == 0) {
            return null;
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (Iterator<String> en = s.keySet().iterator(); en.hasNext();) {
            String groupName = en.next();
            List<TestStepInstance> stps = s.get(groupName);
//            TimeSeries pop = new TimeSeries(groupName, Millisecond.class);
            TimeSeries pop = new TimeSeries(groupName);
            for (Iterator<TestStepInstance> it = stps.iterator(); it.hasNext();) {
                TestStepInstance step = it.next();
                Number num = getNumber(step);
                if (num != null) {
                    switch (chartMode) {
                        case STEP_TIME:
                            pop.addOrUpdate(RegularTimePeriod.createInstance(Millisecond.class, new Date(step.getStartTime()), TimeZone.getDefault()), num);
                            break;
                        case SEQUENCE_TIME:
//                            pop.addOrUpdate(RegularTimePeriod.createInstance(Millisecond.class, new Date(step.getTestSequenceInstance().getStartTime()), RegularTimePeriod.DEFAULT_TIME_ZONE), num);
                            pop.addOrUpdate(RegularTimePeriod.createInstance(Millisecond.class, new Date(step.getTestSequenceInstance().getCreateTime()), TimeZone.getDefault()), num);
                            break;
                    }
                }
            }
            dataset.addSeries(pop);
        }
        JFreeChart chart = null;
        switch (chartMode) {
            case STEP_TIME:
                chart = ChartFactory.createTimeSeriesChart(
                        null,
                        "Step Started Time",
                        getValueString(),
                        dataset,
                        isGrouping(),
                        true,
                        false);
                break;
            case SEQUENCE_TIME:
                chart = ChartFactory.createTimeSeriesChart(
                        null,
                        "Sequence Started Time",
                        getValueString(),
                        dataset,
                        isGrouping(),
                        true,
                        false);
                break;
        }
        chart.setBackgroundPaint((Paint) UIManager.get("Panel.background"));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        XYLineAndShapeRenderer renderer5 = new XYLineAndShapeRenderer();
        renderer5.setBaseSeriesVisibleInLegend(false);
        plot.setRenderer(renderer5);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setDomainCrosshairVisible(true);
//        chart.setTitle(valueName);
        placeLimitMarkers(plot, true);

        //renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer5.setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());

        /* coloring */
        if (isCategorization()) {
            TreeMap<String, Color> cmap = new TreeMap<String, Color>();
            int i = 0;
            for (Iterator<String> it = catstats.keySet().iterator(); it.hasNext(); i++) {
                String groupName = it.next();
                Color c = ChartCategories.getColor(i);
                for (int j = 0; j < dataset.getSeriesCount(); j++) {
                    TimeSeries ts = dataset.getSeries(j);
                    if (ts.getKey().equals(groupName)) {
                        renderer5.setSeriesPaint(j, c);
                    }
                }
            }
        } else {
            renderer5.setSeriesPaint(0, ChartCategories.getColor(0));
        }

//        chart.addProgressListener(new ChartProgressListener() {
//
//            public void chartProgress(final ChartProgressEvent progress) {
//                SwingUtilities.invokeLater(
//                        new Runnable() {
//
//                            @Override
//                            public void run() {
//
//                                System.out.println("progress:" + progress + " " + progress.getType());
//                                if (progress.getType() == ChartProgressEvent.DRAWING_FINISHED) {
//                                    if (plot != null) {
//                                        if (plot.isDomainCrosshairVisible() && plot.isDomainCrosshairLockedOnData()) {
////                            System.out.println("getDomainCrosshairValue:" + plot.getDomainCrosshairValue());
//                                            double xx = plot.getDomainCrosshairValue();
//                                            if (xx != 0.0) {
//                                                long x = (long) xx;
//                                                System.out.println(new Date(x));
//                                                for (TestStepInstance step : testStepInstances.getSteps()) {
//                                                    if (step.getStartTime() != null && step.getStartTime().equals(x)) {
//                                                        testStepInstances.selectStep(step);
//                                                    }
//                                                }
//                                                System.out.println(new Date(x));
//                                            }
//                                        }
////                        if (plot.isRangeCrosshairVisible()) {
////                            System.out.println("getRangeCrosshairValue:" + plot.getRangeCrosshairValue());
////                        }
//                                    }
//                                }
//                            }
//                        });
//            }
//        });

//        chart.addChangeListener(new ChartChangeListener() {
//
//            public void chartChanged(ChartChangeEvent event) {
//                System.out.println("event:" + event);
//                if (event != null) {
////                    JFreeChart chart = event.getChart();
////                    System.out.println("chart:" + chart);
////                    if (chart != null) {
////                        System.out.println("title:" + event.getChart().getTitle());
////                    }
//                    System.out.println("type:" + event.getType());
//                    if (plot != null) {
//                        if (plot.isDomainCrosshairVisible()) {
//                            System.out.println("getDomainCrosshairValue:" + plot.getDomainCrosshairValue());
//                            long x = (long) plot.getDomainCrosshairValue();
//                            for (TestStepInstance step : testStepInstances.getSteps()) {
//                                if (step.getStartTime() != null && step.getStartTime().equals(x)) {
//                                    testStepInstances.selectStep(step);
//                                }
//                            }
//                            System.out.println(new Date(x));
//                        }
//                        if (plot.isRangeCrosshairVisible()) {
//                            System.out.println("getRangeCrosshairValue:" + plot.getRangeCrosshairValue());
//                        }
//                    }
//                }
//            }
//        });
        chart.setTextAntiAlias(false);
        return chart;
    }

    public void placeLimitMarkers(XYPlot plot, boolean range) {
        if (testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null) {
            if (testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit() != null) {
                final Marker lsl = new ValueMarker(testStepInstances.getReferenceStep().getTestLimit().getLowerSpecifiedLimit());
                lsl.setPaint(limitcolor);
                lsl.setLabel("LSL");
                if (range) {
                    lsl.setLabelAnchor(RectangleAnchor.BOTTOM_RIGHT);
                    lsl.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
                    plot.addRangeMarker(lsl);
                } else {
                    lsl.setLabelAnchor(RectangleAnchor.TOP_LEFT);
                    lsl.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
                    plot.addDomainMarker(lsl);
                }

            }
            if (testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit() != null) {
                final Marker usl = new ValueMarker(testStepInstances.getReferenceStep().getTestLimit().getUpperSpeficiedLimit());
                usl.setPaint(limitcolor);
                usl.setLabel("USL");
                if (range) {
                    usl.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
                    usl.setLabelTextAnchor(TextAnchor.BOTTOM_RIGHT);
                    plot.addRangeMarker(usl);
                } else {
                    usl.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
                    usl.setLabelTextAnchor(TextAnchor.TOP_LEFT);
                    plot.addDomainMarker(usl);
                }
            }
        }
    }

    public String getValueString() {
        switch (testStepInstances.getMode()) {
            case PARAMETRIC:
                if (testStepInstances.getReferenceStep() != null && testStepInstances.getReferenceStep().getTestLimit() != null && testStepInstances.getReferenceStep().getTestLimit().getMeasurementUnit() != null) {
                    return VALUE_STR + " [" + testStepInstances.getReferenceStep().getTestLimit().getMeasurementUnit() + "]";
                }
                return VALUE_STR;
            case RUNTIME:
                return "Elapsed Time [ms]";
        }
        return null;
    }

    public JFreeChart getChartValues(
            Iterator<TestStepInstance> values) {
        if (values == null || !values.hasNext()) {
            return null;
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        int count = 0;
        TreeMap<String, XYSeries> map = new TreeMap<String, XYSeries>();
        while (values.hasNext()) {
            TestStepInstance step = values.next();
            Number num = getNumber(step);
            if (num != null) {
                String groupName = getGroupName(step);
                XYSeries pop = map.get(groupName);
                if (pop == null) {
                    pop = new XYSeries(groupName);
                    map.put(groupName, pop);
                }

                pop.add(++count, num.doubleValue());
            }

        }
        for (Iterator<XYSeries> it = map.values().iterator(); it.hasNext();) {
            dataset.addSeries(it.next());
        }

//        NumberAxis xAxis = new NumberAxis("#");
        NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRangeIncludesZero(false);
        NumberAxis yAxis = new NumberAxis(getValueString());
        yAxis.setAutoRangeIncludesZero(false);
        XYLineAndShapeRenderer renderer6 = new XYLineAndShapeRenderer();
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer6);
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeCrosshairVisible(true);
        plot.setDomainCrosshairVisible(true);
        renderer6.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        renderer6.setBaseSeriesVisibleInLegend(false);

//        StandardXYItemLabelGenerator itemlabels=new StandardXYItemLabelGenerator();
//        renderer.setBaseItemLabelGenerator(itemlabels);
//        renderer.setBaseItemLabelsVisible(true);

        JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, isGrouping());
        //chart.setTitle(title);
        placeLimitMarkers(plot, true);
        /* coloring */
        if (isCategorization()) {
            TreeMap<String, Color> cmap = new TreeMap<String, Color>();
            int i = 0;
            for (Iterator<String> it = catstats.keySet().iterator(); it.hasNext(); i++) {
                String groupName = it.next();
                Color c = ChartCategories.getColor(i);
                for (int j = 0; j < dataset.getSeriesCount(); j++) {
                    XYSeries s = dataset.getSeries(j);
                    if (s.getKey().equals(groupName)) {
                        renderer6.setSeriesPaint(j, c);
                    }
                }
            }
        } else {
            renderer6.setSeriesPaint(0, ChartCategories.getColor(0));
        }
        chart.setTextAntiAlias(false);
        return chart;
    }

    public JFreeChart getChartValuesAsListed() {
        return getChartValues(testStepInstances.getShorted(getFilteringIterator()));
    }
    private MyBarRenderer renderer;

    public JFreeChart getChartDistribution(
            boolean horizontal) {
//        System.out.println("Min: " + minValue());
//        System.out.println("Max: " + maxValue());
        XYIntervalSeriesCollection datasetDistribution = createIntervalXYDatasetDistribution(horizontal);
        XYSeriesCollection dataset2 = createXYDatasetGauss(horizontal);
        // create the chart...
        NumberAxis xAxis = new NumberAxis(getValueString());
        xAxis.setAutoRangeIncludesZero(false);
//        NumberAxis yAxis = new NumberAxis("Distribution");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRangeIncludesZero(true);
        //XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer = new MyBarRenderer();
        XYPlot plot = new XYPlot(datasetDistribution, xAxis, yAxis, renderer);
        plot.setOrientation(horizontal ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL);
        renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
        JFreeChart chart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, isGrouping());
        chart.setBackgroundPaint((Paint) UIManager.get("Panel.background"));
//        plot.setBackgroundPaint(Color.white);
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        StandardXYItemLabelGenerator itemlabels = new StandardXYItemLabelGenerator();
        renderer.setBaseItemLabelGenerator(itemlabels);
        renderer.setBaseItemLabelsVisible(true);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        ValueAxis domainAxis = plot.getDomainAxis();
        //domainAxis.setCategoryLabelPositions(horizontal?CategoryLabelPositions.STANDARD:CategoryLabelPositions.UP_90);
        ValueAxis axis2 = new NumberAxis("Gaussian");
        plot.setRangeAxis(1, axis2);
        axis2.setVisible(false);
        final XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        //renderer2.setShapesVisible(false);
        //renderer2.setSeriesVisibleInLegend(false);
        renderer2.setBaseSeriesVisibleInLegend(false);
        //renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setRenderer(1, renderer2);
        renderer.setUseYInterval(true);
        renderer.setBaseSeriesVisibleInLegend(false);
        /* coloring */
        Color c;
        if (isMultipleCategorization()) {
            TreeMap<String, Color> cmap = new TreeMap<String, Color>();
            int i = 0;
            for (Iterator<String> it = catstats.keySet().iterator(); it.hasNext(); i++) {
                String groupName = it.next();
                c = ChartCategories.getColor(i);
                for (int j = 0; j < datasetDistribution.getSeriesCount(); j++) {
                    XYIntervalSeries s = datasetDistribution.getSeries(j);
                    if (s.getKey().equals(groupName)) {
                        GradientPaint gp = new GradientPaint(0.0f, 0.0f, c, 0.0f, 0.0f, c.darker().darker());
                        renderer.setSeriesPaint(j, gp);
                    }
                }
                for (int j = 0; j < dataset2.getSeriesCount(); j++) {
                    XYSeries s = dataset2.getSeries(j);
                    if (s.getKey().equals(groupName)) {
                        renderer2.setSeriesPaint(j, c);
                        renderer2.setSeriesShapesVisible(j, false);
                        renderer2.setSeriesStroke(j, myStroke);
                    }
                }
            }
            c = Color.black;
        } else {
            c = ChartCategories.getColor(0);
            GradientPaint gp = new GradientPaint(0.0f, 0.0f, c, 0.0f, 0.0f, c.darker().darker());
            renderer.setSeriesPaint(0, gp);
        }
        renderer2.setSeriesPaint(0, c);
        renderer2.setSeriesShapesVisible(0, false);
        renderer2.setSeriesStroke(0, myStroke);

        placeLimitMarkers(plot, false);
//        renderer.setAutoPopulateSeriesOutlinePaint(true);
//        renderer.setBaseOutlinePaint(Color.black);
//        renderer.setSeriesOutlinePaint(0, Color.black, true);
//        renderer.setDrawBarOutline(true);

        renderer.setHighlightedItem(0, 0);
        yAxis.setAutoRange(false);
        yAxis.setAutoRange(true);
        xAxis.setRange(leftValue(0), rightValue(numberOfCategories - 1));
        chart.setTextAntiAlias(false);
        return chart;
    }
//    private XYIntervalSeriesCollection dataset;

    public double barWidth() {
        return allstat.getStandardDeviation() * getNumberOfSigmas(allstat) / numberOfCategories;
    }

    private XYIntervalSeriesCollection createIntervalXYDatasetDistribution(boolean inverted) {
        //create the population map
        TreeMap<String, int[]> map = new TreeMap<String, int[]>();
        Iterator<TestStepInstance> it = getFilteringIterator();
        while (it.hasNext()) {
            TestStepInstance step = it.next();
            String groupName = getGroupName(step);
            int[] population = map.get(groupName);
            if (population == null) {
                population = new int[numberOfCategories];
                for (int i = 0; i < numberOfCategories; i++) {
                    population[i] = 0;
                }

                map.put(groupName, population);
            }
//Stat stat=catstats.get(groupName);
            Stat stat = allstat;
            if (stat != null) {
                Number num = getNumber(step);
                if (num != null) {
                    double val = num.doubleValue();
                    int category;
                    if (stat.getStandardDeviation() != 0) {
                        category = (int) (numberOfCategories / 2.0 + numberOfCategories * (val - stat.getAverage()) / (getNumberOfSigmas(stat) * stat.getStandardDeviation()));
                    } else {
                        category = numberOfCategories / 2;
                    }

                    if (category >= 0 && category < numberOfCategories) {
                        population[category]++;
                    }
//                    else {
//                        System.out.println("Value: " + val + "  Category: " + category);
//                    }
                }
            } else {
                System.err.println("There is no stat for:" + groupName);
            }

        }
        XYIntervalSeriesCollection dataset = new XYIntervalSeriesCollection();
        int cat = 0;
        int[] prev = new int[numberOfCategories];
        for (int i = 0; i < prev.length; i++) {
            prev[i] = 0;
        }
        for (Iterator<String> it2 = map.keySet().iterator(); it2.hasNext(); cat++) {
            String groupName = it2.next();
            int[] population = map.get(groupName);
            XYIntervalSeries pop = new XYIntervalSeries(groupName);
            if (inverted) {
                for (int i = numberOfCategories - 1; i >= 0; i--) {
                    if (population[i] != 0) {
//                        pop.add(midValue(i), leftValue(i, cat, map.size()), rightValue(i, cat, map.size()), population[i], prev[i], prev[i] + population[i]);
                        pop.add(midValue(i), leftValue(i), rightValue(i), population[i], prev[i], prev[i] + population[i]);
                        prev[i] = prev[i] + population[i];
                    }
                }
            } else {
                for (int i = 0; i < numberOfCategories; i++) {
                    if (population[i] != 0) {
//                        pop.add(midValue(i), leftValue(i, cat, map.size()), rightValue(i, cat, map.size()), population[i], prev[i], prev[i] + population[i]);
                        pop.add(midValue(i), leftValue(i), rightValue(i), population[i], prev[i], prev[i] + population[i]);
                        prev[i] = prev[i] + population[i];
                    }
                }
            }
            dataset.addSeries(pop);
        }
        return dataset;
    }

    private XYSeriesCollection createXYDatasetGauss(boolean inverted) {
//        System.out.println("createXYDatasetGauss");
        XYSeriesCollection dataset = new XYSeriesCollection();
        if (allstat.isDistribution()) {
            XYSeries pop = new XYSeries("Gauss");
            if (inverted) {
                for (int i = plots - 1; i >= 0; i--) {
                    //System.out.println("adding: i="+i+" value="+Gaussian.getGaussian(midValue(i),stat.getAverage(),stat.getStandardDeviation())+" group="+groupName);
                    double val = plotValue(i, plots);
                    pop.add(val, Gaussian.getGaussian(val, allstat.getAverage(), allstat.getStandardDeviation()));
                }

            } else {
                for (int i = 0; i < plots; i++) {
                    //System.out.println("adding: i="+i+" value="+Gaussian.getGaussian(midValue(i),stat.getAverage(),stat.getStandardDeviation())+" group="+groupName);
                    double val = plotValue(i, plots);
                    pop.add(val, Gaussian.getGaussian(val, allstat.getAverage(), allstat.getStandardDeviation()));
                }
            }
            dataset.addSeries(pop);
            if (isMultipleCategorization()) {
                for (Iterator<String> it = catstats.keySet().iterator(); it.hasNext();) {
                    String groupName = it.next();
                    Stat stat = catstats.get(groupName);
                    if (stat.isDistribution()) {
                        pop = new XYSeries(groupName);
//                        System.out.println(groupName + " Average: " + stat.getAverage());
//                        System.out.println(groupName + " Std.Dev: " + stat.getStandardDeviation());
                        if (inverted) {
                            for (int i = plots - 1; i >= 0; i--) {
                                //System.out.println("adding: i="+i+" value="+Gaussian.getGaussian(midValue(i),stat.getAverage(),stat.getStandardDeviation())+" group="+groupName);
                                double val = plotValue(i, plots);
                                pop.add(val, Gaussian.getGaussian(val, stat.getAverage(), stat.getStandardDeviation()));
                            }

                        } else {
                            for (int i = 0; i < plots; i++) {
                                //System.out.println("adding: i="+i+" value="+Gaussian.getGaussian(midValue(i),stat.getAverage(),stat.getStandardDeviation())+" group="+groupName);
                                double val = plotValue(i, plots);
                                pop.add(val, Gaussian.getGaussian(val, stat.getAverage(), stat.getStandardDeviation()));
                            }
                        }
                        dataset.addSeries(pop);
                    }
                }
            }
        }
        return dataset;
    }

    protected boolean acceptStatus(TestStepInstance step) {
        if (this.jRadioButtonAllStatus.isSelected()) {
            return true;
        }

        if (this.jRadioButtonPassedStep.isSelected()) {
            return step.isPassed();
        }

        if (this.jRadioButtonPassedSequence.isSelected()) {
            return step.getTestSequenceInstance().isPassed();
        }

        return false;
    }

    protected boolean acceptTestCount(TestStepInstance step) {
        if (this.jRadioButtonAllCount.isSelected()) {
            return true;
        }
//        if (this.jRadioButtonFirst.isSelected()) {
//            return step.getUUT().getTestCount() == 1;
//        }
//        if (this.jRadioButtonSecond.isSelected()) {
//            return step.getUUT().getTestCount() == 2;
//        }
//        if (this.jRadioButtonThird.isSelected()) {
//            return step.getUUT().getTestCount() == 3;
//        }
//        if (this.jRadioButtonLast.isSelected()) {
//            return step.getUUT().isLast();
//        }
        return false;
    }

    protected boolean acceptNumeric(TestStepInstance step) {
        return step.getValueNumber() != null;
    }

    protected boolean accept(TestStepInstance step) {
        switch (testStepInstances.getMode()) {
            case PARAMETRIC:
                return acceptNumeric(step) && acceptStatus(step) && acceptTestCount(step);
            case RUNTIME:
                return acceptStatus(step) && acceptTestCount(step);
        }

        return false;
    }

    protected class FilteringIterator implements Iterator<TestStepInstance> {

        private Iterator<TestStepInstance> source;
        private TestStepInstance next;

        public FilteringIterator(Iterator<TestStepInstance> s) {
            source = s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            while ((next == null) && (source.hasNext())) {
                TestStepInstance o = source.next();
                if (accept(o)) {
                    next = o;
                    return true;
                }
            }
            return (next != null);
        }

        @Override
        public TestStepInstance next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            TestStepInstance retval = next;
            next = null;
            return retval;
        }
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        if (renderer != null) {
            ChartEntity entity = event.getEntity();
            if (entity != null) {
                if (entity instanceof XYItemEntity) {
                    XYItemEntity xyie = (XYItemEntity) entity;
                    if (renderer != null) {
                        renderer.setHighlightedItem(xyie.getSeriesIndex(), xyie.getItem());
                        return;
                    }
                }
            }
            renderer.setHighlightedItem(-1, -1);
        }
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
        ChartEntity entity = event.getEntity();
        if (entity != null) {
//            System.out.println("clicked entity: " + entity);
            if (entity instanceof XYItemEntity) {
                XYItemEntity xyie = (XYItemEntity) entity;
//                System.out.println("series: " + xyie.getSeriesIndex() + ", item: " + xyie.getItem());
                XYDataset dset = xyie.getDataset();
                if (dset != null) {
                    final Number xNumber = dset.getX(xyie.getSeriesIndex(), xyie.getItem());
                    if (xNumber != null) {
                        SwingUtilities.invokeLater(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                        long xLong;
                                        switch (chartMode) {
                                            case STEP_TIME:
                                                xLong = xNumber.longValue();
                                                if (xLong != 0L) {
//                                                    System.out.println("Selecting step, which is started at " + new Date(xLong));
                                                    for (TestStepInstance step : testStepInstances) {
                                                        if (step.getStartTime() != null && step.getStartTime().equals(xLong)) {
                                                            testStepInstances.selectStep(step);
                                                            cp.requestFocus();
                                                            return;
                                                        }
                                                    }
                                                }
                                                break;
                                            case SEQUENCE_TIME:
                                                xLong = xNumber.longValue();
                                                if (xLong != 0L) {
//                                                    System.out.println("Selecting sequence, which is started at " + new Date(xLong));
                                                    for (TestStepInstance step : testStepInstances) {
                                                        if (step.getTestSequenceInstance() != null && step.getTestSequenceInstance().getCreateTime() == xLong) {
                                                            testStepInstances.selectStep(step);
                                                            cp.requestFocus();
                                                            return;
                                                        }
                                                    }
                                                }
                                                break;
                                            case LIST:
                                                testStepInstances.selectStep(xNumber.intValue() - 1);
                                                break;
                                            case DISTRIBUTION:
                                                double min = xNumber.doubleValue() - barWidth() / 2.0;
                                                double max = xNumber.doubleValue() + barWidth() / 2.0;
//                                                System.out.println("X: " + xNumber + " min: " + min + " max: " + max);
                                                List<TestStepInstance> select = new ArrayList<TestStepInstance>();
                                                for (TestStepInstance step : testStepInstances) {
                                                    Number number = getNumber(step);
                                                    if (number != null) {
                                                        Double d = number.doubleValue();
                                                        if (d >= min && d < max) {
                                                            select.add(step);
                                                        }
                                                    }

                                                }
                                                testStepInstances.selectSteps(select);
                                                cp.requestFocus();
                                                break;
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupType = new javax.swing.ButtonGroup();
        buttonGroupStatusFilter = new javax.swing.ButtonGroup();
        buttonGroupCountFilter = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanelMainStats = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabelLSL = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabelUSL = new javax.swing.JLabel();
        jPanelChartType = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jRadioButtonStepTime = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jRadioButtonSequenceTime = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jRadioButtonList = new javax.swing.JRadioButton();
        jPanel8 = new javax.swing.JPanel();
        jRadioButtonDistribution = new javax.swing.JRadioButton();
        jPanelSeparate = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jCheckBoxStation = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        jCheckBoxFixture = new javax.swing.JCheckBox();
        jPanel22 = new javax.swing.JPanel();
        jCheckBoxOperator = new javax.swing.JCheckBox();
        jPanel18 = new javax.swing.JPanel();
        jCheckBoxPartNumber = new javax.swing.JCheckBox();
        jPanel23 = new javax.swing.JPanel();
        jCheckBoxPartRevision = new javax.swing.JCheckBox();
        jPanel21 = new javax.swing.JPanel();
        jCheckBoxTestType = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jCheckBoxSerialNumber = new javax.swing.JCheckBox();
        jPanelStatusFilter = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jRadioButtonAllStatus = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jRadioButtonPassedStep = new javax.swing.JRadioButton();
        jPanel11 = new javax.swing.JPanel();
        jRadioButtonPassedSequence = new javax.swing.JRadioButton();
        jPanelCountFilter = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jRadioButtonAllCount = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        jRadioButtonFirst = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        jRadioButtonSecond = new javax.swing.JRadioButton();
        jPanel15 = new javax.swing.JPanel();
        jRadioButtonThird = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        jRadioButtonLast = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane = new javax.swing.JSplitPane();
        jScrollPaneTop = new javax.swing.JScrollPane();
        jTable = new org.jdesktop.swingx.JXTable();
        jScrollPaneBottom = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanelMainStats.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Limits"));
        jPanelMainStats.setLayout(new javax.swing.BoxLayout(jPanelMainStats, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelLSL.setText("LSL:...");
        jPanel3.add(jLabelLSL);

        jPanelMainStats.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelUSL.setText("USL:...");
        jPanel4.add(jLabelUSL);

        jPanelMainStats.add(jPanel4);

        jPanel1.add(jPanelMainStats);

        jPanelChartType.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Chart Type"));
        jPanelChartType.setLayout(new javax.swing.BoxLayout(jPanelChartType, javax.swing.BoxLayout.Y_AXIS));

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupType.add(jRadioButtonStepTime);
        jRadioButtonStepTime.setText("Step Started Time");
        jRadioButtonStepTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonStepTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonStepTimeActionPerformed(evt);
            }
        });
        jPanel5.add(jRadioButtonStepTime);

        jPanelChartType.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupType.add(jRadioButtonSequenceTime);
        jRadioButtonSequenceTime.setText("Sequence Started Time");
        jRadioButtonSequenceTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonSequenceTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSequenceTimeActionPerformed(evt);
            }
        });
        jPanel6.add(jRadioButtonSequenceTime);

        jPanelChartType.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupType.add(jRadioButtonList);
        jRadioButtonList.setText("As Listed");
        jRadioButtonList.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonListActionPerformed(evt);
            }
        });
        jPanel7.add(jRadioButtonList);

        jPanelChartType.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupType.add(jRadioButtonDistribution);
        jRadioButtonDistribution.setSelected(true);
        jRadioButtonDistribution.setText("Distribution");
        jRadioButtonDistribution.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonDistribution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonDistributionActionPerformed(evt);
            }
        });
        jPanel8.add(jRadioButtonDistribution);

        jPanelChartType.add(jPanel8);

        jPanel1.add(jPanelChartType);

        jPanelSeparate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Categories"));
        jPanelSeparate.setLayout(new javax.swing.BoxLayout(jPanelSeparate, javax.swing.BoxLayout.Y_AXIS));

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxStation.setText("Station");
        jCheckBoxStation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxStationActionPerformed(evt);
            }
        });
        jPanel17.add(jCheckBoxStation);

        jPanelSeparate.add(jPanel17);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxFixture.setText("Fixture");
        jCheckBoxFixture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxFixtureActionPerformed(evt);
            }
        });
        jPanel19.add(jCheckBoxFixture);

        jPanelSeparate.add(jPanel19);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxOperator.setText("Operator");
        jCheckBoxOperator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxOperatorActionPerformed(evt);
            }
        });
        jPanel22.add(jCheckBoxOperator);

        jPanelSeparate.add(jPanel22);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxPartNumber.setText("Part Number");
        jCheckBoxPartNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPartNumberActionPerformed(evt);
            }
        });
        jPanel18.add(jCheckBoxPartNumber);

        jPanelSeparate.add(jPanel18);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxPartRevision.setText("Part Revision");
        jCheckBoxPartRevision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPartRevisionActionPerformed(evt);
            }
        });
        jPanel23.add(jCheckBoxPartRevision);

        jPanelSeparate.add(jPanel23);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxTestType.setText("Test Type");
        jCheckBoxTestType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTestTypeActionPerformed(evt);
            }
        });
        jPanel21.add(jCheckBoxTestType);

        jPanelSeparate.add(jPanel21);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 1, 0));

        jCheckBoxSerialNumber.setText("Serial Number");
        jCheckBoxSerialNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSerialNumberActionPerformed(evt);
            }
        });
        jPanel20.add(jCheckBoxSerialNumber);

        jPanelSeparate.add(jPanel20);

        jPanel1.add(jPanelSeparate);

        jPanelStatusFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Test Status Filter"));
        jPanelStatusFilter.setLayout(new javax.swing.BoxLayout(jPanelStatusFilter, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupStatusFilter.add(jRadioButtonAllStatus);
        jRadioButtonAllStatus.setSelected(true);
        jRadioButtonAllStatus.setText("All");
        jRadioButtonAllStatus.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonAllStatus.setPreferredSize(null);
        jRadioButtonAllStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonAllStatusActionPerformed(evt);
            }
        });
        jPanel9.add(jRadioButtonAllStatus);

        jPanelStatusFilter.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupStatusFilter.add(jRadioButtonPassedStep);
        jRadioButtonPassedStep.setText("Passed Step");
        jRadioButtonPassedStep.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonPassedStep.setPreferredSize(null);
        jRadioButtonPassedStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPassedStepActionPerformed(evt);
            }
        });
        jPanel10.add(jRadioButtonPassedStep);

        jPanelStatusFilter.add(jPanel10);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupStatusFilter.add(jRadioButtonPassedSequence);
        jRadioButtonPassedSequence.setText("Passed Sequence");
        jRadioButtonPassedSequence.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonPassedSequence.setPreferredSize(null);
        jRadioButtonPassedSequence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonPassedSequenceActionPerformed(evt);
            }
        });
        jPanel11.add(jRadioButtonPassedSequence);

        jPanelStatusFilter.add(jPanel11);

        jPanel1.add(jPanelStatusFilter);

        jPanelCountFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Test Count Filter"));
        jPanelCountFilter.setLayout(new javax.swing.BoxLayout(jPanelCountFilter, javax.swing.BoxLayout.Y_AXIS));

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupCountFilter.add(jRadioButtonAllCount);
        jRadioButtonAllCount.setSelected(true);
        jRadioButtonAllCount.setText("All");
        jRadioButtonAllCount.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonAllCount.setPreferredSize(null);
        jRadioButtonAllCount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonAllCountActionPerformed(evt);
            }
        });
        jPanel12.add(jRadioButtonAllCount);

        jPanelCountFilter.add(jPanel12);

        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupCountFilter.add(jRadioButtonFirst);
        jRadioButtonFirst.setText("First");
        jRadioButtonFirst.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonFirst.setPreferredSize(null);
        jRadioButtonFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonFirstActionPerformed(evt);
            }
        });
        jPanel13.add(jRadioButtonFirst);

        jPanelCountFilter.add(jPanel13);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupCountFilter.add(jRadioButtonSecond);
        jRadioButtonSecond.setText("Second");
        jRadioButtonSecond.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonSecond.setPreferredSize(null);
        jRadioButtonSecond.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonSecondActionPerformed(evt);
            }
        });
        jPanel14.add(jRadioButtonSecond);

        jPanelCountFilter.add(jPanel14);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupCountFilter.add(jRadioButtonThird);
        jRadioButtonThird.setText("Third");
        jRadioButtonThird.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonThird.setPreferredSize(null);
        jRadioButtonThird.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonThirdActionPerformed(evt);
            }
        });
        jPanel15.add(jRadioButtonThird);

        jPanelCountFilter.add(jPanel15);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        buttonGroupCountFilter.add(jRadioButtonLast);
        jRadioButtonLast.setText("Last");
        jRadioButtonLast.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonLast.setPreferredSize(null);
        jRadioButtonLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLastActionPerformed(evt);
            }
        });
        jPanel16.add(jRadioButtonLast);

        jPanelCountFilter.add(jPanel16);

        jPanel1.add(jPanelCountFilter);

        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel2);

        add(jPanel1, java.awt.BorderLayout.WEST);

        jSplitPane.setBorder(null);
        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jScrollPaneTop.setBorder(null);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable.setName("Categories"); // NOI18N
        jTable.getTableHeader().setReorderingAllowed(false);
        jScrollPaneTop.setViewportView(jTable);

        jSplitPane.setTopComponent(jScrollPaneTop);

        jScrollPaneBottom.setBorder(null);
        jSplitPane.setBottomComponent(jScrollPaneBottom);

        add(jSplitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

private void jRadioButtonStepTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonStepTimeActionPerformed
    setChartMode(ChartMode.STEP_TIME);
}//GEN-LAST:event_jRadioButtonStepTimeActionPerformed

private void jRadioButtonSequenceTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSequenceTimeActionPerformed
    setChartMode(ChartMode.SEQUENCE_TIME);
}//GEN-LAST:event_jRadioButtonSequenceTimeActionPerformed

private void jRadioButtonListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonListActionPerformed
    setChartMode(ChartMode.LIST);
}//GEN-LAST:event_jRadioButtonListActionPerformed

private void jRadioButtonDistributionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonDistributionActionPerformed
    setChartMode(ChartMode.DISTRIBUTION);
}//GEN-LAST:event_jRadioButtonDistributionActionPerformed

private void jRadioButtonAllStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonAllStatusActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonAllStatusActionPerformed

private void jRadioButtonPassedStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPassedStepActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonPassedStepActionPerformed

private void jRadioButtonPassedSequenceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonPassedSequenceActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonPassedSequenceActionPerformed

private void jRadioButtonAllCountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonAllCountActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonAllCountActionPerformed

private void jRadioButtonFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonFirstActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonFirstActionPerformed

private void jRadioButtonSecondActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonSecondActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonSecondActionPerformed

private void jRadioButtonThirdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonThirdActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonThirdActionPerformed

private void jRadioButtonLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLastActionPerformed
    selectionChanged();
}//GEN-LAST:event_jRadioButtonLastActionPerformed

private void jCheckBoxStationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxStationActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxStationActionPerformed

private void jCheckBoxPartNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPartNumberActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxPartNumberActionPerformed

private void jCheckBoxFixtureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxFixtureActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxFixtureActionPerformed

private void jCheckBoxSerialNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSerialNumberActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxSerialNumberActionPerformed

private void jCheckBoxTestTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTestTypeActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxTestTypeActionPerformed

private void jCheckBoxOperatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxOperatorActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxOperatorActionPerformed

private void jCheckBoxPartRevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPartRevisionActionPerformed
    selectionChanged();
}//GEN-LAST:event_jCheckBoxPartRevisionActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupCountFilter;
    private javax.swing.ButtonGroup buttonGroupStatusFilter;
    private javax.swing.ButtonGroup buttonGroupType;
    private javax.swing.JCheckBox jCheckBoxFixture;
    private javax.swing.JCheckBox jCheckBoxOperator;
    private javax.swing.JCheckBox jCheckBoxPartNumber;
    private javax.swing.JCheckBox jCheckBoxPartRevision;
    private javax.swing.JCheckBox jCheckBoxSerialNumber;
    private javax.swing.JCheckBox jCheckBoxStation;
    private javax.swing.JCheckBox jCheckBoxTestType;
    private javax.swing.JLabel jLabelLSL;
    private javax.swing.JLabel jLabelUSL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelChartType;
    private javax.swing.JPanel jPanelCountFilter;
    private javax.swing.JPanel jPanelMainStats;
    private javax.swing.JPanel jPanelSeparate;
    private javax.swing.JPanel jPanelStatusFilter;
    private javax.swing.JRadioButton jRadioButtonAllCount;
    private javax.swing.JRadioButton jRadioButtonAllStatus;
    private javax.swing.JRadioButton jRadioButtonDistribution;
    private javax.swing.JRadioButton jRadioButtonFirst;
    private javax.swing.JRadioButton jRadioButtonLast;
    private javax.swing.JRadioButton jRadioButtonList;
    private javax.swing.JRadioButton jRadioButtonPassedSequence;
    private javax.swing.JRadioButton jRadioButtonPassedStep;
    private javax.swing.JRadioButton jRadioButtonSecond;
    private javax.swing.JRadioButton jRadioButtonSequenceTime;
    private javax.swing.JRadioButton jRadioButtonStepTime;
    private javax.swing.JRadioButton jRadioButtonThird;
    private javax.swing.JScrollPane jScrollPaneBottom;
    private javax.swing.JScrollPane jScrollPaneTop;
    private javax.swing.JSplitPane jSplitPane;
    private org.jdesktop.swingx.JXTable jTable;
    // End of variables declaration//GEN-END:variables
}
