/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JavaTestSuite.java is part of JTStand.
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

/**
 * A collection of tests that I use to examine the behaviour of Java in particular situations. 
 * I expect some of these tests to fail - that doesn't always indicate a bug, only that Java
 * doesn't work the way I might want it to.
 */
public class JavaTestSuite extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     *
     * @return a test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("Java Tests");
        suite.addTest(PaintTests.suite());
        suite.addTest(StrokeTests.suite());
        return suite;
    }

    /**
     * Constructs the test suite.
     *
     * @param name  the suite name.
     */
    public JavaTestSuite(final String name) {
        super(name);
    }

}
