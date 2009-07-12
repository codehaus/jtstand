/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestSequenceInstanceIterator.java is part of JTStand.
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

import java.util.Iterator;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstanceIterator implements Iterator<TestStepInstance> {

    private TestSequenceInstance tsi;
    private TestStepInstance current;

    public TestSequenceInstanceIterator() {
    }

    public TestSequenceInstanceIterator(TestSequenceInstance tsi) {
//            System.out.println("TestSequenceInstanceIterator init...");
        if (tsi == null) {
            throw new IllegalArgumentException("TestSequenceInstance parameter is null!");
        }
        tsi.getSetupStepInstance();
//            System.out.println(".");
        tsi.getMainStepInstance();
//            System.out.println(".");
        tsi.getCleanupStepInstance();
//            System.out.println(".");
        this.tsi = tsi;
        if (tsi.getSetupStepInstance() != null) {
            current = tsi.getSetupStepInstance();
        } else if (tsi.getMainStepInstance() != null) {
            current = tsi.getMainStepInstance();
        } else if (tsi.getCleanupStepInstance() != null) {
            current = tsi.getCleanupStepInstance();
        } else {
            current = null;
        }
//            System.out.println("TestSequenceInstanceIterator initialized.");
    }

    @Override
    public TestStepInstance next() {
        if (current != null) {
            TestStepInstance retval = current;
            current = current.next();
            if (current == null) {
                if (retval.equals(tsi.getSetupStepInstance())) {
                    if (tsi.getMainStepInstance() != null) {
                        current = tsi.getMainStepInstance();
                    } else {
                        current = tsi.getCleanupStepInstance();
                    }
                } else if (retval.equals(tsi.getMainStepInstance())) {
                    current = tsi.getCleanupStepInstance();
                }
            }
            return retval;
        }
        return null;
    }

    @Override
    public void remove() {
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }
}
