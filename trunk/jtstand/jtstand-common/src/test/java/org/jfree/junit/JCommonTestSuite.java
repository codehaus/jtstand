/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JCommonTestSuite.java is part of JTStand.
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

package org.jfree.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.date.junit.DatePackageTests;
import org.jfree.io.junit.IOPackageTests;
import org.jfree.text.junit.TextPackageTests;
import org.jfree.ui.junit.UIPackageTests;
import org.jfree.util.junit.UtilPackageTests;

/**
 * A test suite for the JCommon class library that can be run using JUnit (http://www.junit.org).
 */
public class JCommonTestSuite extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     *
     * @return a test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("JCommon");
        suite.addTest(DatePackageTests.suite());
        suite.addTest(IOPackageTests.suite());
        suite.addTest(TextPackageTests.suite());
        suite.addTest(UIPackageTests.suite());
        suite.addTest(UtilPackageTests.suite());
        return suite;
    }

    /**
     * Constructs the test suite.
     *
     * @param name  the suite name.
     */
    public JCommonTestSuite(final String name) {
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
