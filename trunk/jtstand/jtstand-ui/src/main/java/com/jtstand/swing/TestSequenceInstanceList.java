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
package com.jtstand.swing;

import com.jtstand.FileRevision;
import com.jtstand.TestProject;
import com.jtstand.TestSequenceInstance;
import com.jtstand.query.GeneralQuery;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstanceList extends ArrayList<TestSequenceInstance> implements List<TestSequenceInstance>, Set<TestSequenceInstance>, Serializable {

    public static final long serialVersionUID = 20081114L;
//    static final Logger logger = Logger.getLogger(TestSequenceInstanceList.class.getCanonicalName());
    private transient EntityManager em;
    protected final Object lock = new Object();

    public TestSequenceInstanceList(EntityManager em) {
        super();
        this.em = em;
    }

    public TestProject getTestProject(FileRevision creator) throws JAXBException {
        return TestProject.query(em, creator);
    }

    public TestSequenceInstance find(long id) {
        return em.find(TestSequenceInstance.class, id);
    }

    public boolean isContained(TestSequenceInstance seq) {
        return em.contains(seq);
    }

    @SuppressWarnings("unchecked")
    public TestSequenceInstance replace(long createTime, String host) {
        String queryString = "select ts from TestSequenceInstance ts where ts.createTime = " + createTime;//+ " and ts.finishTime != null";
        if (host != null) {
            queryString += " and ts.testStation.hostName = '" + host + "'";
        }
        List<TestSequenceInstance> sList = (List<TestSequenceInstance>) (new GeneralQuery(em, queryString, 1)).query();
        if (sList == null || sList.size() != 1) {
            return null;
        }
        return replace(sList.get(0));
    }

    public TestSequenceInstance replace(TestSequenceInstance seq) {
        synchronized (lock) {
            int i = indexOf(seq);
            if (i >= 0) {
                System.out.println("Replacing sequence #" + i + ":" + seq);
                return set(i, seq);
            }
        }
        System.out.println("Cannot replace sequence: " + seq);
        return null;
    }

    @Override
    public boolean add(TestSequenceInstance seq) {
        synchronized (lock) {
            return !contains(seq) && super.add(seq);
        }
    }

    @Override
    public boolean addAll(Collection<? extends TestSequenceInstance> list) {
        if (list == null) {
            return false;
        }
        boolean changed = false;
        for (TestSequenceInstance seq : list) {
            changed |= add(seq);
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    public boolean addAll(String q, Integer max) {
        System.out.println("query string:'" + q + "'");
        if (max != null) {
            System.out.println("not more than: " + max);
        }
        return addAll((List<TestSequenceInstance>) (new GeneralQuery(em, q, max)).query());
    }

    @Override
    protected void finalize() throws Throwable {
//        System.out.println("Finalizing TestSequenceInstanceList");
        if (em != null) {
            if (em.isOpen()) {
                em.close();
            }
            em = null;
        }
        TestProject.close();
        super.finalize();
    }
}
