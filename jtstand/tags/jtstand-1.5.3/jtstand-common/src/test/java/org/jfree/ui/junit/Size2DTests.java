/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Size2DTests.java is part of JTStand.
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

package org.jfree.ui.junit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.ui.Size2D;

/**
 * Tests for the {@link Size2D} class.
 */
public class Size2DTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(Size2DTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public Size2DTests(final String name) {
        super(name);
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    public void testEquals() {
        
        Size2D s1 = new Size2D(1.0, 2.0);
        Size2D s2 = new Size2D(1.0, 2.0);
        assertTrue(s1.equals(s2));
        assertTrue(s2.equals(s1));
        
        s1 = new Size2D(2.0, 2.0);
        assertFalse(s1.equals(s2));
        s2 = new Size2D(2.0, 2.0);
        assertTrue(s1.equals(s2));
        
        s1 = new Size2D(2.0, 3.0);
        assertFalse(s1.equals(s2));
        s2 = new Size2D(2.0, 3.0);
        assertTrue(s1.equals(s2));
    }
        
    /**
     * Confirm that cloning works.
     */
    public void testCloning() {
        Size2D s1 = new Size2D(1.0, 2.0);
        Size2D s2 = null;
        try {
            s2 = (Size2D) s1.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.err.println("Failed to clone.");
        }
        assertTrue(s1 != s2);
        assertTrue(s1.getClass() == s2.getClass());
        assertTrue(s1.equals(s2));
    }
    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {

        final Size2D s1 = new Size2D(3.0, 4.0);
        Size2D s2 = null;

        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(s1);
            out.close();

            final ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            s2 = (Size2D) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(s1.equals(s2));

    }

}
