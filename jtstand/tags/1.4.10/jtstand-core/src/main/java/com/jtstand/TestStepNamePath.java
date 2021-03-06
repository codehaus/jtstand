/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepNamePath.java is part of JTStand.
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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"teststep_id", "steppath"}))
public class TestStepNamePath {

//    private static transient ConcurrentHashMap<TestSequence, ConcurrentHashMap<String, TestStepNamePath>> cache = new ConcurrentHashMap<TestSequence, ConcurrentHashMap<String, TestStepNamePath>>();
//    private static transient Object cacheLock = new Object();
//    static TestStepNamePath cacheGet(TestSequence testSequence, String path) {
//        synchronized (cacheLock) {
//            ConcurrentHashMap<String, TestStepNamePath> cache2 = cache.get(testSequence);
//            if (cache2 != null) {
//                return cache2.get(path);
//            }
//        }
//        return null;
//    }
//    static TestStepNamePath cachePut(TestStepNamePath testStepNamePath) {
//        synchronized (cacheLock) {
//            ConcurrentHashMap<String, TestStepNamePath> cache2 = cache.get(testStepNamePath.getTestSequence());
//            if (cache2 == null) {
//                cache2 = new ConcurrentHashMap<String, TestStepNamePath>();
//                cache.put(testStepNamePath.getTestSequence(), cache2);
//            }
//            return cache2.put(testStepNamePath.getStepPath(), testStepNamePath);
//        }
//    }
//    static TestStepNamePath query(EntityManager em, TestStepNamePath testStepNamePath) {
//        if (em == null) {
//            return null;
//        }
//        Log.log("Query TestStepNamePath: '" + testStepNamePath + "'");
//        TestStepNamePath ts = null;
//        ts = cacheGet(testStepNamePath.getTestSequence(), testStepNamePath.getStepPath());
//        if (ts != null && ts.getId() != null) {
//            Log.log("Test Step Name Path is found in cache!");
//            return ts;
//        }
//        TestSequence seq = testStepNamePath.getTestSequence();
//        if (seq.getId() == null) {
//            seq = TestSequence.query(em, seq.getCreator());
//            if (seq == null) {
//                return null;
//            }
//        }
//        try {
//            Log.log("Finding TestStepNamePath in Database...");
//            Query q = em.createQuery("select ts from TestStepNamePath ts where ts.testSequence = :testSequence and ts.stepPath = :stepPath");
//            q.setParameter("testSequence", seq);
//            q.setParameter("stepPath", testStepNamePath.getStepPath());
//            ts = (TestStepNamePath) q.getSingleResult();
//            System.out.println("TestStepNamePath of testSequence '" + ts.getTestSequence() + "' with path: '" + ts.toString() + "' is found in Database!");
//            //TestStep.getMarshaller().marshal(testStep, System.out);
//            synchronized (cacheLock) {
//                cachePut(ts);
//            }
//            return ts;
//        } catch (Exception ex) {
//            Log.log("Exception:" + ex.getMessage());
//        }
//        return null;
//    }
//    static TestStepNamePath query(TestSequence testSequence, String path) {
////        Log.log("Query TestStepNamePath: '" + path + "'");
//        TestStepNamePath ts = null;
//        ts = cacheGet(testSequence, path);
//        if (ts != null) {
////            Log.log("Test Step Name Path is found in cache!");
//            return ts;
//        }
//        TestStepNamePath qts = (new TestStepNamePathQuery(testSequence, path)).query();
//        if (qts != null) {
//            synchronized (cacheLock) {
//                cachePut(qts);
//            }
//            return qts;
//        }
//        return ts;
//    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(length = 2147483647)
    private String stepPath;
    private String stepName;
    @ManyToOne
    private TestStep testStep;
//    @ManyToOne
//    private TestLimit testLimit;
    @ManyToOne
    private TestStep calledTestStep;
    private int stepNumber = 0;

    public int getStepNumber() {
        return stepNumber;
    }

    public TestStepNamePath() {
    }

    public TestStepNamePath(
            TestStep testStep,
            String name,
            String path,
            //TestLimit testLimit,
            TestStep calledTestStep,
            int stepNumber) {
//    public TestStepNamePath(TestSequence testSequence, String name, String path) {
        if (!path.endsWith(name)) {
            throw new IllegalArgumentException("Path have to end with name!");
        }
        this.testStep = testStep;
        this.stepName = name;
        this.stepPath = path;
//        this.testLimit = testLimit;
        this.calledTestStep = calledTestStep;
        this.stepNumber = stepNumber;
//        if (stepNumber == 1112) {
//            throw new IllegalArgumentException("1112"); //TBD remove this!
//        }
//        cachePut(this);
    }

//    public TestLimit getTestLimit() {
//        return testLimit;
//    }
//
//    public void setTestLimit(TestLimit testLimit) {
//        this.testLimit = testLimit;
//    }
    public TestStep getCalledTestStep() {
        return calledTestStep;
    }

    public void setCalledTestStep(TestStep calledTestStep) {
        this.calledTestStep = calledTestStep;
    }

    public String getStepPath() {
        return stepPath;
    }

    public void setStepPath(String stepPath) {
        this.stepPath = stepPath;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public TestStep getTestStep() {
        return testStep;
    }

    public void setTestStep(TestStep testStep) {
        this.testStep = testStep;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (testStep != null ? testStep.hashCode() : 0);
        hash += (stepPath != null ? stepPath.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStepNamePath)) {
            return false;
        }
        TestStepNamePath other = (TestStepNamePath) object;
        if ((this.testStep == null && other.getTestStep() != null) || (this.testStep != null && !this.testStep.equals(other.getTestStep()))) {
            return false;
        }
        if ((this.stepPath == null && other.getStepPath() != null) || (this.stepPath != null && !this.stepPath.equals(other.getStepPath()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + getClass().getCanonicalName() + ":" + getTestStep() + ":" + getStepPath() + "]";
    }
}
