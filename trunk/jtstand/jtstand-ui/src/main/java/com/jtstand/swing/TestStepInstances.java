/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepInstances.java is part of JTStand.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand.swing;

import com.jtstand.TestStepInstance;
import com.jtstand.session.TestStepInstanceList;
import com.jtstand.swing.TestStepInstancesModel.StepsColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.*;
import org.jdesktop.swingx.decorator.SortOrder;
import org.jdesktop.swingx.table.ColumnHeaderRenderer;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class TestStepInstances extends TestStepInstanceList implements PropertyChangeListener {

    public static final long serialVersionUID = 20081114L;
    static final Logger logger = Logger.getLogger(TestStepInstances.class.getCanonicalName());
//    Object stepsLock = new Object();
//    private List<TestStepInstance> steps = Collections.synchronizedList(new ArrayList<TestStepInstance>());
    private JXTable jTable;
    private JSplitPane jSplitPane;
    private JFrame frame;
    private JScrollPane jScrollPaneTop;
    private JScrollPane jScrollPaneBottom;
    private TestStepInstance referenceStep;
    private StatsPanel statsPanel = null;

    public static enum Mode {

        NONE, PARAMETRIC, RUNTIME
    };
    private Mode mode;

    @Override
    public boolean add(TestStepInstance step) {
        boolean changed = super.add(step);
        if (changed) {
            updateTableView(size());
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> list) {
        synchronized (lock) {
            if (jTable != null) {
                jTable.clearSelection();
            }
            boolean changed = super.removeAll(list);
            if (changed) {
                updateTableView(size());
            }
            return changed;
        }
    }

    @Override
    public boolean addAll(Collection<? extends TestStepInstance> list) {
        boolean changed = super.addAll(list);
        if (changed) {
            updateTableView(size());
        }
        return changed;
    }

    void selectStep(int row) {
        synchronized (lock) {
            if (row >= 0 && row < jTable.getRowCount()) {
                jTable.setRowSelectionInterval(row, row);
                Util.scrollToCenter(jTable, row, row);
                return;
            }
        }
    }

    public void selectStep(TestStepInstance step) {
        synchronized (lock) {
            int modelIndex = indexOf(step);
            if (modelIndex >= 0) {
                int row = jTable.convertRowIndexToView(modelIndex);
                if (row >= 0) {
                    jTable.setRowSelectionInterval(row, row);
                    Util.scrollToCenter(jTable, row, row);
                    return;
                }
            }
//            else {
//                System.out.println("[" + step.getClass().getCanonicalName() + ":" + step.getTestSequenceInstance() + ":" + step.getTestStepNamePath() + "]");
//                System.out.println("not found in:");
//                for (TestStepInstance step2 : this) {
//                    System.out.println("[" + step2.getClass().getCanonicalName() + ":" + step2.getTestSequenceInstance() + ":" + step2.getTestStepNamePath() + "]" + step2.equals(step) + ":" + step2.getTestSequenceInstance().equals(step.getTestSequenceInstance()) + ":" + step2.getTestStepNamePath().equals(step.getTestStepNamePath()) + ":" + step2.equals(step));
//                    if (step2.getStartTime().equals(step.getStartTime())) {
//                        System.out.println("[" + step.getClass().getCanonicalName() + ":" + step.getTestSequenceInstance() + ":" + step.getTestStepNamePath() + "]!");
//                        System.out.println((step instanceof TestStepInstance) + ":" + (step2 instanceof TestStepInstance));
//                        System.out.println(step.hashCode() + ":" + step2.hashCode());
//                    }
//                }
//            }
            jTable.clearSelection();
        }
    }

    public void selectSteps(List<TestStepInstance> select) {
        synchronized (lock) {
            int[] rows = jTable.getSelectedRows();
            for (int i = 0; i < rows.length; i++) {
                int row = rows[i];
                int modelIndex = jTable.convertRowIndexToModel(row);
                if (!select.contains(get(modelIndex))) {
                    jTable.getSelectionModel().removeSelectionInterval(row, row);
                }
            }
            int row = -1;
            for (TestStepInstance step : select) {
                int modelIndex = indexOf(step);
                System.out.println("modelIndex:" + modelIndex);
                if (modelIndex >= 0) {
                    row = jTable.convertRowIndexToView(modelIndex);
                    System.out.println("row:" + row);
                    if (row >= 0) {
                        jTable.getSelectionModel().addSelectionInterval(row, row);
                    }
                } else {
                    System.out.println("[" + step.getClass().getCanonicalName() + ":" + step.getTestSequenceInstance() + ":" + step.getTestStepNamePath() + "]");
                    System.out.println("not found in:");
                    for (TestStepInstance step2 : this) {
                        System.out.println("[" + step2.getClass().getCanonicalName() + ":" + step2.getTestSequenceInstance() + ":" + step2.getTestStepNamePath() + "]" + step2.equals(step) + ":" + step2.getTestSequenceInstance().equals(step.getTestSequenceInstance()) + ":" + step2.getTestStepNamePath().equals(step.getTestStepNamePath()) + ":" + step2.equals(step));
                        if (step2.getStartTime().equals(step.getStartTime())) {
                            System.out.println("[" + step.getClass().getCanonicalName() + ":" + step.getTestSequenceInstance() + ":" + step.getTestStepNamePath() + "]!");
                            System.out.println((step instanceof TestStepInstance) + ":" + (step2 instanceof TestStepInstance));
                            System.out.println(step.hashCode() + ":" + step2.hashCode());
                        }
                    }
                }
            }
            /* Scroll to the row, which was selected last */
            if (row >= 0) {
                Util.scrollToCenter(jTable, row, row);
            }
        }
    }
    private MainFrame mainFrame;

    public TestStepInstances(MainFrame mainFrame, TestStepInstances.Mode mode) {
        super(mainFrame.getTestStation().createEntityManager());
        this.mainFrame = mainFrame;
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public TestStepInstance getReferenceStep() {
        return referenceStep;
    }

    public void setReferenceStep(TestStepInstance referenceStep) {
        this.referenceStep = referenceStep;
    }

    public TestStepInstance getLatestStep() {
        TestStepInstance latest = null;
        synchronized (lock) {
            for (TestStepInstance step : this) {
                if (latest == null || step.getTestSequenceInstance().getCreateTime() > latest.getTestSequenceInstance().getCreateTime()) {
                    latest = step;
                }
            }
        }
        return latest;
    }

    public JFrame getFrame() {
        if (getReferenceStep() == null) {
            setReferenceStep(getLatestStep());
        }
        if (frame == null) {
            synchronized (lock) {
                if (size() == 0) {
                    return null;
                }
            }
            frame = new JFrame();
            frame.getContentPane().add(getSplitPane(), BorderLayout.CENTER);
            frame.setTitle("List of Steps");
//            Dimension dim = new Dimension(1024, 768);
//            frame.setSize(dim);
//            frame.setPreferredSize(dim);
            Util.maxIt(frame);
        }
        frame.pack();

//        jScrollPaneTop.setMinimumSize(new Dimension(200, jScrollPaneTop.getSize().height));
//        Util.setVisibleRowCount(jTable, Math.min(jTable.getRowCount(), 3), jSplitPane);
        frame.setVisible(true);
//        System.out.println("Frame is visible.");
        Util.setDividerLocation(jSplitPane, jTable);
        jSplitPane.addPropertyChangeListener(this);
        return frame;
    }

    private JSplitPane getSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
//            jSplitPane.setResizeWeight(0.18);
            jScrollPaneTop = new JScrollPane();
            jScrollPaneTop.setBorder(null);
            jScrollPaneTop.setViewportView(getJTable());
            jSplitPane.setTopComponent(jScrollPaneTop);
            jScrollPaneBottom = new JScrollPane();
            jScrollPaneBottom.setBorder(null);
            jSplitPane.setBottomComponent(jScrollPaneBottom);
            bottomChanged();
        }
        return jSplitPane;
    }

    private void bottomChanged() {
        switch (mode) {
            case NONE:
                hideBottom();
                break;
            case PARAMETRIC:
                bottomParametricStats();
                break;
            case RUNTIME:
                bottomRuntime();
                break;
            default:
                throw new IllegalArgumentException("unsupported mode:" + mode.toString());
        }
    }

    private void bottomParametricStats() {
        if (referenceStep == null) {
            hideBottom();
            return;
        }
        synchronized (lock) {
            if (size() > 0) {
                statsPanel = new StatsPanel(this);
                jSplitPane.setBottomComponent(statsPanel);
                updateTableView(size());
            }
        }
    }

    private void bottomRuntime() {
        if (referenceStep == null) {
            hideBottom();
            return;
        }
        synchronized (lock) {
            if (size() > 0) {
                Component c = new StatsPanel(this);
                jSplitPane.setBottomComponent(c);
                updateTableView(size());
            }
        }
    }

    private void hideBottom() {
        jSplitPane.setBottomComponent(null);
        jSplitPane.setDividerSize(0);
    }

    public Iterator<TestStepInstance> getShorted(Iterator<TestStepInstance> it) {
        if (it == null) {
            return null;
        }
        TreeMap<Integer, TestStepInstance> map = new TreeMap<Integer, TestStepInstance>();
        synchronized (lock) {
            while (it.hasNext()) {
                TestStepInstance step = it.next();
                int modelIndex = indexOf(step);
                if (modelIndex >= 0) {
                    int row = jTable.convertRowIndexToView(modelIndex);
                    if (row >= 0) {
                        map.put(row, step);
                    }
                }
            }
        }
        return map.values().iterator();
    }
    private static Object bgc = UIManager.get("Panel.background");

    public JXTable getJTable() {
        if (jTable == null) {
            jTable = new JXTable(new TestStepInstancesModel(this));
            jTable.setName("Steps");
            jTable.getTableHeader().setReorderingAllowed(false);
            ((ColumnHeaderRenderer) jTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            jTable.setDefaultRenderer(Long.class, new TestStepInstancesRendererLong());
            jTable.setDefaultRenderer(Double.class, new TestStepInstancesRendererDouble(this));
//            jTable.addHighlighter(new TestStepInstancesHighlighter(jTable));
            jTable.addHighlighter(new Highlighter() {

                @Override
                public Component highlight(Component c, ComponentAdapter ca) {
                    if (JLabel.class.isAssignableFrom(c.getClass())) {
                        ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                        if (ca.column == StepsColumn.STATUS.ordinal() || ca.column == StepsColumn.STEPSTATUS.ordinal()) {
                            c.setForeground(MainFrame.getColor(((JLabel) c).getText()));
                        } else if (jTable != null && ca.column == StepsColumn.VALUE.ordinal()) {
//                Object step = jTable.getValueAt(ca.row, StepsColumn.NAME.ordinal());
                            synchronized (lock) {
                                int ix = jTable.convertRowIndexToModel(ca.row);
                                if (ix >= 0) {
                                    if (ix < size()) {
                                        Object step = get(ix);
                                        if (step != null && TestStepInstance.class.isAssignableFrom(step.getClass())) {
                                            if (!((TestStepInstance) step).isValuePassed(((TestStepInstance) step).getValue())) {
                                                c.setForeground(Color.RED);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (ca.column == StepsColumn.ROW.ordinal() && bgc != null && Color.class.isAssignableFrom(bgc.getClass())) {
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
            });
//            jTable.setShowGrid(false);
//            jTable.setGridColor(Color.lightGray);
            jTable.setBackground(Color.white);
//            jTable.setColumnMargin(0);
//            jTable.getSelectionModel().addListSelectionListener(this);
//            addMenu(jTableSequences);
//            jTable.addMouseListener(this);
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//            jTable.getSortedColumn();
//            jTable.addPropertyChangeListener(this);
            jTable.getFilters().addPipelineListener(new PipelineListener() {

                public void contentsChanged(PipelineEvent e) {
//                    System.out.println("PipelineEvent:" + e + " " + e.getType());
                    if (jTable.getColumn(TestStepInstancesModel.StepsColumn.ROW.ordinal()).equals(jTable.getSortedColumn())) {
                        jTable.resetSortOrder();
                    } else {
                        Util.scrollSelectedRowToVisible(jTable);
                    }
                    if (StatsPanel.ChartMode.LIST.equals(statsPanel.getChartMode())) {
                        if (statsPanel != null) {
                            statsPanel.showChart();
                        }
                    }
                }
            });
//            System.out.println("getJTable setVisibleRowCount...");
            Util.setVisibleRowCount(jTable, Math.min(jTable.getRowCount(), 3), jSplitPane);
            Util.packColumnsWidthFixedFirst(jTable, 9);
            addMenu(jTable);
        }
        return jTable;
    }

    private void addMenu(final JXTable jTable) {
        jTable.addMouseListener(new MouseAdapter() {

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && jTable.isEnabled()) {
                    Point p = new Point(e.getX(), e.getY());
                    int col = jTable.columnAtPoint(p);
                    int row = jTable.rowAtPoint(p);
                    /* Translate table index to model index */
                    int mcol = jTable.getColumn(jTable.getColumnName(col)).getModelIndex();
//                    if (row >= 0 && row < jTable.getRowCount()) {
                    cancelCellEditing(jTable);
                    /* create popup menu... */
                    JPopupMenu contextMenu = createContextMenu(jTable, row, mcol);
                    /* ... and show it */
                    if (contextMenu != null && contextMenu.getComponentCount() > 0) {
                        contextMenu.show(jTable, p.x, p.y);
                    }
//                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }
        });
    }

    private void cancelCellEditing(JTable jTable) {
        CellEditor ce = jTable.getCellEditor();
        if (ce != null) {
            ce.cancelCellEditing();
        }
    }

    private JPopupMenu createContextMenu(JXTable jTable, int rowIndex, int columnIndex) {
        List<TestStepInstance> seqList = new ArrayList<TestStepInstance>();
        TestStepInstance clickedSequence = null;
        synchronized (lock) {
            if (jTable != null) {
                int[] rows = jTable.getSelectedRows();
                if (Util.isElement(rows, rowIndex)) {

                    for (int i = 0; i < rows.length; i++) {
                        int row = rows[i];
                        int j = jTable.convertRowIndexToModel(row);
                        if (j >= 0) {
                            TestStepInstance seq = get(j);
                            seqList.add(seq);
                            if (row == rowIndex) {
                                clickedSequence = seq;
                            }
                        }
                    }
                }
            }
        }
        return createContextMenu(jTable, rowIndex, columnIndex, seqList, clickedSequence);
    }

    private JPopupMenu createContextMenu(final JXTable jTable, final int rowIndex, final int columnIndex, final List<TestStepInstance> seqList, final TestStepInstance clickedSequence) {
        JPopupMenu contextMenu = new JPopupMenu();
        if (mainFrame != null) {
            JMenuItem qMenu = contextMenu.add("Query Steps from Database");

            qMenu.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    final QueryDialogTestStep q = mainFrame.getQueryDialog(QueryDialogTestStep.Mode.STEP);
                    ActionListener queryAction = new ActionListener() {

                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            System.out.println(q.toString());
                            if (addAll(q.toString(), q.getMaxResults())) {
                                updateTableView(size());
                            }
                        }
                    };
                    q.selectStep(referenceStep);
                    q.setQueryAction(queryAction);
                    q.setVisible(true);
                }
            });
        }
        if (rowIndex >= 0 && rowIndex < jTable.getRowCount() && columnIndex >= 0 && columnIndex < jTable.getColumnCount()) {
            if (seqList.size() > 0) {
                JMenu selectedMenu = new JMenu((seqList.size() > 1) ? "Selected " + Integer.toString(seqList.size()) + " sequences" : "Selected sequence");
                JMenuItem copyMenu = selectedMenu.add("Copy");
                copyMenu.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("rowIndex:" + rowIndex + " columnIndex:" + columnIndex);
                        Object value = jTable.getValueAt(rowIndex, columnIndex);
                        setClipboardContents(value == null ? "" : value.toString());
                    }
                });
                JMenuItem removeMenu = selectedMenu.add("Remove from this list");
                removeMenu.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeAll(
                                seqList);
                    }
                });
                contextMenu.add(selectedMenu);
            }
        }
        return contextMenu;
    }

    private static void setClipboardContents(String s) {
        StringSelection selection = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                selection, selection);
    }

    public static void resort(JXTable jTable) {
        TableColumn sortedColumn = jTable.getSortedColumn();
        if (sortedColumn != null) {
            Object sortedColumnIdentifier = sortedColumn.getIdentifier();
            if (sortedColumnIdentifier != null) {
                SortOrder so = jTable.getSortOrder(sortedColumnIdentifier);
                if (so != null) {
                    jTable.resetSortOrder();
                    System.out.println("Setting sort order to: " + so + " sorted:" + so.isSorted() + " ascending:" + so.isAscending());
                    jTable.setSortOrder(sortedColumnIdentifier, so);
                }
            }
        }
    }

    private void updateTableView(int size) {
        if (frame != null) {
            frame.requestFocus();
        }
        if (jTable != null) {
            jTable.setToolTipText(Integer.toString(size) + " step" + ((size > 1) ? "s" : ""));
            jTable.revalidate();
            resort(jTable);
            if (jSplitPane != null) {
                int rc = jTable.getRowCount();
                if (rc == 0) {
                    hideBottom();
                } else {
                    if (rc <= 3 && rc != jTable.getVisibleRowCount()) {
                        Util.setVisibleRowCount(jTable, rc, jSplitPane);
                    }
                    if (jSplitPane.getBottomComponent() == null) {
                        statsPanel = new StatsPanel(this);
                        jSplitPane.setBottomComponent(statsPanel);
                        int dividerSize = UIManager.getInt("SplitPane.dividerSize");
                        jSplitPane.setDividerSize(dividerSize);
                    }
                    if (statsPanel != null) {
                        statsPanel.selectionChanged();
                    }
                }
            }
//            jTable.revalidate();
        }
    }

    public TestStepInstance getTestStepInstance(int row) {
        if (jTable != null) {
            synchronized (lock) {
                int i = jTable.convertRowIndexToModel(row);
                if (i >= 0 && i < size()) {
                    return get(i);
                }
            }
        }
        return null;
    }

//    public List<TestStepInstance> getSteps() {
//        synchronized (stepsLock) {
//            return steps;
//        }
//    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        System.out.println("Property '" + evt.getPropertyName() + "' changed to " + evt.getNewValue());
        if (jSplitPane != null && evt.getSource().equals(jSplitPane)) {
            if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY) && !evt.getNewValue().equals((Integer) (-1))) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Util.dividerChanged(jTable, jSplitPane);
                    }
                });
            }
        } else {
            System.out.println("source class:" + evt.getSource().getClass().getCanonicalName());
        }
    }
}
