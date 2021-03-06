/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MyList.java is part of JTStand.
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

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  albert_kurucz
 */
public class MyList extends javax.swing.JPanel implements ListDataListener {

    public static final long serialVersionUID = 20081114L;

    public JCheckBox checkBox() {
        return jCheckBox;
    }

    /** Creates new form MyList */
    public MyList() {
        initComponents();
        DefaultListModel dlm = new DefaultListModel();
        jList.setModel(dlm);
        dlm.addListDataListener(this);
        jList.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        int keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case KeyEvent.VK_DELETE:
                            case KeyEvent.VK_BACK_SPACE:
                                Object[] selected = jList.getSelectedValues();
                                for (int i = 0; i < selected.length; i++) {
                                    ((DefaultListModel) jList.getModel()).removeElement(selected[i]);
                                }
                        }
                    }
                });
    }

    public MyList(String checkBoxText, String borderText) {
        this();
        setBorder(javax.swing.BorderFactory.createTitledBorder(borderText));
    }

    public ListModel getModel() {
        return (ListModel) ((DefaultListModel) jList.getModel());
    }

    private void load(File file) {
        if (file.canRead()) {
            System.out.println("Loading " + file.getName() + " ...");
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                while (br.ready()) {
                    String line = br.readLine();
                    StringTokenizer st = new StringTokenizer(line, "\n\r\t ,");
                    while (st.hasMoreTokens()) {
                        addItem(st.nextToken());
                    }
                }
                br.close();
            } catch (Exception ex) {
                Logger.getLogger(MyList.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Cannot read " + file.getName());
        }

    }

    private void save(File file) {
        if (getModel().getSize() < 1) {
            return;
        }
        if (!file.isFile()) {
            try {
                System.out.println("Creating " + file.getName() + " ...");
                file.createNewFile();
            } catch (IOException ex) {
                System.err.println("Cannot create " + file.getName());
                return;
            }
        }
        if (file.canWrite()) {
            System.out.println("Saving " + file.getName() + " ...");
            FileWriter fr = null;
            try {
                fr = new FileWriter(file);
                for (int i = 0; i < getModel().getSize(); i++) {
                    fr.write(getModel().getElementAt(i).toString() + "\r\n");
                }
            } catch (Exception ex) {
                Logger.getLogger(MyList.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MyList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            System.out.println("Cannot write " + file.getName());
        }

    }

    private void addSerial() {
        addItem(jTextField.getText());
    }

    private void addItem(String newItem) {
        if (newItem != null && newItem.length() > 0) {
            if (!((DefaultListModel) jList.getModel()).contains(newItem)) {
                ((DefaultListModel) jList.getModel()).addElement(newItem);
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelHead = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jCheckBox = new javax.swing.JCheckBox();
        jButtonLoad = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jPanelText = new javax.swing.JPanel();
        jTextField = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jList = new javax.swing.JList();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setLayout(new java.awt.BorderLayout());

        jPanelHead.setLayout(new java.awt.GridBagLayout());

        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jCheckBox.setSelected(true);
        jCheckBox.setText("All");
        jCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxActionPerformed(evt);
            }
        });
        jPanelButtons.add(jCheckBox);

        jButtonLoad.setText("Load");
        jButtonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonLoad);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonSave);

        jButtonClear.setText("Clear");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonClear);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanelHead.add(jPanelButtons, gridBagConstraints);

        jPanelText.setLayout(new java.awt.GridBagLayout());

        jTextField.setPreferredSize(null);
        jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanelText.add(jTextField, gridBagConstraints);

        jButtonAdd.setText("Add");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanelText.add(jButtonAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanelHead.add(jPanelText, gridBagConstraints);

        add(jPanelHead, java.awt.BorderLayout.NORTH);

        jList.setVisibleRowCount(3);
        jScrollPane.setViewportView(jList);

        add(jScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
    ((DefaultListModel) jList.getModel()).removeAllElements();
    jTextField.setText("");
    jCheckBox.setSelected(true);
}//GEN-LAST:event_jButtonClearActionPerformed

private void jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldActionPerformed
    addSerial();
}//GEN-LAST:event_jTextFieldActionPerformed

private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
    addSerial();
}//GEN-LAST:event_jButtonAddActionPerformed

private void jCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxActionPerformed
    if (jCheckBox.isSelected()) {
        jTextField.setText("");
    } else {
        if (jList.getModel().getSize() == 0) {
            jCheckBox.setSelected(true);
            jTextField.requestFocus();
        }
    }
}//GEN-LAST:event_jCheckBoxActionPerformed

private void jButtonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadActionPerformed
    final JFileChooser fc = new JFileChooser();
    //fc.setFileFilter(new DefaultFileFilter("*.txt"));
    if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(this)) {
        load(fc.getSelectedFile());
    }
}//GEN-LAST:event_jButtonLoadActionPerformed

private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
    final JFileChooser fc = new JFileChooser();
    //fc.setFileFilter(new DefaultFileFilter("*.txt"));
    if (JFileChooser.APPROVE_OPTION == fc.showSaveDialog(this)) {
        save(fc.getSelectedFile());
    }
}//GEN-LAST:event_jButtonSaveActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonLoad;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBox;
    private javax.swing.JList jList;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelHead;
    private javax.swing.JPanel jPanelText;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextField jTextField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void intervalAdded(ListDataEvent e) {
        jList.setToolTipText(Integer.toString(jList.getModel().getSize()) + " item" + ((jList.getModel().getSize() > 1) ? "s" : ""));
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        jList.setToolTipText(Integer.toString(jList.getModel().getSize()) + " item" + ((jList.getModel().getSize() > 1) ? "s" : ""));
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
//        jList.setToolTipText(Integer.toString(jList.getModel().getSize()) + " items");
    }
}
