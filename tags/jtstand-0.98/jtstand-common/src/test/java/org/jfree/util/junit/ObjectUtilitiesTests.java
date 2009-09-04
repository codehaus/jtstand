/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectUtilitiesTests.java is part of JTStand.
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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.util.ObjectUtilities;

/**
 * Some tests for the {@link ObjectUtilities} class.
 */
public class ObjectUtilitiesTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(ObjectUtilitiesTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public ObjectUtilitiesTests(String name) {
        super(name);
    }

    /**
     * Some checks for the clone(Object) method.  The method should
     * have these properties:
     * 
     * (1) Return a clone for any object that can be cloned;
     * (2) Throw a NullPointerException for any object that cannot be cloned;
     * (3) Throw an IllegalArgumentException for null.
     * 
     * @throws CloneNotSupportedException if there is a problem cloning.
     */
    public void testCloneObject() throws CloneNotSupportedException {
        Object obj;
        Object clone;
        
        // check String (not Cloneable)
        boolean pass = false;
        obj = "Hello World";  
        try {
            clone = ObjectUtilities.clone(obj);
            pass = false;
        }
        catch (CloneNotSupportedException e) {
            pass = true;
        }
        assertTrue(pass);
        
        // check Integer (not Cloneable)
        pass = false;
        obj = new Integer(123); 
        try {
            clone = ObjectUtilities.clone(obj);
            pass = false;
        }
        catch (CloneNotSupportedException e) {
            pass = true;
        }
        assertTrue(pass);
        
        // check Point (Cloneable)
        obj = new Point(1, 2);  
        clone = ObjectUtilities.clone(obj);
        assertEquals(obj, clone);

        // check null (should throw an IllegalArgumentException)
        obj = null;  
        try {
            clone = ObjectUtilities.clone(obj);
            pass = false;
        }
        catch (IllegalArgumentException e) {
            pass = true;
        }
        assertTrue(pass);
    }
    
    /**
     * Some checks for the deepClone(Collection) method.
     */
    public void testDeepClone() {
        Collection c1 = new ArrayList();
        Collection c2 = null;
        
        // empty list
        try {
            c2 = ObjectUtilities.deepClone(c1);
            assertTrue(c2.isEmpty());
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }
        
        // list containing Cloneable objects
        c1 = new ArrayList();
        c1.add(new Point(1, 2));
        c1.add(new Point(3, 4));
        try {
            c2 = ObjectUtilities.deepClone(c1);
            assertEquals(2, c2.size());
            assertTrue(c2.contains(new Point(1, 2)));
            assertTrue(c2.contains(new Point(3, 4)));
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }

        // list containing Cloneable and null objects
        c1 = new ArrayList();
        c1.add(new Point(1, 2));
        c1.add(null);
        c1.add(new Point(3, 4));
        try {
            c2 = ObjectUtilities.deepClone(c1);
            assertEquals(3, c2.size());
            assertTrue(c2.contains(new Point(1, 2)));
            assertTrue(c2.contains(new Point(3, 4)));
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }
        
        // list containing non-Cloneable objects
        c1.clear();
        c1.add("S1");
        c1.add("S2");
        try {
            c2 = ObjectUtilities.deepClone(c1);
            assertTrue(false);  // if we get to here, the test has failed
        }
        catch (CloneNotSupportedException e) {
            assertTrue(true);
        }
        
        // null list
        try {
            c2 = ObjectUtilities.deepClone(null);
            assertTrue(false);  // if we get to here, the test has failed
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        catch (CloneNotSupportedException e) {
            assertTrue(false);
        }

    }

}
