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
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import java.util.Iterator;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstanceIterator implements Iterator<TestStepInstance> {

    private TestStepInstance current;

    public TestSequenceInstanceIterator() {
    }

    public TestSequenceInstanceIterator(TestSequenceInstance tsi) {
        if (tsi == null) {
            throw new IllegalArgumentException("TestSequenceInstance parameter is null!");
        }
        tsi.getTestStepInstance();
        current = tsi.getTestStepInstance();
    }

    @Override
    public TestStepInstance next() {
        if (current != null) {
            TestStepInstance retval = current;
            current = current.next();
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
