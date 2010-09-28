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

import java.net.UnknownHostException;
import javax.script.Bindings;
import javax.script.ScriptException;
import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.script.SimpleBindings;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"createtime", "finishtime", "teststation_id", "testfixture_id", "serialnumber", "employeenumber", "testsequence_id", "testtype_id", "testproject_id", "failurecode", "failurestep_id"}))
@XmlRootElement(name = "sequence")
@XmlType(name = "testSequenceInstanceType", propOrder = {"testProjectFileRevision", "serialNumber", "employeeNumber", "hostName", "fixtureName", "testTypeReference", "createDate", "finishDate", "status", "failureCode", "testSequenceFileRevision", "testStepInstance"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
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
    public static final String TEST_SEQUENCE_INSTANCE = "testSequenceInstance";
    private static final Object jaxbLock = new Object();

    public static TestSequenceInstance unmarshal(File file)
            throws JAXBException, SVNException {
        TestSequenceInstance testSequenceInstance = null;
        synchronized (jaxbLock) {
            testSequenceInstance = (TestSequenceInstance) getUnmarshaller().unmarshal(file);
        }
        System.out.println("Unmarshalled testSequenceInstance:" + testSequenceInstance);
        return testSequenceInstance;
    }

    public boolean toFile() {
        try {
            toFile(testStation.getSaveDirectory());
            return true;
        } catch (Exception ex) {
            System.out.println("Exception in toFile: " + ex.getMessage());
        }
        return false;
    }

    public void toFile(File saveDirectory) throws JAXBException {
        if (saveDirectory == null) {
            return;
        }
        synchronized (FILE_LOCK) {
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
            long freeMB = saveDirectory.getFreeSpace() / 1048576L;
            System.out.println("Free space of '" + saveDirectory.getPath() + "': " + freeMB + " MB");
            String filepath = saveDirectory.getPath() + File.separatorChar + getFileName() + ".xml";
            File file = new File(filepath);
            getMarshaller().marshal(this, file);
        }
    }

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
    private TestStepInstance testStepInstance;
    private String serialNumber;
    private String employeeNumber;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TestStep testSequence;
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
//    private transient ConcurrentHashMap<FileRevision, TestStep> testSteps = new java.util.concurrent.ConcurrentHashMap<FileRevision, TestStep>();
    public transient EntityManager em;
    private transient PersistingPolicy persistingPolicy = PersistingPolicy.NEVER;
    private transient final Object testStepInstanceLock = new Object();

    @XmlTransient
    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public void setSequenceType(SequenceType sequenceType) {
        this.sequenceType = sequenceType;
    }

    @XmlTransient
    public boolean isInitType() {
        return TestSequenceInstance.SequenceType.FIXTURE_SETUP.equals(getSequenceType()) || TestSequenceInstance.SequenceType.STATION_SETUP.equals(getSequenceType());
    }

    @XmlElement(name = "testType")
    public TestTypeReference getTestTypeReference() {
        return new TestTypeReference(getPartNumber(), getPartRevision(), getTestTypeName());
    }

    public void setTestTypeReference(TestTypeReference testTypeReference) {
        setTestType(testProject.getTestType(testTypeReference));
    }

    @XmlTransient
    public String getTestTypeName() {
        return getTestType() != null ? getTestType().getName() : isInitType() ? STR_INIT : "";
    }

    @XmlTransient
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

    //@XmlElement(name = "partNumber")
    @XmlTransient
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

    //@XmlElement(name = "partRevision")
    @XmlTransient
    public String getPartRevision() {
        if (getTestType() != null && getTestType().getProduct() != null) {
            return getTestType().getProduct().getPartRevision();
        }
        return "";
    }

    @XmlTransient
    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
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
        STEPBYSTEP_FINISHED("Finished"),
        INTERACTIVE("Interactive");
        public final String statusString;

        SequenceStatus(
                String statusString) {
            this.statusString = statusString;
        }
    }
    @Basic(fetch = FetchType.EAGER)
    private SequenceStatus status = SequenceStatus.PENDING;
    private transient TestStepInstance interactiveTestStepInstance;
    private transient SequenceStatus originalStatus;
    private transient final Object interactionLock = new Object();
    private transient boolean interactionPassed;

    public static enum SequenceType {

        NORMAL, STATION_SETUP, FIXTURE_SETUP
    }
    @Basic(fetch = FetchType.EAGER)
    private SequenceType sequenceType;

    @XmlTransient
    public String getInteractionMessage() {
        return (interactiveTestStepInstance == null) ? null : interactiveTestStepInstance.getInteractionMessage();
    }

    public boolean interact(TestStepInstance step) throws InterruptedException {
        System.out.println("Sequence interaction pending...");
        synchronized (interactionLock) {
            System.out.println("Sequence interaction starts");
            if (SequenceStatus.RUNNING == status || SequenceStatus.STEPBYSTEP == status) {
                originalStatus = status;
                this.interactiveTestStepInstance = step;
                setStatus(SequenceStatus.INTERACTIVE);
                while (this.getStatus() == SequenceStatus.INTERACTIVE) {
                    Thread.sleep(500);
                }
                return interactionPassed;
            }
            return false;
        }
    }

    public void finishInteraction(boolean interactionPassed) {
        if (SequenceStatus.INTERACTIVE == status) {
            setStatus(originalStatus);
            this.interactionPassed = interactionPassed;
        }
    }

    public boolean isPersistPerStep() {
        return PersistingPolicy.STEP.equals(persistingPolicy);
    }

    @XmlTransient
    private PersistingPolicy getPersistingPolicy() {
        return persistingPolicy;
    }

    public void setPersistingPolicy(PersistingPolicy persistingPolicy) {
        this.persistingPolicy = persistingPolicy;
    }

    public boolean isPersistPerSequence() {
        return PersistingPolicy.SEQUENCE.equals(getPersistingPolicy());
    }

    @XmlTransient
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
                System.out.println("Connecting testProject...");
                setTestProject(tp);
            } else {
                System.out.println("Unable to connect testProject");
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
        TestStep testStep = getTestSequence();
        if (testStep != null && testStep.getId() == null) {
            TestStep ts = TestStep.query(em, getTestSequence().getCreator());
            if (ts != null) {
                System.out.println("Connecting testStep '" + testStep.getName() + "'...");
                setTestSequence(ts);
            } else {
                System.out.println("Unable to connect testStep '" + testStep.getName() + "'");
            }
        }
        for (TestStepInstance tsi : this) {
            //System.out.println("step: " + tsi.getName());
            TestStep calledTestStep = tsi.getCalledTestStep();
            if (calledTestStep != null && calledTestStep.getId() == null) {
                TestStep ts = TestStep.query(em, calledTestStep.getCreator());
                if (ts != null) {
                    System.out.println("Connecting calledTestStep '" + calledTestStep.getName() + "'...");
                    tsi.setCalledTestStep(ts);
                } else {
                    System.out.println("Unable to connect calledTestStep '" + calledTestStep.getName() + "'");
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
//            String filePath = getTestProjectFileRevision().getFile() == null ? null : getTestProjectFileRevision().getFile().getPath();
//            System.out.println("project file path before:" + filePath);
            em.merge(this);
            LOGGER.fine("Merging testSequenceInstance, committing Transaction...");
            em.getTransaction().commit();
//            filePath = getTestProjectFileRevision().getFile() == null ? null : getTestProjectFileRevision().getFile().getPath();
//            System.out.println("project file path after:" + filePath);
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
            em.persist(this);
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

    @XmlTransient
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

    @XmlTransient
    public TestFixture getTestFixture() {
        return testFixture;
    }

    public void setTestFixture(TestFixture testFixture) {
        this.testFixture = testFixture;
    }

    @XmlElement(name = "station")
    public String getHostName() {
        return testStation != null ? testStation.getHostName() : null;
    }

    public void setHostName(String hostName) throws UnknownHostException {
        setTestStation(testProject.getTestStationOrDefault(hostName));
    }

    @XmlElement(name = "fixture")
    public String getFixtureName() {
        return testFixture != null ? testFixture.getFixtureName() : null;
    }

    public void setFixtureName(String fixtureName) {
        setTestFixture(testStation.getTestFixture(fixtureName));
    }

    @XmlTransient
    public TestStation getTestStation() {
        return testStation;
    }

    public void setTestStation(TestStation testStation) {
        this.testStation = testStation;
    }

//    @XmlElement(name = "projectUrl")
//    public String getProjectUrl() {
//        return testProject == null ? null : testProject.getCreator().getSubversionUrl();
//    }
//
//    @XmlElement(name = "projectRevision")
//    public Long getProjectRevision() {
//        return testProject == null ? null : testProject.getCreator().getRevision();
//    }
    @XmlElement(name = "projectFile")
    public FileRevision getTestProjectFileRevision() {
        return testProject == null ? null : testProject.getCreator();
    }

    public void setTestProjectFileRevision(FileRevision fileRevision) throws JAXBException, SAXException, SVNException, CloneNotSupportedException {
        setTestProject(
                TestProject.unmarshal(
                new FileRevision(fileRevision.getSubversionUrl(), fileRevision.getRevision()),
                false));
    }

    @XmlElement(name = "sequenceFile")
    public FileRevision getTestSequenceFileRevision() {
        return getTestSequence().getCreator();
    }

    public void setTestSequenceFileRevision(FileRevision fileRevision) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        setTestSequence(
                TestStep.unmarshal(testStation.getTestProject().getCreator().resolve(fileRevision.getSubversionUrl(), fileRevision.getRevision()),
                false));
    }

    @XmlTransient
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

    public TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestTypeReference testTypeReference)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        this(sequenceType, serialNumber, employeeNumber, testTypeReference, (testTypeReference.getTestFixture() != null ? testTypeReference.getTestFixture().getTestStation() : testTypeReference.getTestStation()));
    }

    private TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestTypeReference testTypeReference, TestStation testStation)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        this(sequenceType, serialNumber, employeeNumber, testStation.getTestProject().getTestType(testTypeReference), testTypeReference.getTestFixture(), testStation, testStation.getTestProject());
    }

    private TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestType testType, TestFixture testFixture, TestStation testStation, TestProject testProject)
            throws JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException, IOException {
        this(sequenceType, serialNumber, employeeNumber, testType.getTestSequence().getTestSequence(), testType, testFixture, testStation, testProject);
    }

    public TestSequenceInstance(SequenceType sequenceType, String serialNumber, String employeeNumber, TestStep testSequence, TestType testType, TestFixture testFixture, TestStation testStation, TestProject testProject)
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
        if (testSequence != null) {
//            System.out.println("setupstep: " + testSequence.getSetupStep());
            setTestStepInstance(new TestStepInstance(testSequence, this));
        }
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

    @XmlTransient
    public TestStep getTestSequence() {
        return testSequence;
    }

    public void setTestSequence(TestStep newValue) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        testSequence = newValue;
        //... This must be much more complicated!
        if (testStepInstance != null) {
            testStepInstance.setTestStep(testSequence);
            testStepInstance.setNames(newValue.getNames());
        }
    }

    @XmlElement(name = "step")
    public TestStepInstance getTestStepInstance() {
        synchronized (testStepInstanceLock) {
            return testStepInstance;
        }
    }

    public void setTestStepInstance(TestStepInstance testStepInstance) throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
        this.testStepInstance = testStepInstance;
        if (testStepInstance != null) {
            testStepInstance.setTestStep(getTestSequence());
            testStepInstance.setTestSequenceInstance(this);
            testStepInstance.initNames();
        }
    }

    @Override
    public void run() {
//        logger.info("running the sequence...");
        Thread.currentThread().setName(toString());
        setStatus(SequenceStatus.RUNNING);
        if (isPersistPerStep()) {
            persistOrSerialize(getEntityManager());
        }
        startAndJoin(testStepInstance);
        finish();
    }

    public void startAndJoin(TestStepInstance step) {
        if (step != null) {
            step.run();
        }
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
        System.out.println("Sequence status set to " + status.statusString);
        SequenceStatus oldStatus = this.status;
        this.status = status;
        support.firePropertyChange("status", oldStatus, this.status);
    }

    public void finish() {
        dispose();
        setFinishTime(System.currentTimeMillis());
        if (isRunning()) {
            switch (testStepInstance.getStatus()) {
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
                    throw new IllegalStateException("mainStep finished with illegal state: " + testStepInstance.getStatus().statusString);
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

    @XmlTransient
    public boolean isAborted() {
        return SequenceStatus.ABORTED.equals(status);
    }

    @XmlTransient
    public boolean isPassed() {
        return SequenceStatus.PASSED.equals(status);
    }

    @XmlTransient
    public boolean isFailed() {
        return SequenceStatus.FAILED.equals(status);
    }

    @XmlTransient
    public boolean isRunning() {
        return SequenceStatus.RUNNING.equals(status);
    }

    @XmlTransient
    public boolean isStepByStep() {
        return SequenceStatus.STEPBYSTEP.equals(status);
    }

    @XmlTransient
    public boolean isSequenceActive() {
        return isRunning() || isStepByStep();
    }

    @Override
    @XmlTransient
    public Bindings getBindings() {
        if (bindings == null) {
            bindings = new SimpleBindings();
            bindings.put("sequence", this);
        }
        return bindings;
    }

    public boolean containsProperty(String key) {
        if ("sequence".equals(key)) {
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

    @XmlTransient
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
        return Integer.toString(cal.get(Calendar.YEAR))
                + TestStepInstance.FORMATTER_2.format(1 + cal.get(Calendar.MONTH))
                + TestStepInstance.FORMATTER_2.format(cal.get(Calendar.DAY_OF_MONTH))
                + "T"
                + TestStepInstance.FORMATTER_2.format(cal.get(Calendar.HOUR_OF_DAY))
                + TestStepInstance.FORMATTER_2.format(cal.get(Calendar.MINUTE))
                + TestStepInstance.FORMATTER_2.format(cal.get(Calendar.SECOND))
                + "."
                + TestStepInstance.FORMATTER_3.format(cal.get(Calendar.MILLISECOND));
    }

//    public boolean toFile() {
//        try {
//            return toFile(testStation.getSaveDirectory());
//        } catch (ScriptException ex) {
//            Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//    public boolean toFile(File saveDirectory) {
//        if (saveDirectory == null) {
//            return false;
//        }
//        long startTime = System.currentTimeMillis();
//        if (saveDirectory.isFile()) {
//            throw new IllegalArgumentException("Output directory is not a directory: a File is specified!");
//        }
//        if (!saveDirectory.isDirectory()) {
//            if (saveDirectory.mkdirs()) {
//                LOGGER.info("Output directory is created: " + saveDirectory.getPath());
//            } else {
//                throw new IllegalArgumentException("Output directory does not exist and cannot be created: " + saveDirectory);
//            }
//        }
////        Log.log("Serializing Test Sequence Instance to '" + saveDirectory.toString() + "'...");
//        long freeMB = saveDirectory.getFreeSpace() / 1048576L;
//        System.out.println("Free space of '" + saveDirectory.getPath() + "': " + freeMB + " MB");
//        try {
//            String filepath = saveDirectory.getPath() + File.separatorChar + getFileName() + ".state";
//            File file = new File(filepath);
//            synchronized (FILE_LOCK) {
////            Log.log("Saving sequence instance into file: " + file.getPath());
//                FileOutputStream fos = new FileOutputStream(file);
//                ObjectOutputStream s = new ObjectOutputStream(fos);
//                s.writeObject(this);
//                //s.writeObject(getLog());
//                s.flush();
//                s.close();
//            }
////            Log.log("Serializing Test Sequence Instance finished successfully in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
//            return true;
//        } catch (IOException ex1) {
//            LOGGER.log(Level.SEVERE, "Serializing Test Sequence Instance failed in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
//            LOGGER.log(Level.SEVERE, ex1.getMessage());
//            ex1.printStackTrace();
//        }
//        return false;
//    }
//    public static TestSequenceInstance fromFile(File file) {
//        synchronized (FILE_LOCK) {
//            FileInputStream in;
//            ObjectInputStream s = null;
//            TestSequenceInstance tsi = null;
//            try {
//                in = new FileInputStream(file);
//                s = new ObjectInputStream(in);
//                tsi = (TestSequenceInstance) s.readObject();
//                System.out.println("Successfully opened : " + tsi.getFileName());
//            } catch (Exception ex) {
//                System.out.println("Could not read file:" + file.getName());
//            } finally {
//                if (s != null) {
//                    try {
//                        s.close();
//                    } catch (IOException ex) {
//                        Logger.getLogger(TestSequenceInstance.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//            return tsi;
//        }
//    }
//    public static TestSequenceInstance fromFile(File file, boolean delete) {
//        TestSequenceInstance tsi = fromFile(file);
//        if (delete) {
//            file.delete();
//        }
//        return tsi;
//    }
//    public static TestSequenceInstance getInstance(File saveDirectory, boolean delete) {
//        if (!saveDirectory.isDirectory()) {
//            throw new IllegalArgumentException("specified file is not a directory: " + saveDirectory);
//        }
//        File[] filelist = saveDirectory.listFiles();
//        if (filelist.length > 0) {
//            for (File file : filelist) {
//                if (file.getName().endsWith(".state")) {
//                    return fromFile(file, delete);
//                }
//            }
//        }
//        return null;
//    }
//
//    public static List<TestSequenceInstance> getInstances(File saveDirectory, boolean delete) {
//        if (!saveDirectory.isDirectory()) {
//            throw new IllegalArgumentException("specified file is not a directory:" + saveDirectory);
//        }
//        List<TestSequenceInstance> sequences = Collections.synchronizedList(new ArrayList<TestSequenceInstance>());
//        File[] filelist = saveDirectory.listFiles();
//        if (filelist.length > 0) {
//            for (File file : filelist) {
//                if (file.getName().endsWith(".state")) {
//                    TestSequenceInstance seq = fromFile(file, delete);
//                    if (seq != null) {
//                        sequences.add(fromFile(file, delete));
//                    }
//                }
//            }
//        }
//        return sequences;
//    }
    public String getStartedString() {
        return TestStepInstance.getDateWith24Clock(getStarted());
    }

    @XmlTransient
    public Calendar getStarted() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(getCreateTime());
        return cal;
    }

//    public Long getStartTime() {
//        return getCreateTime();
//    }
    @XmlElement(name = "finishTime")
    public Date getFinishDate() {
        return new Date(finishTime);
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

    public Long getElapsed() {
        Long fTime = getFinishTime();
        return (fTime == null) ? getCreateTime() - System.currentTimeMillis() : fTime - getCreateTime();
    }

    @XmlTransient
    public String getStatusString() {
        if (getStatus() == null) {
            return "";
        }
        return getStatus().statusString;
    }

    @XmlElement(name = "createTime")
    public Date getCreateDate() {
        return new Date(createTime);
    }

    public void setCreateDate(Date createDate) {
        setCreateTime(createDate.getTime());
    }

    @XmlTransient
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public TestStepInstance getChild(List<String> pathList) {
        if (getTestStepInstance() != null) {
            return getTestStepInstance().getChild(pathList, 1);
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
