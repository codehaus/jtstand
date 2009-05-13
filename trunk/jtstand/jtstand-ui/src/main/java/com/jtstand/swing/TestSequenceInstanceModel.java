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
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstanceModel extends AbstractTreeTableModel implements Serializable {

    public static final long serialVersionUID = 20081114L;
    static final Logger logger = Logger.getLogger(TestSequenceInstanceModel.class.getCanonicalName());

    public static enum SequenceColumn {

        STEPNUMBER("", Integer.class),
        NAME("Step Name", TreeTableModel.class),
        VALUE("Value", String.class),
        LSL("LSL", String.class),
        USL("USL", String.class),
        LOOP("Loop", String.class),
        STEPSTARTTIME("Step Started", String.class),
        STEPELAPSED("Elapsed", String.class),
        STEPSTATUS("State", String.class);
        public final String columnName;
        public final Class columnClass;

        SequenceColumn(String columnName, Class columnClass) {
            this.columnName = columnName;
            this.columnClass = columnClass;
        }
    }
    public static SequenceColumn[] sequenceColumns = SequenceColumn.values();
    public static int SequenceColumnCount = sequenceColumns.length;
    private TestSequenceInstance testSequenceInstance;

    public TestSequenceInstance getTestSequenceInstance() {
        return testSequenceInstance;
    }

    /**
     *
     */
    public TestSequenceInstanceModel(TestSequenceInstance tsi) {
        super(null);
        init(tsi);
    }

    private void init(TestSequenceInstance tsi) {
        this.testSequenceInstance = tsi;
        List<TestStepInstance> roots = Collections.synchronizedList(new ArrayList<TestStepInstance>());
        if (tsi.getSetupStepInstance() != null) {
//            System.out.println("adding: " + tsi.getSetupStepInstance());
            roots.add(tsi.getSetupStepInstance());
        }
        if (tsi.getMainStepInstance() != null) {
//            System.out.println("adding: " + tsi.getMainStepInstance());
            roots.add(tsi.getMainStepInstance());
        }
        if (tsi.getCleanupStepInstance() != null) {
//            System.out.println("adding: " + tsi.getCleanupStepInstance());
            roots.add(tsi.getCleanupStepInstance());
        }
        root = roots;
//        System.out.println("roots: " + roots);
    }

//    public static void displayJFrame(final TestSequenceInstance tsi) {
//        if (tsi != null) {
//            JFrame jFrame = new JFrame();
//            jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//            jFrame.setMinimumSize(new Dimension(640, 300));
//            jFrame.setPreferredSize(Util.getMaximumWindowDimension());
//            jFrame.setLayout(new BorderLayout());
//            JScrollPane jScrollPane = new JScrollPane();
//            jFrame.getContentPane().add(jScrollPane, BorderLayout.CENTER);
//            jScrollPane.setViewportView((new TestSequenceInstanceModel(tsi)).getTreeTable());
//            jFrame.pack();
//            jFrame.setTitle(tsi.toString());
//            jFrame.setVisible(true);
//            jFrame.requestFocus();
//        }
//    }
    /**
     * Returns the number of children of <code>node</code>.
     */
    @Override
    public int getChildCount(Object node) {
        if (Collection.class.isAssignableFrom(node.getClass())) {
            return ((Collection) node).size();
        }
        return ((TestStepInstance) node).getSteps().size();
    }

    /**
     * Returns the child of <code>node</code> at index <code>i</code>.
     */
    @Override
    public Object getChild(Object node, int i) {
        if (List.class.isAssignableFrom(node.getClass())) {
            return ((List) node).get(i);
        }
        return ((TestStepInstance) node).getSteps().get(i);
    }

    /**
     * Returns true if the passed in object represents a leaf, false
     * otherwise.
     */
    @Override
    public boolean isLeaf(Object node) {
        if (TestStepInstance.class.isAssignableFrom(node.getClass())) {
            return ((TestStepInstance) node).isLeaf();
        }
        if (Collection.class.isAssignableFrom(node.getClass())) {
            return ((Collection) node).size() == 0;
        }
        return true;
    }

    /**
     * Returns the number of columns.
     */
    @Override
    public int getColumnCount() {
        return SequenceColumnCount;
    }

    /**
     * Returns the name for a particular column.
     */
    @Override
    public String getColumnName(
            int column) {
        if (column >= 0 && column < SequenceColumnCount) {
            return sequenceColumns[column].columnName;
        }
        return "";
    }

    /**
     * Returns the class for the particular column.
     */
    @Override
    public Class getColumnClass(
            int column) {
        if (column >= 0 && column < SequenceColumnCount) {
            return sequenceColumns[column].columnClass;
        }
        return null;
    }

    /**
     * Returns the value of the particular column.
     */
    @Override
    public Object getValueAt(Object node, int column) {
        if (node.equals(root)) {
            return "";
        }
        if (TestStepInstance.class.isAssignableFrom(node.getClass())) {
            TestStepInstance step = (TestStepInstance) node;
//            System.out.println("Step " + sequenceColumns[column].columnName);
            switch (sequenceColumns[column]) {
                case STEPNUMBER:
                    return step.getTestStepNamePath().getStepNumber();
                case NAME:
                    return step;
                case VALUE:
                    return step.getValueWithUnit();
                case LSL:
                    return step.getLslWithUnit();
                case USL:
                    return step.getUslWithUnit();
                case LOOP:
                    return step.getLoopsString();
                case STEPSTARTTIME:
                    return step.getStartedStringMs();
                case STEPELAPSED:
                    return step.getElapsedString();
                case STEPSTATUS:
                    return step.getStatusString();
            }
        }
        return "";
    }

    @Override
    public int getHierarchicalColumn() {
        return TestSequenceInstanceModel.SequenceColumn.NAME.ordinal();
    }

    @Override
    public void setValueAt(Object obj1, Object obj2, int i) {
    }

    @Override
    public int getIndexOfChild(Object node, Object child) {
        if (List.class.isAssignableFrom(node.getClass())) {
            return ((List) node).indexOf(child);
        }
        return ((TestStepInstance) node).getSteps().indexOf(child);
    }//    @Override
//    public void finalize() throws Throwable {
//        if (em != null) {
//            if (em.isOpen()) {
//                em.close();
//            }
//            em = null;
//        }
//        super.finalize();
//    }
}
