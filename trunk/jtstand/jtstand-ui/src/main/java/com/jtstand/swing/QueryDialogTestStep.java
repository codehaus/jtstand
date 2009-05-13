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
