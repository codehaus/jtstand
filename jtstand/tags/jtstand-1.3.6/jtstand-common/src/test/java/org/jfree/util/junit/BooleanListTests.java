/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, BooleanListTests.java is part of JTStand.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.util.BooleanList;

/**
 * Tests for the {@link BooleanList} class.
 */
public class BooleanListTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(BooleanListTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public BooleanListTests(final String name) {
        super(name);
    }

    /**
     * Tests the equals() method.
     */
    public void testEquals() {
        
        final BooleanList l1 = new BooleanList();
        l1.setBoolean(0, Boolean.TRUE);
        l1.setBoolean(1, Boolean.FALSE);
        l1.setBoolean(2, null);
        
        final BooleanList l2 = new BooleanList();
        l2.setBoolean(0, Boolean.TRUE);
        l2.setBoolean(1, Boolean.FALSE);
        l2.setBoolean(2, null);
        
        assertTrue(l1.equals(l2));
        assertTrue(l2.equals(l2));
        
    }
    
    
    /**
     * Confirm that cloning works.
     */
    public void testCloning() {
        
        final BooleanList l1 = new BooleanList();
        l1.setBoolean(0, Boolean.TRUE);
        l1.setBoolean(1, Boolean.FALSE);
        l1.setBoolean(2, null);
        
        BooleanList l2 = null;
        try {
            l2 = (BooleanList) l1.clone();
        }
        catch (CloneNotSupportedException e) {
            System.err.println("BooleanListTests.testCloning: failed to clone.");
        }
        assertTrue(l1 != l2);
        assertTrue(l1.getClass() == l2.getClass());
        assertTrue(l1.equals(l2));
        
        l2.setBoolean(0, Boolean.FALSE);
        assertFalse(l1.equals(l2));
        
    }
    
    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {

        final BooleanList l1 = new BooleanList();
        l1.setBoolean(0, Boolean.TRUE);
        l1.setBoolean(1, Boolean.FALSE);
        l1.setBoolean(2, null);

        BooleanList l2 = null;

        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(l1);
            out.close();

            final ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            l2 = (BooleanList) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(l1, l2);

    }

}
