/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ArrayUtilitiesTests.java is part of JTStand.
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
import org.jfree.util.ArrayUtilities;

/**
 * Tests for the {@link ArrayUtilities} class.
 */
public class ArrayUtilitiesTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(ArrayUtilitiesTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public ArrayUtilitiesTests(String name) {
        super(name);
    }

    /**
     * Some tests for the hasDuplicateItems() method.
     */
    public void testHasDuplicateItems() {
        Object[] a1 = new Object[] {"1", "2", "3"};
        Object[] a2 = new Object[] {"1", "1", "3"};
        Object[] a3 = new Object[] {null, "2", null};
        assertFalse(ArrayUtilities.hasDuplicateItems(a1));
        assertTrue(ArrayUtilities.hasDuplicateItems(a2));
        assertFalse(ArrayUtilities.hasDuplicateItems(a3));
    }
    
    /**
     * Some checks for the equalReferencesInArrays() method.
     */
    public void testEqualReferencesInArrays() {
        Object[] a1 = new Object[] {};
        Object[] a2 = new Object[] {};
        Object[] a3 = new Object[] {null};
        Object[] a4 = new Object[] {null};
        Object[] a5 = new Object[] {"A"};
        Object[] a6 = new Object[] {"A"};
        Object[] a7 = new Object[] {"A", "B"};
        Object[] a8 = new Object[] {"A", "B"};
        Object[] a9 = new Object[] {"A", null};
        Object[] a10 = new Object[] {"A", null};
        
        assertTrue(ArrayUtilities.equalReferencesInArrays(a1, a2));
        assertFalse(ArrayUtilities.equalReferencesInArrays(a1, a3));
        assertTrue(ArrayUtilities.equalReferencesInArrays(a3, a4));
        assertFalse(ArrayUtilities.equalReferencesInArrays(a3, a5));
        assertTrue(ArrayUtilities.equalReferencesInArrays(a5, a6));
        assertFalse(ArrayUtilities.equalReferencesInArrays(a5, a7));
        assertTrue(ArrayUtilities.equalReferencesInArrays(a7, a8));
        assertFalse(ArrayUtilities.equalReferencesInArrays(a7, a9));
        assertTrue(ArrayUtilities.equalReferencesInArrays(a9, a10));
    }
}
