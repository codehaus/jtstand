/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, IOPackageTests.java is part of JTStand.
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

package org.jfree.io.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * A collection of tests for the org.jfree.io package.
 * <P>
 * These tests can be run using JUnit (http://www.junit.org).
 *
 */
public class IOPackageTests extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("org.jfree.io");
        suite.addTestSuite(SerialUtilitiesTests.class);
        suite.addTestSuite(IOUtilsTests.class);
        return suite;
    }

    /**
     * Constructs the test suite.
     *
     * @param name  the suite name.
     */
    public IOPackageTests(final String name) {
        super(name);
    }

    /**
     * Runs the test suite using the JUnit text-based runner.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

}
