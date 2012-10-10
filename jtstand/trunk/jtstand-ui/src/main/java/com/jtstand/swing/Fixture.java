/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Fixture.java is part of JTStand.
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

import com.jtstand.FixtureInitTestTypeReference;
import com.jtstand.TestFixture;
import com.jtstand.TestSequenceInstance;
import com.jtstand.query.FixtureInterface;
import com.jtstand.query.Runner;
import com.jtstand.statistics.Yield;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.jboss.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class Fixture extends javax.swing.JPanel implements FixtureInterface, PropertyChangeListener {

    private static final Logger log = Logger.getLogger(Fixture.class.getName());
    public static final long serialVersionUID = 20081114L;
    public static final String STR_PASSED_RETEST = "PASSED_RETEST";
    public static final String STR_FAILED_RETEST = "FAILED_RETEST";
    public static final String STR_ABORTED_RETEST = "ABORTED_RETEST";
    public static final String STR_AUTO = "auto";
    public static final Color runningColor = new Color(51, 153, 255);
    public static final Color stepbystepColor = new Color(204, 255, 255);
    public static final Color abortedColor = MainFrame.fDarkBrown;
    public static final Color failedColor = Color.red;
    public static final Color passedColor = MainFrame.fDarkGreen;
    public static final Color stepvystepColor = Color.red;
    public static final Color interactiveColor = Color.yellow;
    public static final Color defaultColor = (Color) javax.swing.UIManager.get("JPanel.background");

    public static enum State {

        DISABLED("Disabled"),
        READY("Ready"),
        RUNNING("Running"),
        PASSED("Passed"),
        FAILED("Failed"),
        ABORTED("Aborted"),
        STEPBYSTEP("Step by step"),
        INTERACTIVE("Interactive");
        public final String name;

        State(String name) {
            this.name = name;
        }
    }
    private final Object stateLock = new Object();
    private State state = null;
//    private Component message = null;
    private Yield y = new Yield();
    private TestFixture testFixture;
    private MainFrame fi;
    private TestSequenceInstance testSequenceInstance = null;
    private final Object testSequenceInstanceLock = new Object();
    private Runner runner;

    /**
     * Creates a new instance of Fixture
     */
    public Fixture(TestFixture testFixture, MainFrame fi) {
        super();
        this.testFixture = testFixture;
        this.fi = fi;
        initComponents();
        setName(testFixture.getFixtureName());
        log.trace("New Fixture created: " + getName());
    }

    public MainFrame getMainFrame() {
        return fi;
    }

    @Override
    public void init() {
        log.trace(getTestFixture().getFixtureName() + " init...");
        if (testFixture.isDisabled() != null && testFixture.isDisabled()) {
            setState(State.DISABLED);
        } else {
            FixtureInitTestTypeReference initTypeReference = testFixture.getInitTypeReference();
            if (initTypeReference != null) {
                try {
//                    testFixture.initializeProperties();
                    TestSequenceInstance seq = new TestSequenceInstance(TestSequenceInstance.SequenceType.FIXTURE_SETUP, testFixture.getSerialNumber(), STR_AUTO, initTypeReference);
                    if (fi != null) {
                        fi.add(seq);
                    }
                    getNewRunner().execute(seq);
                } catch (Exception ex) {
                    setState(State.DISABLED);
                    log.error("Failed to initialize fixture: " + getName(), ex);
                }
            } else {
                setState(Fixture.State.READY);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == testSequenceInstance) {
            if (evt.getNewValue() instanceof TestSequenceInstance.SequenceStatus) {
                sequenceStatusChanged((TestSequenceInstance.SequenceStatus) evt.getNewValue());
            }
        }
    }

    public TestSequenceInstance getTestSequenceInstance() {
        return testSequenceInstance;
    }

    @Override
    public void setTestSequenceInstance(TestSequenceInstance testSequenceInstance) {
        synchronized (testSequenceInstanceLock) {
            this.testSequenceInstance = testSequenceInstance;
            testSequenceInstance.addPropertyChangeListener(this);
        }
    }

    public void replace(TestSequenceInstance testSequenceInstance) {
        synchronized (testSequenceInstanceLock) {
            if (testSequenceInstance != null && testSequenceInstance.equals(this.testSequenceInstance)) {
                this.testSequenceInstance = testSequenceInstance;
            }
        }
    }

    public void remove(TestSequenceInstance testSequenceInstance) {
        synchronized (testSequenceInstanceLock) {
            if (testSequenceInstance != null && testSequenceInstance.equals(this.testSequenceInstance)) {
                this.testSequenceInstance = null;
            }
        }
    }

    public void removeAll(Collection<?> seqList) {
        synchronized (testSequenceInstanceLock) {
            if (testSequenceInstance != null && seqList.contains(testSequenceInstance)) {
                this.testSequenceInstance = null;
            }
        }
    }

    public TestFixture getTestFixture() {
        return testFixture;
    }

    public void setState(State state) {
        if (this.state == null || !this.state.equals(state)) {
            this.state = state;
//            Log.log("Setting Fixture State to " + state.name);
            update();
        }
    }

    public String getBorderString() {
        if ((testFixture.isDisabled() != null && testFixture.isDisabled()) || y.getN() == 0) {
            return "Fixture '" + getName() + "' - " + state.name;
        } else {
            return "Fixture '" + getName() + "' - " + state.name + " - " + y;
        }
    }

    public void setBorder() {
        String b = getBorderString();
        if (getBorder() != null && TitledBorder.class.isAssignableFrom(getBorder().getClass())) {
            TitledBorder tb = (TitledBorder) getBorder();
            tb.setTitle(b);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Fixture.this.repaint();
                }
            });

        } else {
            setBorder(javax.swing.BorderFactory.createTitledBorder(b));
        }
    }

    public void setBackground() {
        switch (state) {
            case RUNNING:
                setBackground(runningColor);
                break;
            case PASSED:
                setBackground(passedColor);
                break;
            case FAILED:
                setBackground(failedColor);
                break;
            case ABORTED:
                setBackground(abortedColor);
                break;
            case STEPBYSTEP:
                setBackground(stepbystepColor);
                break;
            case INTERACTIVE:
                setBackground(interactiveColor);
                break;
            default:
                setBackground(defaultColor);
        }
    }

    public void update() {
        boolean initType = testSequenceInstance != null && testSequenceInstance.isInitType();
        switch (state) {
            case READY:
                runner = null;
                ((CardLayout) (getLayout())).show(this, "ready");
                break;
            case RUNNING:
                ((CardLayout) (getLayout())).show(this, "running");
                break;
            case PASSED:
                jButtonPassedRetest.setVisible(testSequenceInstance != null && testSequenceInstance.getPropertyBoolean(STR_PASSED_RETEST, false));
                ((CardLayout) (getLayout())).show(this, "passed");
                break;
            case FAILED:
                jButtonFailed.setVisible(!initType);
                jButtonFailedRetest.setVisible(testSequenceInstance != null && testSequenceInstance.getPropertyBoolean(STR_FAILED_RETEST, initType));
                ((CardLayout) (getLayout())).show(this, "failed");
                break;
            case ABORTED:
                jButtonAborted.setVisible(!initType);
                jButtonAbortedRetest.setVisible(testSequenceInstance != null && testSequenceInstance.getPropertyBoolean(STR_ABORTED_RETEST, initType));
                ((CardLayout) (getLayout())).show(this, "aborted");
                break;
            case STEPBYSTEP:
                ((CardLayout) (getLayout())).show(this, "stepbystep");
                break;

            case INTERACTIVE:
                messageText.setText(
                        (testSequenceInstance == null)
                        ? ""
                        : testSequenceInstance.getInteractionMessage());
                ((CardLayout) (getLayout())).show(this, "interactive");
                log.trace("Fixture '" + getName() + "' is showing interactive");
                break;
        }
        setBackground();
        setBorder();
        revalidate();
    }

    public Runner getNewRunner() {
        runner = new Runner(fi, this);
        return runner;
    }

//    public void interact(String message) {
//        messageText.setText(message);
//
//        update();
//    }
//
//    public void hideMessage() {
//        this.message = null;
//        update();
//    }
    private boolean confirmAbort() {
        return 0 == javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Do you really want to abort?",
                "Confirm",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public void pass() {
        y.pass();
        setBorder();
    }

    @Override
    public void fail() {
        y.fail();
        setBorder();
    }

    @Override
    public void sequenceStatusChanged(TestSequenceInstance.SequenceStatus sequenceStatus) {
//        System.out.println("Sequence status changed to " + sequenceStatus.statusString);
        synchronized (stateLock) {
            if (sequenceStatus != null) {
                switch (sequenceStatus) {
                    case RUNNING:
                        setState(State.RUNNING);
                        break;
                    case PASSED:
                        //y.pass();
                        setState(State.PASSED);
                        break;
                    case FAILED:
                        //y.fail();
                        setState(State.FAILED);
                        break;
                    case ABORTED:
                        setState(State.ABORTED);
                        break;
                    case STEPBYSTEP:
                        setState(State.STEPBYSTEP);
                        break;
                    case INTERACTIVE:
                        setState(State.INTERACTIVE);
                        break;
                    default:
                        setState(State.READY);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelDisabled = new javax.swing.JPanel();
        jPanelReady = new javax.swing.JPanel();
        jButtonReady = new javax.swing.JButton();
        jPanelPassed = new javax.swing.JPanel();
        jButtonPassed = new javax.swing.JButton();
        jButtonPassedRetest = new javax.swing.JButton();
        jPanelFailed = new javax.swing.JPanel();
        jButtonFailed = new javax.swing.JButton();
        jButtonFailedRetest = new javax.swing.JButton();
        jPanelRunning = new javax.swing.JPanel();
        jButtonAbort = new javax.swing.JButton();
        jPanelAborted = new javax.swing.JPanel();
        jButtonAborted = new javax.swing.JButton();
        jButtonAbortedRetest = new javax.swing.JButton();
        jPanelStepByStep = new javax.swing.JPanel();
        jButtonStepByStepFinish = new javax.swing.JButton();
        jPanelInteractive = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        messageText = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButtonPass = new javax.swing.JButton();
        jButtonFail = new javax.swing.JButton();

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new java.awt.CardLayout());
        add(jPanelDisabled, "disabled");

        jButtonReady.setText("New Sequence");
        jButtonReady.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadyActionPerformed(evt);
            }
        });
        jPanelReady.add(jButtonReady);

        add(jPanelReady, "ready");

        jPanelPassed.setBackground(passedColor);

        jButtonPassed.setText("Passed");
        jButtonPassed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPassedActionPerformed(evt);
            }
        });
        jPanelPassed.add(jButtonPassed);

        jButtonPassedRetest.setText("Retest");
        jButtonPassedRetest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPassedRetestActionPerformed(evt);
            }
        });
        jPanelPassed.add(jButtonPassedRetest);

        add(jPanelPassed, "passed");

        jPanelFailed.setBackground(failedColor);

        jButtonFailed.setText("Failed");
        jButtonFailed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFailedActionPerformed(evt);
            }
        });
        jPanelFailed.add(jButtonFailed);

        jButtonFailedRetest.setText("Retest");
        jButtonFailedRetest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFailedRetestActionPerformed(evt);
            }
        });
        jPanelFailed.add(jButtonFailedRetest);

        add(jPanelFailed, "failed");

        jPanelRunning.setBackground(runningColor);

        jButtonAbort.setText("Abort");
        jButtonAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbortActionPerformed(evt);
            }
        });
        jPanelRunning.add(jButtonAbort);

        add(jPanelRunning, "running");

        jPanelAborted.setBackground(abortedColor);

        jButtonAborted.setText("Aborted");
        jButtonAborted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbortedActionPerformed(evt);
            }
        });
        jPanelAborted.add(jButtonAborted);

        jButtonAbortedRetest.setText("Retest");
        jButtonAbortedRetest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbortedRetestActionPerformed(evt);
            }
        });
        jPanelAborted.add(jButtonAbortedRetest);

        add(jPanelAborted, "aborted");

        jPanelStepByStep.setBackground(stepbystepColor);

        jButtonStepByStepFinish.setText("Finish");
        jButtonStepByStepFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStepByStepFinishActionPerformed(evt);
            }
        });
        jPanelStepByStep.add(jButtonStepByStepFinish);

        add(jPanelStepByStep, "stepbystep");

        jPanelInteractive.setBackground(interactiveColor);
        jPanelInteractive.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        messageText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(messageText, java.awt.BorderLayout.CENTER);

        jPanelInteractive.add(jPanel1, java.awt.BorderLayout.CENTER);

        jButtonPass.setText("Pass");
        jButtonPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPassActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPass);

        jButtonFail.setText("Fail");
        jButtonFail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFailActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonFail);

        jPanelInteractive.add(jPanel2, java.awt.BorderLayout.SOUTH);

        add(jPanelInteractive, "interactive");
    }// </editor-fold>//GEN-END:initComponents

private void jButtonReadyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReadyActionPerformed
    if (!state.equals(State.READY)) {
        return;
    }
//new StarterCommonDialog(fi.getFrame(), false, "87377", testFixture, testFixture.getTestStation(), testFixture.getTestStation().getTestProject(), fi, this);
    fi.showStarterDialog(this);
}//GEN-LAST:event_jButtonReadyActionPerformed

private void jButtonPassedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPassedActionPerformed
//    message = null;
    setState(State.READY);
}//GEN-LAST:event_jButtonPassedActionPerformed

private void jButtonFailedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFailedActionPerformed
//    message = null;
    setState(State.READY);
}//GEN-LAST:event_jButtonFailedActionPerformed

private void jButtonAbortedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbortedActionPerformed
//    message = null;
    setState(State.READY);
}//GEN-LAST:event_jButtonAbortedActionPerformed

private void jButtonAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbortActionPerformed
    if (!state.equals(State.RUNNING)) {
        return;
    }
    if (confirmAbort()) {
        TestSequenceInstance uut = getTestSequenceInstance();
        if (uut != null) {
            uut.abort();
        }
        if (runner != null) {
            runner.abort();
        }
    }
}//GEN-LAST:event_jButtonAbortActionPerformed

private void jButtonStepByStepFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStepByStepFinishActionPerformed
    if (!state.equals(State.STEPBYSTEP)) {
        return;
    }
    TestSequenceInstance uut = getTestSequenceInstance();
    if (uut != null) {
        uut.finish();
    }
    setState(State.READY);
}//GEN-LAST:event_jButtonStepByStepFinishActionPerformed

private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
    TestSequenceInstance uut = getTestSequenceInstance();
    if (uut != null) {
        fi.selectSequence(uut);
    }
}//GEN-LAST:event_formMouseClicked

private void jButtonPassedRetestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPassedRetestActionPerformed
    if (runner != null) {
        runner.retest();
    }
}//GEN-LAST:event_jButtonPassedRetestActionPerformed

private void jButtonFailedRetestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFailedRetestActionPerformed
    if (runner != null) {
        runner.retest();
    }
}//GEN-LAST:event_jButtonFailedRetestActionPerformed

private void jButtonAbortedRetestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbortedRetestActionPerformed
    if (runner != null) {
        runner.retest();
    }
}//GEN-LAST:event_jButtonAbortedRetestActionPerformed

private void jButtonPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPassActionPerformed
    messageText.setText("");
    if (!state.equals(State.INTERACTIVE)) {
        return;
    }
    if (testSequenceInstance != null) {
        testSequenceInstance.finishInteraction(true);
    }
}//GEN-LAST:event_jButtonPassActionPerformed

private void jButtonFailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFailActionPerformed
    messageText.setText("");
    if (!state.equals(State.INTERACTIVE)) {
        return;
    }
    if (testSequenceInstance != null) {
        testSequenceInstance.finishInteraction(false);
    }
}//GEN-LAST:event_jButtonFailActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAbort;
    private javax.swing.JButton jButtonAborted;
    private javax.swing.JButton jButtonAbortedRetest;
    private javax.swing.JButton jButtonFail;
    private javax.swing.JButton jButtonFailed;
    private javax.swing.JButton jButtonFailedRetest;
    private javax.swing.JButton jButtonPass;
    private javax.swing.JButton jButtonPassed;
    private javax.swing.JButton jButtonPassedRetest;
    private javax.swing.JButton jButtonReady;
    private javax.swing.JButton jButtonStepByStepFinish;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelAborted;
    private javax.swing.JPanel jPanelDisabled;
    private javax.swing.JPanel jPanelFailed;
    private javax.swing.JPanel jPanelInteractive;
    private javax.swing.JPanel jPanelPassed;
    private javax.swing.JPanel jPanelReady;
    private javax.swing.JPanel jPanelRunning;
    private javax.swing.JPanel jPanelStepByStep;
    private javax.swing.JLabel messageText;
    // End of variables declaration//GEN-END:variables
}
