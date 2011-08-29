/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SerialDateUtilitiesTests.java is part of JTStand.
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

package org.jfree.date.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.date.MonthConstants;
import org.jfree.date.SerialDate;
import org.jfree.date.SerialDateUtilities;

/**
 * Some tests for the SerialDateUtilities class.
 *
 */
public class SerialDateUtilitiesTests extends TestCase {

    /**
     * Creates a new test case.
     *
     * @param name  the name.
     */
    public SerialDateUtilitiesTests(final String name) {
        super(name);
    }

    /**
     * Returns a test suite for the JUnit test runner.
     *
     * @return the test suite.
     */
    public static Test suite() {
        return new TestSuite(SerialDateUtilitiesTests.class);
    }

    /**
     * Problem actual day count.
     */
    public void testDayCountActual() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.APRIL, 2002);
        final SerialDate d2 = SerialDate.createInstance(2, MonthConstants.APRIL, 2002);
        final int count = SerialDateUtilities.dayCountActual(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30/360 day count.
     */
    public void testDayCount30() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.APRIL, 2002);
        final SerialDate d2 = SerialDate.createInstance(2, MonthConstants.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30/360ISDA day count.
     */
    public void testDayCount30ISDA() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.APRIL, 2002);
        final SerialDate d2 = SerialDate.createInstance(2, MonthConstants.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30ISDA(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30/360PSA day count.
     */
    public void testDayCount30PSA() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.APRIL, 2002);
        final SerialDate d2 = SerialDate.createInstance(2, MonthConstants.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30PSA(d1, d2);
        assertEquals(1, count);
    }

    /**
     * Problem 30E/360 day count.
     */
    public void testDayCount3030E() {
        final SerialDate d1 = SerialDate.createInstance(1, MonthConstants.APRIL, 2002);
        final SerialDate d2 = SerialDate.createInstance(2, MonthConstants.APRIL, 2002);
        final int count = SerialDateUtilities.dayCount30E(d1, d2);
        assertEquals(1, count);
    }

}
