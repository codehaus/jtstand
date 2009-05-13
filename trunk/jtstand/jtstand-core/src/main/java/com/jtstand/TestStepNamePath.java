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
package com.jtstand;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"testsequence_id", "steppath"}))
public class TestStepNamePath implements Serializable {

    public static final long serialVersionUID = 20081114L;
//    private static transient ConcurrentHashMap<TestSequence, ConcurrentHashMap<String, TestStepNamePath>> cache = new ConcurrentHashMap<TestSequence, ConcurrentHashMap<String, TestStepNamePath>>();
//    private static transient Object cacheLock = new Object();

    public TestStepNamePath() {
    }

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
    private String stepPath;
    private String stepName;
    @ManyToOne(fetch = FetchType.EAGER)
    private TestSequence testSequence;
    @ManyToOne
    private TestLimit testLimit;
    @ManyToOne
    private TestStep calledTestStep;
    private int stepNumber = 0;

//    public TestStepNamePath() {
//    }

    public int getStepNumber() {
        return stepNumber;
    }

    public TestStepNamePath(TestSequence testSequence, String name, String path, TestLimit testLimit, TestStep calledTestStep, int stepNumber) {
//    public TestStepNamePath(TestSequence testSequence, String name, String path) {
        if (!path.endsWith(name)) {
            throw new IllegalArgumentException("Path have to end with name!");
        }
        this.testSequence = testSequence;
        this.stepName = name;
        this.stepPath = path;
        this.testLimit = testLimit;
        this.calledTestStep = calledTestStep;
        this.stepNumber = stepNumber;
//        cachePut(this);
    }

    public TestLimit getTestLimit() {
        return testLimit;
    }

    public void setTestLimit(TestLimit testLimit) {
        this.testLimit = testLimit;
    }

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

    public TestSequence getTestSequence() {
        return testSequence;
    }

    public void setTestSequence(TestSequence testSequence) {
        this.testSequence = testSequence;
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
        hash += (testSequence != null ? testSequence.hashCode() : 0);
        hash += (stepPath != null ? stepPath.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStepNamePath)) {
            return false;
        }
        TestStepNamePath other = (TestStepNamePath) object;
        if ((this.testSequence == null && other.getTestSequence() != null) || (this.testSequence != null && !this.testSequence.equals(other.getTestSequence()))) {
            return false;
        }
        if ((this.stepPath == null && other.getStepPath() != null) || (this.stepPath != null && !this.stepPath.equals(other.getStepPath()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + getClass().getCanonicalName() + ":" + getTestSequence() + ":" + getStepPath() + "]";
    }
}
