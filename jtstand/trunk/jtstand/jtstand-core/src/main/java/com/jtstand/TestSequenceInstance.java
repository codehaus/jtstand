/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestSequenceInstance.java is part of JTStand.
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

import javax.script.Bindings;
import javax.script.ScriptException;
import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.SimpleBindings;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"createtime", "finishtime", "teststation_id", "testfixture_id", "serialnumber", "employeenumber", "testsequence_id", "testtype_id", "testproject_id", "failurecode", "failurestep_id"}))
public class TestSequenceInstance extends AbstractVariables implements Serializable, Runnable, Iterable<TestStepInstance> {

    public static final long serialVersionUID = 20081114L;
    public static final String STR_FIXTURE = "fixture";
    public static final String STR_STATION = "station";
    public static final String STR_INIT = "init";
    public static final Object FILE_LOCK = new Object();
    private static final Logger LOGGER = Logger.getLogger(TestSequenceInstance.class.getCanonicalName());
    private static JAXBContext jc;
    private static Marshaller m;
    private static Unmarshaller um;

    public TestSequenceInstance() {
        super();
    }

    private static JAXBContext getJAXBContext()
            throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(TestSequenceInstance.class);
        }
        return jc;
    }

    public static Marshaller getMarshaller()
            throws JAXBException {
        if (m == null) {
            m = getJAXBContext().createMarshaller();
            if (TestProject.getSchemaLocation() != null) {
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, TestProject.getSchemaLocation());
            }
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        return m;
    }

    public static Unmarshaller getUnmarshaller()
            throws JAXBException {
        if (um == null) {
            um = getJAXBContext().createUnmarshaller();
        }
        return um;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TestStepInstance setupStepInstance;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TestStepInstance mainStepInstance;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TestStepInstance cleanupStepInstance;
    private String serialNumber;
    private String employeeNumber;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TestSequence testSequence;
    @ManyToOne(fetch = FetchType.LAZY)
    private TestFixture testFixture;
    @ManyToOne(fetch = FetchType.EAGER)
    private TestStation testStation;
    @ManyToOne(fetch = FetchType.LAZY)
    private TestType testType;
//    private Product product;
//    private String testType;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private TestProject testProject;
    private long createTime;
    private Long finishTime = null;
    private String failureCode;
    @ManyToOne
    private TestStepInstance failureStep;
    private transient ThreadGroup group = new ThreadGroup(toString());
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
    private transient ConcurrentHashMap<FileRevision, TestStep> testSteps = new java.util.concurrent.ConcurrentHashMap<FileRevision, TestStep>();
    public transient EntityManager em;
    private transient PersistingPolicy persistingPolicy = PersistingPolicy.NEVER;
    private transient Object setupStepInstanceLock = new Object();
    private transient Object mainStepInstanceLock = new Object();
    private transient Object cleanupStepInstanceLock = new Object();

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public void setSequenceType(SequenceType sequenceType) {
        this.sequenceType = sequenceType;
    }

    public boolean isInitType() {
        return TestSequenceInstance.SequenceType.FIXTURE_SETUP.equals(getSequenceType()) || TestSequenceInstance.SequenceType.STATION_SETUP.equals(getSequenceType());
    }

    public String getTestTypeName() {
        return getTestType() != null ? getTestType().getName() : isInitType() ? STR_INIT : "";
    }

    public String getProductName() {
        if (getTestType() != null && getTestType().getProduct() != null) {
            return getTestType().getProduct().getPartNumberWithRevision();
        }
        if (SequenceType.FIXTURE_SETUP.equals(sequenceType)) {
            return STR_FIXTURE;
        }
        if (SequenceType.STATION_SETUP.equals(sequenceType)) {
            return STR_STATION;
        }
        return "";
    }

    public String getPartNumber() {
        if (getTestType() != null && getTestType().getProduct() != null) {
            return getTestType().getProduct().getPartNumber();
        }
        if (SequenceType.FIXTURE_SETUP.equals(sequenceType)) {
            return STR_FIXTURE;
        }
        if (SequenceType.STATION_SETUP.equals(sequenceType)) {
            return STR_STATION;
        }
        return "";
    }

    public String getPartRevision() {
        if (getTestType() != null && getTestType().getProduct() != null) {
            return getTestType().getProduct().getPartRevision();
        }
        return "";
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    private Object readResolve() {
        setupStepInstanceLock = new Object();
        mainStepInstanceLock = new Object();
        cleanupStepInstanceLock = new Object();
        return this;
    }

    public TestStep putTestStep(FileRevision creator, TestStep testStep) {
        return testSteps.put(creator, testStep);
    }

    public TestStep getTestStep(Object creator) {
        //TODO - this is a little messy!
        return testSteps.get(creator);
    }

    public static enum PersistingPolicy {

        NEVER,
        STEP,
        SEQUENCE
    }

    public void addPropertyChangeListener(
            PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

//    public static TestStep getTestStep(FileRevision fileRevision) {
//        return stepFiles.get(fileRevision);
//    }
//
//    public static TestStep putTestStep(TestStep testStep) {
//        return stepFiles.put(testStep.getCreator(), testStep);
//    }
    public static enum SequenceStatus {

        PENDING("Pending"),
        RUNNING("Running"),
        PASSED("Passed"),
        FAILED("Failed"),
        ABORTED("Aborted"),
        STEPBYSTEP("Step by step"),
        STEPBYSTEP_FINISHED("Finished");
        public final String statusString;

        SequenceStatus(
                String statusString) {
            this.statusString = statusString;
        }
    }
    @Basic(fetch = FetchType.EAGER)
    private SequenceStatus status = SequenceStatus.PENDING;

    public static enum SequenceType {

        NORMAL, STATION_SETUP, FIXTURE_SETUP
    }
    @Basic(fetch = FetchType.EAGER)
    private SequenceType sequenceType;

    public boolean isPersistPerStep() {
        return PersistingPolicy.STEP.equals(persistingPolicy);
    }

    private PersistingPolicy getPersistingPolicy() {
        return persistingPolicy;
    }

    public void setPersistingPolicy(PersistingPolicy persistingPolicy) {
        this.persistingPolicy = persistingPolicy;
    }

    public boolean isPersistPerSequence() {
        return PersistingPolicy.SEQUENCE.equals(getPersistingPolicy());
    }

    public Long getId() {
        return id;
    }

    @Override
    public Iterator<TestStepInstance> iterator() {
        return new TestSequenceInstanceIterator(this);
    }

    public void connect(EntityManager em)
            throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        long startTime = System.currentTimeMillis();
        System.out.println("Connect...");
        if (getTestProject() != null && getTestProject().getId() == null) {
            TestProject tp = TestProject.query(em, getTestProject().getCreator());
            if (tp != null) {
                //System.out.println("Connecting testProject...");
                setTestProject(tp);
            } else {
                //System.out.println("Unable to connect testProject");
            }
        }
////        System.out.println("Connecting libraries...");
//        List<Library> toAdd = new ArrayList<Library>();
//        List<Library> toRemove = new ArrayList<Library>();
//        for (Library lib : getTestProject().getLibraries()) {
////            System.out.println(lib.getName());
//            if (lib.getId() == null) {
//                Library library = Library.query(em, lib.getCreator());
//                if (library != null) {
//                    System.out.println("Connecting library '" + library.getName() + "'...");
//                    toRemove.add(lib);
//                    toAdd.add(library);
//                } else {
//                    System.out.println("Unable to connect library");
//                }
//            }
//        }
//        System.out.println("removing...");
//        getTestProject().getLibraries().removeAll(toRemove);
//        System.out.println("adding...");
//        getTestProject().getLibraries().addAll(toAdd);
        if (getTestSequence() != null && getTestSequence().getId() == null) {
            TestSequence ts = TestSequence.query(em, getTestSequence().getCreator());
            if (ts != null) {
                //System.out.println("Connecting testSequence...");
                setTestSequence(ts);
            } else {
                //System.out.println("Unable to connect testSequence");
            }
        }
        for (TestStepInstance tsi : this) {
//            Log.log("step: " + tsi.getName());
            TestStep calledTestStep = tsi.getCalledTestStep();
            if (calledTestStep != null && calledTestStep.getId() == null) {
                TestStep ts = TestStep.query(em, calledTestStep.getCreator());
                if (ts != null) {
                    //System.out.println("Connecting testStep '" + ts.getName() + "'...");
                    tsi.setCalledTestStep(ts);
                } else {
                    //System.out.println("Unable to connect testStep");
                }
            }
        }
        System.out.println("Connecting finished in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
    }

    public boolean merge(EntityManager em) {
        long startTransaction = System.currentTimeMillis();
        System.out.println("Merge...");
        try {
            em.getTransaction().begin();
            LOGGER.fine("Merging testSequenceInstance...");
            em.merge(this);
            LOGGER.fine("Merging testSequenceInstance, committing Transaction...");
            em.getTransaction().commit();
            System.out.println("Merging testSequenceInstance committed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            return true;
        } catch (PersistenceException ex) {
            LOGGER.log(Level.SEVERE, "Merging testSequenceInstance failed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms", ex);
            ex.printStackTrace();
            if (em.getTransaction().isActive()) {
                LOGGER.info("Merging is going to roll back the transaction...");
                em.getTransaction().rollback();
                LOGGER.info("Merging rolled back the transaction in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            }
        }
        return false;
    }

    public void persistOrSerialize(final EntityManager em) {
        if (em != null && em.isOpen()) {
            Thread t = new Thread() {

                @Override
                public void run() {
                    if (!persist(em)) {
                        toFile();
                    }
                }
            };
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            toFile();
        }
    }

    public boolean persist(EntityManager em) {
        long startTransaction = System.currentTimeMillis();
        try {
            connect(em);
            em.getTransaction().begin();
            LOGGER.log(Level.FINE, "Persisting testSequenceInstance...");
            em.persist(this);
            LOGGER.log(Level.FINE, "Persisting testSequenceInstance, committing Transaction...");
            //em.flush();
            em.getTransaction().commit();
            LOGGER.info("Persisting testSequenceInstance committed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Persisting testSequenceInstance failed in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            LOGGER.log(Level.SEVERE, "With exception: " + ex);
            if (em.getTransaction().isActive()) {
                LOGGER.info("Persisting is going to roll back the transaction...");
                em.getTransaction().rollback();
                LOGGER.info("Persisting rolled back the transaction in " + Long.toString(System.currentTimeMillis() - startTransaction) + "ms");
            }
        }
        return false;
    }

//    public TestSequenceInstance() {
//    }
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        if (failureCode != null && failureCode.length() > 255) {
            this.failureCode = failureCode.substring(0, 255);
        } else {
            this.failureCode = failureCode;
        }
    }

    public String getFailureStepPath() {
        return getFailureStep().getTestStepInstancePath();
    }

    public TestStepInstance getFailureStep() {
        return failureStep;
    }

    public void setFailureStep(TestStepInstance failureStep) {
        this.failureStep = failureStep;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getTestFixtureName() {
        if (testFixture == null) {
            return null;
        }
        return testFixture.getFixtureName();
    }

    public TestFixture getTestFixture() {
        return testFixture;
    }

    public void setTestFixture(TestFixture testFixture) {
        this.testFixture = testFixture;
    }

    public String getHostName() {
        return testStation != null ? testStation.getHostName() : null;
    }

    public String getFixtureName() {
        return testFixture != null ? testFixture.getFixtureName() : null;
    }

    public TestStation getTestStation() {
        return testStation;
    }

    public void setTestStation(TestStation testStation) {
        this.testStation = testStation;
    }

    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
        if (testProject != null) {
//            Log.log("Connecting testStation...");
            if (testStation != null) {
                setTestStation(testProject.getTestStation(testStation.getHostName()));
            }
//            Log.log("Connecting testFixture...");
            if (testFixture != null) {
                setTestFixture(testStation.getTestFixture(testFixture.getFixtureName()));
            }
//            Log.log("Connecting testType...");
            if (testType != null) {
                setTestType(testProject.getTestType(testType));
            }
        }
    }
    private static long lastCreateTime = 0;
    private static Object lastCreateTimeLock = new Object();

    public TestSequenceInstance(String serialNumber, String employeeNumber, TestTypeReference testTypeReference)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        this(SequenceType.NORMAL, serialNumber, employeeNumber, testTypeReference, (testTypeReference.getTestFixture() != null ? testTypeReference.getTestFixture().getTestStation() : testTypeReference.getTestStation()));
    }

    private TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestTypeReference testTypeReference, TestStation testStation)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        this(sequenceType, serialNumber, employeeNumber, testStation.getTestProject().getTestType(testTypeReference), testTypeReference.getTestFixture(), testStation, testStation.getTestProject());
    }

    private TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestType testType, TestFixture testFixture, TestStation testStation, TestProject testProject)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        this(sequenceType, serialNumber, employeeNumber, testType.getTestSequence().getTestSequence(), testType, testFixture, testStation, testProject);
    }

    public TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestSequence testSequence, TestType testType, TestFixture testFixture, TestStation testStation, TestProject testProject)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        super();
        if (serialNumber == null) {
            throw new IllegalArgumentException("Serial number of a new TestSequenceInstance cannot be null");
        }
        if (testSequence == null) {
            throw new IllegalArgumentException("Test Sequence of a new Test Sequence Instance cannot be null");
        }
//        logger.info("new test sequence instance...");
        createTime = System.currentTimeMillis();
        /* Make sure createTime is unique within this station! */
        synchronized (lastCreateTimeLock) {
            while (createTime == lastCreateTime) {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                }
                createTime = System.currentTimeMillis();
            }
            lastCreateTime = createTime;
        }
        setSequenceType(sequenceType);
        setTestSequence(testSequence);
        setSerialNumber(serialNumber);
        if (testProject != null && testProject.getAuthentication() != null && testProject.getAuthentication().getOperator() != null) {
            setEmployeeNumber(testProject.getAuthentication().getOperator());
        } else {
            setEmployeeNumber(employeeNumber);
        }
        setTestFixture(testFixture);
        setTestStation(testStation);
        setTestType(testType);
//        setProduct(product);
        setTestProject(testProject);
        if (testSequence.getSetupStep() != null) {
//            System.out.println("setupstep: " + testSequence.getSetupStep());
            setSetupStepInstance(new TestStepInstance(testSequence.getSetupStep(), this));
        }
        if (!isInitType()) {
            if (testSequence.getMainStep() != null) {
//                System.out.println("mainstep: " + testSequence.getMainStep());
                setMainStepInstance(new TestStepInstance(testSequence.getMainStep(), this));
            }
            if (testSequence.getCleanupStep() != null) {
//                System.out.println("cleanupstep: " + testSequence.getCleanupStep());
                setCleanupStepInstance(new TestStepInstance(testSequence.getCleanupStep(), this));
            }
        }
//        System.out.println("setupstepinstance: " + getSetupStepInstance());
//        System.out.println("mainstepinstance: " + getMainStepInstance());
//        System.out.println("cleanupstepinstance: " + getCleanupStepInstance());
        setPersistingPolicy(PersistingPolicy.valueOf(getPropertyString(TestProject.STR_PERSISTING_POLICY, PersistingPolicy.NEVER.toString())));
//        logger.log(Level.INFO, "PersistingPolicy: " + getPersistingPolicy());
    }
//    public TestSequenceInstance factory() throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException{
//        return new TestSequenceInstance(serialNumber, employeeNumber, testSequence, testType, testFixture, testStation, testProject);
//    }

    @Override
    public int hashCode() {
        int hash = (int) createTime;
        hash += (testStation != null ? testStation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestSequenceInstance)) {
            return false;
        }
        TestSequenceInstance other = (TestSequenceInstance) object;
        if (this.createTime != other.getCreateTime()) {
            return false;
        }
        if ((this.testStation == null && other.getTestStation() != null) || (this.testStation != null && !this.testStation.equals(other.getTestStation()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        System.out.println(getClass().getCanonicalName() + " toString...");
        return "[" + getClass().getCanonicalName() + ":" + getCreateTime() + ":" + getTestStation() + "]";
//        System.out.println(getClass().getCanonicalName() + " toString.");
//        return str;
//        if (getStartedString() != null) {
//            out += getStartedString();
//        }
//        if (serialNumber != null) {
//            out += "  '" + serialNumber + "'";
//        }
//        if (product != null) {
//            out += " of '" + product.getPartNumber();
//            if (product.getPartRevision() != null) {
//                out += "@" + product.getPartRevision();
//            }
//            out += "'";
//        }
//        if (testStation != null) {
//            out += " on '" + testStation.getHostName();
//            if (testFixture != null) {
//                out += "@" + testFixture.getFixtureName();
//            }
//            out += "'";
//        }
//        if (employeeNumber != null) {
//            out += " by '" + employeeNumber + "'";
//        }
//        return out;
    }

    public TestSequence getTestSequence() {
        return testSequence;
    }

    public void setTestSequence(TestSequence newValue) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        testSequence = newValue;
        //... This must be much more complicated!
        if (setupStepInstance != null) {
            setupStepInstance.setTestStep(testSequence.getSetupStep());
            setupStepInstance.setNames(newValue.getNames());
        }
        if (!isInitType()) {
            if (mainStepInstance != null) {
                mainStepInstance.setTestStep(testSequence.getMainStep());
                mainStepInstance.setNames(newValue.getNames());
            }
            if (cleanupStepInstance != null) {
                cleanupStepInstance.setTestStep(testSequence.getCleanupStep());
                cleanupStepInstance.setNames(newValue.getNames());
            }
        }
    }

    public TestStepInstance getSetupStepInstance() {
        synchronized (setupStepInstanceLock) {
            return setupStepInstance;
        }
    }

    public void setSetupStepInstance(TestStepInstance setupStepInstance) {
        this.setupStepInstance = setupStepInstance;
    }

    public TestStepInstance getMainStepInstance() {
        synchronized (mainStepInstanceLock) {
            return mainStepInstance;
        }
    }

    public void setMainStepInstance(TestStepInstance mainStepInstance) {
        this.mainStepInstance = mainStepInstance;
    }

    public TestStepInstance getCleanupStepInstance() {
        synchronized (cleanupStepInstanceLock) {
            return cleanupStepInstance;
        }
    }

    public void setCleanupStepInstance(TestStepInstance cleanupStepInstance) {
        this.cleanupStepInstance = cleanupStepInstance;
    }

    @Override
    public void run() {
//        logger.info("running the sequence...");
        Thread.currentThread().setName(toString());
        setStatus(SequenceStatus.RUNNING);
        if (isPersistPerStep()) {
            persistOrSerialize(getEntityManager());
        }
        startAndJoin(setupStepInstance);
        if (!isInitType()) {
            startAndJoin(mainStepInstance);
            startAndJoin(cleanupStepInstance);
        }
        finish();
    }

//    public void runSetup() {
//        Thread.currentThread().setName(toString());
//        setStatus(SequenceStatus.RUNNING);
//        if (isPersistPerStep()) {
//            em = TestProject.getEntityManagerFactory().createEntityManager();
//            persistOrSerialize(em);
//        }
//        startAndJoin(setupStepInstance);
//        finish();
//    }
//
//    public void runCleanup() {
//        Thread.currentThread().setName(toString());
//        setStatus(SequenceStatus.RUNNING);
//        if (isPersistPerStep()) {
//            em = TestProject.getEntityManagerFactory().createEntityManager();
//            persistOrSerialize(em);
//        }
//        startAndJoin(cleanupStepInstance);
//        finish();
//    }
    public void startAndJoin(TestStepInstance step) {
        if (step != null) {
            step.run();
        }
//        Thread t = start(step);
//        if (t != null) {
//            try {
//                t.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    public Thread start(TestStepInstance step) {
        if (!isAborted() && step != null) {
            Thread t = new Thread(group, step);
            t.start();
            return t;
        }
        return null;
    }

    public SequenceStatus getStatus() {
        return status;
    }

    public void setStatus(SequenceStatus status) {
        SequenceStatus oldStatus = this.status;
        this.status = status;
        support.firePropertyChange("status", oldStatus, this.status);
    }

    public void finish() {
        dispose();
        finishTime = System.currentTimeMillis();
        if (isRunning()) {
            switch (isInitType() ? setupStepInstance.getStatus() : mainStepInstance.getStatus()) {
                case PASSED:
                    setStatus(SequenceStatus.PASSED);
                    break;
                case FAILED:
                    setStatus(SequenceStatus.FAILED);
                    break;
                case ABORTED:
                case PENDING:
                    setStatus(SequenceStatus.ABORTED);
                    break;
                case STEPBYSTEP_FINISHED:
                    setStatus(SequenceStatus.STEPBYSTEP_FINISHED);
                    break;
                default:
                    throw new IllegalStateException("mainStep finished with illegal state: " + mainStepInstance.getStatus().statusString);
            }
        } else if (isStepByStep()) {
            setStatus(TestSequenceInstance.SequenceStatus.STEPBYSTEP_FINISHED);
            if (group.activeCount() > 0) {
                group.interrupt();
            }
        } else {
            return;
        }

        if (isPersistPerSequence()) {
            toFile();
        } else if (isPersistPerStep()) {
            Thread t = new Thread() {

                @Override
                public void run() {
                    if (!merge(em)) {
                        em.close();
                        toFile();
                    } else {
                        em.close();
                    }
                    em = null;
                }
            };
            t.start();
        }
    }

    public void abort() {
        if (isRunning()) {
            setStatus(SequenceStatus.ABORTED);
            if (group != null) {
                group.interrupt();
            }
        }
    }

    public boolean isAborted() {
        return SequenceStatus.ABORTED.equals(status);
    }

    public boolean isPassed() {
        return SequenceStatus.PASSED.equals(status);
    }

    public boolean isFailed() {
        return SequenceStatus.FAILED.equals(status);
    }

    public boolean isRunning() {
        return SequenceStatus.RUNNING.equals(status);
    }

    public boolean isStepByStep() {
        return SequenceStatus.STEPBYSTEP.equals(status);
    }

    public boolean isSequenceActive() {
        return isRunning() || isStepByStep();
    }

    @Override
    public Bindings getBindings() {
        if (bindings == null) {
            bindings = new SimpleBindings();
            bindings.put("sequence", this);
        }
        return bindings;
    }

    public boolean containsProperty(String key) {
        if("sequence".equals(key)){
            return true;
        }
        if (getTestSequence() != null) {
            for (TestProperty tsp : getTestSequence().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
        }
        if (getTestType() != null) {
            for (TestProperty tsp : getTestType().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
            if (getTestType().getProduct() != null) {
                for (TestProperty tsp : getTestType().getProduct().getProperties()) {
                    if (tsp.getName().equals(key)) {
                    return true;
                    }
                }
            }
        }
        if (getTestFixture() != null) {
            for (TestProperty tsp : getTestFixture().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
        }
        if (getTestStation() != null) {
            for (TestProperty tsp : getTestStation().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
        }
        if (getTestProject() != null) {
            for (TestProperty tsp : getTestProject().getProperties()) {
                if (tsp.getName().equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object getPropertyObject(String keyString, Bindings bindings) throws ScriptException {
        if (bindings != null) {
            bindings.put("sequence", this);
        }
        if (getTestSequence() != null) {
            for (TestProperty tsp : getTestSequence().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        if (getTestType() != null) {
            for (TestProperty tsp : getTestType().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
            if (getTestType().getProduct() != null) {
                for (TestProperty tsp : getTestType().getProduct().getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return tsp.getPropertyObject(bindings);
                    }
                }
            }
        }
        if (getTestFixture() != null) {
            for (TestProperty tsp : getTestFixture().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        if (getTestStation() != null) {
            for (TestProperty tsp : getTestStation().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        if (getTestProject() != null) {
            for (TestProperty tsp : getTestProject().getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
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

    public String getFileName() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCreateTime());
        if (serialNumber != null) {
            return getSerialNumber() + "@" + printTimeToFileName(cal);
        } else {
            return printTimeToFileName(cal);
        }
    }

    public static String printTimeToFileName(Calendar cal) {
        return Integer.toString(cal.get(Calendar.YEAR)) +
                TestStepInstance.FORMATTER_2.format(1 + cal.get(Calendar.MONTH)) +
                TestStepInstance.FORMATTER_2.format(cal.get(Calendar.DAY_OF_MONTH)) +
                "T" +
                TestStepInstance.FORMATTER_2.format(cal.get(Calendar.HOUR_OF_DAY)) +
                TestStepInstance.FORMATTER_2.format(cal.get(Calendar.MINUTE)) +
                TestStepInstance.FORMATTER_2.format(cal.get(Calendar.SECOND)) +
                "." +
                TestStepInstance.FORMATTER_3.format(cal.get(Calendar.MILLISECOND));
    }

    public boolean toFile() {
        try {
            return toFile(testStation.getSaveDirectory());
        } catch (ScriptException ex) {
            Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//    public boolean toFile(Object saveDirectory) {
//        if (saveDirectory == null) {
//            return false;
//        }
//        if (File.class.isAssignableFrom(saveDirectory.getClass())) {
//            return toFile((File) saveDirectory);
//        }
//        return toFile(new File(saveDirectory.toString()));
//    }
//
//    public boolean toFile(String saveDirectory) {
//        return (saveDirectory != null) && toFile(new File(saveDirectory));
//    }
//
    public boolean toFile(File saveDirectory) {
        if (saveDirectory == null) {
            return false;
        }
        long startTime = System.currentTimeMillis();
        if (saveDirectory.isFile()) {
            throw new IllegalArgumentException("Output directory is not a directory: a File is specified!");
        }
        if (!saveDirectory.isDirectory()) {
            if (saveDirectory.mkdirs()) {
                LOGGER.info("Output directory is created: " + saveDirectory.getPath());
            } else {
                throw new IllegalArgumentException("Output directory does not exist and cannot be created: " + saveDirectory);
            }
        }
//        Log.log("Serializing Test Sequence Instance to '" + saveDirectory.toString() + "'...");
        long freeMB = saveDirectory.getFreeSpace() / 1048576L;
        System.out.println("Free space of '" + saveDirectory.getPath() + "': " + freeMB + " MB");
        try {
            String filepath = saveDirectory.getPath() + File.separatorChar + getFileName() + ".state";
            File file = new File(filepath);
            synchronized (FILE_LOCK) {
//            Log.log("Saving sequence instance into file: " + file.getPath());
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream s = new ObjectOutputStream(fos);
                s.writeObject(this);
                //s.writeObject(getLog());
                s.flush();
                s.close();
            }
//            Log.log("Serializing Test Sequence Instance finished successfully in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
            return true;
        } catch (IOException ex1) {
            LOGGER.log(Level.SEVERE, "Serializing Test Sequence Instance failed in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
            LOGGER.log(Level.SEVERE, ex1.getMessage());
            ex1.printStackTrace();
        }
        return false;
    }

    public static TestSequenceInstance fromFile(File file) throws IOException, ClassNotFoundException {
//        Log.log("Opening state file: " + file.getPath());
        synchronized (FILE_LOCK) {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(in);
            TestSequenceInstance tsi = (TestSequenceInstance) s.readObject();
            s.close();
//        Log.log("Successfully opened : " + tsi.getFileName());
            return tsi;
        }
    }

    public static TestSequenceInstance fromFile(File file, boolean delete) throws IOException, ClassNotFoundException {
        TestSequenceInstance tsi = fromFile(file);
        if (delete) {
            file.delete();
        }
        return tsi;
    }

    public static TestSequenceInstance getInstance(File saveDirectory, boolean delete) {
        if (!saveDirectory.isDirectory()) {
            throw new IllegalArgumentException("specified file is not a directory: " + saveDirectory);
        }
        File[] filelist = saveDirectory.listFiles();
        if (filelist.length > 0) {
            for (File file : filelist) {
                if (file.getName().endsWith(".state")) {
                    try {
                        return fromFile(file, delete);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }

    public static List<TestSequenceInstance> getInstances(File saveDirectory, boolean delete) {
        if (!saveDirectory.isDirectory()) {
            throw new IllegalArgumentException("specified file is not a directory:" + saveDirectory);
        }
        List<TestSequenceInstance> sequences = Collections.synchronizedList(new ArrayList<TestSequenceInstance>());
        File[] filelist = saveDirectory.listFiles();
        if (filelist.length > 0) {
            for (File file : filelist) {
                if (file.getName().endsWith(".state")) {
                    try {
                        sequences.add(fromFile(file, delete));
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return sequences;
    }

    public String getStartedString() {
        return TestStepInstance.getDateWith24Clock(getStarted());
    }

    public Calendar getStarted() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCreateTime());
        return cal;
    }

//    public Long getStartTime() {
//        return getCreateTime();
//    }
    public Long getFinishTime() {
        return finishTime;
    }

    public Long getElapsed() {
        Long fTime = getFinishTime();
        return (fTime == null) ? getCreateTime() - System.currentTimeMillis() : fTime - getCreateTime();
    }

    public String getStatusString() {
        if (getStatus() == null) {
            return "";
        }
        return getStatus().statusString;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public TestStepInstance getChild(List<String> pathList) {

        String stepInstanceName = pathList.get(0);

        if (getSetupStepInstance() != null && getSetupStepInstance().getName().equals(stepInstanceName)) {
            return getSetupStepInstance().getChild(pathList, 1);
        }
        if (getMainStepInstance() != null && getMainStepInstance().getName().equals(stepInstanceName)) {
            return getMainStepInstance().getChild(pathList, 1);
        }
        if (getCleanupStepInstance() != null && getCleanupStepInstance().getName().equals(stepInstanceName)) {
            return getCleanupStepInstance().getChild(pathList, 1);
        }
        return null;
    }

    private EntityManager getEntityManager() {
        if (em == null) {
            em = getTestStation().getEntityManagerFactory().createEntityManager();
        }
        return em;
    }

    public boolean merge(TestStepInstance step) {
        if (!this.equals(step.getTestSequenceInstance())) {
            throw new IllegalArgumentException("Cannot merge " + step.getClass().getCanonicalName() + ", which is not a descendant!");
        }
        return step.merge(getEntityManager());
    }

    @Override
    protected void finalize() throws Throwable {
//        System.out.println("Finalizing TestSequenceInstance");
        if (em != null) {
            if (em.isOpen()) {
                em.close();
            }
            em = null;
        }
        super.finalize();
    }
}
