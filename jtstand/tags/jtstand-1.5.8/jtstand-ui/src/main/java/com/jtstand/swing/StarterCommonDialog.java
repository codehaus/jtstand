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

import com.jtstand.Authentication;
import com.jtstand.FixtureTestTypeReference;
import com.jtstand.TestFixture;
import com.jtstand.TestProject;
import com.jtstand.TestSequenceInstance;
import com.jtstand.TestSequenceInstance.SequenceType;
import com.jtstand.TestStation;
import com.jtstand.TestTypeReference;
import com.jtstand.query.Runner;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jboss.logging.Logger;

public class StarterCommonDialog extends JDialog implements StarterInterface {

    private static final Logger log = Logger.getLogger(StarterCommonDialog.class.getName());
    public static final long serialVersionUID = 20081114L;
    public static final String STARTER_PANEL = "STARTER_PANEL";
    public static final String DEBUG_ENABLED = "DEBUG_ENABLED";
    public static final String ID_ENABLED = "ID_ENABLED";
    public static final String SERIAL_NUMBER_PATTERN = "SERIAL_NUMBER_PATTERN";
    public static final String STR_SN_TO_TEST_TYPE = "SN_TO_TEST_TYPE";
    public static final Class<?>[] emptyContructor = {};
    List<FixtureTestTypeReference> testTypeReferences;
    FixtureTestTypeReference selectedTestType;
    AbstractStarterPanel starterPanel;
    AdvancedStartPanel advancedStartPanel;
    boolean debug;
    TestSequenceInstance tsi;
//    String sn;
//    Frame parentFrame;
//    private transient Binding binding;
    Dimension initialSize;
    StarterProperties properties = new StarterProperties(this);
    Fixture fixture;

    public StarterCommonDialog(Fixture fixture) {
        super((JFrame) SwingUtilities.getWindowAncestor(fixture), false);
        this.fixture = fixture;
//        if (testProject == null) {
//            throw new IllegalArgumentException("testProject cannot be null!");
//        }
//        this.fixture = fixture;
//        testFixture.getTestStation().getTestProject().getAuthentication().getOperator();

        //        if (testFixture != null && testFixture.getTestTypes().size() > 0) {
//            testTypeReferences = testFixture.getTestTypes();
//        } else {
//            if (testStation != null && testStation.getTestTypes().size() > 0) {
//                testTypeReferences = testStation.getTestTypes();
//            }
//        }

        testTypeReferences = fixture.getTestFixture().getTestTypes();
        if (testTypeReferences == null || testTypeReferences.isEmpty()) {
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

//        String t = "New Sequence";
//        if (testStation != null) {
////            t += " on " + testStation.getHostName();
//            if (testFixture != null) {
//                t += " on " + testFixture.getFixtureName();
//            }
//        }
////        if (employeeNumber != null) {
////            t += " by " + employeeNumber;
////        }
        setTitle("New Sequence on " + fixture.getTestFixture().getFixtureName());
        initComponents();
    }

    private String getStarterPanelClassName() {
        return properties.getPropertyString(STARTER_PANEL, StarterCommonPanel.class.getCanonicalName());
    }

    private boolean isDebugEnabled() {
        return properties.getPropertyBoolean(DEBUG_ENABLED, false);
    }

    private boolean isIdEnabled() {
        return properties.getPropertyBoolean(ID_ENABLED, true);
    }

    public static void initPartNumbers(AbstractStarterPanel starter, List<FixtureTestTypeReference> testTypeReferences, String selectedPartNumber) {
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
                if (testTypeReferences.get(0).getTestFixture().getTestStation().getTestProject().isSerialNumberOK(starter.jTextFieldSN().getText(), partNumber, null, null, testTypeReferences)) {
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

    public static void initPartRevs(AbstractStarterPanel starter, List<FixtureTestTypeReference> testTypeReferences, String selectedPartNumber, String selectedPartRev) {
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
                if (testTypeReferences.get(0).getTestFixture().getTestStation().getTestProject().isSerialNumberOK(starter.jTextFieldSN().getText(), selectedPartNumber, product.getPartRevision(), null, testTypeReferences)) {
                    String partRevision = product.getPartRevision();
                    if (!partNumberRevisions.contains(partRevision)) {
                        partNumberRevisions.add(partRevision);
                    }
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

    public static void initTestTypes(AbstractStarterPanel starter, List<FixtureTestTypeReference> testTypeReferences, String selectedPartNumber, String selectedPartRev, String selectedTestType) {
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
                if (testTypeReferences.get(0).getTestFixture().getTestStation().getTestProject().isSerialNumberOK(starter.jTextFieldSN().getText(), selectedPartNumber, selectedPartRev, testTypeReference.getName(), testTypeReferences)) {
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

    public static void init(AbstractStarterPanel starter, List<FixtureTestTypeReference> testTypeReferences, TestTypeReference selectedTestType) {
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

    private void setSelectedTestType(FixtureTestTypeReference selectedTestType) {
        if (this.selectedTestType != selectedTestType) {
            log.trace("selected a new test type: " + selectedTestType);
            this.selectedTestType = selectedTestType;
            String cname = getStarterPanelClassName();
            if (!starterPanel.getClass().getCanonicalName().equals(cname)) {
                /* starter panel is different for this lately selected product! */
                initComponents();
            }
            if (!isDebugEnabled()) {
                hideAdvancedPanel();
            }
            if (starterPanel.jButtonDebug() != null) {
                //System.out.println("Setting debug button enabled: " + isDebugEnabled());
                starterPanel.jButtonDebug().setEnabled(isDebugEnabled());
            }
            init(starterPanel, testTypeReferences, selectedTestType);
            //initTestTypes(starterPanel, testTypeReferences, selectedTestType.getPartNumber(), selectedTestType.getPartNumber(), selectedTestType.getName());
        }
    }

    private void changeAction() {
        Object pn = starterPanel.jComboBoxPartNumber().getSelectedItem();
        Object rev = starterPanel.jComboBoxPartRev().getSelectedItem();
        Object type = starterPanel.jComboBoxTestType().getSelectedItem();
        if (pn != null && rev != null && type != null) {
            try {
                for (FixtureTestTypeReference pr : testTypeReferences) {
                    if (pr.getPartNumber().equals(pn) && pr.getPartRevision().equals(rev) && pr.getName().equals(type)) {
                        setSelectedTestType(pr);
                        return;
                    }
                }
                for (FixtureTestTypeReference pr : testTypeReferences) {
                    if (pr.getPartNumber().equals(pn) && pr.getPartRevision().equals(rev)) {
                        setSelectedTestType(pr);
                        return;
                    }
                }
                for (FixtureTestTypeReference pr : testTypeReferences) {
                    if (pr.getPartNumber().equals(pn) && pr.getName().equals(type)) {
                        setSelectedTestType(pr);
                        return;
                    }
                }
                for (FixtureTestTypeReference pr : testTypeReferences) {
                    if (pr.getPartNumber().equals(pn)) {
                        setSelectedTestType(pr);
                        return;
                    }
                }
            } catch (Exception ex) {
                log.error("Exception while changin test type" + ex);
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

    public static boolean isMatch(String stringValue, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(stringValue);
        m.find();
        return m.matches() && m.group().equals(stringValue);
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
        String sn = starterPanel.jTextFieldSN().getText().toUpperCase().trim();
        starterPanel.jTextFieldSN().setText(sn);
        recognizeSN(sn, starterPanel.jComboBoxPartNumber(), starterPanel.jComboBoxPartRev());

        //check serial number format if mathes to requirements of the test type
        if (isIdEnabled()) {
            if (!isSerialNumberOK(sn)) {
                starterPanel.jTextFieldSN().requestFocus();
                return false;
            }
        }
        String employeeNumber = getEmployeeNumber();

        if (employeeNumber == null || employeeNumber.length() == 0) {
            Login login = new Login((JFrame) SwingUtilities.getWindowAncestor(fixture), true, getAuthentication());
            employeeNumber = getEmployeeNumber();
        }
        if (employeeNumber == null || employeeNumber.length() == 0) {
            return false;
        }

        try {
            MainFrame fi = fixture.getMainFrame();
            if (fi != null && !fi.isMemoryEnoughRetry()) {
                throw new IllegalStateException("Not enough memory to start a new sequence");
            }
            log.trace("Creating instance of: " + selectedTestType);
            tsi = new TestSequenceInstance(SequenceType.NORMAL, sn, employeeNumber, selectedTestType);
            if (fi != null) {
                fi.add(tsi);
            }
        } catch (Exception ex) {
            log.error("Exception", ex);
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

    private void initComponents() {
        hideAdvancedPanel();
        if (starterPanel != null) {
            remove(starterPanel);
        }
        try {
            starterPanel = (AbstractStarterPanel) Class.forName(getStarterPanelClassName()).getConstructor(emptyContructor).newInstance();
        } catch (Exception ex) {
            log.error("Exception", ex);
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

        if (starterPanel.jComboBoxTestType().getActionListeners().length == 0) {
            starterPanel.jComboBoxTestType().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    jComboBoxTestTypeActionPerformed(evt);
                }
            });
        }

        starterPanel.jTextFieldSN().setEnabled(isIdEnabled());

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

    private void jComboBoxTestTypeActionPerformed(ActionEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                changeAction();
            }
        });
    }

    private void jComboBoxPartNumberActionPerformed(ActionEvent evt) {
//        Object name = starterPanel.jComboBoxPartNumber().getSelectedItem();
//        if (name == null) {
//            return;
//        }
//        initPartRevs(starterPanel, testTypeReferences, name.toString(), null);
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

    public boolean isSerialNumberOK(String serialNumber) {
        if (serialNumber == null || serialNumber.length() == 0) {
            log.warn("Serial Number cannot be blank!");
            return false;
        }
        String serialNumberPattern = properties.getPropertyString(SERIAL_NUMBER_PATTERN, null);
        if (serialNumberPattern == null) {
            return true;
        }
        return isMatch(serialNumber, serialNumberPattern);
    }

    public Authentication getAuthentication() {
        return getTestProject().getAuthentication();
    }

    @Override
    public String getEmployeeNumber() {
        return getAuthentication().getOperator();
    }

    @Override
    public TestStation getTestStation() {
        return getTestFixture().getTestStation();
    }

    @Override
    public TestFixture getTestFixture() {
        return fixture.getTestFixture();
    }

    @Override
    public FixtureTestTypeReference getTestType() {
        return selectedTestType;
    }

    @Override
    public TestProject getTestProject() {
        return getTestStation().getTestProject();
    }
}
