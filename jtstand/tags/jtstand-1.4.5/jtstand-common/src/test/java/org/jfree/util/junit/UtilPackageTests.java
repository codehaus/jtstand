/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, UtilPackageTests.java is part of JTStand.
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

package org.jfree.util.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A collection of tests for the org.jfree.util package.
 * <P>
 * These tests can be run using JUnit (http://www.junit.org).
 */
public class UtilPackageTests extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     *
     * @return The test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("org.jfree.util");
        suite.addTestSuite(ArrayUtilitiesTests.class);
        suite.addTestSuite(BooleanListTests.class);
        suite.addTestSuite(ObjectListTests.class);
        suite.addTestSuite(ObjectTableTests.class);
        suite.addTestSuite(ObjectUtilitiesTests.class);
        suite.addTestSuite(PaintListTests.class);
        suite.addTestSuite(PaintUtilitiesTests.class);
        suite.addTestSuite(RotationTests.class);
        suite.addTestSuite(ShapeListTests.class);
        suite.addTestSuite(ShapeUtilitiesTests.class);
        suite.addTestSuite(SortOrderTests.class);
        suite.addTestSuite(UnitTypeTests.class);
        return suite;
    }

    /**
     * Constructs the test suite.
     *
     * @param name  the suite name.
     */
    public UtilPackageTests(String name) {
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
