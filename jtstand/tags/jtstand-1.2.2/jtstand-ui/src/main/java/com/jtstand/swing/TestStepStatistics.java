/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepStatistics.java is part of JTStand.
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
import com.jtstand.TestStation;
import com.jtstand.TestStepInstance;
import com.jtstand.session.TestStepInstanceList;
import com.jtstand.statistics.Stats;
import com.jtstand.statistics.Yields;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;

/**
 *
 * @author albert_kurucz
 */
public class TestStepStatistics extends ArrayList<TestSequenceInstance> {

    public static final long serialVersionUID = 20081114L;
    static final Logger logger = Logger.getLogger(TestStepStatistics.class.getCanonicalName());
    private JXTable jTable;
    private JSplitPane jSplitPane;
    private JFrame frame;
    private JScrollPane jScrollPaneTop;
    private JScrollPane jScrollPaneBottom;
    private Stats stats = new Stats();
    private Yields yields = new Yields();
    private Object statsLock = new Object();
    private TestSequenceInstance baseSequence;
    private final Object lock = new Object();
    private transient EntityManager em;

    private EntityManager getEntityManager() {
        if (em == null) {
            em = getTestStation().getEntityManagerFactory().createEntityManager();
        }
        return em;
    }

    public boolean isContained(TestSequenceInstance seq) {
        return getEntityManager().contains(seq);
    }

    public DecimalFormat getDecimalFormat(int row) {
        if (jTable != null && row >= 0 && row < jTable.getRowCount()) {
            int i = jTable.convertRowIndexToModel(row);
            if (i >= 0 && i < stats.size()) {
                String name = stats.keySet().toArray()[row].toString();
                if (name != null) {
                    TestStepInstance step = baseSequence.getChild(Util.getPathList(name));
                    if (step != null) {
                        String decFormatStr = step.getPropertyString(TestStepInstance.STR_DECIMAL_FORMAT, null);
                        if (decFormatStr != null) {
                            return new DecimalFormat(decFormatStr);
                        }
                    }
                }
            }
        }
        return null;
    }

    private void cancelProgress() throws InterruptedException {
        if (pbar.isVisible()) {
            pbar.cancel();
            while (pbar.isVisible()) {
                Thread.sleep(10);
            }
        }
    }

    private void addMenu(final JXTable jTable) {
        jTable.addMouseListener(new MouseAdapter() {

            private void cancelCellEditing(JXTable jTable) {
                CellEditor ce = jTable.getCellEditor();
                if (ce != null) {
                    ce.cancelCellEditing();
                }
            }

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

    private JPopupMenu createContextMenu(JXTable jTable, int rowIndex, int columnIndex) {
        List<String> selectedStats = new ArrayList<String>();
        synchronized (statsLock) {
            int[] rows = jTable.getSelectedRows();
            if (Util.isElement(rows, rowIndex)) {
                for (int i = 0; i < rows.length; i++) {
                    int modelIndex = jTable.convertRowIndexToModel(rows[i]);
                    String name = stats.keySet().toArray()[modelIndex].toString();
                    selectedStats.add(name);
                }
            }
        }
        if (selectedStats.size() == 1) {
            final String path = selectedStats.get(0);
            JPopupMenu contextMenu = new JPopupMenu();

            JMenuItem reloadMenu = contextMenu.add("Chart of '" + path + "'");

            reloadMenu.addActionListener(new ActionListener() {

                public void actionPerformed(
                        ActionEvent e) {

                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            Thread t = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        List<TestStepInstance> steps = querySteps(path);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(TestStepStatistics.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            t.setPriority(Thread.MIN_PRIORITY);
                            t.start();
                        }
                    });
                }
            });
            return contextMenu;
        }
        return null;
    }

    private List<TestStepInstance> querySteps(String path) throws InterruptedException {
        TestStepInstances steps = new TestStepInstances(mainFrame, TestStepInstances.Mode.PARAMETRIC);
        List<String> pathList = Util.getPathList(path);
        synchronized (lock) {
            cancelProgress();
            pbar.reset();
            pbar.setMaximum(size());
            pbar.setMinimum(0);
            pbar.setValue(0);
            progressLabel.setText("Chart:");
            progressLabel.setVisible(true);
            pbar.setVisible(true);
            pbar.setStringPainted(true);
            bar.setVisible(true);
            for (TestSequenceInstance seq : this) {
                if (seq.getFinishTime() != null) {
                    if (isContained(seq)) {
                        steps.add(seq.getId(), path);
                    } else {
                        TestStepInstance addstep = seq.getChild(pathList);
                        steps.add(addstep);
                    }
                }
                pbar.setValue(pbar.getValue() + 1);
                if (pbar.isCancelled()) {
                    pbar.setVisible(false);
                    progressLabel.setVisible(false);
                    bar.setVisible(false);
                    return null;
                }
            }
        }
        if (steps.size() > 0) {
            steps.getFrame();
        }
        pbar.setVisible(false);
        progressLabel.setVisible(false);
        bar.setVisible(false);
        return steps;
    }
    private MainFrame mainFrame;

    public TestStation getTestStation() {
        return mainFrame.getTestStation();
    }

    public TestStepStatistics(MainFrame mainFrame, TestSequenceInstance seq) {
        this.mainFrame = mainFrame;
        this.baseSequence = seq;
    }

    public TestStepStatistics(MainFrame mainFrame, Long id) {
        this.mainFrame = mainFrame;
        long startTime = System.currentTimeMillis();
        baseSequence = find(id);
        if (baseSequence != null) {
            logger.info("TestSequenceInstance is found in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
        } else {
            throw new IllegalArgumentException("TestSequenceInstance with id=" + id + " cannot be found");
        }
    }

    @Override
    public boolean add(TestSequenceInstance seq) {
        boolean changed = false;
        synchronized (lock) {
            changed = super.add(seq);
        }
        if (changed) {
            for (TestStepInstance tsi : seq) {
                synchronized (statsLock) {
                    stats.addValue(tsi.getTestStepInstancePath(), tsi.getValueNumber());
                }
            }
        }
        return changed;
    }

    public TestSequenceInstance find(long id) {
        return getEntityManager().find(TestSequenceInstance.class, id);
    }

    public boolean add(Long id) {
        boolean changed = false;
        long startTime = System.currentTimeMillis();
        TestSequenceInstance seq = find(id);
        if (seq != null) {
            logger.info("TestSequenceInstance is found in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
            synchronized (lock) {
                changed = super.add(seq);
            }
            if (changed) {
                TestStepInstanceList sList = new TestStepInstanceList(getTestStation().createEntityManager());
                sList.addStepsOfSequence(id);
                for (TestStepInstance tsi : sList) {
                    synchronized (statsLock) {
                        stats.addValue(tsi.getTestStepInstancePath(), tsi.getValueNumber());
                    }
                }
            }
        }
        return changed;
    }

    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.getContentPane().add(getSplitPane(), BorderLayout.CENTER);
            bar = createBar();
            frame.getContentPane().add(bar, BorderLayout.SOUTH);
            frame.setTitle("Statistics");
//            Dimension dim = new Dimension(800, 600);
//            frame.setSize(dim);
//            frame.setPreferredSize(dim);
            frame.pack();
            Util.maxItWidth(frame);
        }
        frame.setVisible(true);
        return frame;
    }
    private JXStatusBar bar;
    private JXProgressBar pbar;
    private JLabel statusLabel;
    private JLabel progressLabel;

    private JXStatusBar createBar() {
        bar = new JXStatusBar();
        bar.putClientProperty(BasicStatusBarUI.AUTO_ADD_SEPARATOR, false);
        statusLabel = new JLabel("");
        bar.add(statusLabel, MainFrame.getStatusLabelConstraint());     // Fixed width of 100 with no inserts

        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        bar.add(sep, MainFrame.getSeparatorConstraint());
        progressLabel = new JLabel("");
        progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        progressLabel.setVisible(false);
        bar.add(progressLabel);
        pbar = new JXProgressBar();
        bar.add(pbar, MainFrame.getProgressBarConstraint());            // Fill with no inserts - will use remaining space

        pbar.setVisible(false);
        bar.setVisible(false);
        return bar;
    }

    private JSplitPane getSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
//            jSplitPane.setResizeWeight(0.5);
            jScrollPaneTop = new JScrollPane();
            jScrollPaneTop.setBorder(null);
            jScrollPaneTop.setViewportView(getJTable());
            jSplitPane.setTopComponent(jScrollPaneTop);
            jScrollPaneBottom = new JScrollPane();
            jScrollPaneBottom.setBorder(null);
            jSplitPane.setBottomComponent(jScrollPaneBottom);
//            resizeSequences();
            hideBottom();

        }
        return jSplitPane;
    }

    private void hideBottom() {
        jScrollPaneBottom.setVisible(false);
        jSplitPane.setDividerSize(0);
    }

    public JXTable getJTable() {
        if (jTable == null) {
            jTable = new JXTable(new TestStepStatisticsModel(this, stats, baseSequence));
            jTable.setName("Statistics");
            jTable.getTableHeader().setReorderingAllowed(false);
            //((ColumnHeaderRenderer) jTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            jTable.setDefaultRenderer(Double.class, new TestStepStatisticsRenderer(this));
            jTable.addHighlighter(new TestStepStatisticsHighlighter(jTable));
//            jTable.setShowGrid(false);
//            jTable.setGridColor(Color.lightGray);
            jTable.setBackground(Color.white);
//            jTable.setColumnMargin(0);
//            jTable.getSelectionModel().addListSelectionListener(this);
            addMenu(jTable);
//            jTable.addMouseListener(this);
            jTable.setAutoCreateRowSorter(true);

            jTable.getRowSorter().addRowSorterListener(new RowSorterListener(){

                @Override
                public void sorterChanged(RowSorterEvent e) {
                    if (jTable.getColumn(TestStepStatisticsModel.StatisticsColumn.ROW.ordinal()).equals(jTable.getSortedColumn())) {
                        jTable.resetSortOrder();
                    } else {
                        Util.scrollSelectedRowToVisible(jTable);
                    }
                }
            });
            jTable.setVisibleRowCount(jTable.getRowCount());
            Util.packColumnsWidthFixedFirst(jTable, 9);
        }
        return jTable;
    }
}
