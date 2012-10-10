/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepInstance.java is part of JTStand.
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
package com.jtstand;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.ParserConfigurationException;
import org.jboss.logging.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

/**
 *
 * @author albert_kurucz
 *
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"parent_id", "teststepnamepath_id"}),
    @UniqueConstraint(columnNames = {"testsequenceinstance_id", "teststepnamepath_id"}),
    @UniqueConstraint(columnNames = {"testStepInstancePosition", "parent_id"})})
//@XmlRootElement(name = "step")
@XmlType(name = "testStepInstanceType", propOrder = {"status", "loops", "startDate", "finishDate", "valueNumber", "valueString", "steps"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestStepInstance extends AbstractVariables implements Runnable, StepInterface {

    private static final Logger log = Logger.getLogger(TestStepInstance.class.getName());
    public static final String STR_DECIMAL_FORMAT = "DECIMAL_FORMAT";
    public static final Class<?>[] STEP_INTERFACE_CONSTRUCTOR = {StepInterface.class};
    public static final Class<?>[] NULL_CONSTRUCTOR = {};
    private static JAXBContext jc;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private TestStep testStep;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private TestStep calledTestStep;
    @ManyToOne(fetch = FetchType.EAGER)
    private TestSequenceInstance testSequenceInstance;
    private Long startTime;
    private Long finishTime;
    private Long loops = null;
    @ManyToOne(fetch = FetchType.LAZY)
    private TestStepInstance parent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("testStepInstancePosition ASC")
    private List<TestStepInstance> steps = new ArrayList<TestStepInstance>();
    private String valueString = null;
    private Double valueNumber = null;
    private int testStepInstancePosition;
    @ManyToOne(fetch = FetchType.EAGER)
    private TestStepNamePath testStepNamePath;
    private transient Thread thisThread = null;
    private transient long nanoTime;
    private transient String valueWithUnit = null;
    private transient String lslWithUnit = null;
    private transient String uslWithUnit = null;
    private transient final Object parentLock = new Object();
    private transient TestStepInstanceBindings bindings;

//    public void initializeProperties() throws ScriptException {
//        for (TestStepProperty tp : testStep.getProperties()) {
//            if (tp.isEager() != null && tp.isEager()) {
//                System.out.println("Evaluating eager step property: " + tp.getName());
//                put(tp.getName(), tp.getPropertyObject(getBindings()));
//            }
//        }
//    }
    public static JAXBContext getJAXBContext()
            throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(TestStepInstance.class);
        }
        return jc;
    }

    public void connect(EntityManager em) throws URISyntaxException, JAXBException, SVNException {
        if (calledTestStep != null && calledTestStep.getId() == null) {
            TestStep ts = testSequenceInstance.getConnectedTestStep(em, calledTestStep);
            if (ts != null) {
//                System.out.println("Connecting calledTestStep '" + calledTestStep.getName() + "'...");
                connectCalledTestStep(em, ts);
                return;
            }
        }
        for (TestStepInstance step : steps) {
            step.connect(em);
        }
    }

    public void connectCalledTestStep(EntityManager em, TestStep calledTestStep) throws URISyntaxException, JAXBException, SVNException {
        if (steps.size() != calledTestStep.getSteps().size()) {
            throw new IllegalArgumentException("childrens size mismatch");
        }
        this.calledTestStep = calledTestStep;
        Iterator<TestStep> it = calledTestStep.getSteps().iterator();
        for (TestStepInstance tsi : steps) {
            tsi.connectTestStep(em, it.next());
        }
    }

    public void connectTestStep(EntityManager em, TestStep testStep) throws URISyntaxException, JAXBException, SVNException {
        this.testStep = testStep;
        if (calledTestStep != null) {
            if (calledTestStep.getId() == null) {
                TestStep ts = testSequenceInstance.getConnectedTestStep(em, calledTestStep);
                if (ts != null) {
//                    System.out.println("Connecting calledTestStep '" + calledTestStep.getName() + "'...");
                    connectCalledTestStep(em, ts);
                    return;
                }
            }
            for (TestStepInstance step : steps) {
                step.connect(em);
            }
        } else {
            if (steps.size() != testStep.getSteps().size()) {
                throw new IllegalArgumentException("childrens size mismatch");
            }
            Iterator<TestStep> it = testStep.getSteps().iterator();
            for (TestStepInstance tsi : steps) {
                tsi.connectTestStep(em, it.next());
            }
        }
    }

//    @XmlTransient
//    @Override
//    public Logger getLogger() {
//        return LOGGER;
//    }
    public String evaluate(String str) {
        return str;
    }

//    @XmlElement(name = "calledTestStepFile")
//    public FileRevision getCalledTestStepFileRevision() {
//        return calledTestStep == null ? null: calledTestStep.getCreator();
//    }
//
//    public void setCalledTestStepFileRevision(FileRevision fileRevision) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
//        //System.out.println("Setting up file revision of test step:" + fileRevision);
//        setTestStep(TestStep.unmarshal(fileRevision));
//    }
    @XmlAttribute
    @Override
    public String getName() {
        TestStepNamePath tsnp = getTestStepNamePath();
        return (tsnp == null) ? evaluateName() : tsnp.getStepName();
    }

    public void setName(String name) {
        /* dummy function to satisfy JAXB */
    }

    @XmlTransient
    public String getTestStepInstancePath() {
        return getTestStepNamePath().getStepPath();
    }

    public String evaluateName() {
        TestStepNamePath tsnp = getTestStepNamePath();
        if (tsnp != null) {
            return tsnp.getStepName();
        }
        return evaluate(getTestStep().getName());
    }

    public void setNames(Map<String, TestStepNamePath> names) {
        setTestStepNamePath(names.get(getTestStepNamePath().getStepPath()));
        for (TestStepInstance child : getSteps()) {
            child.setNames(names);
        }
    }

    private String evaluatePath() {
        TestStepNamePath tsnp = getTestStepNamePath();
        if (tsnp != null) {
            return tsnp.getStepPath();
        }
        return evaluatePath(evaluateName());
    }

    private String evaluatePath(String eName) {
        return (parent == null) ? eName : parent.evaluatePath() + "." + eName;
    }

    @XmlTransient
    public List<String> getPathList() {
        List<String> retval = (parent == null) ? new ArrayList<String>() : parent.getPathList();
        retval.add(getName());
        return retval;
    }

    @XmlTransient
    public int getPosition() {
        return testStepInstancePosition;
    }

    public void setPosition(int position) {
        this.testStepInstancePosition = position;
    }

    @XmlTransient
    String getInteractionMessage() {
        return message;
    }
    private transient String message;

    void interact(String message) throws InterruptedException {
        this.message = message;
        getTestSequenceInstance().interact(this);
    }

    void startInteraction(String message) throws InterruptedException {
        this.message = message;
        getTestSequenceInstance().startInteraction(this);
    }

    public void finishInteraction(boolean interactionPassed) {
        getTestSequenceInstance().finishInteraction(interactionPassed);
        this.message = null;
    }

    public static enum StepStatus {

        PENDING("Pending"),
        LOCKED("Locked"),
        RUNNING("Running"),
        PASSED("Passed"),
        FAILED("Failed"),
        NOTEST("Skipped"),
        ABORTED("Aborted"),
        STEPBYSTEP("Step by step"),
        STEPBYSTEP_FINISHED("Finished");
        //INTERACTIVE("Interactive");
        public final String statusString;

        StepStatus(String statusString) {
            this.statusString = statusString;
        }
    }
    @Basic(fetch = FetchType.EAGER)
    protected StepStatus status = StepStatus.PENDING;

    @XmlTransient
    public Long getElapsed() {
        return (startTime == null) ? null : ((finishTime == null) ? System.currentTimeMillis() : finishTime) - startTime;
    }

    @XmlTransient
    public String getElapsedString() {
        Long ela = getElapsed();
        return (ela == null) ? "" : ela.toString() + "ms";
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    public TestStepInstance() {
        super();
    }

    public TestStepInstance(TestStep testStep, TestSequenceInstance testSequenceInstance)
            throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
//        Log.log("Creating new root step instance:" + testStep.getName() + " for sequence instance:" + testSequenceInstance.getSerialNumber());
        super();
        setTestSequenceInstance(testSequenceInstance);
        init(testStep);
    }

    private TestStepInstance(TestStep testStep, TestStepInstance parent)
            throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
//        Log.log("Creating new step instance:" + testStep.getName() + " for parent:" + parent.getName());
        super();
        setParent(parent);
        init(testStep);
    }

    public TestStepInstance previous() {
        TestStepInstance p = getParent();
        return (p == null) ? null : p.previous(this);
    }

    public TestStepInstance tail() {
        List<TestStepInstance> children = getSteps();
        return (children.size() == 0) ? this : children.get(children.size() - 1).tail();
    }

    public TestStepInstance previous(TestStepInstance child) {
        List<TestStepInstance> children = getSteps();
        int pos = children.indexOf(child);
        if (pos < 0) {
            pos = children.size();
        }
        return (pos > 0) ? children.get(pos - 1).tail() : this;
    }

    public TestStepInstance next() {
//        System.out.println("Getting next of " + getTestStepInstancePath());
        List<TestStepInstance> children = getSteps();
        if (children == null) {
            throw new IllegalStateException("Children of " + getTestStepInstancePath() + " is null!");
        }
        if (!children.isEmpty()) {
            return children.get(0);
        } else {
            TestStepInstance p = getParent();
            return (p == null) ? null : p.next(this);
        }
    }

    private TestStepInstance next(TestStepInstance child) {
        int pos = getSteps().indexOf(child) + 1;
        if (pos < getSteps().size()) {
            return getSteps().get(pos);
        } else {
            if (getParent() != null) {
                return getParent().next(this);
            } else {
                return null;
            }
        }
    }

    private void init(TestStep testStep) throws URISyntaxException, JAXBException, SVNException, IOException, ParserConfigurationException, SAXException {
        this.testStep = testStep;
        setPosition(testStep.getPosition());
        this.calledTestStep = getCalledTestStep(true);
        updateTestStepNamePath();
        initChildren(getCalledTestStep() != null ? getCalledTestStep() : getTestStep());
    }

    private TestStep getCalledTestStep(boolean useCache) throws URISyntaxException, JAXBException, SVNException {
        return testSequenceInstance.getCalledTestStep(getTestStep().getStepReference(), useCache);
    }

    private void initChildren(TestStep testStep) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        for (TestStep child : testStep.getSteps()) {
            TestStepInstance tsi = new TestStepInstance(child, this);
            steps.add(tsi);
        }
    }

    @XmlTransient
    public TestStepNamePath getTestStepNamePath() {
        return testStepNamePath;
    }

    public void setTestStepNamePath(TestStepNamePath testStepNamePath) {
        this.testStepNamePath = testStepNamePath;
        if (testStepNamePath != null) {
            getTestSequenceInstance().getTestSequence().getNames().put(testStepNamePath.getStepPath(), testStepNamePath);
        }
    }

    public void checkLoop(FileRevision creator) {
        if (calledTestStep != null) {
            if (calledTestStep.getCreator().getSubversionUrl().equals(creator.getSubversionUrl())) {
                throw new IllegalArgumentException("loop detected!");
            }
        }
        if (parent != null) {
            parent.checkLoop(creator);
        }
    }

    @XmlTransient
    public TestStep getCalledTestStep() {
        return calledTestStep;
    }

    public void setCalledTestStep(TestStep calledTestStep) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        this.calledTestStep = calledTestStep;
        if (calledTestStep != null) {
            if (steps.size() != calledTestStep.getSteps().size()) {
                throw new IllegalArgumentException("childrens size mismatch");
            } else {
                Iterator<TestStep> it = calledTestStep.getSteps().iterator();
                for (TestStepInstance tsi : steps) {
                    tsi.setTestStep(it.next());
                }
            }
        }
    }

    @XmlTransient
    public TestStep getTestStep() {
        return testStep;
    }

    private int evaluateStepNumber() {
        TestStepNamePath tsnp = getTestStepNamePath();
        if (tsnp != null) {
            return tsnp.getStepNumber();
        }
        TestStepInstance prev = previous();
//        System.out.println("previous of '" + this + "' is '" + prev + "'");
        return (prev == null) ? 1 : prev.evaluateStepNumber() + 1;
    }

    private void updateTestStepNamePath() {
        TestSequenceInstance seq = getTestSequenceInstance();
        if (seq != null) {
            TestStep seqts = seq.getTestSequence();
            if (seqts != null) {
                String eName = evaluateName();
                String ePath = evaluatePath(eName);
                int stepNumber = evaluateStepNumber();
                //System.out.println("Evaluated name: '" + eName + "'" + " path: '" + ePath + "'");
                Map<String, TestStepNamePath> names = seqts.getNames();
                TestStepNamePath ts = names.get(ePath);
                if (ts == null) {
                    ts = new TestStepNamePath(
                            getTestSequenceInstance().getTestSequence(),
                            eName,
                            ePath,
                            //evaluateTestLimit(),
                            calledTestStep,
                            stepNumber);
                }
                setTestStepNamePath(ts);
            }
        }
    }

    public void setTestStep(TestStep testStep) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        this.testStep = testStep;
        if (testStep != null) {
            setPosition(testStep.getPosition());
            updateTestStepNamePath();

//            if (getParent() != null) {
//                getParent().checkLoop(testStep.getCreator());
//            }

            if (testStep.getStepReference() != null) {
//                calledTestStep = testStep.getCalledTestStep(this, true);
                calledTestStep = getCalledTestStep(true);
                if (steps.size() != calledTestStep.getSteps().size()) {
                    throw new IllegalArgumentException("Childrens of '" + getTestStepInstancePath() + "' size mismatch. Expected:" + steps.size() + " Received:" + calledTestStep.getSteps().size());
                } else {
                    Iterator<TestStep> it = calledTestStep.getSteps().iterator();
                    for (TestStepInstance tsi : steps) {
                        tsi.setTestStep(it.next());
                    }
                }
            } else {
                if (steps.size() != testStep.getSteps().size()) {
                    throw new IllegalArgumentException("childrens size mismatch");
                } else {
                    Iterator<TestStep> it = testStep.getSteps().iterator();
                    for (TestStepInstance tsi : steps) {
                        tsi.setTestStep(it.next());
                    }
                }
            }
        }
    }

    public void setTestStepNoCache(TestStep testStep) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        this.testStep = testStep;
        if (testStep != null) {
            setPosition(testStep.getPosition());
            updateTestStepNamePath();

            if (testStep.getStepReference() != null) {
                //calledTestStep = testStep.getCalledTestStep(this, false);
                calledTestStep = getCalledTestStep(false);
                if (steps.size() != calledTestStep.getSteps().size()) {
                    throw new IllegalArgumentException("childrens size mismatch");
                } else {
                    Iterator<TestStep> it = calledTestStep.getSteps().iterator();
                    for (TestStepInstance tsi : steps) {
                        tsi.setTestStepNoCache(it.next());
                    }
                }
            } else {
                if (steps.size() != testStep.getSteps().size()) {
                    throw new IllegalArgumentException("childrens size mismatch");
                } else {
                    Iterator<TestStep> it = testStep.getSteps().iterator();
                    for (TestStepInstance tsi : steps) {
                        tsi.setTestStepNoCache(it.next());
                    }
                }
            }
        }
    }

    public StepStatus getStatus() {
        return status;
    }

    public void setStatus(StepStatus status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testSequenceInstance != null ? testSequenceInstance.hashCode() : 0);
        hash += (testStepNamePath != null ? testStepNamePath.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStepInstance)) {
            return false;
        }
        TestStepInstance other = (TestStepInstance) object;
        if ((this.testSequenceInstance == null && other.getTestSequenceInstance() != null) || (this.testSequenceInstance != null && !this.testSequenceInstance.equals(other.getTestSequenceInstance()))) {
            return false;
        }
        if ((this.testStepNamePath == null && other.getTestStepNamePath() != null) || (this.testStepNamePath != null && !this.testStepNamePath.equals(other.getTestStepNamePath()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }

    @XmlTransient
    public TestSequenceInstance getTestSequenceInstance() {
        return testSequenceInstance;
    }

    public void setTestSequenceInstance(TestSequenceInstance testSequenceInstance) {
        this.testSequenceInstance = testSequenceInstance;
        for (TestStepInstance child : steps) {
            child.setParent(this);
        }
    }

    @XmlElement(name = "startTime")
    public Date getStartDate() {
        return startTime == null ? null : new Date(startTime);
    }

    public void setStartDate(Date date) {
        setStartTime(date.getTime());
    }

    @XmlTransient
    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
        nanoTime = System.nanoTime();
    }

    @XmlElement(name = "finishTime")
    public Date getFinishDate() {
        return finishTime == null ? null : new Date(finishTime);
    }

    public void setFinishDate(Date date) {
        setFinishTime(date.getTime());
    }

    @XmlTransient
    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Long getLoops() {
        return loops;
    }

    public void setLoops(Long loops) {
        this.loops = loops;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
//        computeValueWithUnit();
    }

    public Double getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(Double valueNumber) {
        this.valueNumber = valueNumber;
//        computeValueWithUnit();
    }

    @XmlElement(name = "step")
    public List<TestStepInstance> getSteps() {
        return steps;
    }

    public TestStepInstance getChild(String name) {
        for (TestStepInstance child : getSteps()) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public void setSteps(List<TestStepInstance> steps) {
        this.steps = steps;
        if (steps != null) {
            for (ListIterator<TestStepInstance> iterator = steps.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestStepInstance testStepInstance = iterator.next();
                testStepInstance.setParent(this);
                testStepInstance.setPosition(index);
            }
        }
    }

    @XmlTransient
    public TestStepInstance getParent() {
        synchronized (parentLock) {
            return parent;
        }
    }

    public void setParent(TestStepInstance parent) {
        this.parent = parent;
        if (parent != null) {
            setTestSequenceInstance(parent.getTestSequenceInstance());
        }
    }

//    public String getStepClass() {
//        if (getTestStep().getStepClass() != null) {
//            return getTestStep().getStepClass();
//        }
//        if (getCalledTestStep() != null) {
//            return getCalledTestStep().getStepClass();
//        }
//        return null;
//    }
    public TestStepScript getScript() {
        if (getTestStep().getScript() != null) {
            return getTestStep().getScript();
        }
        if (getCalledTestStep() != null) {
            return getCalledTestStep().getScript();
        }
        return null;
    }

    public boolean isParallel() {
        if (getTestStep().getParallel() != null) {
            return getTestStep().getParallel();
        }
        return getCalledTestStep() != null && getCalledTestStep().getParallel() != null && getCalledTestStep().getParallel();
    }

    public boolean isCleanup() {
        if (getTestStep().getCleanup() != null) {
            return getTestStep().getCleanup();
        }
        return getCalledTestStep() != null && getCalledTestStep().getCleanup() != null && getCalledTestStep().getCleanup();
    }

    private long getPreSleep() {
        if (getTestStep().getPreSleep() != null) {
            return getTestStep().getPreSleep();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getPreSleep() != null) {
            return getCalledTestStep().getPreSleep();
        }
        return 0;
    }

    private long getPostSleep() {
        if (getTestStep().getPostSleep() != null) {
            return getTestStep().getPostSleep();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getPostSleep() != null) {
            return getCalledTestStep().getPostSleep();
        }
        return 0;
    }

    private long getLoopSleep() {
        if (getTestStep().getLoopSleep() != null) {
            return getTestStep().getLoopSleep();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getLoopSleep() != null) {
            return getCalledTestStep().getLoopSleep();
        }
        return 0;
    }

    private String getLocks() {
        if (getTestStep().getLocks() != null) {
            return getTestStep().getLocks();
        }
        if (getCalledTestStep() != null) {
            return getCalledTestStep().getLocks();
        }
        return null;
    }

    private long getMaxLoops() {
        if (getTestStep().getMaxLoops() != null) {
            return getTestStep().getMaxLoops();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getMaxLoops() != null) {
            return getCalledTestStep().getMaxLoops();
        }
        return 1;
    }
    private static long lastTime = 0;
    private static Object lastTimeLock = new Object();

    @Override
    public void run() {
//        Log.log(getTestStepInstancePath() + " started");
        /* Make sure startTime is unique within this station! */
        thisThread = Thread.currentThread();
        long sTime = System.currentTimeMillis();
        synchronized (lastTimeLock) {
            if (sTime <= lastTime) {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException ex) {
                    if (this.isAborted()) {
                        return;
                    }
                }
                sTime = lastTime + 1;
            }
            lastTime = sTime;
        }
        StringBuffer tName = new StringBuffer();
        if (getTestSequenceInstance().getHostName() != null) {
            tName.append(getTestSequenceInstance().getHostName());
        }
        if (getTestSequenceInstance().getTestFixtureName() != null) {
            if (tName.length() > 0) {
                tName.append('@');
            }
            tName.append(getTestSequenceInstance().getTestFixtureName());
        }
        if (tName.length() > 0) {
            tName.append('@');
        }
        tName.append(getTestStepInstancePath());
        thisThread.setName(tName.toString());
        setStartTime(sTime);
//        Log.log(getTestStepInstancePath() + " started at " + getStartedStringMs());
        setFinishTime(null);
        setLoops(null);
        try {
            String locks = getLocks();
            if (locks != null) {
                if (locks.indexOf('|') >= 0) {
                    for (StringTokenizer tk = new StringTokenizer(locks, "|"); tk.hasMoreTokens();) {
                        getVariable(tk.nextToken());
                    }
                    run1();
                    for (StringTokenizer tk = new StringTokenizer(locks, "|"); tk.hasMoreTokens();) {
                        releaseVariable(tk.nextToken());
                    }
                } else {
                    getVariable(locks);
                    run1();
                    releaseVariable(locks);
                }
            } else {
                run1();
            }
        } catch (InterruptedException iex) {
            log.info(getTestStepInstancePath() + " is interrupted!");
            setStatus(StepStatus.ABORTED);
        } catch (Throwable ex) {
            log.warn("Exception while executing " + this.getName(), ex);
            getTestSequenceInstance().setFailureCode(ex.getMessage());
            getTestSequenceInstance().setFailureStep(this);
            if (!isAborted()) {
                setStatus(StepStatus.FAILED);
            }
        }
        if (getTestSequenceInstance().isAborted()) {
            setStatus(StepStatus.ABORTED);
            for (TestStepInstance child : steps) {
                if (child.getStatus().equals(StepStatus.RUNNING)) {
                    child.setStatus(StepStatus.ABORTED);
                }
            }
        }
        if (status.equals(StepStatus.RUNNING)) {
//            Log.log(getTestStepInstancePath() + " has passed");
            setStatus(StepStatus.PASSED);
        }
        setFinishTime(nanoTime != 0 ? startTime + (System.nanoTime() - nanoTime) / 1000000 : System.currentTimeMillis());
//        Log.log(getTestStepInstancePath() + " finished at " + getFinishedStringMs());
        if (getTestSequenceInstance().isPersistPerStep()) {
            mergeOrSerialize();
        }
        dispose();
    }

    public void mergeOrSerialize() {
        final TestStepInstance step = this;
        Thread t = new Thread(new Runnable() {
            public void run() {
                if (!getTestSequenceInstance().merge(step)) {
                    getTestSequenceInstance().toFile();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            log.warn(null, ex);
        }
    }

    public boolean merge(EntityManager em) {
        long startTransaction = System.currentTimeMillis();
        try {
            em.getTransaction().begin();
//            Log.log("Merging " + getTestStepInstancePath() + "...");
            em.merge(this);
//            Log.log("Merging " + getTestStepInstancePath() + ", Commiting Transaction...");
            em.getTransaction().commit();
//            Log.log("Merging " + getTestStepInstancePath() + " committed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            return true;
        } catch (Exception ex) {
            log.error("Merging testStepInstance failed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
        return false;
    }

    public TestStep.RunMode getRunMode() {
        if (getTestStep().getRunMode() != null) {
            return getTestStep().getRunMode();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getRunMode() != null) {
            return getCalledTestStep().getRunMode();
        }
        return TestStep.RunMode.NORMAL;
    }

    public TestStep.PassAction getPassAction() {
        if (getTestStep().getPassAction() != null) {
            return getTestStep().getPassAction();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getPassAction() != null) {
            return getCalledTestStep().getPassAction();
        }
        return TestStep.PassAction.NEXT_TEST;
    }

    public TestStep.FailAction getFailAction() {
        if (getTestStep().getFailAction() != null) {
            return getTestStep().getFailAction();
        }
        if (getCalledTestStep() != null && getCalledTestStep().getFailAction() != null) {
            return getCalledTestStep().getFailAction();
        }
        return TestStep.FailAction.STOP;
    }

    private void skip() {
        setStatus(StepStatus.NOTEST);
        for (TestStepInstance child : steps) {
            child.skip();
        }
    }

    private void run1() throws InterruptedException {
        String failureCode = null;
        TestStep.RunMode runMode = getRunMode();
        if (runMode != null && runMode.equals(TestStep.RunMode.SKIP)) {
            skip();
            return;
        }
        if (getTestStep().getMessage() != null) {
            log.info(getTestStep().getMessage());
            //TBD improve this!
        }
        if (getPreSleep() > 0) {
            Thread.sleep(getPreSleep());
        }
        do {
            setStatus(StepStatus.RUNNING);
            if (loops == null) {
                loops = 1L;
            } else {
                loops += 1;
            }
            if (loops > 1 && getLoopSleep() > 0) {
                Thread.sleep(getLoopSleep());
            }
            if (getTestSequenceInstance().isAborted()) {
                return;
            }
//            try {
//                initializeProperties();
//            } catch (Throwable ex) {
//                failureCode = ex.getMessage();
//                System.out.println("failureCode: " + failureCode);
//                if (!isAborted()) {
//                    setStatus(StepStatus.FAILED);
//                }
//                continue;
//            }
            if (!steps.isEmpty()) {
                ThreadGroup group = new ThreadGroup(getName());
                if (isParallel()) {
//                    Log.log("Running the children of " + getPath() + " parallel");
                    List<Thread> threads = new ArrayList<Thread>();
                    for (TestStepInstance child : steps) {
                        if (child.getRunMode().equals(TestStep.RunMode.SKIP) || loops != null && loops.longValue() == 1 && child.getRunMode().equals(TestStep.RunMode.SKIP_FIRST)) {
                            //Log.log("skipping child:" + child.getName());
                            child.skip();
                        } else {
                            //Log.log("running child:" + child.getName());
                            Thread t = new Thread(group, child);
                            threads.add(t);
                            t.start();
                        }
                    }
                    for (Thread t : threads) {
                        if (!getTestSequenceInstance().isAborted()) {
                            t.join();
                        } else {
                            abort();
                            return;
                        }
                    }
                    for (TestStepInstance child : steps) {
                        if (child.isAborted()) {
                            setStatus(StepStatus.ABORTED);
                            break;
                        }
                    }
                    if (!isAborted()) {
                        for (TestStepInstance child : steps) {
                            if (child.isFailed() && !child.getFailAction().equals(TestStep.FailAction.NEXT_TEST)) {
                                setStatus(StepStatus.FAILED);
                                break;
                            }
                        }
                    }
                } else {
//                    Log.log("Running the children of " + getPath() + " sequentially");
                    boolean childFailed = false;
                    for (TestStepInstance child : steps) {
                        if (child.getRunMode().equals(TestStep.RunMode.SKIP)
                                || (loops != null && loops.longValue() == 1 && child.getRunMode().equals(TestStep.RunMode.SKIP_FIRST))) {
                            child.skip();
                        } else {
                            if (child.isCleanup() || !childFailed) {
                                child.run();
                            }
                            if (child.isFailed() && !child.getFailAction().equals(TestStep.FailAction.NEXT_TEST)) {
                                childFailed = true;
                            }
                        }
                        if (child.isAborted()) {
                            abort();
                            return;
                        }
                    }
                    if (childFailed) {
                        setStatus(StepStatus.FAILED);
                    }
                }
            }
            if (getTestSequenceInstance().isAborted() || status.equals(StepStatus.FAILED) && getFailAction().equals(TestStep.FailAction.STOP)) {
                return;
            }
//            if (isRunning() && getStepClass() != null) {
//                Object stepObject;
//                try {
//                    //TBD what is this?
//                    stepObject = getVariable(getStepClass());
//                    bind(stepObject);
//                } catch (Exception ex) {
//                    Class<?> stepClass = Thread.currentThread().getContextClassLoader().loadClass(getStepClass());
////                    Class<?> stepClass = Class.forName(getStepClass());
//                    try {
//                        Constructor<?> stepObjectContructor = stepClass.getConstructor(STEP_INTERFACE_CONSTRUCTOR);
//                        stepObject = stepObjectContructor.newInstance((StepInterface) this);
//                    } catch (NoSuchMethodException ex2) {
//                        Constructor<?> stepObjectContructor = stepClass.getConstructor(NULL_CONSTRUCTOR);
//                        stepObject = stepObjectContructor.newInstance();
//                        bind(stepObject);
//                    }
//                }
//                ((Runnable) stepObject).run();
//                if (getTestSequenceInstance().isAborted()) {
//                    return;
//                }
//            }
            if (isRunning() && getScript() != null) {
                try {
                    getScript().execute(this);
                    if (getTestSequenceInstance().isAborted()) {
                        return;
                    }
                    checkValuePassed(getValue());
                } catch (Throwable ex) {
                    failureCode = ex.getMessage();
                    log.info("failureCode: " + failureCode);
                    if (!isAborted()) {
                        setStatus(StepStatus.FAILED);
                    }
                }
            }
            //} while (loops.longValue() < getMaxLoops() && ((status.equals(StepStatus.PASSED) || status.equals(StepStatus.RUNNING)) && getPassAction().equals(TestStep.PassAction.LOOP) || status.equals(StepStatus.FAILED) && getFailAction().equals(TestStep.FailAction.LOOP)));
        } while (loops.longValue() < getMaxLoops() && ((status.equals(StepStatus.PASSED) || status.equals(StepStatus.RUNNING)) && getPassAction().equals(TestStep.PassAction.LOOP) || isFailedSoftly() && getFailAction().equals(TestStep.FailAction.LOOP)));
        if (getTestSequenceInstance().isAborted()) {
            return;
        }
        if (status.equals(StepStatus.FAILED)) {
            getTestSequenceInstance().setFailureCode(failureCode);
            getTestSequenceInstance().setFailureStep(this);
        }
        switch (runMode) {
            case FORCE_PASS:
                setStatus(StepStatus.PASSED);
                break;
            case FORCE_FAIL:
                setStatus(StepStatus.FAILED);
                break;
            default:
                break;
        }
        if (getPostSleep() > 0) {
            Thread.sleep(getPostSleep());
        }
    }

//    private void bind(Object stepObject) throws IllegalArgumentException, IllegalAccessException {
//        Field[] fa = stepObject.getClass().getDeclaredFields();
//        for (Field f : fa) {
//            if (f.getType().isAssignableFrom(StepInterface.class)) {
//                f.setAccessible(true);
//                f.set(stepObject, (StepInterface) this);
//            }
//        }
//    }

    @Override
    public boolean isAborted() {
        return StepStatus.ABORTED.equals(status);
    }

    public boolean isPassed() {
        return StepStatus.PASSED.equals(status);
    }

    public boolean isFailed() {
        return StepStatus.FAILED.equals(status);
    }

    public boolean isFailedSoftly() {
        if (!StepStatus.FAILED.equals(status)) {
            return false;
        }
        if (TestStep.FailAction.STOP.equals(getFailAction())) {
            return false;
        }
        for (TestStepInstance child : getSteps()) {
            if (child.isFailedHardly()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFailedHardly() {
        return StepStatus.FAILED.equals(status) && !isFailedSoftly();
    }

    public boolean isRunning() {
        return StepStatus.RUNNING.equals(status);
    }

    public boolean isSiblingRunning() {
        if (StepStatus.RUNNING.equals(status)) {
            return true;
        }
        for (TestStepInstance child : getSteps()) {
            if (child.isSiblingRunning()) {
                return true;
            }
        }
        return false;
    }

    public boolean isNumericKind() {
        if (getValueNumber() != null) {
            return true;
        }
        if (getValueString() != null) {
            return false;
        }
        TestLimit myLimit = getTestLimit();
        return myLimit != null && myLimit.isNumericKind();
    }

    @Override
    public void abort(Thread thread) {
        super.abort(thread);
        if (parent != null) {
            parent.abort(thread);
        }
//        if (getTestSequenceInstance() != null) {
//            getTestSequenceInstance().abort(thread);
//        }
    }

    @Override
    public void abort() {
        setStatus(StepStatus.ABORTED);
        abort(thisThread);
        getTestSequenceInstance().abort();
    }

    @Override
    @XmlTransient
    public Bindings getBindings() {
        if (this.bindings == null) {
            this.bindings = new TestStepInstanceBindings(this);
        }
        return this.bindings;
    }

    @Override
    public Object getPropertyObjectUsingBindings(String keyString, Bindings bindings) throws ScriptException {
        if (getTestStep() != null) {
            for (TestProperty tsp : getTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        } else {
            log.error("testStep is null, while getting property:'" + keyString + "'!");
        }
        if (getCalledTestStep() != null) {
            for (TestProperty tsp : getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        if (getParent() != null) {
            return getParent().getPropertyObjectUsingBindings(keyString, bindings);
        }
        if (getTestSequenceInstance() != null) {
            if (getTestSequenceInstance().getTestType() != null) {
                for (TestProperty tsp : getTestSequenceInstance().getTestType().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return tsp.getPropertyObject(bindings);
                    }
                }
                if (getTestSequenceInstance().getTestType().getProduct() != null) {
                    for (TestProperty tsp : getTestSequenceInstance().getTestType().getProduct().getProperties()) {
                        if (tsp.getName().equals(keyString)) {
                            return tsp.getPropertyObject(bindings);
                        }
                    }
                }
            }
            if (getTestSequenceInstance().getTestFixture() != null) {
                for (TestProperty tsp : getTestSequenceInstance().getTestFixture().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return tsp.getPropertyObject(bindings);
                    }
                }
            }
            if (getTestSequenceInstance().getTestStation() != null) {
                for (TestProperty tsp : getTestSequenceInstance().getTestStation().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return tsp.getPropertyObject(bindings);
                    }
                }
            }
            if (getTestSequenceInstance().getTestProject() != null) {
                for (TestProperty tsp : getTestSequenceInstance().getTestProject().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return tsp.getPropertyObject(bindings);
                    }
                }
            }
        }
        try {
            String prop = System.getProperty(keyString);
            if (prop != null) {
                return prop;
            }
        } catch (IllegalArgumentException ex1) {
            ex1.printStackTrace();
        } catch (SecurityException ex2) {
            ex2.printStackTrace();
        }
        try {
            return System.getenv(keyString);
        } catch (IllegalArgumentException ex1) {
            ex1.printStackTrace();
        } catch (SecurityException ex2) {
            ex2.printStackTrace();
        }
        return null;

    }

    @Override
    public Object getVariable(String keyString) throws InterruptedException, ScriptException {
        return getVariable(keyString, false);
    }

    @Override
    public Object getVariableWait(String keyString) throws InterruptedException, ScriptException {
        return getVariable(keyString, true);
    }

    public boolean isThreadInFamily(Thread t) {
        if (t == null) {
            throw new IllegalArgumentException("isThreadInFamily called with null");
        }
        if (thisThread != null && thisThread.isAlive()) {
            if (thisThread.equals(t)) {
                return true;
            }
        } else {
            throw new IllegalStateException("Test step " + getTestStepInstancePath() + " is not running when isThreadInFamily is called!");
        }
        return getParent() != null && getParent().isThreadInFamily(t);
    }

    @XmlTransient
    public Thread getThisThread() {
        return thisThread;
    }

    public Object getVariable(String keyString, boolean wait) throws InterruptedException, ScriptException {
        return getVariable(keyString, wait, this);
    }

    public boolean containsProperty(String key) {
        if (getTestStep() != null) {
            for (TestStepProperty tsp : getTestStep().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
        } else {
            log.error("getVariable : testStep is null!");
        }
        if (getCalledTestStep() != null) {
            for (TestStepProperty tsp : getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
        }
        if (getParent() != null) {
            return getParent().containsProperty(key);
        }
        if (getTestSequenceInstance() != null) {
            if (getTestSequenceInstance().getTestType() != null) {
                for (TestProperty tsp : getTestSequenceInstance().getTestType().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        return true;
                    }
                }
                if (getTestSequenceInstance().getTestType().getProduct() != null) {
                    for (TestProperty tsp : getTestSequenceInstance().getTestType().getProduct().getProperties()) {
                        if (tsp.getName().equals(key)) {
                            return true;
                        }
                    }
                }
            }
            return getTestSequenceInstance().getTestFixture().containsProperty(key);
        }
        return false;
    }

    public Object getVariable(String keyString, boolean wait, TestStepInstance step) throws InterruptedException, ScriptException {
        log.trace("Step: " + getName() + " is getting variable: '" + keyString + "'");
        if ("out".equals(keyString)) {
            return System.out;
        }
        if ("value".equals(keyString)) {
            return getValue();
        }
        if (getTestStep() != null) {
            for (TestStepProperty tsp : getTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return getVariable(keyString, wait, tsp, step);
                }
            }
        } else {
            log.error("getVariable : testStep is null!");
        }
        if (getCalledTestStep() != null) {
            for (TestStepProperty tsp : getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return getVariable(keyString, wait, tsp, step);
                }
            }
        }
        if (getParent() != null) {
            return getParent().getVariable(keyString, wait, step);
        }
        TestSequenceInstance seq = getTestSequenceInstance();
        if (seq != null) {
            TestFixture fixture = seq.getTestFixture();
            if (fixture != null) {
                for (TestFixtureProperty tsp : fixture.getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return fixture.getVariable(keyString, wait, tsp, step);
                    }
                }
            }
            TestStation station = seq.getTestStation();
            if (station != null) {
                for (TestStationProperty tsp : station.getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return station.getVariable(keyString, wait, tsp, step);
                    }
                }
                TestProject project = station.getTestProject();
                if (project != null) {
                    for (TestProjectProperty tsp : project.getProperties()) {
                        if (tsp.getName().equals(keyString)) {
                            /**
                             * Variables defined at project level are still
                             * stored at station level
                             */
                            return station.getVariable(keyString, wait, tsp, step);
                        }
                    }
                }
            }
        } else {
            log.error("getVariable : testSequenceInstance is null!");
        }
        throw new IllegalArgumentException("Undefined variable in TestStepInstance:" + keyString);
    }

    @Override
    public void releaseVariable(String keyString) {
        releaseVariable(keyString, this);
    }

    private void releaseVariable(String keyString, TestStepInstance step) {
        if (getTestStep() != null) {
            for (TestStepProperty tsp : getTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    releaseVariable(keyString, tsp, step);
                    return;
                }
            }
        } else {
            log.error("getVariable : testStep is null!");
        }
        if (getCalledTestStep() != null) {
            for (TestStepProperty tsp : getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    releaseVariable(keyString, tsp, step);
                    return;
                }
            }
        }
        if (parent != null) {
            parent.releaseVariable(keyString);
            return;
        }
        TestSequenceInstance seq = getTestSequenceInstance();
        if (seq != null) {
            if (seq.getTestFixture() != null) {
                for (TestFixtureProperty tsp : seq.getTestFixture().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        seq.getTestFixture().releaseVariable(keyString, tsp, step);
                        return;
                    }
                }
            }
            if (seq.getTestStation() != null) {
                for (TestStationProperty tsp : seq.getTestStation().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        seq.getTestStation().releaseVariable(keyString, tsp, step);
                        return;
                    }
                }
            }
        } else {
            log.error("releaseVariable : testSequenceInstance is null!");
        }
        throw new IllegalArgumentException("Undefined variable in TestStepInstance:" + keyString);
    }

    public Set<String> keySetPublic() {
        Set<String> keys = new HashSet<String>();
        keys.addAll(super.keySet());
        if (parent != null) {
            keys.addAll(parent.keySetPublic());
        } else {
            TestSequenceInstance seq = getTestSequenceInstance();
            if (seq != null) {
                if (seq.getTestFixture() != null) {
                    keys.addAll(seq.getTestFixture().keySet());
                }
                if (seq.getTestStation() != null) {
                    keys.addAll(seq.getTestStation().keySet());
                }
            }
        }
        return keys;
    }

    @Override
    @XmlTransient
    public Object getValue() {
//        return value;
        if (getValueNumber() != null) {
            return getValueNumber();
        }
        return getValueString();
    }

    @Override
    public void setValue(Object value) {
//        Log.log("setting value of '" + getName() + "' to:" + value);
//        this.value = value;
        if (value == null) {
            setValueString(null);
            setValueNumber(null);
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            setValueString(null);
            setValueNumber(((Number) value).doubleValue());
        } else {
            setValueNumber(null);
            setValueString(String.valueOf(value));
        }
    }

    public void checkValuePassed(Object value) {
        if (!isValuePassed(value)) {
            throw new IllegalArgumentException("Value is out of limit");
        }
    }

    public boolean isValuePassed(Object value) {
        TestLimit tl = getTestLimit();
        if (tl == null) {
            return true;
        }
        if (value == null) {
            return false;
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            return isValuePassed(tl, ((Number) value).doubleValue());
        } else {
            return isValuePassed(tl, value.toString());
        }
    }

//    public boolean isValueOnTarget(Object value) {
//        TestLimit tl = getTestLimit();
//        if (tl == null || (tl.getTargetCPL() == null && tl.getTargetCPU() == null)) {
//            return true;
//        }
//        if (value == null) {
//            return false;
//        }
//        if (Number.class.isAssignableFrom(value.getClass())) {
//            return isValueOnTarget(tl,
//                    ((Number) value).doubleValue());
//        } else {
//            return isValuePassed(tl, value.toString());
//        }
//    }
    private boolean isValuePassed(TestLimit tl, double value) {
        switch (tl.getComp()) {
            case EQ:
                return value == tl.getNominal();
            case NE:
                return value != tl.getNominal();
            case GT:
                return value > tl.getLowerSpecifiedLimit();
            case GE:
                return value >= tl.getLowerSpecifiedLimit();
            case LT:
                return value < tl.getUpperSpeficiedLimit();
            case LE:
                return value <= tl.getUpperSpeficiedLimit();
            case GTLT:
                return value > tl.getLowerSpecifiedLimit()
                        && value < tl.getUpperSpeficiedLimit();
            case GELE:
                return value >= tl.getLowerSpecifiedLimit()
                        && value <= tl.getUpperSpeficiedLimit();
            case GELT:
                return value >= tl.getLowerSpecifiedLimit()
                        && value < tl.getUpperSpeficiedLimit();
            case GTLE:
                return value > tl.getLowerSpecifiedLimit()
                        && value <= tl.getUpperSpeficiedLimit();
            case BOOL:
                return value != 0.0;
//            default:
//                throw new IllegalArgumentException("Unsupported compare type for Numeric value:" + tl.getComp());
        }
        return false;
    }

//    private boolean isValueOnTarget(TestLimit tl, double value) {
//        Double targetCPL = tl.getTargetCPL();
//        Double targetCPU = tl.getTargetCPU();
//        switch (tl.getComp()) {
//            case EQ:
//                return value == tl.getNominal();
//            case NE:
//                return value != tl.getNominal();
//            case GT:
//                if (targetCPL == null) {
//                    return true;
//                } else {
//                    return value > tl.getLowerSpecifiedLimit();
//                }
//            case GE:
//                return value >= tl.getLowerSpecifiedLimit();
//            case LT:
//                return value < tl.getUpperSpeficiedLimit();
//            case LE:
//                return value <= tl.getUpperSpeficiedLimit();
//            case GTLT:
//                return value > tl.getLowerSpecifiedLimit() &&
//                        value < tl.getUpperSpeficiedLimit();
//            case GELE:
//                return value >= tl.getLowerSpecifiedLimit() &&
//                        value <= tl.getUpperSpeficiedLimit();
//            case GELT:
//                return value >= tl.getLowerSpecifiedLimit() &&
//                        value < tl.getUpperSpeficiedLimit();
//            case GTLE:
//                return value > tl.getLowerSpecifiedLimit() &&
//                        value <= tl.getUpperSpeficiedLimit();
//            case BOOL:
//                return value != 0.0;
//            default:
//                throw new IllegalArgumentException("Unsupported compare type for Numeric value:" + tl.getComp());
//        }
//    }
    private boolean isValuePassed(TestLimit tl, String value) {
        switch (tl.getComp()) {
            case EQ:
                return value.equals(tl.getMeasurementUnit());
            case NE:
                return !value.equals(tl.getMeasurementUnit());
//            default:
//                throw new IllegalArgumentException("Unsupported compare type for String value:" + tl.getComp());
        }
        return false;
    }

    private TestLimit getTestLimit(String useLimit) {
        if (useLimit == null) {
            return null;
        }
        if (getTestStep() != null) {
            for (TestLimit limit : getTestStep().getTestLimits()) {
                if (useLimit.equals(limit.getName())) {
                    return limit;
                }
            }
        } else {
            return null;
        }
        if (getCalledTestStep() != null) {
            for (TestLimit limit : getCalledTestStep().getTestLimits()) {
                if (useLimit.equals(limit.getName())) {
                    return limit;
                }
            }
        }
        if (parent != null) {
            return parent.getTestLimit(useLimit);
        }
        TestSequenceInstance seq = getTestSequenceInstance();
        if (seq != null) {
            for (TestLimit limit : seq.getTestSequence().getTestLimits()) {
                if (useLimit.equals(limit.getName())) {
                    return limit;
                }
            }
            TestFixture testFixture = seq.getTestFixture();
            if (testFixture != null) {
                for (TestLimit limit : testFixture.getTestLimits()) {
                    if (useLimit.equals(limit.getName())) {
                        return limit;
                    }
                }
            }
            TestStation testStation = seq.getTestStation();
            if (testStation != null) {
                for (TestLimit limit : testStation.getTestLimits()) {
                    if (useLimit.equals(limit.getName())) {
                        return limit;
                    }
                }
            }
            TestType testType = seq.getTestType();
            if (testType != null) {
                for (TestLimit limit : testType.getTestLimits()) {
                    if (useLimit.equals(limit.getName())) {
                        return limit;
                    }
                }
                Product product = testType.getProduct();
                if (product != null) {
                    for (TestLimit limit : product.getTestLimits()) {
                        if (useLimit.equals(limit.getName())) {
                            return limit;
                        }
                    }
                }
            }
            TestProject testProject = seq.getTestProject();
            if (testProject != null) {
                for (TestLimit limit : testProject.getTestLimits()) {
                    if (useLimit.equals(limit.getName())) {
                        return limit;
                    }
                }
            }
        }
//        throw new IllegalArgumentException("Specified limit is not found:" + useLimit);
        return null;
    }

    @Override
    public TestLimit getTestLimit() {
        return evaluateTestLimit();
        //return getTestStepNamePath().getTestLimit();
    }

    private TestLimit evaluateTestLimit() {
        if (getTestStep().getUseLimit() != null) {
            return getTestLimit(getTestStep().getUseLimit());
        }
//        if (getTestStep().getTestLimits() != null && getTestStep().getTestLimits().size() == 1 && getTestStep().getScript()!=null) {
//            return getTestStep().getTestLimits().get(0);
//        }
        if (getCalledTestStep() != null && getCalledTestStep().getUseLimit() != null) {
            return getTestLimit(getCalledTestStep().getUseLimit());
        }
//        if (getTestStep().getTestLimits().size() > 1) {
//            /*
//             * Multiple limits are specified,
//             * none is selected, which means no limit!
//             */
//            return null;
//        }
//        if (getCalledTestStep() != null && getCalledTestStep().getTestLimits() != null && getCalledTestStep().getTestLimits().size() == 1 && getCalledTestStep().getScript()!=null) {
//            return getCalledTestStep().getTestLimits().get(0);
//        }
        return null;
    }

    public boolean isLeaf() {
        if (getCalledTestStep() != null) {
            return getCalledTestStep().getSteps().isEmpty();
        }
        return getTestStep().getSteps().isEmpty();
    }

    @XmlTransient
    public String getValueWithUnit() {
        //if (valueWithUnit == null) {
        computeValueWithUnit();
        //}
        return valueWithUnit;
    }

    private void computeValueWithUnit() {
        valueWithUnit = getValueString();
        if (valueWithUnit != null) {
//            Log.log("Value str:" + valueWithUnit);
            return;
        }
        if (valueNumber != null) {
            String decFormatStr = this.getPropertyString(STR_DECIMAL_FORMAT, null);
            if (decFormatStr != null) {
                valueWithUnit = (new DecimalFormat(decFormatStr)).format(valueNumber);
            } else {
                valueWithUnit = valueNumber.toString();
            }
        } else {
            valueWithUnit = "";
        }
        if (valueWithUnit.length() > 0) {
            TestLimit testLimit = getTestLimit();
            if (testLimit != null && testLimit.getMeasurementUnit() != null) {
                valueWithUnit += "[" + testLimit.getMeasurementUnit() + "]";
            }
        }
        //Log.log("Value num:" + valueWithUnit);
    }

    @XmlTransient
    public String getLslWithUnit() {
        if (lslWithUnit == null) {
            computeLslWithUnit();
        }
        return lslWithUnit;
    }

    private void computeLslWithUnit() {
        TestLimit testLimit = getTestLimit();
        lslWithUnit = (testLimit != null) ? testLimit.getLslStringWithUnit() : "";
    }

    @XmlTransient
    public String getUslWithUnit() {
        if (uslWithUnit == null) {
            computeUslWithUnit();
        }
        return uslWithUnit;
    }

    private void computeUslWithUnit() {
        TestLimit testLimit = getTestLimit();
        uslWithUnit = (testLimit != null) ? testLimit.getUslStringWithUnit() : "";
    }

    @XmlTransient
    public String getLoopsString() {
        return loops == null ? "" : loops.toString();
    }

    @XmlTransient
    public Calendar getStarted() {
        if (startTime == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime);
        return cal;
    }

    @XmlTransient
    public String getStartedStringMs() {
        if (startTime == null) {
            return "";
        }
        return get24ClockMs(getStarted());
    }

    @XmlTransient
    public Calendar getFinished() {
        if (finishTime == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(finishTime);
        return cal;
    }

    @XmlTransient
    public String getFinishedStringMs() {
        if (finishTime == null) {
            return "";
        }
        return get24ClockMs(getFinished());
    }

    @XmlTransient
    public String getStatusString() {
        if (status == null) {
            return "";
        }
        return status.statusString;
    }

    public static String get24ClockMs(Calendar cal) {
        return FORMATTER_2.format(cal.get(Calendar.HOUR_OF_DAY)) + ":" + FORMATTER_2.format(cal.get(Calendar.MINUTE)) + ":" + FORMATTER_2.format(cal.get(Calendar.SECOND)) + "." + FORMATTER_3.format(cal.get(Calendar.MILLISECOND));
    }

    public static String get24Clock(Calendar cal) {
        return FORMATTER_2.format(cal.get(Calendar.HOUR_OF_DAY)) + ":" + FORMATTER_2.format(cal.get(Calendar.MINUTE)) + ":" + FORMATTER_2.format(cal.get(Calendar.SECOND));
    }

    public static String getDate(Calendar cal) {
        return Integer.toString(cal.get(Calendar.YEAR)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
    }

    public static String getDateWith24Clock(Calendar cal) {
        return getDate(cal) + " " + get24Clock(cal);
    }

    public static String getDateWith24ClockMs(Calendar cal) {
        return getDate(cal) + " " + get24ClockMs(cal);
    }
    public static final NumberFormat FORMATTER_2 =
            new DecimalFormat("00");
    public static final NumberFormat FORMATTER_3 =
            new DecimalFormat("000");

    public TestStepInstance getChild(List<String> pathList, int level) {
        if (pathList.size() == level) {
            return this;
        }
        String stepInstanceName = pathList.get(level);
        for (TestStepInstance child : steps) {
            if (child.getName().equals(stepInstanceName)) {
                return child.getChild(pathList, level + 1);
            }
        }
        return null;
    }

    public void persist(EntityManager em) {
        em.persist(this);
    }
}
