/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestSequence.java is part of JTStand.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import org.tmatesoft.svn.core.SVNException;

import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Albert Kurucz
 */
@Entity
@XmlType(name = "testSequenceType", propOrder = {"name", "properties", "testLimits", "setupStep", "mainStep", "cleanupStep"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
@XmlRootElement(name = TestSequence.TEST_SEQUENCE)
public class TestSequence implements Serializable {

    public static final long serialVersionUID = 20081114L;
    public static final String TEST_SEQUENCE="testSequence";
    private static final Logger LOGGER = Logger.getLogger(TestSequence.class.getCanonicalName());
    private static JAXBContext jc;
    private static Marshaller m;
    private static Unmarshaller um;
    private static Object jaxbLock = new Object();

    private static JAXBContext getJAXBContext()
            throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(TestSequence.class);
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

    public static TestSequence query(EntityManager em, FileRevision creator) {
        if (em == null || creator == null) {
            return null;
        }
        FileRevision c = FileRevision.query(em, creator);
        if (c == null) {
            return null;
        }
        try {
            LOGGER.log(Level.FINE, "Finding Test Sequence in Database...");
            Query q = em.createQuery("select ts from TestSequence ts where ts.creator = :creator");
            q.setParameter("creator", c);
            TestSequence testSequence = (TestSequence) q.getSingleResult();
            LOGGER.fine("TestSequence is found in Database!");
            return testSequence;
        } catch (Exception ex) {
            return null;
        }
    }
    private static transient ConcurrentHashMap<FileRevision, TestSequence> cache = new java.util.concurrent.ConcurrentHashMap<FileRevision, TestSequence>();
    private static transient Object cacheLock = new Object();

//    public static TestSequence query(FileRevision creator) {
//        TestSequence testSequence = null;
//        synchronized (cacheLock) {
//            testSequence = cache.get(creator);
//        }
//        if (testSequence != null) {
//            Log.log("Test Sequence is found in cache!");
//            return testSequence;
//        }
//        TestSequence qTestSequence = (new TestSequenceQuery(creator)).query();
//        if (qTestSequence != null) {
//            synchronized (cacheLock) {
//                cache.put(creator, qTestSequence);
//            }
//            return qTestSequence;
//        }
//        return testSequence;
//    }
    public static TestSequence unmarshal(FileRevision fileRevision)
            throws JAXBException, SVNException {
        TestSequence testSequence = null;
        synchronized (cacheLock) {
            testSequence = cache.get(fileRevision);
        }
        if (testSequence != null) {
            LOGGER.info("Test Sequence is found in cache!");
            return testSequence;
        }
        synchronized (jaxbLock) {
            testSequence = (TestSequence) fileRevision.unmarshal(getUnmarshaller());
        }
        synchronized (cacheLock) {
            cache.put(fileRevision, testSequence);
        }
        testSequence.setCreator(fileRevision);
        LOGGER.log(Level.FINE, "Unmarshalled testSequence: " + testSequence);
        return testSequence;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private TestStep setupStep;
    @OneToOne(cascade = CascadeType.ALL)
    private TestStep mainStep;
    @OneToOne(cascade = CascadeType.ALL)
    private TestStep cleanupStep;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_SEQUENCE)
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestSequenceProperty> properties = new ArrayList<TestSequenceProperty>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_SEQUENCE)
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestLimit> testLimits = new ArrayList<TestLimit>();
    @ManyToOne(cascade = CascadeType.ALL)
    private FileRevision creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_SEQUENCE, fetch = FetchType.EAGER)
    @MapKey(name = "stepPath")
    private Map<String, TestStepNamePath> names = new HashMap<String, TestStepNamePath>();
    private transient Object testLimitsLock = new Object();
    private transient Object propertiesLock = new Object();

    private Object readResolve() {
        testLimitsLock = new Object();
        propertiesLock = new Object();
        return this;
    }

    @XmlTransient
    public Map<String, TestStepNamePath> getNames() {
        return names;
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlElement(name = "limit")
    public List<TestLimit> getTestLimits() {
        synchronized (testLimitsLock) {
//            List<TestSequenceLimit> limits = new ArrayList<TestSequenceLimit>();
//            for (TestSequenceLimit limit : testSequenceLimits.values()) {
//                limits.add(limit);
//            }
//            return limits;
            return testLimits;
        }
    }

    public void setTestLimits(List<TestLimit> testLimits) {
//        System.out.println("setTestLimits...");
        this.testLimits = testLimits;
        if (testLimits != null) {
            for (ListIterator<TestLimit> iterator = testLimits.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestLimit testLimit = iterator.next();
                testLimit.setTestSequence(this);
                testLimit.setPosition(index);
            }
        }
    }

    public TestLimit getTestLimit(String useLimit) {
        if (useLimit != null) {
            for (TestLimit limit : getTestLimits()) {
                if (useLimit.equals(limit.getName())) {
                    return limit;
                }
            }
        }
        return null;
    }

    @XmlElement(name = "property")
    public List<TestSequenceProperty> getProperties() {
        synchronized (propertiesLock) {
            return properties;
        }
    }

    public void setProperties(List<TestSequenceProperty> properties) {
//        System.out.println("setProperties...");

        this.properties = properties;
        if (properties != null) {
            for (ListIterator<TestSequenceProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestSequenceProperty testSequenceProperty = iterator.next();
                testSequenceProperty.setTestSequence(this);
                testSequenceProperty.setPosition(index);
            }
        }
    }

//    public String getProperty(String keyString) {
//        for (TestSequenceProperty tsp : getProperties()) {
//            if (tsp.getName().equals(keyString)) {
//                return tsp.getPropertyValue();
//            }
//        }
//        return null;
//    }
    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
//        System.out.println("setting name...");
        this.name = name;
    }

    @XmlElement
    public TestStep getSetupStep() {
        return setupStep;
    }

    public void setSetupStep(TestStep setupStep) {
        this.setupStep = setupStep;
        if (setupStep != null) {
            setupStep.setCreator(creator);
        }
    }

    @XmlElement
    public TestStep getMainStep() {
        return mainStep;
    }

    public void setMainStep(TestStep mainStep) {
//        System.out.println("Setting main step...");
        this.mainStep = mainStep;
        if (mainStep != null) {
            mainStep.setCreator(creator);
        }
    }

    @XmlElement
    public TestStep getCleanupStep() {
        return cleanupStep;
    }

    public void setCleanupStep(TestStep cleanupStep) {
        this.cleanupStep = cleanupStep;
        if (cleanupStep != null) {
            cleanupStep.setCreator(creator);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestSequence)) {
            return false;
        }
        TestSequence other = (TestSequence) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + getClass().getCanonicalName() + ":" + getCreator() + "]";
    //String out = this.getClass().getCanonicalName() + "[Name=" + name + ", Revision=" + revision + "]";

//        String out = "sequence[" + name + "]";
//        if (setupStep != null) {
//            out += "setupStep: " + setupStep;
//        }
//        out += "mainStep: " + mainStep;
//        if (cleanupStep != null) {
//            out += "cleanupStep: " + cleanupStep;
//        }
//        return out;

//        return (getCreator() != null) ? getCreator().toString() : this.getClass().getCanonicalName();
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        setProperties(getProperties());
        setTestLimits(getTestLimits());
        if (setupStep != null) {
            setupStep.setCreator(creator);
        }
        if (mainStep != null) {
            mainStep.setCreator(creator);
        }
        if (cleanupStep != null) {
            cleanupStep.setCreator(creator);
        }
    }

//    public static void main(String[] args) {
//        try {
//            TestSequence ts = (TestSequence) getUnmarshaller().unmarshal(new FileInputStream(new File("./src/config/demoRandom.xml")));
//            getMarshaller().marshal(ts, System.out);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(TestSequence.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JAXBException ex) {
//            Logger.getLogger(TestSequence.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
