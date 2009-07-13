/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StarterCommonDialog.java is part of JTStand.
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

import com.jtstand.*;
import com.jtstand.query.FrameInterface;
import com.jtstand.query.Runner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StarterCommonDialog extends JDialog implements StarterInterface {

    public static final long serialVersionUID = 20081114L;
    public static final String STR_STARTER_PANEL = "STARTER_PANEL";
    public static final String STR_DEBUG_ENABLED = "DEBUG_ENABLED";
    public static final String STR_SERIAL_NUMBER_CRITERIA = "SERIAL_NUMBER_CRITERIA";
    public static final String STR_SN_TO_TEST_TYPE = "SN_TO_TEST_TYPE";
    public static final Class<?>[] emptyContructor = {};
    private List<TestTypeReference> testTypeReferences;
    private TestTypeReference selectedTestType;
    private String employeeNumber;
    private TestFixture testFixture;
    private TestStation testStation;
    private TestProject testProject;
    private AbstractStarterPanel starterPanel;
    private FrameInterface fi;
    private Fixture fixture;
    private AdvancedStartPanel advancedStartPanel;
    private boolean debug;
    private TestSequenceInstance tsi;
    private String sn;
    private Frame parentFrame;
//    private transient Binding binding;
    private Dimension initialSize;
    private PropertiesInterface properties = new StarterProperties(this);

    public StarterCommonDialog(Frame parentFrame, boolean modal, String employeeNumber, TestFixture testFixture, TestStation testStation, TestProject testProject, FrameInterface fi, Fixture fixture) {
        super(parentFrame, modal);

        if (testProject == null) {
            throw new IllegalArgumentException("testProject cannot be null!");
        }
        this.parentFrame = parentFrame;
        this.employeeNumber = employeeNumber;
        this.testFixture = testFixture;
        this.testStation = testStation;
        this.testProject = testProject;
        this.fi = fi;
        this.fixture = fixture;

        if (testFixture != null && testFixture.getTestTypes().size() > 0) {
            testTypeReferences = testFixture.getTestTypes();
        } else {
            if (testStation != null && testStation.getTestTypes().size() > 0) {
                testTypeReferences = testStation.getTestTypes();
            }
        }
        if (testTypeReferences == null || testTypeReferences.size() == 0) {
            throw new IllegalArgumentException("Products list is empty");
        }
        selectedTestType = testTypeReferences.get(0);
        setLayout(new BorderLayout(5, 5));

        addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent evt) {
                formWindowGainedFocus(evt);
            }

            @Override
            public void windowLostFocus(WindowEvent evt) {
            }
        });
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        String t = "New Sequence";
        if (testStation != null) {
            t += " on " + testStation.getHostName();
            if (testFixture != null) {
                t += "@" + testFixture.getFixtureName();
            }
        }
//        if (employeeNumber != null) {
//            t += " by " + employeeNumber;
//        }
        setTitle(t);
        initComponents();
    }

    private String getStarterPanelClassName() {
        return properties.getPropertyString(STR_STARTER_PANEL, StarterCommonPanel.class.getCanonicalName());
    }

    private boolean isDebugEnabled() {
        return properties.getPropertyBoolean(STR_DEBUG_ENABLED, false);
    }

    public static void initPartNumbers(AbstractStarterPanel starter, List<TestTypeReference> testTypeReferences, String selectedPartNumber) {
//        JTextField jTextFieldSN = starter.jTextFieldSN();
        JComboBox jComboBoxPartNumber = starter.jComboBoxPartNumber();
        if (jComboBoxPartNumber.getItemCount() > 0) {
            jComboBoxPartNumber.removeAllItems();
        }
        if (testTypeReferences == null || testTypeReferences.size() == 0) {
            return;
        }
        List<String> partNumbers = new ArrayList<String>();
        for (TestTypeReference product : testTypeReferences) {
            String partNumber = product.getPartNumber();
            if (!partNumbers.contains(partNumber)) {
                if (testTypeReferences.get(0).getTestStationReally().getTestProject().isSerialNumberOK(starter.jTextFieldSN().getText(), partNumber, null, null, testTypeReferences)) {
                    partNumbers.add(partNumber);
                }
            }
        }
        for (String pn : partNumbers) {
            jComboBoxPartNumber.addItem(pn);
            if (pn.equals(selectedPartNumber)) {
                jComboBoxPartNumber.getModel().setSelectedItem(pn);
            }
        }
    }

    public static void initPartRevs(AbstractStarterPanel starter, List<TestTypeReference> testTypeReferences, String selectedPartNumber, String selectedPartRev) {
        JComboBox jComboBoxPartRev = starter.jComboBoxPartRev();
        if (jComboBoxPartRev.getItemCount() > 0) {
            jComboBoxPartRev.removeAllItems();
        }
        if (testTypeReferences == null || testTypeReferences.size() == 0) {
            return;
        }
        List<String> partNumberRevisions = new ArrayList<String>();
        for (TestTypeReference product : testTypeReferences) {
            if (product.getPartNumber().equals(selectedPartNumber)) {
                if (testTypeReferences.get(0).getTestStationReally().getTestProject().isSerialNumberOK(starter.jTextFieldSN().getText(), selectedPartNumber, product.getPartRevision(), null, testTypeReferences)) {
                    partNumberRevisions.add(product.getPartRevision());
                }
            }
        }
        for (String pnr : partNumberRevisions) {
            jComboBoxPartRev.addItem(pnr);
            if (pnr.equals(selectedPartRev)) {
                jComboBoxPartRev.getModel().setSelectedItem(pnr);
            }
        }
    }

    public static void initTestTypes(AbstractStarterPanel starter, List<TestTypeReference> testTypeReferences, String selectedPartNumber, String selectedPartRev, String selectedTestType) {
        JComboBox jComboBoxTestType = starter.jComboBoxTestType();
        if (jComboBoxTestType.getItemCount() > 0) {
            jComboBoxTestType.removeAllItems();
        }
        if (testTypeReferences == null || testTypeReferences.size() == 0) {
            return;
        }
        List<String> testTypes = new ArrayList<String>();
        for (TestTypeReference testTypeReference : testTypeReferences) {
            if (testTypeReference.getPartNumber().equals(selectedPartNumber) && testTypeReference.getPartRevision().equals(selectedPartRev)) {
                if (testTypeReferences.get(0).getTestStationReally().getTestProject().isSerialNumberOK(starter.jTextFieldSN().getText(), selectedPartNumber, selectedPartRev, testTypeReference.getName(), testTypeReferences)) {
                    testTypes.add(testTypeReference.getName());
                }
            }
        }
        for (String tt : testTypes) {
            jComboBoxTestType.addItem(tt);
            if (tt.equals(selectedTestType)) {
                jComboBoxTestType.getModel().setSelectedItem(tt);
            }
        }
    }

    public static void init(AbstractStarterPanel starter, List<TestTypeReference> testTypeReferences, TestTypeReference selectedTestType) {
        initPartNumbers(starter, testTypeReferences, selectedTestType == null ? null : selectedTestType.getPartNumber());
        String partNumber = starter.jComboBoxPartNumber().getSelectedItem() == null ? null : starter.jComboBoxPartNumber().getSelectedItem().toString();
        if (partNumber != null) {
            initPartRevs(starter, testTypeReferences, partNumber, selectedTestType == null ? null : selectedTestType.getPartRevision());
            String partRevision = starter.jComboBoxPartRev().getSelectedItem() == null ? null : starter.jComboBoxPartRev().getSelectedItem().toString();
            initTestTypes(starter, testTypeReferences, partNumber, partRevision, selectedTestType == null ? null : selectedTestType.getName());
        }
    }

    private void jTextFieldSNKeyReleased(java.awt.event.KeyEvent evt) {
//        System.out.println("KeyReleased. SN: " + starterPanel.jTextFieldSN().getText());
        init(starterPanel, testTypeReferences, selectedTestType);
    }

    private void changeAction() {
        Object pn = starterPanel.jComboBoxPartNumber().getSelectedItem();
        Object rev = starterPanel.jComboBoxPartRev().getSelectedItem();
        if (pn != null && rev != null) {
            try {
                for (TestTypeReference pr : testTypeReferences) {
                    if (pr.getPartNumber().equals(pn) && pr.getPartRevision().equals(rev)) {
                        selectedTestType = pr;
                    }
                }
                String cname = getStarterPanelClassName();
                if (!starterPanel.getClass().getCanonicalName().equals(cname)) {
                    /* starter panel is different for this lately selected product! */
                    initComponents();
                }
                if (!isDebugEnabled()) {
                    hideAdvancedPanel();
                }
                if (starterPanel.jButtonDebug() != null) {
                    starterPanel.jButtonDebug().setEnabled(isDebugEnabled());
                }
                initTestTypes(starterPanel, testTypeReferences, pn.toString(), rev.toString(), null);
            } catch (Exception ex) {
                System.err.println("Exception while changin test type:" + ex.getMessage());
            }
        }
    }

    public void recognizeSN(String sn, javax.swing.JComboBox jComboBoxPartNumber, javax.swing.JComboBox jComboBoxPartRev) {
        String sn2tt = properties.getPropertyString(STR_SN_TO_TEST_TYPE, null);
        if (sn2tt == null) {
            return;
        }
        StringTokenizer st = new StringTokenizer(sn2tt, ";");
        while (st.hasMoreTokens()) {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), ":");
            if (st2.hasMoreTokens()) {
                String snPattern = st2.nextToken();
                //System.out.println("Pattern:"+snPattern);
                if (st2.hasMoreTokens()) {
                    StringTokenizer st3 = new StringTokenizer(st2.nextToken(), "@");
                    if (st3.hasMoreTokens()) {
                        String pn = st3.nextToken();
                        if (st3.hasMoreTokens()) {
                            String rev = st3.nextToken();
//                            System.out.println("Checking pattern: " + snPattern + " Test type: " + pn + " Rev: " + rev);
                            if (isMatch(sn, snPattern)) {
//                                System.out.println(sn + " matches with " + snPattern);
                                for (int i = 0; i < jComboBoxPartNumber.getItemCount(); i++) {
                                    if (jComboBoxPartNumber.getItemAt(i).toString().equals(pn)) {
                                        jComboBoxPartNumber.setSelectedIndex(i);
                                    }
                                }
                                for (int i = 0; i < jComboBoxPartRev.getItemCount(); i++) {
                                    if (jComboBoxPartRev.getItemAt(i).toString().equals(rev)) {
                                        jComboBoxPartRev.setSelectedIndex(i);
                                    }
                                }
                                return;
                            }
//                            else {
//                                System.out.println(sn + " does not match with " + snPattern);
//                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean isMatch(String sn, String snCriteria) {
        if (sn.length() != snCriteria.length()) {
            return false;
        }
        for (int i = 0; i < sn.length(); i++) {
            char crit = snCriteria.charAt(i);
            char ch = sn.charAt(i);
            switch (crit) {
                case '#':
                    if (!Character.isDigit(ch)) {
                        return false;
                    }
                    break;
                case '$':
                    if (!Character.isLetter(ch)) {
                        return false;
                    }
                    break;
                case '@':
                    if (!Character.isLetterOrDigit(ch)) {
                        return false;
                    }
                    break;
                default:
                    if (ch != crit) {
                        return false;
                    }
            }
        }
        return true;
    }

    private boolean startWithCheck() {
//        Product product = testProject.getProduct(selectedTestType);
//        if (product == null) {
//            log("Product does not exist in this project:" + selectedTestType.getPartNumber() + "@" + selectedTestType.getPartRevision());
//            return false;
//        }
//        Object selectedTestType = starterPanel.jComboBoxTestType().getSelectedItem();
//        if (selectedTestType == null) {
//            log("Test type is not selected");
//            return false;
//        }
        if (selectedTestType == null) {
            return false;
        }
        sn = starterPanel.jTextFieldSN().getText().toUpperCase().trim();
        starterPanel.jTextFieldSN().setText(sn);
        recognizeSN(sn, starterPanel.jComboBoxPartNumber(), starterPanel.jComboBoxPartRev());

        //check serial number format if mathes to requirements of the test type
        if (!isSerialNumberOK()) {
            starterPanel.jTextFieldSN().requestFocus();
            return false;
        }
//        if (employeeNumber == null || employeeNumber.length() == 0) {
//            Login login = new Login(parentFrame, true, testProject.getAuthentication());
//            employeeNumber = testProject.getAuthentication().getOperator();
//        }
//        if (employeeNumber == null || employeeNumber.length() == 0) {
//            return false;
//        }

        try {
            if (fi != null && !fi.isMemoryEnoughRetry()) {
                throw new IllegalStateException("Not enough memory to start a new sequence");
            }
            tsi = new TestSequenceInstance(sn, employeeNumber, selectedTestType);
            if (fi != null) {
                fi.add(tsi);
            }
        } catch (Exception ex) {
            Logger.getLogger(StarterCommonDialog.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        if (debug) {
            tsi.setStatus(TestSequenceInstance.SequenceStatus.STEPBYSTEP);
            if (fixture != null) {
                fixture.setTestSequenceInstance(tsi);
                fixture.setState(Fixture.State.STEPBYSTEP);
            }
        } else {
            runner.execute(tsi);
        }
        return true;
    }

    public void log(String message) {
        System.out.println(message);
    }

    private void initComponents() {
        hideAdvancedPanel();
        if (starterPanel != null) {
            remove(starterPanel);
        }
        try {
            starterPanel = (AbstractStarterPanel) Class.forName(getStarterPanelClassName()).getConstructor(emptyContructor).newInstance();
        } catch (Exception ex) {
            Logger.getLogger(StarterCommonDialog.class.getName()).log(Level.SEVERE, null, ex);
            starterPanel = new StarterCommonPanel();
        }

        add(starterPanel, BorderLayout.CENTER);
        if (starterPanel.jComboBoxPartNumber().getActionListeners().length == 0) {
            starterPanel.jComboBoxPartNumber().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    jComboBoxPartNumberActionPerformed(evt);
                }
            });
        }

        if (starterPanel.jComboBoxPartRev().getActionListeners().length == 0) {
            starterPanel.jComboBoxPartRev().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    jComboBoxPartRevActionPerformed(evt);
                }
            });
        }

        if (starterPanel.jTextFieldSN().getActionListeners().length == 0) {
            starterPanel.jTextFieldSN().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    jTextFieldSNActionPerformed(evt);
                }
            });
        }

        if (starterPanel.jTextFieldSN().getKeyListeners().length == 0) {
            starterPanel.jTextFieldSN().addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTextFieldSNKeyReleased(evt);
                }
            });
        }
        if (starterPanel.jButtonStart().getActionListeners().length == 0) {
            starterPanel.jButtonStart().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    jButtonStartActionPerformed(evt);
                }
            });
        }

        /* Some start panels may not provide a debug button! */
        if (starterPanel.jButtonDebug() != null) {
            starterPanel.jButtonDebug().setEnabled(isDebugEnabled());
            if (starterPanel.jButtonDebug().getActionListeners().length == 0) {
                starterPanel.jButtonDebug().addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        jButtonDebugActionPerformed(evt);
                    }
                });
            }
        }
        if (starterPanel.jButtonCancel().getActionListeners().length == 0) {
            starterPanel.jButtonCancel().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    jButtonCancelActionPerformed(evt);
                }
            });
        }

        init(starterPanel, testTypeReferences, selectedTestType);
//        initPartNumbers(starterPanel, testTypeReferences, selectedTestType.getPartNumber());
//        String name = starterPanel.jComboBoxPartNumber().getSelectedItem().toString();
//        if (name != null) {
//            initPartRevs(starterPanel, testTypeReferences, name, selectedTestType.getPartRevision());
//            initTestTypes(starterPanel, testTypeReferences, name, selectedTestType.getPartRevision(), null);
//        }
        pack();
        initialSize = getSize();
        setMinimumSize(initialSize);
        Util.centerOnParent(this);
        setVisible(true);
        requestFocus();
    }

    private void jComboBoxPartRevActionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                changeAction();
            }
        });
    }

    private void jComboBoxPartNumberActionPerformed(ActionEvent evt) {
        Object name = starterPanel.jComboBoxPartNumber().getSelectedItem();
        if (name == null) {
            return;
        }
        initPartRevs(starterPanel, testTypeReferences, name.toString(), null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                changeAction();


            }
        });
    }
    private Runner runner;

    private void jButtonStartActionPerformed(ActionEvent evt) {
        runner = fixture.getNewRunner();
        if ((evt.getModifiers() & (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK)) == (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK)) {
            if (!isDebugEnabled() || advancedStartPanel != null) {
                return;
            }
            runner.setMinTotal(100);
        } else if ((evt.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) {
            if (isDebugEnabled()) {
                toggleAdvancedPanel();
            } else {
                hideAdvancedPanel();
            }
            return;
        }
        if (advancedStartPanel != null) {
            advancedStartPanel.setupRunner(runner);
        }
        debug = false;
        if (startWithCheck()) {
            dispose();
        }
    }

    private void jButtonStepByStepActionPerformed(ActionEvent evt) {
        debug = true;
        if (startWithCheck()) {
            dispose();
        }
    }

    private void jButtonDebugActionPerformed(ActionEvent evt) {
        toggleAdvancedPanel();
    }

    private void toggleAdvancedPanel() {
        if (advancedStartPanel != null) {
            hideAdvancedPanel();
        } else {
            showAdvancedPanel();
        }
    }

    private void showAdvancedPanel() {
        if (advancedStartPanel == null) {
            advancedStartPanel = new AdvancedStartPanel(starterPanel);
            advancedStartPanel.jButtonStepByStep().addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    jButtonStepByStepActionPerformed(evt);
                }
            });

            add(advancedStartPanel, BorderLayout.EAST);
            pack();
            setMinimumSize(getSize());
            advancedStartPanel.requestFocus();
            Util.centerOnParent(this);
        }
    }

    private void hideAdvancedPanel() {
        if (advancedStartPanel != null) {
            remove(advancedStartPanel);
            advancedStartPanel = null;

            setMinimumSize(initialSize);
            setSize(initialSize);

            pack();
            //center();
            starterPanel.jButtonStart().setText("Start Sequence");
            starterPanel.jTextFieldSN().requestFocus();
        }
    }

    private void jTextFieldSNActionPerformed(ActionEvent evt) {
        starterPanel.jButtonStart().doClick();
    }

    private void formWindowGainedFocus(WindowEvent evt) {
        Util.centerOnParent(this);
        starterPanel.jTextFieldSN().requestFocus();
    }

    private void jButtonCancelActionPerformed(ActionEvent evt) {
        this.dispose();
    }

    public boolean isSerialNumberOK() {
        if (sn == null || sn.length() == 0) {
            log("Serial Number cannot be blank!");
            return false;
        }
        String snCriteria = properties.getPropertyString(STR_SERIAL_NUMBER_CRITERIA, null);
        if (snCriteria == null) {
            return true;
        }
        StringTokenizer st = new StringTokenizer(snCriteria, ";");
        if (st.countTokens() > 1) {
            while (st.hasMoreTokens()) {
                if (isMatch(sn, st.nextToken())) {
                    return true;
                }
            }
            log("Serial Number does not match criteria:" + snCriteria);
            return false;
        }
        if (sn.length() != snCriteria.length()) {
            log("Serial Number does not match criteria:" + snCriteria);
            log("Length should be:" + snCriteria.length());
            return false;
        }
        for (int i = 0; i < sn.length(); i++) {
            char crit = snCriteria.charAt(i);
            char ch = sn.charAt(i);
            switch (crit) {
                case '#':
                    if (!Character.isDigit(ch)) {
                        log("Serial Number does not match criteria:" + snCriteria);
                        log("At position " + (i + 1) + " there should be a digit!");
                        return false;
                    }
                    break;
                case '$':
                    if (!Character.isLetter(ch)) {
                        log("Serial Number does not match criteria:" + snCriteria);
                        log("At position " + (i + 1) + " there should be a letter!");
                        return false;
                    }
                    break;
                case '@':
                    if (!Character.isLetterOrDigit(ch)) {
                        log("Serial Number does not match criteria:" + snCriteria);
                        log("At position " + (i + 1) + " there should be a letter or digit!");
                        return false;
                    }
                    break;
                default:
                    if (ch != crit) {
                        log("Serial Number does not match criteria:" + snCriteria);
                        log("At position " + (i + 1) + " there should be a '" + crit + "'!");
                        return false;
                    }
            }
        }
        return true;
    }

    @Override
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    @Override
    public TestStation getTestStation() {
        return testStation;
    }

    @Override
    public TestFixture getTestFixture() {
        return testFixture;
    }

    @Override
    public TestTypeReference getTestType() {
        return selectedTestType;
    }

    @Override
    public TestProject getTestProject() {
        return testProject;
    }
}
