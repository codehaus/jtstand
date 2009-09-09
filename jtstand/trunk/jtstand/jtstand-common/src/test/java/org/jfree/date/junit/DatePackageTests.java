/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DatePackageTests.java is part of JTStand.
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

/**
 * A test suite for the <code>org.jfree.date</code> package.
 *
 */
public class DatePackageTests extends TestCase {

    /**
     * Returns a test suite for the JUnit test runner.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("org.jfree.date");
        suite.addTestSuite(SerialDateTests.class);
        suite.addTestSuite(SerialDateUtilitiesTests.class);
        suite.addTestSuite(SpreadsheetDateTests.class);
        return suite;
    }

    /**
     * Creates a new test case.
     *
     * @param name  the name.
     */
    public DatePackageTests(final String name) {
        super(name);
    }

}
