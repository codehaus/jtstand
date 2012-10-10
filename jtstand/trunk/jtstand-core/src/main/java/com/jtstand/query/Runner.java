/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Runner.java is part of JTStand.
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
package com.jtstand.query;

import com.jtstand.*;
import org.jboss.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class Runner extends Thread {

    private static final Logger log = Logger.getLogger(Runner.class.getName());
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
    private TestStep testSequence;
    private TestSequenceInstance.SequenceType sequenceType;

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
        sequenceType = seq.getSequenceType();
        fi.selectSequence(seq);
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
            log.info("Free Memory after running " + Integer.toString(numFailed + numPassed) + " times: " + Runtime.getRuntime().freeMemory());
        }
        if (fixture != null) {
            fixture.sequenceStatusChanged(seq.getStatus());
//            if (!isInit) {
//                fixture.init();
//            }
        }
        seq = null;
    }

    public void retest() {
        try {
            log.trace("retesting...");
            getSequence();
            run1();
            if (fixture != null) {
                fixture.sequenceStatusChanged(seq.getStatus());
            }
            seq = null;
        } catch (Exception ex) {
            log.error("Cannot retest", ex);
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
