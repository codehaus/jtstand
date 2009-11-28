/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, QueryDialog.java is part of JTStand.
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

import com.jtstand.TestStation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

/**
 *
 * @author  albert_kurucz
 * You can change the display format to include the time. It doesn't have a chooser per say but the spinner still works.

We use the followings format:

"MMM dd, yyyy  '-' hh:mm:ss a z";


 */
public class QueryDialog extends javax.swing.JDialog implements MouseWheelListener {

    public static final long serialVersionUID = 20081114L;
    private JXDateTimePicker fromTime = new JXDateTimePicker(null);
    private JXDateTimePicker toTime = new JXDateTimePicker(null);
    private static DefaultComboBoxModel allModel;
    private MyList jPanelIgnore;
    private MyList jPanelInclude;
    //private QueryDialog myself;
    private TestStation testStation;

    public TestStation getTestStation() {
        return testStation;
    }

    /** Creates new form QueryDialog */
    public QueryDialog(java.awt.Frame parent, boolean modal, TestStation testStation) {
        super(parent, modal);
        this.testStation = testStation;
        //myself = this;

        if (allModel == null) {
            allModel = new DefaultComboBoxModel();
            allModel.addElement("All");
        }
        initComponents();
//        int w = 195;
        //fromTime.setPreferredSize(new java.awt.Dimension(w, 32));
        //toTime.setPreferredSize(new java.awt.Dimension(w, 32));
        jPanelFromPanel.add(fromTime);
        jLabelFrom.setLabelFor(fromTime);
        jPanelToPanel.add(toTime);
        jLabelTo.setLabelFor(toTime);
        jSpinnerMaxResults.setVisible(false);
        jComboBoxStation.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        int keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case KeyEvent.VK_ESCAPE:
                                all(jComboBoxStation);
                        }
                    }
                });
        jComboBoxFixture.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        int keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case KeyEvent.VK_ESCAPE:
                                all(jComboBoxFixture);
                        }
                    }
                });
        jComboBoxPartNumber.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        int keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case KeyEvent.VK_ESCAPE:
                                all(jComboBoxPartNumber);
                        }
                    }
                });
        jComboBoxPartRevision.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        int keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case KeyEvent.VK_ESCAPE:
                                all(jComboBoxPartRevision);
                        }
                    }
                });
        jComboBoxTestType.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyTyped(KeyEvent e) {
                        int keyChar = e.getKeyChar();

                        switch (keyChar) {
                            case KeyEvent.VK_ESCAPE:
                                all(jComboBoxTestType);
                        }
                    }
                });
        all(jComboBoxStation);
        all(jComboBoxFixture);
        all(jComboBoxPartNumber);
        all(jComboBoxPartRevision);
        all(jComboBoxTestType);
        jComboBoxStation.addMouseWheelListener(this);
        jComboBoxFixture.addMouseWheelListener(this);
        jComboBoxSection.addMouseWheelListener(this);
        jSpinnerMaxResults.addMouseWheelListener(this);
        jComboBoxPartNumber.addMouseWheelListener(this);
        jComboBoxPartRevision.addMouseWheelListener(this);
        jSpinnerMaxResults.setPreferredSize(new Dimension(85, jSpinnerMaxResults.getPreferredSize().height));
        jPanelInclude = new MyList();
        jPanelInclude.checkBox().setText("All");
        jPanelInclude.checkBox().setMnemonic('A');
        //jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Include Serial Numbers"));
        jPanelInclude.checkBox().addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                includeCheckBoxActionPerformed(evt);
            }
        });

        jPanel1.add(jPanelInclude);
        jPanelIgnore = new MyList();
        jPanelIgnore.checkBox().setText("None");
        jPanelIgnore.checkBox().setMnemonic('N');
//        jPanelIgnore.setBorder(javax.swing.BorderFactory.createTitledBorder("Exclude Serial Numbers"));
        jPanelIgnore.checkBox().addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ignoreCheckBoxActionPerformed(evt);
            }
        });
        jPanel2.add(jPanelIgnore);
        pack();
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                setMinimumSize(getSize());
            }
        });

    }
    private ActionListener queryAction;

    public void setQueryAction(ActionListener newQueryAction) {
        if (queryAction != null) {
            jButtonQuery.removeActionListener(queryAction);
        }
        queryAction = newQueryAction;
        jButtonQuery.addActionListener(queryAction);
    }

    public JButton queryButton() {
        return queryButton();
    }

    private void includeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
//        System.out.println("include all selected:"+jPanelInclude.checkBox().isSelected());
        if (!jPanelInclude.checkBox().isSelected() && jPanelInclude.getModel().getSize() > 0) {
            jPanelIgnore.checkBox().setSelected(true);
        }
    }

    private void ignoreCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
//        System.out.println("ignore none selected:"+jPanelIgnore.checkBox().isSelected());
        if (!jPanelIgnore.checkBox().isSelected() && jPanelIgnore.getModel().getSize() > 0) {
            jPanelInclude.checkBox().setSelected(true);
        }
    }

    public static String add(String q, String str) {
        if (q.length() > 0) {
            q += " and ";
        } else {
            q += " where ";
        }
        q += str;
        return q;
    }

    public Integer getMaxResults() {
        Object item = jComboBoxSection.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            return (Integer) jSpinnerMaxResults.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "select ts from TestSequenceInstance ts" + toString("", "ts.");
    }

    protected String toString(String q, String base) {
        Object item;
        item = jComboBoxStation.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            q = add(q, base + "testStation.hostName = '" + item.toString() + "'");
        }
        item = jComboBoxFixture.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            q = add(q, base + "testFixture.fixtureName = '" + item.toString() + "'");
        }
        item = jComboBoxPartNumber.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            q = add(q, base + "testType.product.partNumber = '" + item.toString() + "'");
        }
        item = jComboBoxPartRevision.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            q = add(q, base + "testType.product.partRevision = '" + item.toString() + "'");
        }
        item = jComboBoxTestType.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            q = add(q, base + "testType.name = '" + item.toString() + "'");
        }
        Date d = fromTime.getDate();
        if (d != null) {
            q = add(q, base + "createTime >= " + Long.toString(d.getTime()));
        }

        d = toTime.getDate();
        if (d != null) {
            q = add(q, base + "finishTime < " + Long.toString(d.getTime()));
        }

        if (!jPanelIgnore.checkBox().isSelected()) {
            for (int i = 0; i < jPanelIgnore.getModel().getSize(); i++) {
                q = add(q, base + "serialNumber <> '" + jPanelIgnore.getModel().getElementAt(i).toString() + "'");
            }
        }

        if (!jPanelInclude.checkBox().isSelected() && jPanelInclude.getModel().getSize() > 0) {
            if (jPanelInclude.getModel().getSize() > 1) {
                q = add(q, "(");
                for (int i = 0; i < jPanelInclude.getModel().getSize(); i++) {
                    if (i > 0) {
                        q += " or ";
                    }
                    q += base + "serialNumber = '" + jPanelInclude.getModel().getElementAt(i).toString() + "'";
                }
                q += ")";
            } else {
                q = add(q, base + "serialNumber = '" + jPanelInclude.getModel().getElementAt(0).toString() + "'");
            }
        }

        item = jComboBoxSection.getSelectedItem();
        if (item != null && !item.toString().equals("All")) {
            if ("First".equals(item.toString())) {
                q += " order by createTime ASC";
            } else if ("Last".equals(item.toString())) {
                q += " order by createTime DESC";
            }
        }
        return q;
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

        jPanelMain = new javax.swing.JPanel();
        jPanelLeft = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanelFromPanel = new javax.swing.JPanel();
        jLabelFrom = new javax.swing.JLabel();
        jPanelToPanel = new javax.swing.JPanel();
        jLabelTo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxSection = new javax.swing.JComboBox();
        jSpinnerMaxResults = new javax.swing.JSpinner();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jComboBoxPartNumber = new javax.swing.JComboBox();
        jPanel32 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jComboBoxPartRevision = new javax.swing.JComboBox();
        jPanel34 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jComboBoxTestType = new javax.swing.JComboBox();
        jPanel39 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jComboBoxStation = new javax.swing.JComboBox();
        jPanel27 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jComboBoxFixture = new javax.swing.JComboBox();
        jPanel30 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonQuery = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanelMain.setLayout(new java.awt.GridBagLayout());

        jPanelLeft.setLayout(new javax.swing.BoxLayout(jPanelLeft, javax.swing.BoxLayout.Y_AXIS));

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Time"));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.Y_AXIS));

        jPanelFromPanel.setMinimumSize(new java.awt.Dimension(270, 25));
        jPanelFromPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelFrom.setDisplayedMnemonic('F');
        jLabelFrom.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabelFrom.setText("From:");
        jLabelFrom.setMaximumSize(new java.awt.Dimension(42, 15));
        jLabelFrom.setMinimumSize(new java.awt.Dimension(42, 15));
        jLabelFrom.setPreferredSize(new java.awt.Dimension(42, 15));
        jPanelFromPanel.add(jLabelFrom);

        jPanel16.add(jPanelFromPanel);

        jPanelToPanel.setMinimumSize(new java.awt.Dimension(270, 25));
        jPanelToPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelTo.setDisplayedMnemonic('T');
        jLabelTo.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabelTo.setText("To:");
        jLabelTo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabelTo.setMaximumSize(new java.awt.Dimension(42, 15));
        jLabelTo.setMinimumSize(new java.awt.Dimension(42, 15));
        jLabelTo.setPreferredSize(new java.awt.Dimension(42, 15));
        jPanelToPanel.add(jLabelTo);

        jPanel16.add(jPanelToPanel);

        jPanelLeft.add(jPanel16);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Number"));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setDisplayedMnemonic('M');
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setLabelFor(jComboBoxSection);
        jLabel1.setText("Max Results:");
        jLabel1.setMaximumSize(new java.awt.Dimension(85, 15));
        jLabel1.setPreferredSize(new java.awt.Dimension(85, 15));
        jPanel6.add(jLabel1);

        jComboBoxSection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All", "Last", "First" }));
        jComboBoxSection.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxSectionPopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jComboBoxSection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSectionActionPerformed(evt);
            }
        });
        jComboBoxSection.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxSectionFocusGained(evt);
            }
        });
        jPanel6.add(jComboBoxSection);

        jSpinnerMaxResults.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(1), null, Integer.valueOf(1)));
        jPanel6.add(jSpinnerMaxResults);

        jPanel3.add(jPanel6);

        jPanelLeft.add(jPanel3);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Type"));
        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.Y_AXIS));

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jLabel8.setDisplayedMnemonic('P');
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel8.setLabelFor(jComboBoxPartNumber);
        jLabel8.setText("Part Number:");
        jLabel8.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel8.setMaximumSize(new java.awt.Dimension(90, 15));
        jLabel8.setPreferredSize(new java.awt.Dimension(90, 15));
        jPanel14.add(jLabel8);

        jPanel31.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel31.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel31.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel14.add(jPanel31);

        jComboBoxPartNumber.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxPartNumberPopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxPartNumberPopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBoxPartNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxPartNumberFocusGained(evt);
            }
        });
        jPanel14.add(jComboBoxPartNumber);

        jPanel32.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel32.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel32.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel14.add(jPanel32);

        jPanel13.add(jPanel14);

        jPanel22.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel22.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel22.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel13.add(jPanel22);

        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        jLabel9.setDisplayedMnemonic('R');
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel9.setLabelFor(jComboBoxPartRevision);
        jLabel9.setText("Revision:");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel9.setMaximumSize(new java.awt.Dimension(90, 15));
        jLabel9.setPreferredSize(new java.awt.Dimension(90, 15));
        jPanel15.add(jLabel9);

        jPanel33.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel33.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel33.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel15.add(jPanel33);

        jComboBoxPartRevision.setPreferredSize(null);
        jComboBoxPartRevision.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxPartRevisionPopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxPartRevisionPopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBoxPartRevision.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxPartRevisionFocusGained(evt);
            }
        });
        jPanel15.add(jComboBoxPartRevision);

        jPanel34.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel34.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel34.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel15.add(jPanel34);

        jPanel13.add(jPanel15);

        jPanel24.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel24.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel24.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel13.add(jPanel24);

        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        jLabel10.setDisplayedMnemonic('R');
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel10.setLabelFor(jComboBoxPartRevision);
        jLabel10.setText("Test Type:");
        jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel10.setMaximumSize(new java.awt.Dimension(90, 15));
        jLabel10.setPreferredSize(new java.awt.Dimension(90, 15));
        jPanel17.add(jLabel10);

        jPanel36.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel36.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel36.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel17.add(jPanel36);

        jComboBoxTestType.setPreferredSize(null);
        jComboBoxTestType.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxTestTypePopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxTestTypePopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBoxTestType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxTestTypeFocusGained(evt);
            }
        });
        jPanel17.add(jComboBoxTestType);

        jPanel39.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel39.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel39.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel17.add(jPanel39);

        jPanel13.add(jPanel17);

        jPanel23.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel23.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel23.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel13.add(jPanel23);

        jPanelLeft.add(jPanel13);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Location"));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setDisplayedMnemonic('s');
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setLabelFor(jComboBoxStation);
        jLabel6.setText("Station:");
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel6.setMaximumSize(new java.awt.Dimension(90, 15));
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 15));
        jPanel11.add(jLabel6);

        jPanel28.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel28.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel28.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel11.add(jPanel28);

        jComboBoxStation.setPreferredSize(null);
        jComboBoxStation.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxStationPopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxStationPopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBoxStation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxStationFocusGained(evt);
            }
        });
        jPanel11.add(jComboBoxStation);

        jPanel27.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel27.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel27.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel11.add(jPanel27);

        jPanel4.add(jPanel11);

        jPanel21.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel21.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel21.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel4.add(jPanel21);

        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setDisplayedMnemonic('x');
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel7.setLabelFor(jComboBoxFixture);
        jLabel7.setText("Fixture:");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel7.setMaximumSize(new java.awt.Dimension(90, 15));
        jLabel7.setPreferredSize(new java.awt.Dimension(90, 15));
        jPanel12.add(jLabel7);

        jPanel29.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel29.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel29.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel12.add(jPanel29);

        jComboBoxFixture.setPreferredSize(null);
        jComboBoxFixture.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxFixturePopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxFixturePopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBoxFixture.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBoxFixtureFocusGained(evt);
            }
        });
        jPanel12.add(jComboBoxFixture);

        jPanel30.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel30.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel30.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel12.add(jPanel30);

        jPanel4.add(jPanel12);

        jPanel8.setMaximumSize(new java.awt.Dimension(5, 5));
        jPanel8.setMinimumSize(new java.awt.Dimension(5, 5));
        jPanel8.setPreferredSize(new java.awt.Dimension(5, 5));
        jPanel4.add(jPanel8);

        jPanelLeft.add(jPanel4);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Exclude Serial Numbers"));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanelLeft.add(jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelMain.add(jPanelLeft, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Include Serial Numbers"));
        jPanel1.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelMain.add(jPanel1, gridBagConstraints);

        getContentPane().add(jPanelMain, java.awt.BorderLayout.CENTER);

        jPanelButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 15));

        jButtonQuery.setMnemonic('q');
        jButtonQuery.setText("Query");
        jButtonQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQueryActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonQuery);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonCancel);

        getContentPane().add(jPanelButtons, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
    this.setVisible(false);
}//GEN-LAST:event_jButtonCancelActionPerformed

private void jComboBoxSectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSectionActionPerformed
    jSpinnerMaxResults.setVisible(!jComboBoxSection.getSelectedItem().toString().equals("All"));
}//GEN-LAST:event_jComboBoxSectionActionPerformed
    private boolean repopup = false;

private void jComboBoxStationPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxStationPopupMenuWillBecomeVisible
    if (!repopup) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
//        for (String hostName : (new HostNamesQuery(testStation)).query()) {
        for (String hostName : getTestStation().getHostNames()) {
            model.addElement(hostName);
        }
        jComboBoxStation.setModel(model);
        repopup = true;
        jComboBoxStation.hidePopup();
        jComboBoxStation.showPopup();
        repopup = false;
    }

}//GEN-LAST:event_jComboBoxStationPopupMenuWillBecomeVisible

    private void all(JComboBox combo) {
        combo.setModel(allModel);
    }

private void jComboBoxStationPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxStationPopupMenuCanceled
    all(jComboBoxStation);
}//GEN-LAST:event_jComboBoxStationPopupMenuCanceled

private void jComboBoxSectionPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxSectionPopupMenuCanceled
    jComboBoxSection.setSelectedItem("All");
}//GEN-LAST:event_jComboBoxSectionPopupMenuCanceled

private void jComboBoxFixturePopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxFixturePopupMenuWillBecomeVisible
    if (!repopup) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        Object hostName = jComboBoxStation.getSelectedItem();
//        for (String fixtureName : (new FixtureNamesQuery(testStation, hostName != null ? hostName.toString() : null)).query()) {
        for (String fixtureName : getTestStation().getTestFixtureNames(hostName != null ? hostName.toString() : null)) {
            model.addElement(fixtureName);
        }
        jComboBoxFixture.setModel(model);
        repopup = true;
        jComboBoxFixture.hidePopup();
        jComboBoxFixture.showPopup();
        repopup = false;
    }
}//GEN-LAST:event_jComboBoxFixturePopupMenuWillBecomeVisible

private void jComboBoxFixturePopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxFixturePopupMenuCanceled
    all(jComboBoxFixture);
}//GEN-LAST:event_jComboBoxFixturePopupMenuCanceled

private void jComboBoxPartNumberPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxPartNumberPopupMenuWillBecomeVisible
    if (!repopup) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
//        for (String partNumber : (new PartNumbersQuery(testStation).query())) {
        for (String partNumber : getTestStation().getPartNumbers()) {
            model.addElement(partNumber);
        }
        jComboBoxPartNumber.setModel(model);
        repopup = true;
        jComboBoxPartNumber.hidePopup();
        jComboBoxPartNumber.showPopup();
        repopup = false;
    }
}//GEN-LAST:event_jComboBoxPartNumberPopupMenuWillBecomeVisible

private void jComboBoxPartNumberPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxPartNumberPopupMenuCanceled
    all(jComboBoxPartNumber);
}//GEN-LAST:event_jComboBoxPartNumberPopupMenuCanceled

private void jComboBoxPartRevisionPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxPartRevisionPopupMenuCanceled
    all(jComboBoxPartRevision);
}//GEN-LAST:event_jComboBoxPartRevisionPopupMenuCanceled

private void jComboBoxPartRevisionPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxPartRevisionPopupMenuWillBecomeVisible
    if (!repopup) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        Object partNumber = jComboBoxPartNumber.getSelectedItem();
//        for (String revName : (new PartNumberRevsQuery(testStation, partNumber != null ? partNumber.toString() : null)).query()) {
        for (String element : getTestStation().getPartNumberRevs(partNumber == null ? null : partNumber.toString())) {
            model.addElement(element);
        }
        jComboBoxPartRevision.setModel(model);
        repopup = true;
        jComboBoxPartRevision.hidePopup();
        jComboBoxPartRevision.showPopup();
        repopup = false;
    }
}//GEN-LAST:event_jComboBoxPartRevisionPopupMenuWillBecomeVisible

private void jComboBoxPartNumberFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxPartNumberFocusGained
    jComboBoxPartNumber.showPopup();
}//GEN-LAST:event_jComboBoxPartNumberFocusGained

private void jComboBoxPartRevisionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxPartRevisionFocusGained
    jComboBoxPartRevision.showPopup();
}//GEN-LAST:event_jComboBoxPartRevisionFocusGained

private void jComboBoxStationFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxStationFocusGained
    jComboBoxStation.showPopup();
}//GEN-LAST:event_jComboBoxStationFocusGained

private void jComboBoxFixtureFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxFixtureFocusGained
    jComboBoxFixture.showPopup();
}//GEN-LAST:event_jComboBoxFixtureFocusGained

private void jComboBoxSectionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxSectionFocusGained
    jComboBoxSection.showPopup();
}//GEN-LAST:event_jComboBoxSectionFocusGained

private void jButtonQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonQueryActionPerformed
    setVisible(false);
}//GEN-LAST:event_jButtonQueryActionPerformed

private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
// TODO add your handling code here:
}//GEN-LAST:event_formFocusGained

private void jComboBoxTestTypePopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxTestTypePopupMenuCanceled
    // TODO add your handling code here:
}//GEN-LAST:event_jComboBoxTestTypePopupMenuCanceled

private void jComboBoxTestTypePopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxTestTypePopupMenuWillBecomeVisible
    if (!repopup) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        Object partNumber = jComboBoxPartNumber.getSelectedItem();
        Object partRevision = jComboBoxPartRevision.getSelectedItem();
//        for (String revName : (new PartNumberRevsQuery(testStation, partNumber != null ? partNumber.toString() : null)).query()) {
        for (String element : getTestStation().getTestTypes(partNumber == null ? null : partNumber.toString(), partRevision == null ? null : partRevision.toString())) {
            model.addElement(element);
        }
        jComboBoxTestType.setModel(model);
        repopup = true;
        jComboBoxTestType.hidePopup();
        jComboBoxTestType.showPopup();
        repopup = false;
    }

}//GEN-LAST:event_jComboBoxTestTypePopupMenuWillBecomeVisible

private void jComboBoxTestTypeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBoxTestTypeFocusGained
    // TODO add your handling code here:
}//GEN-LAST:event_jComboBoxTestTypeFocusGained

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        //JXMonthView v=new JXMonthView();
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                QueryDialog dialog = new QueryDialog(new javax.swing.JFrame(), true);
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
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (jSpinnerMaxResults.equals(e.getSource())) {
//            System.out.println("Current:"+jSpinnerMaxResults.getValue());
            int i = (Integer) jSpinnerMaxResults.getValue() - e.getWheelRotation();
            if (i >= 1) {
                jSpinnerMaxResults.setValue(new Integer(i));
            }
        }
        if (JComboBox.class.isAssignableFrom(e.getSource().getClass())) {
            JComboBox combo = (JComboBox) e.getSource();
            if (combo.getModel().getSize() > 1) {
                //           System.out.println("visible:"+combo.isPopupVisible()+" showing:"+combo.isShowing());
                int i = combo.getSelectedIndex();
                //             System.out.println("Selected index:"+i);
                i += e.getWheelRotation();
                if (i >= combo.getItemCount()) {
                    i = 0;
                }
                if (i < 0) {
                    i = combo.getItemCount() - 1;
                }
//               System.out.println("Selecting index:"+i);
                combo.getModel().setSelectedItem(combo.getModel().getElementAt(i));
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonQuery;
    private javax.swing.JComboBox jComboBoxFixture;
    private javax.swing.JComboBox jComboBoxPartNumber;
    private javax.swing.JComboBox jComboBoxPartRevision;
    private javax.swing.JComboBox jComboBoxSection;
    private javax.swing.JComboBox jComboBoxStation;
    private javax.swing.JComboBox jComboBoxTestType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelFromPanel;
    private javax.swing.JPanel jPanelLeft;
    private javax.swing.JPanel jPanelMain;
    private javax.swing.JPanel jPanelToPanel;
    private javax.swing.JSpinner jSpinnerMaxResults;
    // End of variables declaration//GEN-END:variables
}
