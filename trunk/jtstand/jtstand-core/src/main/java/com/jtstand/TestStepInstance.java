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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.Map.Entry;
import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.persistence.*;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.ScriptContext;

/**
 *
 * @author albert_kurucz
 *
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_id", "teststepnamepath_id"}), @UniqueConstraint(columnNames = {"testsequenceinstance_id", "teststepnamepath_id"}), @UniqueConstraint(columnNames = {"testStepInstancePosition", "parent_id"})})
public class TestStepInstance extends AbstractVariables implements Serializable, Runnable, StepInterface, Bindings {

    public static final long serialVersionUID = 20081114L;
    private static final Logger LOGGER = Logger.getLogger(TestStepInstance.class.getCanonicalName());
    public static final String STR_DECIMAL_FORMAT = "DECIMAL_FORMAT";
    public static final Class<?>[] STEP_INTERFACE_CONSTRUCTOR = {StepInterface.class};
    public static final Class<?>[] NULL_CONSTRUCTOR = {};
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private TestStep testStep;
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private TestStep calledTestStep;
    @ManyToOne(fetch = FetchType.EAGER)
    private TestSequenceInstance testSequenceInstance;
    private Long startTime;
    private Long finishTime;
    private long loops = 0;
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
//    private transient Object thisThreadLock = new Object();
    private transient Object parentLock = new Object();
//    private transient Object stepObject;
    private transient Map<String, Object> localVariablesMap = new HashMap<String, Object>();

    public Logger getLogger() {
        return LOGGER;
    }
    //@Override

    private Object readResolve() {
//        thisThreadLock = new Object();
        parentLock = new Object();
        return this;
    }

    public String evaluate(String str) {
        return str;
    }

    public String getName() {
        return getTestStepNamePath().getStepName();
    }

    public String getTestStepInstancePath() {
        return getTestStepNamePath().getStepPath();
    }

    public String evaluateName() {
        return evaluate(getTestStep().getName());
    }

    public void setNames(Map<String, TestStepNamePath> names) {
        setTestStepNamePath(names.get(getTestStepNamePath().getStepPath()));
        for (TestStepInstance child : getSteps()) {
            child.setNames(names);
        }
    }

    private String evaluatePath() {
        return evaluatePath(evaluateName());
    }

    private String evaluatePath(String eName) {
        return (parent == null) ? eName : parent.evaluatePath() + "." + eName;
    }

    public List<String> getPathList() {
        List<String> retval = (parent == null) ? new ArrayList<String>() : parent.getPathList();
        retval.add(getName());
        return retval;
    }

    public int getPosition() {
        return testStepInstancePosition;
    }

    public void setPosition(int position) {
        this.testStepInstancePosition = position;
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
        public final String statusString;

        StepStatus(String statusString) {
            this.statusString = statusString;
        }
    }
    @Basic(fetch = FetchType.EAGER)
    protected StepStatus status = StepStatus.PENDING;

    public Long getElapsed() {
        return (startTime == null) ? null : ((finishTime == null) ? System.currentTimeMillis() : finishTime) - startTime;
    }

    public String getElapsedString() {
        Long ela = getElapsed();
        return (ela == null) ? "" : ela.toString() + "ms";
    }

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

    public TestStepInstance next() {
//        System.out.println("Getting next of " + getTestStepInstancePath());
        List<TestStepInstance> children = this.getSteps();
        if (children == null) {
            throw new IllegalStateException("Children of " + getTestStepInstancePath() + " is null!");
        }
        if (!children.isEmpty()) {
            return children.get(0);
        } else {
            if (getParent() != null) {
                return getParent().next(this);
            } else {
                return null;
            }
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

    private void init(TestStep testStep)
            throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        this.testStep = testStep;
        setPosition(testStep.getPosition());
        this.calledTestStep = testStep.getCalledTestStep(this);

        String eName = evaluateName();
        if (getTestStepNamePath() == null || !eName.equals(getName())) {
            String ePath = evaluatePath(eName);
            Map<String, TestStepNamePath> names = getTestSequenceInstance().getTestSequence().getNames();
            TestStepNamePath ts = names.get(ePath);
            if (ts == null) {
                ts = new TestStepNamePath(getTestSequenceInstance().getTestSequence(), eName, ePath, evaluateTestLimit(), calledTestStep, names.size() + 1);
            }
            setTestStepNamePath(ts);
        }

        initChildren(getCalledTestStep() != null ? getCalledTestStep() : getTestStep());
    }

    private void initChildren(TestStep testStep) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        for (TestStep child : testStep.getSteps()) {
            steps.add(new TestStepInstance(child, this));
        }
    }

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

    public TestStep getTestStep() {
        return testStep;
    }

    public void setTestStep(TestStep testStep) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        this.testStep = testStep;
        if (testStep != null) {
            setPosition(testStep.getPosition());
            if (testStep.getSteps().size() > 0) {
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

    public TestSequenceInstance getTestSequenceInstance() {
        return testSequenceInstance;
    }

    public void setTestSequenceInstance(TestSequenceInstance testSequenceInstance) {
        this.testSequenceInstance = testSequenceInstance;
        for (TestStepInstance child : steps) {
            child.setTestSequenceInstance(testSequenceInstance);
        }
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
        nanoTime = System.nanoTime();
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public long getLoops() {
        return loops;
    }

    public void setLoops(long loops) {
        this.loops = loops;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
        computeValueWithUnit();
    }

    public Double getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(Double valueNumber) {
        this.valueNumber = valueNumber;
        computeValueWithUnit();
    }

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

    public String getStepClass() {
        if (getTestStep().getStepClass() != null) {
            return getTestStep().getStepClass();
        }
        if (getCalledTestStep() != null) {
            return getCalledTestStep().getStepClass();
        }
        return null;
    }

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

    public void runGroovyScript(String scriptText) {
        parse(scriptText).run();
    }

    public Script parse(String scriptText) {
        return (new GroovyShell(getTestSequenceInstance().getTestProject().getGroovyClassLoader(), getBinding())).parse(scriptText);
    }

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
        setStatus(StepStatus.RUNNING);
        setLoops(0);
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
            LOGGER.info(getTestStepInstancePath() + " is interrupted!");
            setStatus(StepStatus.ABORTED);
        } catch (Throwable ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
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
            Logger.getLogger(TestStepInstance.class.getName()).log(Level.SEVERE, null, ex);
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
            LOGGER.log(Level.SEVERE, "Merging testStepInstance failed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
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

    private void run1() throws Exception {
        TestStep.RunMode runMode = getRunMode();
        if (runMode != null && runMode.equals(TestStep.RunMode.SKIP)) {
            skip();
            return;
        }
        if (getTestStep().getMessage() != null) {
            LOGGER.info(getTestStep().getMessage());
            //... improve!
        }
        if (getPreSleep() > 0) {
            Thread.sleep(getPreSleep());
        }
        do {
            loops += 1;
            if (loops > 1 && getLoopSleep() > 0) {
                Thread.sleep(getLoopSleep());
            }
            if (getTestSequenceInstance().isAborted()) {
                return;
            }
            if (!steps.isEmpty()) {
                ThreadGroup group = new ThreadGroup(getName());
                if (isParallel()) {
//                    Log.log("Running the children of " + getPath() + " parallel");
                    List<Thread> threads = new ArrayList<Thread>();
                    for (TestStepInstance child : steps) {
                        if (child.getRunMode().equals(TestStep.RunMode.SKIP) || loops == 1 && child.getRunMode().equals(TestStep.RunMode.SKIP_FIRST)) {
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
                    for (TestStepInstance child : steps) {
                        if (child.getRunMode().equals(TestStep.RunMode.SKIP) ||
                                (loops == 1 && child.getRunMode().equals(TestStep.RunMode.SKIP_FIRST))) {
//                            Log.log("Skipping child:" + child.getName());
                            child.skip();
                        } else {

//                            Thread t = (new Thread(group, child));
//                            t.start();
//                            t.join();

                            child.run();

                            if (child.isFailed() && !child.getFailAction().equals(TestStep.FailAction.NEXT_TEST)) {
//                                Log.log("Child failed:" + child.getName() + " Value:" + child.getValue());
                                setStatus(StepStatus.FAILED);
                                break;
                            }
                        }
                        if (child.isAborted()) {
                            abort();
                            return;
                        }
                    }
                }
            }
            if (getTestSequenceInstance().isAborted() || status.equals(StepStatus.FAILED) && getFailAction().equals(TestStep.FailAction.STOP)) {
                return;
            }
            if (isRunning() && getStepClass() != null) {
                Object stepObject;
                try {
                    //TBD what is this?
                    stepObject = getVariable(getStepClass());
                    bind(stepObject);
                } catch (Exception ex) {
                    Class<?> stepClass = getTestSequenceInstance().getTestProject().getGroovyClassLoader().loadClass(getStepClass());
//                    Class<?> stepClass = Class.forName(getStepClass());
                    try {
                        Constructor<?> stepObjectContructor = stepClass.getConstructor(STEP_INTERFACE_CONSTRUCTOR);
                        stepObject = stepObjectContructor.newInstance((StepInterface) this);
                    } catch (NoSuchMethodException ex2) {
                        Constructor<?> stepObjectContructor = stepClass.getConstructor(NULL_CONSTRUCTOR);
                        stepObject = stepObjectContructor.newInstance();
                        bind(stepObject);
                    }
                }
                ((Runnable) stepObject).run();
                if (getTestSequenceInstance().isAborted()) {
                    return;
                }
            }
            if (isRunning() && getScript() != null) {
                getScript().execute(this);
                if (getTestSequenceInstance().isAborted()) {
                    return;
                }
            }
            if (runMode.equals(TestStep.RunMode.NORMAL)) {
                checkValuePassed(getValue());
//                setStatus(isValuePassed(getValue()) ? StepStatus.PASSED : StepStatus.FAILED);
            }
        } while (loops < getMaxLoops() && ((status.equals(StepStatus.PASSED) || status.equals(StepStatus.RUNNING)) && getPassAction().equals(TestStep.PassAction.LOOP) || status.equals(StepStatus.FAILED) && getFailAction().equals(TestStep.FailAction.LOOP)));
        if (getTestSequenceInstance().isAborted()) {
            return;
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

    private void bind(Object stepObject) throws IllegalArgumentException, IllegalAccessException {
        Field[] fa = stepObject.getClass().getDeclaredFields();
        for (Field f : fa) {
            if (f.getType().isAssignableFrom(StepInterface.class)) {
                f.setAccessible(true);
                f.set(stepObject, (StepInterface) this);
            }
        }
    }

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
        if (getTestSequenceInstance() != null) {
            getTestSequenceInstance().abort(thread);
        }
    }

    @Override
    public void abort() {
        setStatus(StepStatus.ABORTED);
        abort(thisThread);
        getTestSequenceInstance().abort();
    }

    @Override
    public Binding getBinding() {
        if (binding == null) {
            binding = new Binding();
            binding.setVariable("step", (StepInterface) this);
            binding.setVariable("binding", binding);
        }
        return binding;
    }

    @Override
    public Object getPropertyObject(String keyString, Binding binding) {
//        System.out.println("Getting property:'" + keyString + "'...");
//        if (binding != null) {
//            binding.setVariable("step", (StepInterface) this);
//        }
        if (getTestStep() != null) {
            for (TestProperty tsp : getTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(getTestSequenceInstance().getTestProject().getGroovyClassLoader(), binding);
                }
            }
        } else {
            System.err.println("getPropertyObject : testStep is null!");
        }
        if (getCalledTestStep() != null) {
            for (TestProperty tsp : getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(getTestSequenceInstance().getTestProject().getGroovyClassLoader(), binding);
                }
            }
        }
        if (getParent() != null) {
            return getParent().getPropertyObject(keyString, binding);
        }
        return getTestSequenceInstance().getPropertyObject(keyString, binding);
    }

    @Override
    public Object getVariable(String keyString) throws InterruptedException {
        return getVariable(keyString, false);
    }

    @Override
    public Object getVariableWait(String keyString) throws InterruptedException {
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

    public Thread getThisThread() {
        return thisThread;
    }

    public Object getVariable(String keyString, boolean wait) throws InterruptedException {
        return getVariable(keyString, wait, this);
    }

    public Object getVariable(String keyString, boolean wait, TestStepInstance step) throws InterruptedException {
//        System.out.println("Getting variable: '" + keyString + "'");
        if ("out".equals(keyString)) {
            return System.out;
        }
        if (getTestStep() != null) {
            for (TestStepProperty tsp : getTestStep().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return getVariable(keyString, wait, tsp, step);
                }
            }
        } else {
            System.err.println("getVariable : testStep is null!");
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
            if (seq.getTestSequence() != null) {
                for (TestSequenceProperty tsp : seq.getTestSequence().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return seq.getVariable(keyString, wait, tsp, step);
                    }
                }
            }
            if (seq.getTestFixture() != null) {
                for (TestFixtureProperty tsp : seq.getTestFixture().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return seq.getTestFixture().getVariable(keyString, wait, tsp, step);
                    }
                }
            }
            if (seq.getTestStation() != null) {
                for (TestStationProperty tsp : seq.getTestStation().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return seq.getTestStation().getVariable(keyString, wait, tsp, step);
                    }
                }
                if (seq.getTestStation().getTestProject() != null) {
                    for (TestProjectProperty tsp : seq.getTestStation().getTestProject().getProperties()) {
                        if (tsp.getName().equals(keyString)) {
                            /**
                             * variables defined at project level are still stored at station level
                             */
                            return seq.getTestStation().getVariable(keyString, wait, tsp, step);
                        }
                    }
                }
            }
        } else {
            System.err.println("getVariable : testSequenceInstance is null!");
        }
        throw new IllegalArgumentException("Undefined variable:" + keyString);
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
            System.err.println("getVariable : testStep is null!");
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
            if (seq.getTestSequence() != null) {
                for (TestSequenceProperty tsp : seq.getTestSequence().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        seq.releaseVariable(keyString, tsp, step);
                        return;
                    }
                }
            }
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
            System.err.println("releaseVariable : testSequenceInstance is null!");
        }
        throw new IllegalArgumentException("Undefined variable:" + keyString);
    }

    @Override
    public Object put(String key, Object variableValue) {
        System.out.println("Setting variable: '" + key + "' to " + variableValue);
        if ("value".equals(key)) {
            Object retval = getValue();
            setValue(variableValue);
            return retval;
        }
        for (TestStepProperty tsp : getTestStep().getProperties()) {
            if (tsp.getName().equals(key)) {
                if ((tsp.isFinal() == null || tsp.isFinal()) && super.containsKey(key)) {
                    throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                }
                return super.put(key, variableValue);
            }
        }
        if (getCalledTestStep() != null) {
            for (TestStepProperty tsp : getCalledTestStep().getProperties()) {
                if (tsp.getName().equals(key)) {
                    if ((tsp.isFinal() == null || tsp.isFinal()) && super.containsKey(key)) {
                        throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                    }
                    return super.put(key, variableValue);
                }
            }
        }
        if (parent != null) {
            return parent.put(key, variableValue);
        }
        TestSequenceInstance seq = getTestSequenceInstance();
        if (seq != null) {
            if (seq.getTestSequence() != null) {
                for (TestSequenceProperty tsp : seq.getTestSequence().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.put(key, variableValue);
                    }
                }
            }
            if (seq.getTestFixture() != null) {
                for (TestFixtureProperty tsp : seq.getTestFixture().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.getTestFixture().containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.getTestFixture().put(key, variableValue);
                    }
                }
            }
            if (seq.getTestStation() != null) {
                for (TestStationProperty tsp : seq.getTestStation().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.getTestStation().containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.getTestStation().put(key, variableValue);
                    }
                }
            }
            if (seq.getTestProject() != null) {
                for (TestProjectProperty tsp : seq.getTestProject().getProperties()) {
                    if (tsp.getName().equals(key)) {
                        if ((tsp.isFinal() == null || tsp.isFinal()) && seq.getTestStation().containsKey(key)) {
                            throw new IllegalStateException("Cannot change final variable: '" + key + "'");
                        }
                        return seq.getTestStation().put(key, variableValue);
                    }
                }
            }
        }
        return localVariablesMap.put(key, variableValue);
        //throw new IllegalArgumentException("Undefined variable:" + key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        for (Entry<? extends String, ? extends Object> variableEntry : toMerge.entrySet()) {
            put(variableEntry.getKey(), variableEntry.getValue());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return "value".equals(key) ||
                localVariablesMap.containsKey((String) key) ||
                containsKeyPublic(key);
    }

    public boolean containsKeyPublic(Object key) {
        if (super.containsKey((String) key)) {
            return true;
        }
        if (parent != null) {
            return parent.containsKeyPublic(key);
        }
        TestSequenceInstance seq = getTestSequenceInstance();
        return seq != null && (seq.containsKey((String) key) ||
                seq.getTestFixture() != null && seq.getTestFixture().containsKey((String) key) ||
                seq.getTestStation() != null && seq.getTestStation().containsKey((String) key));
    }

    @Override
    public Object get(Object key) {
        if ("$type$".equals(key)) {
            return getClass().getName();
        }
        if ("context".equals(key)) {
            return ScriptContext.ENGINE_SCOPE;
        }
        if ("step".equals(key)) {
            return this;
        }
        if ("id".equals(key)) {
            return id;
        }
        if ("testStep".equals(key)) {
            return testStep;
        }
        if ("calledTestStep".equals(key)) {
            return calledTestStep;
        }
        if ("testSequenceInstance".equals(key)) {
            return testSequenceInstance;
        }
        if ("startTime".equals(key)) {
            return startTime;
        }
        if ("finishTime".equals(key)) {
            return finishTime;
        }
        if ("loops".equals(key)) {
            return loops;
        }
        if ("parent".equals(key)) {
            return parent;
        }
        if ("steps".equals(key)) {
            return steps;
        }
        if ("valueString".equals(key)) {
            return valueString;
        }
        if ("valueNumber".equals(key)) {
            return valueNumber;
        }
        if ("position".equals(key)) {
            return testStepInstancePosition;
        }

        if (localVariablesMap.containsKey((String) key)) {
            return localVariablesMap.get((String) key);
        }
        try {
            return getVariable((String) key);
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    @Override
    public Object remove(Object key) {
        return localVariablesMap.remove((String) key);
    }

    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        /* 'value' is always there, and cannot be removed */
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (String key : keySet()) {
            if (value.equals(get(key))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        /* we cannot support the clear, because 'value' cannot be removed */
        //throw new UnsupportedOperationException("'clear' operation is not supported.");
        localVariablesMap.clear();
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = keySetPublic();
        keys.add("value");
        keys.addAll(localVariablesMap.keySet());
        return keys;
    }

    private Set<String> keySetPublic() {
        Set<String> keys = super.keySet();
        if (parent != null) {
            keys.addAll(parent.keySetPublic());
        } else {
            TestSequenceInstance seq = getTestSequenceInstance();
            if (seq != null) {
                keys.addAll(seq.keySet());
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
    public Collection<Object> values() {
        Collection<Object> values = new ArrayList<Object>();
        for (String key : keySet()) {
            values.add(get(key));
        }
        return values;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();
        for (String key : keySet()) {
            entries.add(new HashMap.SimpleEntry(key, get(key)));
        }
        return entries;
    }

    @Override
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
                return value > tl.getLowerSpecifiedLimit() &&
                        value < tl.getUpperSpeficiedLimit();
            case GELE:
                return value >= tl.getLowerSpecifiedLimit() &&
                        value <= tl.getUpperSpeficiedLimit();
            case GELT:
                return value >= tl.getLowerSpecifiedLimit() &&
                        value < tl.getUpperSpeficiedLimit();
            case GTLE:
                return value > tl.getLowerSpecifiedLimit() &&
                        value <= tl.getUpperSpeficiedLimit();
            case BOOL:
                return value != 0.0;
            default:
                throw new IllegalArgumentException("Unsupported compare type for Numeric value:" + tl.getComp());
        }

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
            default:
                throw new IllegalArgumentException("Unsupported compare type for String value:" + tl.getComp());
        }
    }

    private TestLimit getTestLimit(String useLimit) {
        if (useLimit == null) {
            return null;
        }
        if (getTestStep() != null) {
            for (TestLimit limit : getTestStep().getTestLimits()) {
                if (limit.getName().equals(useLimit)) {
                    return limit;
                }
            }
        } else {
            return null;
        }
        if (getCalledTestStep() != null) {
            for (TestLimit limit : getCalledTestStep().getTestLimits()) {
                if (limit.getName().equals(useLimit)) {
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
                if (limit.getName().equals(useLimit)) {
                    return limit;
                }
            }
        }
        throw new IllegalArgumentException("Specified limit is not found:" + useLimit);
    }

    @Override
    public TestLimit getTestLimit() {
        return getTestStepNamePath().getTestLimit();
    }

    private TestLimit evaluateTestLimit() {
        if (getTestStep().getUseLimit() != null) {
            return getTestLimit(getTestStep().getUseLimit());
        }
        if (getTestStep().getTestLimits() != null && getTestStep().getTestLimits().size() == 1) {
            return getTestStep().getTestLimits().get(0);
        }
        if (getCalledTestStep() != null && getCalledTestStep().getUseLimit() != null) {
            return getTestLimit(getCalledTestStep().getUseLimit());
        }
        if (getTestStep().getTestLimits().size() > 1) {
            /*
             * Multiple limits are specified,
             * none is selected, which means no limit!
             */
            return null;
        }
        if (getCalledTestStep() != null && getCalledTestStep().getTestLimits() != null && getCalledTestStep().getTestLimits().size() == 1) {
            return getCalledTestStep().getTestLimits().get(0);
        }
        return null;
    }

    public boolean isLeaf() {
        if (getCalledTestStep() != null) {
            return getCalledTestStep().getSteps().size() == 0;
        }
        return getTestStep().getSteps().size() == 0;
    }

    public String getValueWithUnit() {
        if (valueWithUnit == null) {
            computeValueWithUnit();
        }
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
                valueWithUnit += testLimit.getMeasurementUnit();
            }
        }
        //Log.log("Value num:" + valueWithUnit);
    }

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

    public String getLoopsString() {
        return Long.toString(loops);
    }

    public Calendar getStarted() {
        if (startTime == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startTime);
        return cal;
    }

    public String getStartedStringMs() {
        if (startTime == null) {
            return "";
        }
        return get24ClockMs(getStarted());
    }

    public Calendar getFinished() {
        if (finishTime == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(finishTime);
        return cal;
    }

    public String getFinishedStringMs() {
        if (finishTime == null) {
            return "";
        }
        return get24ClockMs(getFinished());
    }

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
