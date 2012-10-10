/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StandardGradientPaintTransformerTests.java is part of JTStand.
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
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

/**
 * Tests for the {@link StandardGradientPaintTransformer} class.
 */
public class StandardGradientPaintTransformerTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(StandardGradientPaintTransformerTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public StandardGradientPaintTransformerTests(final String name) {
        super(name);
    }

    /**
     * Test that the equals() method distinguishes all fields.
     */
    public void testEquals() {
        StandardGradientPaintTransformer t1 = new StandardGradientPaintTransformer();
        StandardGradientPaintTransformer t2 = new StandardGradientPaintTransformer();
        assertTrue(t1.equals(t2));
        assertTrue(t2.equals(t1));
        
        t1 = new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL);
        assertFalse(t1.equals(t2));
        t2 = new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL);
        assertTrue(t1.equals(t2));
    }
    
    /**
     * Serialize an instance, restore it, and check for identity.
     */
    public void testSerialization() {

        final StandardGradientPaintTransformer t1 = new StandardGradientPaintTransformer();
        StandardGradientPaintTransformer t2 = null;

        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(t1);
            out.close();

            final ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            t2 = (StandardGradientPaintTransformer) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertTrue(t1.equals(t2)); 

    }

}
