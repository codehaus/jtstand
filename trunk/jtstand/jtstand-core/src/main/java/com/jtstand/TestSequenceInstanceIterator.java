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
