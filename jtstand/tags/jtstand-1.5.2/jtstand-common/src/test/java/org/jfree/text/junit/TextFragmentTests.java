/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TextFragmentTests.java is part of JTStand.
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

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.text.TextFragment;

/**
 * Tests for the {@link TextFragment} class.
 */
public class TextFragmentTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(TextFragmentTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public TextFragmentTests(final String name) {
        super(name);
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    public void testEquals() {
        
        TextFragment tf1 = new TextFragment("Test");
        TextFragment tf2 = new TextFragment("Test");
        assertTrue(tf1.equals(tf2));
        assertTrue(tf2.equals(tf1));

        // text
        tf1 = new TextFragment("Test 1");
        assertFalse(tf1.equals(tf2));
        tf2 = new TextFragment("Test 1");
        assertTrue(tf1.equals(tf2));

        // font
        tf1 = new TextFragment("Test 1", new Font("Arial", Font.BOLD, 11));
        assertFalse(tf1.equals(tf2));
        tf2 = new TextFragment("Test 1", new Font("Arial", Font.BOLD, 11));
        assertTrue(tf1.equals(tf2));
        
        // paint
        tf1 = new TextFragment("Test 1", new Font("Arial", Font.BOLD, 11), Color.red);
        assertFalse(tf1.equals(tf2));
        tf2 = new TextFragment("Test 1", new Font("Arial", Font.BOLD, 11), Color.red);
        assertTrue(tf1.equals(tf2));

    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {

        final TextFragment tf1 = new TextFragment("Test");
        TextFragment tf2 = null;

        try {
            final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            final ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(tf1);
            out.close();

            final ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            tf2 = (TextFragment) in.readObject();
            in.close();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        assertEquals(tf1, tf2);

    }

}
