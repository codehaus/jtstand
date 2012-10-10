/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MainFrame.java is part of JTStand.
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

//import com.jgoodies.looks.FontPolicies;
//import com.jgoodies.looks.FontPolicy;
//import com.jgoodies.looks.FontSet;
//import com.jgoodies.looks.FontSets;
//import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jtstand.Authentication;
import com.jtstand.TestSequenceInstance;
import com.jtstand.TestStation;
import com.jtstand.TestStepInstance;
import com.jtstand.TestStepScript;
import com.jtstand.query.FrameInterface;
import com.jtstand.query.ToDatabase;
import com.jtstand.swing.TestSequenceInstanceModel.SequenceColumn;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jboss.logging.Logger;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.plaf.basic.BasicStatusBarUI;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 *
 *
 */
public class MainFrame extends AbstractTestSequenceInstanceListTableModel implements TableModel, TableModelListener, TreeSelectionListener, List<TestSequenceInstance>, Set<TestSequenceInstance>, Serializable, ListSelectionListener, PropertyChangeListener, MouseListener, FrameInterface, TreeExpansionListener {

    private static final Logger log = Logger.getLogger(MainFrame.class.getName());
//    public static final long serialVersionUID = 20081114L;
    static final String STARTER_DIALOG_CLASS_NAME = "STARTER_DIALOG_CLASS_NAME";
//    public static final int WIDTH0 = -1;
    private JXTable jTable = null;
    private JSplitPane jSplitPane = null;
    private JScrollPane jScrollPaneTop = null;
    private JScrollPane jScrollPaneSequence = null;
    private TestSequenceInstance selectedSequence = null;
    private TestStepInstance selectedStep = null;
    private JXTreeTable jXTreeTable = null;
    private Ticker ticker;
    private QueryDialogTestStep queryDialog = null;
    private TestStation testStation;
    private ToDatabase toDatabase;
    private Dialog starterDialog = null;
    private JXStatusBar bar;
    private JXProgressBar pbar;
    private JLabel statusLabel;
    private JLabel operatorLabel;
    private JLabel progressLabel;
    private JLabel freeMemoryLabel;
    private JLabel freeDiskLabel;
    private Fixtures fixtures;
    private JDialog login;
//    private File saveDirectory;
    private static Runtime rt;
    private JSplitPane jSplitPaneSequenceStep;
    private RSyntaxTextArea textArea;
    private RTextScrollPane jScrollPaneStep;
    private String title;
    private String version;

    @Override
    public void tableChanged(TableModelEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                resizeSequences();
            }
        });
    }

    private void resizeSequences() {
        synchronized (lock) {
            int rc = jTable.getRowCount();
            int vrc = jTable.getVisibleRowCount();
//            System.out.println("Sequences Model row count: " + jTable.getModel().getRowCount() + "   Table row count: " + rc + "   Visible row count: " + vrc);
            if (vrc > rc || vrc < 3 && vrc != rc) {
                Util.setVisibleRowCount(jTable, Math.min(rc, 3), jSplitPane);
//                Util.setDividerLocation(jSplitPane, jTable);
                Util.packColumnsWidthFixedFirst(jTable, 9);
            }
            Util.packColumn(jTable, 0, 9, true);
        }
    }

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        Util.packColumnsWidthFixedFirst(jXTreeTable, 9);
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
//        Util.packColumnsWidthFixedFirst(jXTreeTable, 9);
    }

    public static enum SequencesColumn {

        ROW("", Integer.class),
        LOCATION("Location", String.class),
        OPERATOR("Operator", String.class),
        PN("Product Type", String.class),
        TESTTYPE("Test Type", String.class),
        SN("Serial Number", String.class),
        STARTTIME("Sequence Started", Long.class),
        ELAPSED("Elapsed", Long.class),
        STATUS("State", String.class),
        FAILEDATSTEP("Failed at Step", String.class),
        ERROR("Error Code", String.class);
        public final String columnName;
        public final Class columnClass;

        SequencesColumn(String columnName, Class columnClass) {
            this.columnName = columnName;
            this.columnClass = columnClass;
        }
    }
    public static SequencesColumn[] sequencesColumns = SequencesColumn.values();
    public static final int SEQUENCES_COLUMN_COUNT = sequencesColumns.length;

    @Override
    public int getColumnCount() {
        return SEQUENCES_COLUMN_COUNT;
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
    public Object getValueAt(int row, int column) {
        if (row >= size()) {
            return null;
        }
        final TestSequenceInstance seq = get(row);
        if (seq == null) {
            return null;
        }
        return getValueAt(seq, row, sequencesColumns[column]);
    }

    private Object getValueAt(TestSequenceInstance seq, int row, SequencesColumn column) {
        switch (column) {
            case ROW:
                return 1 + getJTable().convertRowIndexToView(row);
            case SN:
                return seq.getSerialNumber();
            case PN:
                return seq.getProductName();
            case TESTTYPE:
                return seq.getTestTypeName();
            case LOCATION:
                if (seq.getTestStation() == null) {
                    return "";
                }
                if (seq.getTestFixture() == null) {
                    return seq.getTestStation().getHostName();
                } else {
                    return seq.getTestStation().getHostName() + "@" + seq.getTestFixture().getFixtureName();
                }
            case OPERATOR:
                return seq.getEmployeeNumber() == null ? "" : seq.getEmployeeNumber();
            case STARTTIME:
                return Long.valueOf(seq.getCreateTime());
            case ELAPSED:
                return seq.getElapsed();
            case STATUS:
                return seq.getStatusString();
            case FAILEDATSTEP:
                return seq.getFailureStep() == null ? null : seq.getFailureStep().getTestStepNamePath().getStepPath();
            case ERROR:
                return seq.getFailureCode();
            default:
                return null;
        }
    }

    public static long getAvailableMemory() {
        if (rt == null) {
            rt = Runtime.getRuntime();
        }
        return rt.freeMemory() + rt.maxMemory() - rt.totalMemory();
    }

    public static boolean isMemoryEnough() {
        return getAvailableMemory() > 55000000L;
    }

//    public TestProject getTestProject() {
//        return testStation.getTestProject();
//    }
    @Override
    public TestStation getTestStation() {
        return testStation;
    }

    @Override
    public boolean isMemoryEnoughRetry() {
        for (int i = 0; i < 1000; i++) {
            if (isMemoryEnough()) {
                return true;
            }
            if (removeOldest() && isMemoryEnough()) {
                return true;
            }
            log.warn("Available memory is low. Sleeping before starting a new sequence...");
            //System.gc();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                return false;
            }
        }
        return false;
    }

    public void showFreeMemory(long free) {
        freeMemoryLabel.setText("M: " + Util.getBytes(free) + " ");
    }

    public void showFreeDisk(long free) {
        freeDiskLabel.setText("D: " + Util.getBytes(free) + " ");
    }
    public static final Class[] STARTER_DIALOG_CONSTRUCTOR = {Fixture.class};

    public void showStarterDialog(Fixture fixture) {
        if (starterDialog != null) {
            starterDialog.dispose();
        }
        if (fixture.getTestFixture().getTestStation().getTestProject().getAuthentication() != null
                && fixture.getTestFixture().getTestStation().getTestProject().getAuthentication().getOperator() == null) {
            login = new Login(frame, true, fixture.getTestFixture().getTestStation().getTestProject().getAuthentication());
            if (fixture.getTestFixture().getTestStation().getTestProject().getAuthentication().getOperator() == null) {
                return;
            }
        }
        String starterDialogClassName = fixture.getTestFixture().getPropertyString(STARTER_DIALOG_CLASS_NAME, StarterCommonDialog.class.getName());
        try {
            Class<?> starterDialogClass = Class.forName(starterDialogClassName);
            Constructor<?> starterDialogContructor = starterDialogClass.getConstructor(STARTER_DIALOG_CONSTRUCTOR);
            starterDialog = (Dialog) starterDialogContructor.newInstance(fixture);
        } catch (Exception ex) {
            log.error("Exception", ex);
        }
//        starterDialog = new StarterCommonDialog(frame, false, fixture.getTestFixture().getTestStation().getTestProject().getAuthentication() == null ? null : fixture.getTestFixture().getTestStation().getTestProject().getAuthentication().getOperator(), fixture.getTestFixture(), fixture.getTestFixture().getTestStation(), fixture.getTestFixture().getTestStation().getTestProject(), this, fixture);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                starterDialog = null;
            }
        });
    }

    public static void setLookAndFeel() {
        try {
//            String crosslaf = UIManager.getCrossPlatformLookAndFeelClassName();
//            System.out.println("Cross Platfowm Look & Feel:" + crosslaf);

//            String systemlaf = UIManager.getSystemLookAndFeelClassName();
//            System.out.println("System Look & Feel:" + systemlaf);

//            String nimbus = null;
//            String windows = null;
            LookAndFeelInfo[] lafis = UIManager.getInstalledLookAndFeels();
//            System.out.println("Installed Look & Feels:");
            for (int i = 0; i < lafis.length; i++) {
                LookAndFeelInfo lafi = lafis[i];
                String name = lafi.getName();
//                System.out.println("[" + i + "]=" + name);
//                if ("Nimbus".equals(name)) {
//                    nimbus = lafi.getClassName();
//                } else if ("Windows".equals(name)) {
//                    windows = lafi.getClassName();
//                }
            }

//            if (windows != null) {
//                UIManager.setLookAndFeel(windows);
//                return;
//            }
//            UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);

//            FontSet fontSet = FontSets.createDefaultFontSet(
//                    //                    new Font("Tahoma", Font.PLAIN, 11), // control font
//                    //                    new Font("Tahoma", Font.PLAIN, 11), // menu font
//                    //                    new Font("Tahoma", Font.PLAIN, 11) // title font
//                    //                    new Font("Sans", Font.PLAIN, 11), // control font
//                    //                    new Font("Sans", Font.PLAIN, 11), // menu font
//                    //                    new Font("Sans", Font.PLAIN, 11) // title font
//                    new Font("Verdana", Font.PLAIN, 12), // control font
//                    new Font("Verdana", Font.PLAIN, 12), // menu font
//                    new Font("Verdana", Font.PLAIN, 12) // title font
//                    );
//            FontPolicy fixedPolicy = FontPolicies.createFixedPolicy(fontSet);
//            PlasticXPLookAndFeel.setFontPolicy(fixedPolicy);
//            UIManager.setLookAndFeel(new PlasticXPLookAndFeel());

//            System.out.println("getControlTextFont:" + PlasticXPLookAndFeel.getControlTextFont());
//            System.out.println("getTitleTextFont:" + PlasticXPLookAndFeel.getTitleTextFont());
//            System.out.println("getMenuTextFont:" + PlasticXPLookAndFeel.getMenuTextFont());
//            System.out.println("getPlasticTheme:" + PlasticXPLookAndFeel.getPlasticTheme());

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel(systemlaf);

        } catch (Exception ex) {
            log.error("Exception", ex);
        }
    }

    public MainFrame(TestStation testStation, String title, String version) throws ScriptException {
        this();
//        testStation.initializeProperties();
        this.testStation = testStation;
        this.title = title;
        this.version = version;
        setLookAndFeel();
        if (!testStation.databaseValidate()) {
            switch (getTestStation().getDriver()) {
                case h2:
                case derby:
                    try {
                        getTestStation().databaseReset(null, null);
                    } catch (Exception ex) {
                        log.fatal("Cannot reset the database");
                        System.exit(-1);
                    }
                    break;
                default:
                    new ResetDatabase(null, true, getTestStation());
            }
        }
        getFrame();
        toDatabase = new ToDatabase(getTestStation(), this);
    }
    private JFrame frame;

    public String getTitle() {
        return (title == null)
                ? (getTestStation().getTestProject().getName()
                + " - revision "
                + getTestStation().getTestProject().getCreator().getRevision()
                //                + " on "
                //                + getTestStation().getHostName()
                + " - version "
                + ((version == null)
                ? getClass().getPackage().getImplementationVersion()
                : version))
                : title;
    }

    public JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame();
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        addWindowListener(new java.awt.event.WindowAdapter() {
//
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent evt) {
//                formWindowClosing(evt);
//            }
//        });
            log.trace("Loading icon...");
            URL iconURL = getClass().getResource("/images/jtbean.png");// Thread.currentThread().getContextClassLoader().getResource("/images/jtbean.png");
            log.trace("Icon URL: " + iconURL);
            if (iconURL != null) {
                ImageIcon image = new ImageIcon(iconURL);
                frame.setIconImage(image.getImage());
            }
            frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    formWindowClosing(evt);
                }
            });
            frame.setTitle(getTitle());
            if (testStation.getFixtures().size() > 0) {
                fixtures = new Fixtures(testStation, this);
                frame.getContentPane().add(fixtures, BorderLayout.NORTH);
            }
            frame.getContentPane().add(getSplitPane(), BorderLayout.CENTER);
            bar = createBar();
            frame.getContentPane().add(bar, BorderLayout.SOUTH);
            Util.maxIt(frame);
            Util.setDividerLocation(jSplitPane, jTable);
            jSplitPane.addPropertyChangeListener(this);

            fixtures.init();
        }
        frame.pack();
        frame.setVisible(true);
//        if (SystemTray.isSupported()) {
//            final SystemTray tray = SystemTray.getSystemTray();
//        }
        return frame;
    }

    public static JXStatusBar.Constraint getProgressBarConstraint() {
        JXStatusBar.Constraint c = new JXStatusBar.Constraint();
        c.setFixedWidth(200);
        return c;
    }

    public static JXStatusBar.Constraint getStatusLabelConstraint() {
        return new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);
    }

    public static JXStatusBar.Constraint getSeparatorConstraint() {
        JXStatusBar.Constraint c = new JXStatusBar.Constraint();
        c.setFixedWidth(6);
        return c;
    }

    private JXStatusBar createBar() {
        bar = new JXStatusBar();
        bar.putClientProperty(BasicStatusBarUI.AUTO_ADD_SEPARATOR, false);

//        if (getTestProject().getAuthentication() != null) {
//            JButton login = new JButton("Login");
//            bar.add(login);
//        }

        operatorLabel = new JLabel("Operator is not logged in. ");
        if (getTestStation().getTestProject().getAuthentication() != null) {
            operatorLabel.setToolTipText("Right click for log in");
            addLoginMenu(operatorLabel);
            bar.add(operatorLabel);
            bar.add(new JSeparator(JSeparator.VERTICAL), getSeparatorConstraint());
            getTestStation().getTestProject().getAuthentication().addPropertyChangeListener(this);
        }
        statusLabel = new JLabel("");
        bar.add(statusLabel, getStatusLabelConstraint());     // Fixed width of 100 with no inserts


        bar.add(new JSeparator(JSeparator.VERTICAL), getSeparatorConstraint());
        progressLabel = new JLabel("");
        progressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        progressLabel.setVisible(false);
        bar.add(progressLabel);
        pbar = new JXProgressBar();
//        pbar.setBorder(new EtchedBorder());
        bar.add(pbar, getProgressBarConstraint());            // Fill with no inserts - will use remaining space
        pbar.setBorder(null);
        pbar.setVisible(false);
//        ImageIcon icon = new ImageIcon(this.getClass().getResource("resources/buton.png"));
//        JLabel progressCancelButton = new JLabel(icon);
//        bar.add(progressCancelButton);

        bar.add(new JSeparator(JSeparator.VERTICAL), getSeparatorConstraint());
        freeDiskLabel = new JLabel("D: x.xxx GB ");
        try {
            freeDiskLabel.setToolTipText("Available Disk Space on '" + getTestStation().getSaveDirectory().getPath() + "'");
        } catch (ScriptException ex) {
            log.error("Exception", ex);
        }
        freeDiskLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bar.add(freeDiskLabel);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        showFreeDisk(getTestStation().getSaveDirectory().getUsableSpace());
                    } catch (ScriptException ex) {
                        log.error("Exception", ex);
                    }
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ex) {
                        log.warn("Exception", ex);
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();

        bar.add(new JSeparator(JSeparator.VERTICAL), getSeparatorConstraint());
        freeMemoryLabel = new JLabel("M: xxxx MB ");
        freeMemoryLabel.setToolTipText("Available Memory");
        freeMemoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        bar.add(freeMemoryLabel);
        Thread tFreeMemory = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    showFreeMemory(getAvailableMemory());
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ex) {
                        log.warn("Exception", ex);
                    }
                }
            }
        });
        tFreeMemory.setDaemon(true);
        tFreeMemory.start();
        return bar;
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        frame.setTitle("Closing...");
        frame.setEnabled(false);
        try {
            log.info("cancelProgress...");
            cancelProgress();
        } catch (InterruptedException ex) {
            log.warn("Exception", ex);
        }
        if (toDatabase != null) {
            try {
                log.trace("toDatabase.abort()...");
                toDatabase.abort();
            } catch (InterruptedException ex) {
                log.warn("Exception", ex);
            }
        }
    }

    public MainFrame() {
        super();
        ticker = new Ticker();
    }

    private void cancelProgress() throws InterruptedException {
        if (pbar.isVisible()) {
            pbar.cancel();
            while (pbar.isVisible()) {
                Thread.sleep(10);
            }
        }
    }

    private List<TestStepInstance> querySteps(String path, TestStepInstances.Mode mode) throws InterruptedException {
        TestStepInstances steps = new TestStepInstances(this, mode);
        List<String> pathList = Util.getPathList(path);
        List<TestSequenceInstance> seqList = new ArrayList<TestSequenceInstance>();
        synchronized (lock) {
            seqList.addAll(this);
        }
        if (seqList.size() == 0) {
            return steps;
        }
        cancelProgress();
        pbar.reset();
        pbar.setMaximum(seqList.size());
        pbar.setMinimum(0);
        pbar.setValue(0);
        progressLabel.setText("Chart:");
        progressLabel.setVisible(true);
        pbar.setVisible(true);
        pbar.setStringPainted(true);
        for (TestSequenceInstance seq : seqList) {
            if (seq.getFinishTime() != null) {
                if (isContained(seq)) {
                    steps.add(seq.getId(), path);
                } else {
                    TestStepInstance addstep = seq.getChild(pathList);
                    if (addstep != null) {
                        steps.add(addstep);
                    }
                }
            }
            pbar.setValue(pbar.getValue() + 1);
            if (pbar.isCancelled()) {
                pbar.setVisible(false);
                progressLabel.setVisible(false);
                return null;
            }
        }
        if (steps.size() > 0) {
            steps.getFrame();
        }
        pbar.setVisible(false);
        progressLabel.setVisible(false);
        return steps;
    }

    public boolean removeOldest() {
        synchronized (lock) {
            List<TestSequenceInstance> seqList = new ArrayList<TestSequenceInstance>();
            long oldest = 0L;
            for (TestSequenceInstance tsi : this) {
                if (seqList.size() == 0) {
                    seqList.add(tsi);
                    oldest = tsi.getCreateTime();
                } else if (tsi.getCreateTime() < oldest) {
                    seqList.set(0, tsi);
                    oldest = tsi.getCreateTime();
                }
            }
            boolean changed = super.removeAll(seqList);
            if (changed) {
                fixtures.removeAll(seqList);
                if (seqList.contains(selectedSequence)) {
                    hideSequence();
                }
                updateTableView(size());
                System.gc();
            }
            return changed;
        }
    }

    public boolean remove(long createTime, String host) {
        synchronized (lock) {
            List<TestSequenceInstance> seqList = new ArrayList<TestSequenceInstance>();
            for (TestSequenceInstance tsi : this) {
                if (tsi.getCreateTime() == createTime && tsi.getTestStation().getHostName().equals(host)) {
                    seqList.add(tsi);
                }
            }
            boolean changed = super.removeAll(seqList);
            if (changed) {
                fixtures.removeAll(seqList);
                if (seqList.contains(selectedSequence)) {
                    hideSequence();
                }
                updateTableView(size());
                System.gc();
            }
            return changed;
        }
    }

    @Override
    public TestSequenceInstance replace(long createTime, String host) {
//        System.out.println("Replacing: " + createTime + " " + host);
        if (!isMemoryEnough()) {
            log.warn("Not enough memory to replace.");
            return null;
        }
        if (getSequence(createTime, host) == null) {
//            System.out.println("There is no candidate to replace.");
            return null;
        }
        TestSequenceInstance seq = super.replace(createTime, host);
        fixtures.replace(seq);
        System.gc();
        return seq;
    }

//    @Override
//    public boolean add(
//            final TestSequenceInstance seq) {
//
//
//        return checkChangable(
//                new Changable() {
//
//                    boolean isChanged() {
//                        return MainFrame.super.add(seq);
//                    }
//                });
//    }
//
//    @Override
//    public boolean addAll(
//            final Collection<? extends TestSequenceInstance> seqList) {
//        return checkChangable(new Changable() {
//
//            boolean isChanged() {
//                return MainFrame.super.addAll(seqList);
//            }
//        });
//    }
    @Override
    public boolean removeAll(Collection<?> seqList) {
        synchronized (lock) {
            if (jTable != null) {
                jTable.clearSelection();
            }
            boolean changed = super.removeAll(seqList);
            if (changed) {
                fixtures.removeAll(seqList);
                if (seqList.contains(selectedSequence)) {
                    hideSequence();
                }
                updateTableView(size());
                System.gc();
            }
            return changed;
        }
    }

    public TestSequenceInstance getSequence(int rowIndex) {
        synchronized (lock) {
            if (jTable != null) {
                int i = jTable.convertRowIndexToModel(rowIndex);
                if (i >= 0) {
                    return get(i);
                }
            }
        }
        return null;
    }

    public TestSequenceInstance getSequence(long createTime, String host) {
        synchronized (lock) {
            for (TestSequenceInstance seq : this) {
//                System.out.println("Sequence: " + seq.getCreateTime());
                if (seq.getCreateTime() == createTime && seq.getTestStation() != null && seq.getTestStation().getHostName().equals(host)) {
                    return seq;
                }
            }
        }
        return null;
    }

//    public TestSequence getTestSequence(FileRevision rev) {
//	synchronized (lock) {
//	    for (TestSequenceInstance seq : sequences) {
//		if (seq.getTestSequence().getId() != null && seq.getTestSequence().getCreator().equals(rev)) {
////                    Log.log("Sequence is found in the list!");
//		    return seq.getTestSequence();
//		}
//	    }
//	}
//	return null;
//    }
    public List<TestSequenceInstance> getSelectedSequences() {
        synchronized (lock) {
            List<TestSequenceInstance> seqList = new ArrayList<TestSequenceInstance>();
            if (jTable != null) {
                int[] rows = jTable.getSelectedRows();
                if (rows.length > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        int row = rows[i];
                        int j = jTable.convertRowIndexToModel(row);
                        if (j >= 0) {
                            seqList.add(get(j));
                        }
                    }
                }
            }
            return seqList;
        }
    }

    public void scrollSelectedSequenceToVisible() {
        synchronized (lock) {
            if (jTable != null) {
                int[] rows = jTable.getSelectedRows();
                if (rows.length == 1) {
                    jTable.scrollRowToVisible(rows[0]);
                }
            }
        }
    }

    public List<TestSequenceInstance> getSelectedSequencesConditionally(int rowIndex) {
        synchronized (lock) {
            List<TestSequenceInstance> seqList = new ArrayList<TestSequenceInstance>();
            if (jTable != null) {
                int[] rows = jTable.getSelectedRows();
                if (Util.isElement(rows, rowIndex)) {
                    for (int i = 0; i < rows.length; i++) {
                        int row = rows[i];
                        int j = jTable.convertRowIndexToModel(row);
                        if (j >= 0) {
                            seqList.add(get(j));
                        }
                    }
                }
            }
            return seqList;
        }
    }

    private void updateTableView(int size) {
        if (jTable != null) {
            if (jSplitPane != null) {
                resizeSequences();
            }
            jTable.revalidate();
        }
    }

//    public static void displayJFrame(final TestSequenceInstance tsi) {
//        if (tsi != null) {
//            JFrame jFrame = new JFrame();
//            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//            jFrame.setMinimumSize(new Dimension(640, 300));
//            jFrame.setPreferredSize(Util.getMaximumWindowDimension());
//            jFrame.setLayout(new BorderLayout());
//            jFrame.getContentPane().add((new TestSequenceInstancesModel(tsi)).getSplitPane(), BorderLayout.CENTER);
//            jFrame.pack();
//            jFrame.setTitle(tsi.toString());
//            jFrame.setVisible(true);
//            jFrame.requestFocus();
//        }
//    }
//    public static void displayJFrame(final List<TestSequenceInstance> sequences) {
//        if (sequences != null) {
//            JFrame jFrame = new JFrame();
//            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//            //jFrame.setMinimumSize(new Dimension(640, 300));
//            jFrame.setPreferredSize(Util.getMaximumWindowDimension());
//            jFrame.setLayout(new BorderLayout());
//            jFrame.getContentPane().add((new TestSequenceInstancesModel(sequences)).getSplitPane(), BorderLayout.CENTER);
//            jFrame.pack();
//            jFrame.setTitle("Test Results");
//            jFrame.setVisible(true);
//            jFrame.requestFocus();
//        }
//    }
    public JXTable getJTable() {
        if (jTable == null) {
            jTable = new JXTable(this) {
                public static final long serialVersionUID = 20081114L;

                @Override
                public String getToolTipText(MouseEvent e) {
                    return MainFrame.this.getToolTipTextSequences(e);
                }
            };
            jTable.getColumnExt(jTable.convertColumnIndexToView(SequencesColumn.FAILEDATSTEP.ordinal())).setVisible(false);
            jTable.getColumnExt(jTable.convertColumnIndexToView(SequencesColumn.ERROR.ordinal())).setVisible(false);
            jTable.getTableHeader().setReorderingAllowed(false);
            //((ColumnHeaderRenderer) jTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            jTable.setDefaultRenderer(Long.class, new TestSequenceInstancesRenderer());
            jTable.addHighlighter(new TestSequenceInstancesHighlighter());
//            jTable.setShowGrid(false);
//            jTable.setGridColor(Color.lightGray);
            jTable.setBackground(Color.white);
//            jTable.setColumnMargin(0);
            jTable.getSelectionModel().addListSelectionListener(this);
            addMenu(jTable);
            jTable.addMouseListener(this);
            Util.packColumnsWidthFixedFirst(jTable, 9);
            jTable.setName("Sequences");
            jTable.setAutoCreateRowSorter(true);
            jTable.getRowSorter().addRowSorterListener(new RowSorterListener() {
                @Override
                public void sorterChanged(RowSorterEvent e) {
                    if (jTable.getColumn(SequencesColumn.ROW.ordinal()).equals(jTable.getSortedColumn())) {
                        jTable.resetSortOrder();
                    } else {
                        Util.scrollSelectedRowToVisible(jTable);
                    }
                }
            });
            addTableModelListener(this);
        }
        return jTable;
    }

    private TestStepInstance getSelectedStep() {
        if (jXTreeTable == null) {
            return null;
        }
        int[] rows = jXTreeTable.getSelectedRows();
        if (rows.length != 1) {
            return null;
        }
        Object myobject = jXTreeTable.getValueAt(rows[0], TestSequenceInstanceModel.SequenceColumn.NAME.ordinal());
        if (!TestStepInstance.class.isAssignableFrom(myobject.getClass())) {
            return null;
        }
        return (TestStepInstance) myobject;
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.getSource().equals(jXTreeTable.getTreeSelectionModel())) {
            selectedStepChanged();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
//            System.out.println("valueChanged source:" + e.getSource().getClass());

            if (e.getSource().equals(jTable.getSelectionModel())) {
                /* Sequence/Sequences selection changed on the sequences list */
                selectedSequenceChanged();
            }

//            if (e.getSource().equals(jXTreeTable.getSelectionModel())) {
//                selectedStepChanged();
//            }

            if (jXTreeTable != null && e.getSource().equals(jXTreeTable.getSelectionModel())) {
//                System.out.println("Treetable event!!!");
                TestStepInstance sStep = getSelectedStep();
                if (sStep != null) {
                    setSelectedStep(sStep);

//                    System.out.println("Selected step:" + selectedStep.getTestStepInstancePath());
                }
            }
        }
    }

    public void setSelectedStep(TestStepInstance step) {
        selectedStep = step;
        if (selectedStep != null && queryDialog != null) {
            queryDialog.selectStep(step);
        }
    }

    public void selectedSequenceChanged() {
        if (jTable.getRowCount() > 0) {
            int firstindex = jTable.getSelectedRow();
            if (jTable.getSelectedRowCount() != 1 || firstindex < 0 || size() < 1 || firstindex >= size()) {
                hideSequence();
                return;
            } else {
                firstindex = jTable.convertRowIndexToModel(firstindex);
                selectSequence(get(firstindex));
            }
        }
    }

    public void selectedStepChanged() {
        int[] rows = jXTreeTable.getSelectedRows();
        if (rows.length == 1) {
            if (textArea != null) {
                showStep();
            }
        } else {
            hideStep();
        }
    }

    @Override
    public void selectSequence(TestSequenceInstance select) {
        if (select == null) {
            jTable.clearSelection();
            return;
        }
//        System.out.println("Selecting: " + select.getStartedString() + "@" + select.getHostName());
        synchronized (lock) {
            int pos = indexOf(select);
            if (pos >= 0) {
                final int row = jTable.convertRowIndexToView(pos);
                if (pos >= 0 && jTable.getSelectedRow() != row) {
                    jTable.setRowSelectionInterval(row, row);
                    jTable.scrollRowToVisible(row);
                }
                if (!select.equals(selectedSequence)) {
                    displaySequence(select);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            jTable.scrollRowToVisible(row);
                        }
                    });

                }
            } else {
                log.info("Cannot select sequence:" + select);
            }
        }
    }

    private void hideSequence() {
//        jScrollPaneBottom.setVisible(false);
        jSplitPane.setDividerSize(0);
        jSplitPane.setBottomComponent(null);
        if (jScrollPaneSequence != null) {
            if (jScrollPaneSequence.getComponentCount() > 0) {
                jScrollPaneSequence.removeAll();
            }
            jScrollPaneSequence = null;
        }
        jXTreeTable = null;
        //System.gc();
    }

    private void toggleStep() {
        if (textArea == null) {
            showStep();
        } else {
            hideStep();
        }
    }

    private void showStep() {
        TestStepInstance tsi = getSelectedStep();
        if (tsi != null) {
            TestStepScript s = tsi.getScript();
            if (s != null) {
                String text = s.getText();
                if (text != null && text.trim().length() > 0) {
                    textArea = new RSyntaxTextArea();
                    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
                    textArea.setText(text);
                    textArea.setEditable(false);
                    jScrollPaneStep = new RTextScrollPane(textArea);
                    jXTreeTable.setVisibleRowCount(Math.min(5, jXTreeTable.getRowCount()));
                    jSplitPaneSequenceStep.setBottomComponent(jScrollPaneStep);
                    jSplitPaneSequenceStep.setDividerSize(UIManager.getInt("SplitPane.dividerSize"));
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            int row = jXTreeTable.getSelectedRow();
                            if (row > 0) {
                                jXTreeTable.scrollRowToVisible(row);
                            }
                        }
                    });
                    return;
                }
            }
        }
        if (textArea != null) {
            textArea.setText("");
        }
    }

    private void hideStep() {
        jSplitPaneSequenceStep.setDividerSize(0);
        jSplitPaneSequenceStep.setBottomComponent(null);
        if (jScrollPaneStep != null) {
            if (jScrollPaneStep.getComponentCount() > 0) {
                jScrollPaneStep.removeAll();
            }
            jScrollPaneStep = null;
        }
        textArea = null;
    }

    public void displaySequence(TestSequenceInstance select) {
//        System.out.println("Display: " + select.getStartedString() + "@" + select.getHostName());
        log.trace("contains:" + isContained(select));
        TestSequenceInstanceModel tsim;
        if (isContained(select)) {
            tsim = new TestSequenceInstanceModel(getTestStation().getTestSequenceInstance(select.getId()));
        } else {
            tsim = new TestSequenceInstanceModel(select);
        }
        if (jXTreeTable == null) {
            jXTreeTable = new JXTreeTable(tsim) {
                public static final long serialVersionUID = 20081114L;

                @Override
                public String getToolTipText(MouseEvent e) {
                    return MainFrame.this.getToolTipTextSequence(e);
                }
            };
            jXTreeTable.getSelectionModel().addListSelectionListener(this);
            jXTreeTable.addMouseListener(this);
            jXTreeTable.setLeafIcon(null);
            jXTreeTable.addTreeExpansionListener(this);
            jXTreeTable.setRootVisible(false);
            jXTreeTable.getTableHeader().setReorderingAllowed(false);
//            ((org.jdesktop.swingx.table.ColumnHeaderRenderer) jXTreeTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
            jXTreeTable.addHighlighter(new TestSequenceInstanceHighlighter(jXTreeTable));
            jXTreeTable.getTreeSelectionModel().addTreeSelectionListener(this);

            //jXTreeTable.setDefaultRenderer(String.class, new TestSequenceInstanceRenderer());
//            jXTreeTable.setShowGrid(true);
//            jXTreeTable.setGridColor(Color.lightGray);
            addTreeTableMenu(jXTreeTable);
            jXTreeTable.setBackground(Color.white);
            jScrollPaneSequence = new JScrollPane();
            jScrollPaneSequence.setBorder(null);
            if (jSplitPaneSequenceStep == null) {
                jSplitPaneSequenceStep = new JSplitPane();
//                jSplitPaneSequenceStep.setBorder(null);
                jSplitPaneSequenceStep.setOrientation(JSplitPane.VERTICAL_SPLIT);
                jSplitPaneSequenceStep.addPropertyChangeListener(this);
            }
            jSplitPaneSequenceStep.setTopComponent(jScrollPaneSequence);
            jSplitPane.setBottomComponent(jSplitPaneSequenceStep);
            jScrollPaneSequence.setViewportView(jXTreeTable);
        } else {
            jXTreeTable.setTreeTableModel(tsim);
        }
        hideStep();
        Util.packColumnsWidthFixedFirst(jXTreeTable, 9);

//        if (selectedSequence != null) {
//            TestStepInstance selectedStep = selectedSequence.getSelectedStep();
//            if (selectedStep != null) {
//                selectStep(selectedStep);
//            }
//        }
        int dividerSize = UIManager.getInt("SplitPane.dividerSize");
        jSplitPane.setDividerSize(dividerSize);
        jScrollPaneSequence.setVisible(true);
        selectedSequence = select;
        if (selectedStep != null) {
            selectStep(selectedStep);
        }
//        jSplitPane.setDividerLocation(jScrollPaneSequenceList.getPreferredSize().height);
        Util.setVisibleRowCount(jTable, Math.min(jTable.getRowCount(), jTable.getVisibleRowCount()), jSplitPane);
//        Util.setDividerLocation(jSplitPane, jTable);
        jXTreeTable.requestFocus();
    }

    public void expandFailedOrRunning() {
        if (selectedSequence == null) {
            return;
        }
        if (selectedSequence.getTestStepInstance() != null) {
            if (selectedSequence.getTestStepInstance().isFailed()) {
                canExpandFailed(selectedSequence.getTestStepInstance());
            } else if (selectedSequence.getTestStepInstance().isRunning()) {
                canExpandRunning(selectedSequence.getTestStepInstance());
            } else if (selectedSequence.getTestStepInstance().isAborted()) {
                canExpandAborted(selectedSequence.getTestStepInstance());
            }
        }
    }

    public void canExpandFailed(TestStepInstance tsi) {
        if (jXTreeTable != null) {
            for (int row = 0; row < jXTreeTable.getRowCount(); row++) {
                if (jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal()) instanceof TestStepInstance) {
                    if (((TestStepInstance) jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal())).getTestStepInstancePath().equals(tsi.getTestStepInstancePath())) {
                        jXTreeTable.expandPath(jXTreeTable.getPathForRow(row));
//                        System.out.println("expanded:" + tsi);
                        if (!tsi.isLeaf()) {
                            for (TestStepInstance child : tsi.getSteps()) {
                                if (child.isFailed()) {
                                    canExpandFailed(child);
                                    return;
                                }
                            }
                        }
                        jXTreeTable.setRowSelectionInterval(row, row);
//                    jXTreeTable.scrollRectToVisible(jXTreeTable.getCellRect(row, 0, false));
                        Util.scrollToCenter(jXTreeTable, row, 0);
                        selectedStep = tsi;
                        return;
                    }
                }
            }
        }
        log.warn("could not expand:" + tsi);
    }

    public void canExpandAborted(TestStepInstance tsi) {
        if (jXTreeTable != null) {
            for (int row = 0; row < jXTreeTable.getRowCount(); row++) {
                if (jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal()) instanceof TestStepInstance) {
                    if (((TestStepInstance) jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal())).getTestStepInstancePath().equals(tsi.getTestStepInstancePath())) {
                        jXTreeTable.expandPath(jXTreeTable.getPathForRow(row));
//                    System.out.println("expanded:" + tsi);
                        if (!tsi.isLeaf()) {
                            for (TestStepInstance child : tsi.getSteps()) {
                                if (child.isAborted()) {
                                    canExpandAborted(child);
                                    return;
                                }
                            }
                        }
                        jXTreeTable.setRowSelectionInterval(row, row);
//                    jXTreeTable.scrollRectToVisible(jXTreeTable.getCellRect(row, 0, false));
                        Util.scrollToCenter(jXTreeTable, row, 0);
                        selectedStep = tsi;
                        return;
                    }
                }
            }
        }
        log.warn("could not expand:" + tsi);
    }

    public void canExpandRunning(TestStepInstance tsi) {
        if (jXTreeTable != null) {
            for (int row = 0; row < jXTreeTable.getRowCount(); row++) {
                if (jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal()) instanceof TestStepInstance) {
                    if (((TestStepInstance) jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal())).getTestStepInstancePath().equals(tsi.getTestStepInstancePath())) {
                        jXTreeTable.expandPath(jXTreeTable.getPathForRow(row));
//                    System.out.println("expanded:" + tsi);
                        if (!tsi.isLeaf()) {
                            List<TestStepInstance> children = tsi.getSteps();
                            ListIterator<TestStepInstance> li = children.listIterator(children.size());
                            while (li.hasPrevious()) {
                                TestStepInstance child = li.previous();
                                if (child.isRunning()) {
                                    canExpandRunning(child);
                                    return;
                                }
                            }
                        }
                        jXTreeTable.setRowSelectionInterval(row, row);
//                    jXTreeTable.scrollRectToVisible(jXTreeTable.getCellRect(row, 0, false));
                        Util.scrollToCenter(jXTreeTable, row, 0);
                        selectedStep = tsi;
                        return;
                    }
                }
            }
        }
        log.warn("could not expand:" + tsi);
    }
//    public boolean expand(TestStepInstance tsi) {
//        List<String> pathList = tsi.getPathList();
//        System.out.println("getting tree path for:");
//
//        for (String str : pathList) {
//            System.out.println(str);
//        }
//
//        TestStepInstance node = tsi.getRoot(pathList.get(0));
//        if (node == null) {
//            return false;
//        }
//        if (!canExpand(node)) {
//            return false;
//        }
//        for (int i = 1; i < pathList.size(); i++) {
//            node = node.getChild(pathList.get(i));
//            if (node != null) {
//                if (!canExpand(node)) {
//                    return false;
//                }
//            } else {
//                return false;
//            }
//        }
//        return true;
//    }

    private TestStepInstance getRoot(String name) {
        Object root = jXTreeTable.getTreeTableModel().getRoot();
        if (!jXTreeTable.getTreeTableModel().isLeaf(root)) {
            for (int i = 0; i < jXTreeTable.getTreeTableModel().getChildCount(root); i++) {
                Object node = jXTreeTable.getTreeTableModel().getChild(root, i);

                if (TestStepInstance.class.isAssignableFrom(node.getClass())) {
//                    System.out.println("checking:" + ((TestStepInstance) node).getTestStepInstancePath());
                    if (((TestStepInstance) node).getTestStepInstancePath().equals(name)) {
//                        System.out.println("getRoot has found the root step:" + ((TestStepInstance) node).getTestStepInstancePath());
                        return (TestStepInstance) node;
                    }
                }
            }
        }
        //System.out.println("getRoot did not found root with name: " + name);
        return null;
    }

    public boolean expand(List<String> pathList) {
//        System.out.println("getting tree path for: ");
//        for (String str : pathList) {
//            System.out.println(str);
//        }

        TestStepInstance node = getRoot(pathList.get(0));
        if (node == null) {
            return false;
        }
        if (!canExpand(node)) {
            return false;
        }
        for (int i = 1; i < pathList.size(); i++) {
            node = node.getChild(pathList.get(i));
            if (node != null) {
                if (!canExpand(node)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean canExpand(TestStepInstance tsi) {
        for (int row = 0; row
                < jXTreeTable.getRowCount(); row++) {
            if (jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal()) instanceof TestStepInstance) {
                if (((TestStepInstance) jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal())).getTestStepInstancePath().equals(tsi.getTestStepInstancePath())) {
                    jXTreeTable.expandPath(jXTreeTable.getPathForRow(row));
//                    System.out.println("expanded: " + tsi);
                    return true;
                }
            }
        }
        log.warn("could not expand: " + tsi);
        return false;
    }

    public void selectStep(TestStepInstance selectedStep) {
        expand(selectedStep.getPathList());

        for (int row = 0; row
                < jXTreeTable.getRowCount(); row++) {
            if (jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal()) instanceof TestStepInstance) {
                if (((TestStepInstance) jXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal())).getTestStepInstancePath().equals(selectedStep.getTestStepInstancePath())) {
                    //System.out.println(JXTreeTable.getValueAt(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal()));
                    jXTreeTable.setRowSelectionInterval(row, row);
//                    jXTreeTable.scrollRectToVisible(jXTreeTable.getCellRect(row, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal(), false));
                    Util.scrollToCenter(jXTreeTable, row, 0);
                    return;
                }
            }
        }
    }

    public JSplitPane getSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            //jSplitPane.setBorder(null);
            jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
//            jSplitPane.setResizeWeight(0.3);
            jScrollPaneTop = new JScrollPane();
            jScrollPaneTop.setBorder(null);
            jScrollPaneTop.setViewportView(getJTable());
//            jScrollPaneTop.revalidate();
//            jSplitPane.setDividerLocation(jScrollPaneTop.getPreferredSize().height + jSplitPane.getInsets().top);
            jSplitPane.setTopComponent(jScrollPaneTop);
//            jSplitPane.addPropertyChangeListener(this);
            resizeSequences();
            hideSequence();
        }
        return jSplitPane;
    }
//    public void dividerChanged() {
//        if (!jScrollPaneBottom.isVisible()) {
//            return;
//        }
//        int current = jSplitPane.getDividerLocation();
////        Insets i = jSplitPane.getInsets();
////        Insets j = jScrollPaneTop.getInsets();
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
//        scrollSelectedSequenceToVisible();
//    }
    boolean wasSequenceActive = false;

    public void tick(final boolean forceRepaint) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (jTable != null) {
                    boolean active = isSequenceActive();
                    if (forceRepaint || wasSequenceActive || active) {
                        jTable.repaint();
                        wasSequenceActive = active;
                    }
                }
                JXTreeTable tree = jXTreeTable;
                TestSequenceInstance seq = selectedSequence;
                if (tree != null && tree.isVisible() && seq != null) {
                    if (forceRepaint || seq.isSequenceActive()) {
                        jXTreeTable.repaint();
                    }
                }
            }
        });
    }

    public boolean isSequenceActive() {
        synchronized (lock) {
            for (TestSequenceInstance seq : this) {
                if (seq.isSequenceActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        System.out.println("Property '" + evt.getPropertyName() + "' changed to " + evt.getNewValue());

        if (jSplitPane != null && evt.getSource().equals(jSplitPane)) {
            if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY) && !evt.getNewValue().equals((Integer) (-1))) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        dividerChanged();
//                        System.out.println("jSplitPane DIVIDER_LOCATION_PROPERTY");
                Util.dividerChanged(jTable, jSplitPane);
//                    }
//                });
            }
        } else if (jSplitPaneSequenceStep != null && evt.getSource().equals(jSplitPaneSequenceStep)) {
            if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY) && !evt.getNewValue().equals((Integer) (-1))) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        dividerChanged();
//                        System.out.println("jSplitPaneSequenceStep DIVIDER_LOCATION_PROPERTY");
                Util.dividerChanged(jXTreeTable, jSplitPaneSequenceStep);
//                    }
//                });
            }
        } else if (getTestStation().getTestProject().getAuthentication() != null
                && evt.getPropertyName().equals(Authentication.OPERATOR_PROPERTY)) {
//            System.out.println("Property '" + evt.getPropertyName() + "' changed to " + evt.getNewValue());
            if (evt.getNewValue() == null) {
                operatorLabel.setText("Operator is logged out. ");
                operatorLabel.setToolTipText("Right click for log in");
            } else {
                operatorLabel.setText("Operator: " + evt.getNewValue() + " ");
                operatorLabel.setToolTipText("Right click for log out");
            }
        } else if (TestSequenceInstance.class.isAssignableFrom(evt.getSource().getClass())) {
            TestSequenceInstance seq = (TestSequenceInstance) evt.getSource();
            log.debug("Property change of test sequence: " + seq);
            tick(true);
            if (!seq.isSequenceActive()) {
                seq.removePropertyChangeListener(this);
            }
        } else {
            log.debug("source class: " + evt.getSource().getClass().getCanonicalName());
        }
    }

    class Ticker extends Thread {

        private boolean running = true;

        public Ticker() {
            this.setDaemon(true);
            this.start();
        }

        public void finish() {
            running = false;
        }

        @Override
        public void run() {
            try {
                while (running) {
                    tick(false);
                    sleep(1000);
                }
            } catch (InterruptedException ex) {
                //do nothing if interrupted
            }
        }
    }

    private void cancelCellEditing(JTable jTable) {
        CellEditor ce = jTable.getCellEditor();
        if (ce != null) {
            ce.cancelCellEditing();
        }
    }

    private void addLoginMenu(final JLabel jLabel) {
        jLabel.addMouseListener(new MouseAdapter() {
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && jLabel.isEnabled()) {

                    Point p = new Point(e.getX(), e.getY());
                    if (jLabel.contains(p)) {
                        /* create popup menu... */
                        JPopupMenu contextMenu = createLoginContextMenu(jLabel);
                        /* ... and show it */
                        if (contextMenu != null && contextMenu.getComponentCount() > 0) {
                            contextMenu.show(jLabel, p.x, p.y);
                        }
                    }
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

    private JPopupMenu createLoginContextMenu(JLabel jLabel) {
        if (getTestStation().getTestProject().getAuthentication() == null) {
            return null;
        }
        JPopupMenu contextMenu = new JPopupMenu();

        if (getTestStation().getTestProject().getAuthentication().getOperator() == null) {
            JMenuItem loginMenu = contextMenu.add("Log in");
            loginMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    login = new Login(frame, true, getTestStation().getTestProject().getAuthentication());
                }
            });
        } else {
            JMenuItem logoutMenu = contextMenu.add("Log out");
            logoutMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getTestStation().getTestProject().getAuthentication().setOperator(null);
                }
            });
        }
        return contextMenu;
    }

    private void addMenu(final JXTable jTable) {
        jTable.getTableHeader().addMouseListener(new MouseAdapter() {
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
                    JPopupMenu contextMenu = createContextMenuHeader(jTable, row, mcol);
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

    private JPopupMenu createContextMenu(JXTable jTable, int rowIndex, int columnIndex) {
        List<TestSequenceInstance> seqList = new ArrayList<TestSequenceInstance>();
        TestSequenceInstance clickedSequence = null;
        synchronized (lock) {
            if (jTable != null) {
                int[] rows = jTable.getSelectedRows();
                if (Util.isElement(rows, rowIndex)) {

                    for (int i = 0; i < rows.length; i++) {
                        int row = rows[i];
                        int j = jTable.convertRowIndexToModel(row);
                        if (j >= 0) {
                            TestSequenceInstance seq = get(j);
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

    public QueryDialogTestStep getQueryDialog(QueryDialogTestStep.Mode mode) {
        if (queryDialog == null) {
            queryDialog = new QueryDialogTestStep(null, false, mode, getTestStation());
        } else {
            queryDialog.setMode(mode);
        }
        Util.centerOn(queryDialog, jSplitPane);
        return queryDialog;
    }

//    abstract class Changable {
//
//        abstract boolean isChanged();
//    }
//
//    private boolean checkChangable(Changable x) {
//        synchronized (lock) {
//            int firstIndex = -1;
//            int lastIndex = -1;
//            int originalSize = size();
//            if (jTable != null) {
//                int firstRow = Util.getFirstVisibleRowIndex(jTable);
////                System.out.println("first row: " + firstRow);
//                if (firstRow >= 0 && firstRow < jTable.getRowCount()) {
//                    firstIndex = jTable.convertRowIndexToModel(firstRow);
//                }
//                int lastRow = Util.getLastVisibleRowIndex(jTable);
////                System.out.println("last row: " + lastRow);
//                if (lastRow > 0 && lastRow < jTable.getRowCount()) {
//                    lastIndex = jTable.convertRowIndexToModel(lastRow);
//                }
//            }
//            boolean changed = x.isChanged();
//            if (changed) {
//                updateTableView(size());
//                if (firstIndex >= 0) {
//                    int j = jTable.convertRowIndexToView(firstIndex);
//                    if (j >= 0) {
//                        jTable.scrollRowToVisible(j);
//                    }
//                }
//                if (lastIndex >= 0) {
//                    int j = jTable.convertRowIndexToView(lastIndex);
//                    if (j >= 0) {
//                        jTable.scrollRowToVisible(j);
//                    }
//                }
//                if (originalSize < 12) {
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            Util.packColumns(jTable, 9, MainFrame.WIDTH0);
//                        }
//                    });
//                }
//                tick(true);
//
//            }
//            return changed;
//        }
//    }
    private JPopupMenu createContextMenuHeader(final JXTable jTable, final int rowIndex, final int columnIndex) {
        if (columnIndex != SequencesColumn.ROW.ordinal()) {
            return null;
        }
        JPopupMenu contextMenu = new JPopupMenu();
        addAboutMenu(contextMenu);
        return contextMenu;
    }

    private JPopupMenu createContextMenu(final JXTable jTable, final int rowIndex, final int columnIndex, final List<TestSequenceInstance> seqList, final TestSequenceInstance clickedSequence) {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenu menu = new JMenu("Load Sequences from Database");
        JMenuItem reloadMenu = menu.add("Last 100 Sequence");
        JMenuItem qMenu = menu.add("Query Sequences");
        contextMenu.add(menu);
//        final MainFrame list = this;
        reloadMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String queryString = "select ts from TestSequenceInstance ts where ts.finishTime != null order by ts.createTime desc";
                addAll(queryString, 100);
//                MainFrame.this.checkChangable(new Changable() {
//
//                    boolean isChanged() {
//                        return MainFrame.super.addAll(queryString, 100);
//                    }
//                });
            }
        });

        qMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final QueryDialog q = getQueryDialog(QueryDialogTestStep.Mode.SEQUENCE);
                ActionListener queryAction = new ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        addAll(q.toString(), q.getMaxResults());
//                        checkChangable(new Changable() {
//
//                            boolean isChanged() {
//                                return addAll(q.toString(), q.getMaxResults());
//                            }
//                        });
                    }
                };
                q.setQueryAction(queryAction);
                q.setVisible(true);
            }
        });

        if (rowIndex >= 0 && rowIndex < jTable.getRowCount() && columnIndex >= 0 && columnIndex < jTable.getColumnCount()) {
            if (seqList.size() > 0) {
                JMenu selectedMenu = new JMenu((seqList.size() > 1) ? "Selected " + Integer.toString(seqList.size()) + " sequences" : "Selected sequence");

                JMenuItem copyMenu = selectedMenu.add("Copy");
                copyMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
//                        System.out.println("rowIndex: " + rowIndex + " columnIndex: " + columnIndex);
                        Object value = jTable.getValueAt(rowIndex, columnIndex);
                        setClipboardContents(value == null ? "" : value.toString());
                    }
                });

                if (seqList.size() > 2) {
                    JMenuItem removeMenu = selectedMenu.add("Statistics");
                    removeMenu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(
                                ActionEvent e) {

                            SwingUtilities.invokeLater(
                                    new Runnable() {
                                        @Override
                                        public void run() {


                                            Thread t = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        cancelProgress();
                                                    } catch (InterruptedException ex) {
                                                        return;
                                                    }
                                                    pbar.reset();
                                                    pbar.setMaximum(seqList.size());
                                                    pbar.setMinimum(0);
                                                    pbar.setValue(0);
                                                    progressLabel.setText("Statistics:");
                                                    progressLabel.setVisible(true);
                                                    pbar.setVisible(true);
                                                    pbar.setStringPainted(true);
                                                    TestStepStatistics tss = new TestStepStatistics(MainFrame.this, clickedSequence);
                                                    for (TestSequenceInstance tsi : seqList) {
                                                        if (MainFrame.super.isContained(tsi)) {
                                                            tss.add(tsi.getId());
                                                        } else {
                                                            tss.add(tsi);
                                                        }
                                                        pbar.setValue(pbar.getValue() + 1);
                                                        if (pbar.isCancelled()) {
                                                            pbar.setVisible(false);
                                                            progressLabel.setVisible(false);
                                                            return;
                                                        }
                                                    }
                                                    tss.getFrame();
                                                    pbar.setVisible(false);
                                                    progressLabel.setVisible(false);
                                                }
                                            });
                                            t.setPriority(Thread.MIN_PRIORITY);
                                            t.start();
                                        }
                                    });
                        }
                    });
                }

                JMenuItem removeMenu = selectedMenu.add("Remove from this list");
                removeMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeAll(seqList);
                    }
                });

//                JMenuItem removeDatabaseMenu = selectedMenu.add("Remove from database");
//                removeDatabaseMenu.addActionListener(new ActionListener() {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        removeAll(seqList);
//                    }
//                });

                contextMenu.add(selectedMenu);
            }//            if (jTable.getSortedColumn() != null) {
//                JMenuItem resetMenu = new JMenuItem();
//                resetMenu.setText("Reset sort order");
//                resetMenu.addActionListener(new ActionListener() {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        jTable.resetSortOrder();
//                    }
//                });
//                contextMenu.add(resetMenu);
//            }

//            if (jTable.getRowCount() > 1) {
//                int modelIndex = jTable.convertRowIndexToModel(rowIndex);
//                List<TestSequenceInstance> seqs = ((TestSequenceInstancesModel1) jTable.getModel()).getSequences();
//                if (modelIndex >= 0 && modelIndex < seqs.size()) {
//                    final TestSequenceInstance baseSequence = seqs.get(modelIndex);
//                    JMenuItem statsMenu = new JMenuItem();
//                    statsMenu.setText("Statistics");
//                    statsMenu.addActionListener(new ActionListener() {
//
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            TestStepStatistics tss = new TestStepStatistics(baseSequence);
//                            synchronized (lock) {
//                                for (TestSequenceInstance tsi : sequences) {
//                                    if (getEntityManager().contains(tsi)) {
//                                        tss.add(tsi.getId());
//                                    } else {
//                                        tss.add(tsi);
//                                    }
//                                }
//                            }
//                            tss.getFrame();
//                        }
//                    });
//                    contextMenu.add(statsMenu);
//                }
//            }
        }

        addAboutMenu(contextMenu);
        return contextMenu;
    }

    private void addAboutMenu(JPopupMenu contextMenu) {
        JMenuItem aboutMenu = new JMenuItem("About JTStand");
        aboutMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutDialog(frame, true);
            }
        });
        contextMenu.add(aboutMenu);
    }

    public static void setClipboardContents(String s) {
        StringSelection selection = new StringSelection(s);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                selection, selection);
    }

    class MyColumnFactory extends ColumnFactory {

        @Override
        public TableCellRenderer getCellRenderer(JXTable table, TableColumnExt ce) {
            return null;
        }
    };

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("click count: " + e.getClickCount());
        if (e.getClickCount() == 2) {
            Point p = e.getPoint();
            Object s = e.getSource();
            if (p != null) {
                if (jTable != null && s.equals(jTable)) {
                    int tableClickedRow = jTable.rowAtPoint(p);
                    if (tableClickedRow >= 0) {
//                        System.out.println("tableClickedRow: " + tableClickedRow);
//                        selectSequence(getSequence(tableClickedRow));
                        //...
                        int tableClickedColumn = jTable.columnAtPoint(p);
//                        System.out.println("tableClickedColumn: " + tableClickedColumn);
                        if (SequencesColumn.STATUS.ordinal() == tableClickedColumn) {
                            expandFailedOrRunning();
                        }
                    }
                }
                if (jXTreeTable != null && s.equals(jXTreeTable)) {
                    int treeClickedRow = jXTreeTable.rowAtPoint(p);
                    if (treeClickedRow >= 0) {
                        int treeClickedColumn = jXTreeTable.columnAtPoint(p);
                        if (treeClickedColumn >= 0) {
                            if (TestSequenceInstanceModel.SequenceColumn.STEPSTATUS.ordinal() == treeClickedColumn) {
                                Object o = jXTreeTable.getModel().getValueAt(treeClickedRow, SequenceColumn.NAME.ordinal());
                                if (o != null) {
                                    log.trace("Row: " + treeClickedRow + " Column: " + treeClickedColumn + " Object:" + o);
                                    if (o instanceof TestStepInstance) {
                                        TestStepInstance clickedStep = (TestStepInstance) o;
                                        if (clickedStep.isFailed() || clickedStep.isRunning()) {
                                            expandFailedOrRunning();
                                        } else {
                                            // neither running, nor failed
                                            if (jXTreeTable.isExpanded(treeClickedRow)) {
                                                jXTreeTable.collapseRow(treeClickedRow);
                                            } else {
                                                jXTreeTable.expandRow(treeClickedRow);
                                            }
                                        }
                                    }
                                }
                            }
                            if (TestSequenceInstanceModel.SequenceColumn.NAME.ordinal() == treeClickedColumn) {
                                toggleStep();
                            }
                            if (selectedStep != null && queryDialog != null && queryDialog.isVisible()) {
                                queryDialog.requestFocus();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    public static Color fDarkGreen = Color.green.darker();
    public static Color fDarkBrown = new Color(153, 102, 0);

    public static final Color getColor(
            String status) {
        if (status.equals(TestStepInstance.StepStatus.FAILED.statusString)) {
            return Color.red;
        } else if (status.equals(TestStepInstance.StepStatus.PASSED.statusString)) {
            return fDarkGreen;
        } else if (status.equals(TestStepInstance.StepStatus.NOTEST.statusString)) {
            return fDarkBrown;
        } else if (status.equals(TestStepInstance.StepStatus.RUNNING.statusString)) {
            return Color.blue;
        } else if (status.equals(TestStepInstance.StepStatus.ABORTED.statusString)) {
            return fDarkBrown;
        } else if (status.equals(TestStepInstance.StepStatus.STEPBYSTEP.statusString)) {
            return Color.blue;
        } else if (status.equals(TestStepInstance.StepStatus.STEPBYSTEP_FINISHED.statusString)) {
            return fDarkBrown;
        } else {
            return Color.black;
        }

    }

    private void cancelCellEditing() {
        CellEditor ce = jXTreeTable.getCellEditor();
        if (ce != null) {
            ce.cancelCellEditing();
        }

    }

    private void addTreeTableMenu(final JTable jTable) {
        jTable.addMouseListener(new MouseAdapter() {
            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && jTable.isEnabled()) {
                    Point p = new Point(e.getX(), e.getY());
                    int col = jTable.columnAtPoint(p);
                    int row = jTable.rowAtPoint(p);
                    /* Translate table index to model index */
                    int mcol = jTable.getColumn(jTable.getColumnName(col)).getModelIndex();
                    if (row >= 0 && row < jTable.getRowCount()) {
                        cancelCellEditing();
                        /* create popup menu... */
                        JPopupMenu contextMenu = createContextMenu(row, mcol);
                        /* ... and show it */
                        if (contextMenu != null && contextMenu.getComponentCount() > 0) {
                            contextMenu.show(jTable, p.x, p.y);
                        }

                    }
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

    private JPopupMenu createContextMenu(final int rowIndex,
            final int columnIndex) {

        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem copyMenu = new JMenuItem();
        copyMenu.setText("Copy");
        copyMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("rowIndex: " + rowIndex + " columnIndex: " + columnIndex);
                Object value = jXTreeTable.getValueAt(rowIndex, columnIndex);
                setClipboardContents(value == null ? "" : value.toString());
            }
        });
        contextMenu.add(copyMenu);
        log.trace("getting stepObject...");
        Object stepObject = jXTreeTable.getValueAt(rowIndex, TestSequenceInstanceModel.SequenceColumn.NAME.ordinal());
        if (stepObject != null && TestStepInstance.class.isAssignableFrom(stepObject.getClass())) {
            final TestStepInstance step = (TestStepInstance) stepObject;
            final String path = step.getTestStepInstancePath();
            JMenu menu = new JMenu("Chart of '" + path + "'");
            JMenuItem timeMenu = menu.add("Run Time Statistics");

            timeMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Thread t =
                                            new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                List<TestStepInstance> steps = querySteps(path, TestStepInstances.Mode.RUNTIME);
                                            } catch (InterruptedException ex) {
                                                log.warn("Exception", ex);
                                            }
                                        }
                                    });
                                    t.setPriority(Thread.MIN_PRIORITY);
                                    t.start();
                                }
                            });
                }
            });
            log.trace("Finding out if numeric...");
            if (step.isNumericKind()) {
                log.trace("Adding Parametric Statistics menu");
                JMenuItem statMenu = menu.add("Parametric Statistics");
                statMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pbar != null) {
                            pbar.cancel();
                        }
                        SwingUtilities.invokeLater(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Thread t =
                                                new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    List<TestStepInstance> steps = querySteps(path, TestStepInstances.Mode.PARAMETRIC);
                                                } catch (InterruptedException ex) {
                                                    log.warn("Exception", ex);
                                                }
                                            }
                                        });
                                        t.setPriority(Thread.MIN_PRIORITY);
                                        t.start();
                                    }
                                });
                    }
                });
            }
            if (menu.getItemCount() > 0) {
                contextMenu.add(menu);
            }
            log.trace("Finding out if running...");
            if (step.getTestSequenceInstance().isStepByStep() && !step.isSiblingRunning()) {
                JMenuItem startMenu = new JMenuItem();
                startMenu.setText("Start " + step.getName());
                startMenu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        step.getTestSequenceInstance().start(step);
                    }
                });
                contextMenu.add(startMenu);
            }
        }
        log.trace("Context menu created.");
        return contextMenu;
    }

    public String getToolTipTextSequence(MouseEvent e) {
        TreePath path = jXTreeTable.getPathForLocation(e.getX(), e.getY());
        if (path == null) {
            return null;
        }
        TestStepInstance tsi = (TestStepInstance) path.getLastPathComponent();

        int col = jXTreeTable.columnAtPoint(e.getPoint());
        if (col >= 0) {
            int modelCol = jXTreeTable.convertColumnIndexToModel(col);
            if (modelCol == TestSequenceInstanceModel.SequenceColumn.STEPSTATUS.ordinal()
                    && tsi.isFailed()
                    && tsi.equals(tsi.getTestSequenceInstance().getFailureStep())) {
                return tsi.getTestSequenceInstance().getFailureCode();
            }
        }
        if (tsi != null && tsi.getTestStep() != null && tsi.getTestStep().getRemark() != null) {
            return tsi.getTestStep().getRemark();
        }
        /* This should show the source code somehow, but does not work */
//        if (tsi.getStepClass() != null) {
//            Object o = tsi.getTestSequenceInstance().getTestProject().getGroovyClassLoader();
//            Method[] methods = o.getClass().getMethods();
//            for (int i = 0; i < methods.length; i++) {
//                Method m = methods[i];
//                if (m.getName().equals("getSource")) {
////                    System.out.println("getSource found!");
//                    Class<?>[] types = m.getParameterTypes();
////                    for (int j = 0; j < types.length; j++) {
////                        System.out.println("parameter type[" + j + "]" + types[j].getCanonicalName());
////                    }
//                    if (types.length == 1 && types[0].equals(String.class)) {
//                        m.setAccessible(true);
//                        try {
//                            return (String) m.invoke(o, tsi.getStepClass());
//                        } catch (IllegalAccessException ex) {
//                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (IllegalArgumentException ex) {
//                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (InvocationTargetException ex) {
//                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                }
//            }
//        }
        return null;
    }

    public String getToolTipTextSequences(MouseEvent e) {
        int row = jTable.rowAtPoint(e.getPoint());
        if (row >= 0) {
            int modelRow = jTable.convertRowIndexToModel(row);
            if (modelRow >= 0 && modelRow < size()) {
                TestSequenceInstance seq = get(modelRow);
                if (seq.getFailureCode() != null) {
                    int col = jTable.columnAtPoint(e.getPoint());
                    if (col >= 0) {
                        int modelCol = jTable.convertColumnIndexToModel(col);
                        if (modelCol == SequencesColumn.STATUS.ordinal()) {
                            return seq.getFailureCode();
                        }
                    }
                }
            }
        }
        return Integer.toString(this.size()) + " sequence" + ((size() > 1) ? "s" : "");
    }
}
