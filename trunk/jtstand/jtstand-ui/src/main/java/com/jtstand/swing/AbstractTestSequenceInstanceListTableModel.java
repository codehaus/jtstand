/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AbstractTestSequenceInstanceListTableModel.java is part of JTStand.
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
package com.jtstand.swing;

import com.jtstand.FileRevision;
import com.jtstand.TestProject;
import com.jtstand.TestSequenceInstance;
import com.jtstand.TestStation;
import com.jtstand.query.GeneralQuery;

import javax.persistence.EntityManager;
import javax.swing.table.TableModel;
import javax.xml.bind.JAXBException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author albert_kurucz
 */
public abstract class AbstractTestSequenceInstanceListTableModel extends AbstractListTableModel<TestSequenceInstance> implements TableModel, List<TestSequenceInstance>, Set<TestSequenceInstance> {

    public static final long serialVersionUID = 20081114L;
//    static final Logger logger = Logger.getLogger(TestSequenceInstanceList.class.getCanonicalName());
    private transient EntityManager em;
    protected final Object lock = new Object();

    public abstract TestStation getTestStation();

    private EntityManager getEntityManager() {
        if (em == null && getTestStation().getEntityManagerFactory() != null) {
            em = getTestStation().getEntityManagerFactory().createEntityManager();
        }
        return em;
    }

    public TestProject getTestProject(FileRevision creator) throws JAXBException {
        return TestProject.query(getEntityManager(), creator);
    }

    public TestSequenceInstance find(long id) {
        return getEntityManager().find(TestSequenceInstance.class, id);
    }

    public boolean isContained(TestSequenceInstance seq) {
        EntityManager myem = getEntityManager();
        if (myem == null) {
            return false;
        }
        return myem.contains(seq);
    }

    @SuppressWarnings("unchecked")
    public TestSequenceInstance replace(long createTime, String host) {
        String queryString = "select ts from TestSequenceInstance ts where ts.createTime = " + createTime;//+ " and ts.finishTime != null";
        if (host != null) {
            queryString += " and ts.testStation.hostName = '" + host + "'";
        }
        List<TestSequenceInstance> sList = (List<TestSequenceInstance>) (new GeneralQuery(getEntityManager(), queryString, 1)).query();
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
        return addAll((List<TestSequenceInstance>) (new GeneralQuery(getEntityManager(), q, max)).query());
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
