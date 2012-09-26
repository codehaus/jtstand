/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStepInstanceList.java is part of JTStand.
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
package com.jtstand.session;

import com.jtstand.TestSequenceInstance;
import com.jtstand.TestStepInstance;
import com.jtstand.TestStepNamePath;
import com.jtstand.query.GeneralQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author albert_kurucz
 */
public class TestStepInstanceList extends ArrayList<TestStepInstance> {

    public static final long serialVersionUID = 20081114L;
    private transient EntityManager em;
    protected final Object lock = new Object();

    public TestStepInstanceList(EntityManager em) {
        super();
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public boolean addStepsOfSequence(long sequenceID) {
        String queryString = "select ts from TestStepInstance ts where ts.testSequenceInstance.id = " + Long.toString(sequenceID) + " and ts.valueNumber != null";
        List<TestStepInstance> sList = (List<TestStepInstance>) (new GeneralQuery(em, queryString, null)).query();
        return addAll(sList);
    }

    @Override
    public boolean add(TestStepInstance step) {
//        System.out.println("adding TestStepInstance which started at: " + step.getStartedStringMs() + "...");
        if (step != null && step.getStartTime() != null && step.getFinishTime() != null) {
            synchronized (lock) {
                int i = 0;
                for (; i < size(); i++) {
                    TestStepInstance prev = get(i);
                    if (prev.equals(step)) {
                        return false;
                    }
                    if (prev.getStartTime() > step.getStartTime()) {
                        break;
                    }
                }
                add(i, step);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends TestStepInstance> list) {
        boolean changed = false;
        for (TestStepInstance step : list) {
            changed |= add(step);
        }
        return changed;
    }

    @SuppressWarnings("unchecked")
    public boolean addAll(String q, Integer max) {
        return addAll((List<TestStepInstance>) (new GeneralQuery(em, q, max)).query());
    }

    @SuppressWarnings("unchecked")
    public boolean add(long id, String path) {
        long startTime = System.currentTimeMillis();
        TestSequenceInstance seq = em.find(TestSequenceInstance.class, id);
        if (seq == null) {
            return false;
        }
        seq.getTestSequence().getNames().size();
//        LOGGER.info("TestSequenceInstance is found in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");

        String queryString = "select ts from TestStepNamePath ts where ts.testStep.id = " + seq.getTestSequence().getId() + " and ts.stepPath like '" + path + "'";
        List<TestStepNamePath> tsnpList = (List<TestStepNamePath>) (new GeneralQuery(em, queryString, 1)).query();
        if (tsnpList == null || tsnpList.size() != 1) {
//            System.out.println("Not found TestStepNamePath with testStep.id: " + seq.getTestSequence().getId() + " stepPath: " + path);
            return false;
        }
        TestStepNamePath tsnp = tsnpList.get(0);

        queryString = "select ts from TestStepInstance ts where ts.testSequenceInstance.id = " + id + " and ts.testStepNamePath.id = " + tsnp.getId();
        List<TestStepInstance> sList = (List<TestStepInstance>) (new GeneralQuery(em, queryString, 1)).query();
        if (sList == null || sList.size() != 1) {
//            System.out.println("Not found TestStepInstance with testSequenceInstance.id: " + id + " testStepNamePath.id: " + tsnp.getId());
            return false;
        }
        return add(sList.get(0));
    }

    @Override
    protected void finalize() throws Throwable {
//        System.out.println("Finalizing TestStepInstanceList");
        if (em != null) {
            if (em.isOpen()) {
                em.close();
            }
        }
        super.finalize();
    }
}
