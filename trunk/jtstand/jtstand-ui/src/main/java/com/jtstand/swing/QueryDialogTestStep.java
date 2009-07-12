/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, QueryDialogTestStep.java is part of JTStand.
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

import com.jtstand.TestStation;
import com.jtstand.TestStepInstance;

import java.awt.*;

/**
 *
 * @author albert_kurucz
 */
public class QueryDialogTestStep extends QueryDialog {

    public static final long serialVersionUID = 20081114L;

    public static enum Mode {

        SEQUENCE,
        STEP
    };
    private Mode mode;
    private TestStepSelectPanel testStepSelectPanel;

    public QueryDialogTestStep(Frame parent, boolean modal, Mode mode, TestStation testStation) {
        super(parent, modal, testStation);
        init(mode);
    }

    public QueryDialogTestStep(Frame parent, boolean modal, TestStation testStation) {
        super(parent, modal, testStation);
        init(Mode.SEQUENCE);
    }

    public void selectStep(TestStepInstance step) {
        if (testStepSelectPanel != null) {
            testStepSelectPanel.selectStep(step);
        }
    }

    public void setMode(Mode mode) {
        if (!mode.equals(this.mode)) {
            this.mode = mode;
            switch (mode) {
                case SEQUENCE:
                    setTitle("Query Sequences");
                    testStepSelectPanel.setVisible(false);
                    pack();
                    break;
                case STEP:
                    setTitle("Query Steps");
                    testStepSelectPanel.setVisible(true);
                    pack();
                    break;
                default:
                    throw new IllegalArgumentException("Illegal mode:" + mode);
            }
        }
    }

    private void init(Mode mode) {
        testStepSelectPanel = new TestStepSelectPanel();
        getContentPane().add(testStepSelectPanel, java.awt.BorderLayout.NORTH);
        setMode(mode);
        addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (testStepSelectPanel.isVisible()) {
                    testStepSelectPanel.requestFocus();
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                QueryDialogTestStep dialog = new QueryDialogTestStep(new javax.swing.JFrame(), true, Mode.STEP, getTestProject());
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    @Override
    public String toString() {
        switch (mode) {
            case SEQUENCE:
                return super.toString();
            case STEP:
                String q = toString("", "ts.testSequenceInstance.");
                return "select ts from TestStepInstance ts" + testStepSelectPanel.toString(add(q, "ts.valueNumber != null"), "ts.");
            default:
                return null;
        }
    }
}
