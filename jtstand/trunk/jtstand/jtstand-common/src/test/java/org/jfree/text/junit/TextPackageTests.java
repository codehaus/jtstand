/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TextPackageTests.java is part of JTStand.
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

package org.jfree.text.junit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * A collection of tests for the org.jfree.text package. These tests can be run using JUnit 
 * (http://www.junit.org).
 */
public class TextPackageTests extends TestCase {

    /**
     * Returns a test suite to the JUnit test runner.
     *
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite("org.jfree.text");
        suite.addTestSuite(TextBlockTests.class);
        suite.addTestSuite(TextBlockAnchorTests.class);
        suite.addTestSuite(TextBoxTests.class);
        suite.addTestSuite(TextFragmentTests.class);
        suite.addTestSuite(TextLineTests.class);
        return suite;
    }

    /**
     * Constructs the test suite.
     *
     * @param name  the suite name.
     */
    public TextPackageTests(final String name) {
        super(name);
    }

}
