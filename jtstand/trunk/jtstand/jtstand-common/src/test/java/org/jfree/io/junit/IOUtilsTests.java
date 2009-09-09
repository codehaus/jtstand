/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, IOUtilsTests.java is part of JTStand.
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

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.io.IOUtils;

/**
 * Creates a new test case.
 */
public class IOUtilsTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(IOUtilsTests.class);
    }

    /**
     * Constructs a new set of tests.
     */
    public IOUtilsTests() {
        super();
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public IOUtilsTests(final String name) {
        super(name);
    }

    /**
     * A test for the createRelativeURL() method.
     * 
     * @throws IOException if there is an I/O problem.
     */
    public void testCreateRelativeURL() throws IOException {
        final URL baseurl = new URL
            ("http://test.com:80/test/a/funny/directory/basefile.xml");

        final URL testInput1 = new URL ("http://test.com:80/test/a/funny/directory/datafile.jpg");
        String result = IOUtils.getInstance().createRelativeURL(testInput1, baseurl);
        assertEquals("datafile.jpg", result);
        assertEquals(testInput1, new URL (baseurl, result));

        final URL testInput2 = new URL ("http://test.com:80/test/adatafile.jpg");
        result = IOUtils.getInstance().createRelativeURL(testInput2, baseurl);
        assertEquals("../../../adatafile.jpg", result);
        assertEquals(testInput2, new URL (baseurl, result));

        final URL testInput3 = new URL ("http://test.com:80/test/adatafile.jpg?query=test");
        result = IOUtils.getInstance().createRelativeURL(testInput3, baseurl);
        assertEquals("../../../adatafile.jpg?query=test", result);
        assertEquals(testInput3, new URL (baseurl, result));
    }
    
}
