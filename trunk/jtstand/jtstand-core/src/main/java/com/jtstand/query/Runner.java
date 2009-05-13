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
package com.jtstand.query;

import com.jtstand.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class Runner extends Thread {

    private int numPassed = 0;
    private int numFailed = 0;
    private int minTotal = 0;
    private int minPassed = 0;
    private int minFailed = 0;
    private int maxTotal = 0;
    private int maxPassed = 0;
    private int maxFailed = 0;
    private boolean skipManualSteps = false;
    private TestSequenceInstance seq;
    private FrameInterface fi;
//    private PropertyChangeListener pcl;
    private FixtureInterface fixture = null;
    private boolean aborted;
    private String serialNumber;
    private String employeeNumber;
    private TestFixture testFixture;
    private TestStation testStation;
    private TestProject testProject;
    private TestType testType;
    private TestSequence testSequence;
//    private String testTypeName;
//    private String productName;
    private TestSequenceInstance.SequenceType sequenceType;
//    private long previouslyUsedMemory = 0L;

    public Runner(FrameInterface fi, FixtureInterface fixture) {
        super();
        this.fi = fi;
        this.fixture = fixture;
    }

    public void execute(TestSequenceInstance seq) {
        this.seq = seq;
        serialNumber = seq.getSerialNumber();
        employeeNumber = seq.getEmployeeNumber();
        testType = seq.getTestType();
        testFixture = seq.getTestFixture();
        testStation = seq.getTestStation();
        testProject = seq.getTestProject();
        testSequence = seq.getTestSequence();
//        testTypeName = seq.getTestTypeName();
//        productName = seq.getProductName();
        sequenceType = seq.getSequenceType();
        this.start();
    }

    public void setMinTotal(int minTotal) {
        this.minTotal = minTotal;
    }

    public void setMinPassed(int minPassed) {
        this.minPassed = minPassed;
    }

    public void setMinFailed(int minFailed) {
        this.minFailed = minFailed;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxPassed(int maxPassed) {
        this.maxPassed = maxPassed;
    }

    public void setMaxFailed(int maxFailed) {
        this.maxFailed = maxFailed;
    }

    public void setSkipManualSteps(boolean skipManualSteps) {
        this.skipManualSteps = skipManualSteps;
    }

    public void abort() {
        aborted = true;
    }

    private boolean more() {
        if (aborted) {
            return false;
        }
        if (maxTotal > 0 && (numPassed + numFailed) >= maxTotal) {
            return false;
        }
        if (maxPassed > 0 && numPassed >= maxPassed) {
            return false;
        }
        if (maxFailed > 0 && numFailed >= maxFailed) {
            return false;
        }
        if (minTotal > 0 && (numPassed + numFailed) < minTotal) {
            return true;
        }
        if (minPassed > 0 && numPassed < minPassed) {
            return true;
        }
        if (minFailed > 0 && numFailed < minFailed) {
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (run1()) {
        }
        if (fixture != null) {
            fixture.sequenceStatusChanged(seq.getStatus());
        }
        seq = null;
    }

    public void retest() {
        try {
            System.out.println("retesting...");
            getSequence();
            run1();
            if (fixture != null) {
                fixture.sequenceStatusChanged(seq.getStatus());
            }
            seq = null;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.SEVERE, "Cannot retest", ex);
        }
    }

    private void getSequence() throws Exception {
        if (fi != null && !fi.isMemoryEnoughRetry()) {
            throw new IllegalStateException("Not enough memory to start a new sequence");
        }
        seq = new TestSequenceInstance(sequenceType, serialNumber, employeeNumber, testSequence, testType, testFixture, testStation, testProject);
        if (fi != null) {
            fi.add(seq);
        }
    }

    private boolean run1() {
        //Log.log("Sequence with sn:'" + seq.getSerialNumber() + "' starting...");
        //Log.log("Free Memory: " + Runtime.getRuntime().freeMemory());
        if (fixture != null) {
            fixture.setTestSequenceInstance(seq);
        }
        Thread t = new Thread(seq);
        t.start();
        if (fixture != null) {
            fixture.sequenceStatusChanged(TestSequenceInstance.SequenceStatus.RUNNING);//.setState(Fixture.State.RUNNING);
        }
        try {
            t.join();
            if (seq.getStatus().equals(TestSequenceInstance.SequenceStatus.PASSED)) {
                if (fixture != null) {
                    fixture.pass();
                }
                numPassed++;
            } else if (seq.getStatus().equals(TestSequenceInstance.SequenceStatus.FAILED)) {
                if (fixture != null) {
                    fixture.fail();
                }
                numFailed++;
            } else {
                return false;
            }
        } catch (InterruptedException ex) {
            return false;
        }
        if (more()) {
            try {
                getSequence();
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }
}
