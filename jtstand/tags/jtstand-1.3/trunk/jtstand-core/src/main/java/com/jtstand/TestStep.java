/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStep.java is part of JTStand.
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

import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Albert Kurucz
 */
@Entity
@Table(uniqueConstraints = {
    //@UniqueConstraint(columnNames = {"creator_id", "treelevel", "name"}),
    @UniqueConstraint(columnNames = {"parent_id", "name"}),
    @UniqueConstraint(columnNames = {"parent_id", "testStepPosition"})})
@XmlRootElement(name = "step")
@XmlType(name = "testStepType", propOrder = {"useLimit", "postSleep", "preSleep", "loopSleep", "maxLoops", "failAction", "passAction", "runMode", "name", "remark", "properties", "testLimits", "stepReference", "script", "steps"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestStep implements Serializable {

    public static final long serialVersionUID = 20081114L;
    public static final String TEST_STEP = "testStep";
    private static JAXBContext jc;
    private static Marshaller m;
    private static Unmarshaller um;
    private static final Object JAXB_LOCK = new Object();

    private static JAXBContext getJAXBContext()
            throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(TestStep.class);
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

    public static TestStep query(EntityManager em, FileRevision creator) {
        if (em == null || creator == null) {
            return null;
        }
        FileRevision c = FileRevision.query(em, creator);
        if (c == null) {
            return null;
        }
        try {
            Query q = em.createQuery("select ts from TestStep ts where ts.creator = :creator and ts.parent is null");
            q.setParameter("creator", c);
            TestStep testStep = (TestStep) q.getSingleResult();
            TestStep.getMarshaller().marshal(testStep, TestProject.NULL_OUTPUT_STREAM);
            return testStep;
        } catch (Exception ex) {
            return null;
        }
    }

//    public static TestStep query(FileRevision creator) {
////        Log.log("Query Test Step:" + creator);
//        TestStep testStep = null;
//        synchronized (cacheLock) {
//            testStep = cache.get(creator);
//        }
//        if (testStep != null) {
////            Log.log("Test Step is found in cache!");
//            return testStep;
//        }
//        TestStep qTestStep = (new TestStepQuery(creator)).query();
//        if (qTestStep != null) {
//            synchronized (cacheLock) {
//                cache.put(creator, qTestStep);
//            }
//            return qTestStep;
//        }
//        return testStep;
//    }
    public static TestStep unmarshal(FileRevision fileRevision, boolean useCache)
            throws JAXBException, SVNException {

        //System.out.println("unmarshalling:" + fileRevision);
        synchronized (JAXB_LOCK) {
            TestStep testStep = (TestStep) fileRevision.unmarshal(getUnmarshaller(), useCache);
            testStep.setCreator(fileRevision);
            return testStep;
        }
        //LOGGER.log(Level.FINE, "Unmarshalled testStep:" + testStep);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private FileRevision creator;
    @ManyToOne(fetch = FetchType.LAZY)
    private TestStep parent;
    private String name;
    //private String stepClass;
    private String message;
    private String remark;
    private Boolean parallel;
    private Boolean cleanup;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    @OrderBy("testStepPosition ASC")
    private List<TestStep> steps = new ArrayList<TestStep>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testStep", fetch = FetchType.LAZY)
    @OrderBy("testStepPropertyPosition ASC")
    private List<TestStepProperty> properties = new ArrayList<TestStepProperty>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testStep", fetch = FetchType.LAZY)
    @OrderBy("testLimitPosition ASC")
    private List<TestStepLimit> testLimits = new ArrayList<TestStepLimit>();
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "testStep")
    private StepReference stepReference;
    private int testStepPosition;
    /* root is on level zero */
    private int treeLevel;
    //private String treePath;
    private String locks;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "testStep")
    private TestStepScript script;

    @XmlElement
    public TestStepScript getScript() {
        return script;
    }

    public void setScript(TestStepScript script) {
        this.script = script;
        if (script != null) {
            script.setTestStep(this);
        }
    }

    @XmlAttribute
    public String getLocks() {
        return locks;
    }

    public void setLocks(String locks) {
        this.locks = locks;
    }

    @XmlTransient
    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
        if (getSteps() != null) {
            for (TestStep child : getSteps()) {
                child.setTreeLevel(treeLevel + 1);
            }
        }
    }

//    @XmlTransient
//    public String getTreePath() {
//        return treePath;
//    }
//
//    public void setTreePath(String treePath) {
//        this.treePath = treePath;
//        if (treePath != null && getSteps() != null) {
//            for (TestStep child : getSteps()) {
//                child.setTreePath(treePath + "." + child.getName());
//            }
//        }
//    }
    @XmlTransient
    public int getPosition() {
        return testStepPosition;
    }

    public void setPosition(int position) {
        this.testStepPosition = position;
    }

    public static enum RunMode {

        NORMAL, SKIP, FORCE_PASS, FORCE_FAIL, SKIP_FIRST
    }
    @Basic(fetch = FetchType.EAGER)
    private RunMode runMode;

    public static enum PassAction {

        NEXT_TEST, LOOP
    }
    @Basic(fetch = FetchType.EAGER)
    private PassAction passAction;

    public static enum FailAction {

        NEXT_TEST, LOOP, STOP
    }
    @Basic(fetch = FetchType.EAGER)
    private FailAction failAction;
    private Integer maxLoops;
    private Integer loopSleep;
    private Integer preSleep;
    private Integer postSleep;
    private String useLimit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_STEP, fetch = FetchType.EAGER)
    @MapKey(name = "stepPath")
    private Map<String, TestStepNamePath> names = new HashMap<String, TestStepNamePath>();
    private transient final Object testLimitsLock = new Object();
    private transient final Object propertiesLock = new Object();
    private transient final Object stepsLock = new Object();

    @XmlTransient
    public Map<String, TestStepNamePath> getNames() {
        return names;
    }

    public TestStep getCalledTestStep(TestStepInstance tsi, boolean useCache) throws URISyntaxException, IOException, JAXBException, ParserConfigurationException, SAXException, SVNException {
        if (getStepReference() == null) {
            return null;
        }
        return getStepReference().getTestStep(tsi, useCache);
    }

    @XmlElement(name = "limit")
    public List<TestStepLimit> getTestLimits() {
        synchronized (testLimitsLock) {
            return testLimits;
        }
    }

    public void setTestLimits(List<TestStepLimit> testLimits) {
        this.testLimits = testLimits;
        if (testLimits != null) {
            for (ListIterator<TestStepLimit> iterator = testLimits.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestStepLimit testLimit = iterator.next();
//                System.out.println("setting testStepLimit's testStep...");
                testLimit.setTestStep(this);
                testLimit.setPosition(index);
            }
        }
    }

    @XmlElement(name = "stepFile")
    public StepReference getStepReference() {
        return stepReference;
    }

    public void setStepReference(StepReference stepReference) {
        this.stepReference = stepReference;
        if (stepReference != null) {
            stepReference.setTestStep(this);
        }
    }

    @XmlAttribute
    public String getUseLimit() {
        return useLimit;
    }

    public void setUseLimit(String useLimit) {
        this.useLimit = useLimit;
    }

    @XmlElement(name = "property")
    public List<TestStepProperty> getProperties() {
        synchronized (propertiesLock) {
            return properties;
        }
    }

    public void setProperties(List<TestStepProperty> properties) {
        this.properties = properties;
        if (properties != null) {
            for (ListIterator<TestStepProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestStepProperty testStepProperty = iterator.next();
                testStepProperty.setTestStep(this);
                testStepProperty.setPosition(index);
            }
        }
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlAttribute
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    @XmlElement
//    public String getExecuteCode() {
//        return executeCode;
//    }
//
//    public void setExecuteCode(String executeCode) {
//        this.executeCode = executeCode;
//    }
    @XmlAttribute
    public RunMode getRunMode() {
        return runMode;
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    @XmlAttribute
    public PassAction getPassAction() {
        return passAction;
    }

    public void setPassAction(PassAction passAction) {
        this.passAction = passAction;
    }

    @XmlAttribute
    public FailAction getFailAction() {
        return failAction;
    }

    public void setFailAction(FailAction failAction) {
        this.failAction = failAction;
    }

    @XmlAttribute
    public Integer getMaxLoops() {
        return maxLoops;
    }

    public void setMaxLoops(Integer maxLoops) {
        if (maxLoops != null && maxLoops < 2) {
            throw new IllegalArgumentException("maxLoops must be at least 2!");
        }
        this.maxLoops = maxLoops;
    }

    @XmlAttribute
    public Integer getLoopSleep() {
        return loopSleep;
    }

    public void setLoopSleep(Integer loopSleep) {
        if (loopSleep != null && loopSleep < 0) {
            throw new IllegalArgumentException("loopSleep cannot be negative!");
        }
        this.loopSleep = (loopSleep != null && loopSleep == 0) ? null : loopSleep;
    }

    @XmlAttribute
    public Integer getPreSleep() {
        return preSleep;
    }

    public void setPreSleep(Integer preSleep) {
        this.preSleep = (preSleep != null && preSleep == 0) ? null : preSleep;
    }

    @XmlAttribute
    public Integer getPostSleep() {
        return postSleep;
    }

    public void setPostSleep(Integer postSleep) {
        this.postSleep = (postSleep != null && postSleep == 0) ? null : postSleep;
    }

    @XmlElement(name = "step")
    public List<TestStep> getSteps() {
        synchronized (stepsLock) {
            return steps;
        }
    }

    public void setSteps(List<TestStep> steps) {
        if (steps != null) {
            for (ListIterator<TestStep> iterator = steps.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestStep testStep = iterator.next();
                testStep.setParent(this);
                testStep.setPosition(index);
            }
        }
        this.steps = steps;
    }

    @XmlTransient
    public TestStep getParent() {
        return parent;
    }

    public void setParent(TestStep parent) {
        this.parent = parent;
        if (parent != null) {
            setCreator(parent.getCreator());
        }
    }

//    @XmlAttribute
//    public String getStepClass() {
//        return stepClass;
//    }
//
//    public void setStepClass(String stepClass) {
//        this.stepClass = stepClass;
//    }
    public TestStep getRootTestStep() {
        if (parent == null) {
            return this;
        }
        return parent.getRootTestStep();
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        //LOGGER.log(Level.FINE, "setting creator of test step '" + getName() + "' to:" + creator);

        this.creator = creator;
        if (getStepReference() != null) {
            getStepReference().setTestStep(this);
        }
        if (getScript() != null) {
            getScript().setTestStep(this);
        }
        setSteps(getSteps());
        setProperties(getProperties());
        setTestLimits(getTestLimits());
        setTreeLevel(0);
//        setTreePath(getName());
    }

    @Override
    public int hashCode() {
        int hash = treeLevel;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStep)) {
            return false;
        }
        TestStep other = (TestStep) object;
        if (this.treeLevel != other.getTreeLevel()) {
            return false;
        }
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.name == null && other.getName() != null) || (this.name != null && !this.name.equals(other.getName()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + getClass().getCanonicalName() + ":" + getTreeLevel() + ":" + getCreator() + ":" + getName() + "]";
        //String out = this.getClass().getCanonicalName() + "[Name=" + name + ", Creator=" + creator + "]";
//        String out = "step[" + name + "]";
//        if (creator == null) {
//            out += "c";
//        }
//        for (TestStep child : getSteps()) {
//            out += "." + child;
//        }
//        return out;

//        return (getCreator() != null ? getCreator().toString() : "null") + "@" + (getTreePath() != null ? getTreePath() : "null") + "@" + Integer.toString(treeLevel);
    }

    @XmlAttribute
    public Boolean getParallel() {
        return parallel;
    }

    public void setParallel(Boolean parallel) {
        this.parallel = parallel;
    }

    @XmlAttribute
    public Boolean getCleanup() {
        return cleanup;
    }

    public void setCleanup(Boolean cleanup) {
        this.cleanup = cleanup;
    }
}
