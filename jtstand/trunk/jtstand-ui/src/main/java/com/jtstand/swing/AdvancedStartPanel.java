/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AdvancedStartPanel.java is part of JTStand.
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

import com.jtstand.query.Runner;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author  albert_kurucz
 */
public class AdvancedStartPanel extends javax.swing.JPanel {

    public static final long serialVersionUID = 20081114L;
    private AbstractStarterPanel starterPanel;

    /** Creates new form AdvancedStartPanel */
    public AdvancedStartPanel(AbstractStarterPanel starterPanel) {
        this.starterPanel = starterPanel;
        initComponents();
        this.jLabelAtLeast.setPreferredSize(this.jLabelNotMoreThan.getPreferredSize());
        KeyAdapter ka = new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    getToolkit().beep();
                    e.consume();
                }
            }
        };
        jTextFieldMinTotal.addKeyListener(ka);
        jTextFieldMinPassed.addKeyListener(ka);
        jTextFieldMinFailed.addKeyListener(ka);
        jTextFieldMaxTotal.addKeyListener(ka);
        jTextFieldMaxPassed.addKeyListener(ka);
        jTextFieldMaxFailed.addKeyListener(ka);

        jTextFieldMinTotal.getDocument().addDocumentListener(new MyDocumentListener());
        jTextFieldMinPassed.getDocument().addDocumentListener(new MyDocumentListener());
        jTextFieldMinFailed.getDocument().addDocumentListener(new MyDocumentListener());
        jTextFieldMaxTotal.getDocument().addDocumentListener(new MyDocumentListener());
        jTextFieldMaxPassed.getDocument().addDocumentListener(new MyDocumentListener());
        jTextFieldMaxFailed.getDocument().addDocumentListener(new MyDocumentListener());
    }

    public void setupRunner(Runner runner) {
        runner.setMinTotal(getMinTotal());
        runner.setMinPassed(getMinPassed());
        runner.setMinFailed(getMinFailed());
        runner.setMaxTotal(getMaxTotal());
        runner.setMaxPassed(getMaxPassed());
        runner.setMaxFailed(getMaxFailed());
        runner.setSkipManualSteps(isSkipManualSteps());
    }

    class MyDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // do nothing
        }
    }

    private void update() {
        if (isLoop()) {
            starterPanel.jButtonStart().setText("Loop Sequences");
        } else {
            starterPanel.jButtonStart().setText("Start Sequence");
        }
    }

    public boolean isLoop(JTextField f, int i) {
        if (f == null) {
            return false;
        }
        String txt = f.getText();
        if (txt == null || txt.length() == 0) {
            return false;
        }
        try {
            if (Integer.parseInt(txt) < i) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean isLoop() {
        return isLoop(jTextFieldMinTotal, 2) || isLoop(jTextFieldMinPassed, 1) || isLoop(jTextFieldMinFailed, 1);
    }

    private int getIntValue(JTextField field) {
        if (field == null) {
            return 0;
        }
        String str = field.getText();
        if (str == null || str.length() == 0) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    public int getMinTotal() {
        return getIntValue(jTextFieldMinTotal);
    }

    public int getMinPassed() {
        return getIntValue(jTextFieldMinPassed);
    }

    public int getMinFailed() {
        return getIntValue(jTextFieldMinFailed);
    }

    public int getMaxTotal() {
        return getIntValue(jTextFieldMaxTotal);
    }

    public int getMaxPassed() {
        return getIntValue(jTextFieldMaxPassed);
    }

    public int getMaxFailed() {
        return getIntValue(jTextFieldMaxFailed);
    }

    public boolean isSkipManualSteps() {
        return jCheckBoxSkipManualSteps.isSelected();
    }

    public JButton jButtonStepByStep() {
        return jButtonStepByStep;
    }
//    @Override
//    public void processKeyEvent(KeyEvent ev) {
//
//        char c = ev.getKeyChar();
//        if(Character.isDigit(c)){
//            super.processKeyEvent(ev);
//        }else{
//            ev.consume();
//        }
//    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabelAtLeast = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldMinTotal = new javax.swing.JTextField();
        jTextFieldMinPassed = new javax.swing.JTextField();
        jTextFieldMinFailed = new javax.swing.JTextField();
        jTextFieldMaxTotal = new javax.swing.JTextField();
        jTextFieldMaxPassed = new javax.swing.JTextField();
        jTextFieldMaxFailed = new javax.swing.JTextField();
        jLabelNotMoreThan = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxSkipManualSteps = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jButtonStepByStep = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 8, 8));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Loop"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelAtLeast.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelAtLeast.setText("At least");
        jLabelAtLeast.setToolTipText("<html>In order to loop,<br>at least one<br>of the 'At least' fields<br>must be filled.<br><br>When every one of the 'At least' criterias<br>are reached,<br>looping ends.</html>");
        jLabelAtLeast.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel1.add(jLabelAtLeast, gridBagConstraints);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Finished Sequences:");
        jLabel3.setToolTipText("Either Passed or Failed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Passed Sequences:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Failed Sequences:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        jPanel1.add(jLabel5, gridBagConstraints);

        jTextFieldMinTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jTextFieldMinTotal, gridBagConstraints);

        jTextFieldMinPassed.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jTextFieldMinPassed, gridBagConstraints);

        jTextFieldMinFailed.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel1.add(jTextFieldMinFailed, gridBagConstraints);

        jTextFieldMaxTotal.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(jTextFieldMaxTotal, gridBagConstraints);

        jTextFieldMaxPassed.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(jTextFieldMaxPassed, gridBagConstraints);

        jTextFieldMaxFailed.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 8);
        jPanel1.add(jTextFieldMaxFailed, gridBagConstraints);

        jLabelNotMoreThan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNotMoreThan.setText("Not more than");
        jLabelNotMoreThan.setToolTipText("<html>The 'Not more than' criterias<br>are stronger<br>than the 'At least' criterias.<br><br>When any of the<br>'Not more than' criterias<br>are reached,<br>looping ends<br>even if some 'At least'<br>criterias are not fulfilled.</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel1.add(jLabelNotMoreThan, gridBagConstraints);

        jPanel3.add(jPanel1);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Skip"));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 1));

        jCheckBoxSkipManualSteps.setText("Skip Interactive Steps");
        jCheckBoxSkipManualSteps.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBoxSkipManualSteps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSkipManualStepsActionPerformed(evt);
            }
        });
        jPanel4.add(jCheckBoxSkipManualSteps);

        jPanel3.add(jPanel4);

        add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 15));

        jButtonStepByStep.setText("Start Step by Step");
        jButtonStepByStep.setToolTipText("Start a debug session, ignoring loop and skip settings");
        jPanel2.add(jButtonStepByStep);
        jButtonStepByStep.getAccessibleContext().setAccessibleDescription("Start step by step debug session, ignoring loop settings");

        add(jPanel2, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

private void jCheckBoxSkipManualStepsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSkipManualStepsActionPerformed
}//GEN-LAST:event_jCheckBoxSkipManualStepsActionPerformed

private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
    jTextFieldMinTotal.requestFocus();
}//GEN-LAST:event_formFocusGained
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonStepByStep;
    private javax.swing.JCheckBox jCheckBoxSkipManualSteps;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelAtLeast;
    private javax.swing.JLabel jLabelNotMoreThan;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextFieldMaxFailed;
    private javax.swing.JTextField jTextFieldMaxPassed;
    private javax.swing.JTextField jTextFieldMaxTotal;
    private javax.swing.JTextField jTextFieldMinFailed;
    private javax.swing.JTextField jTextFieldMinPassed;
    private javax.swing.JTextField jTextFieldMinTotal;
    // End of variables declaration//GEN-END:variables
}
