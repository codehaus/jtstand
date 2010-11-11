/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AttributedStringUtilitiesTests.java is part of JTStand.
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

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.util.AttributedStringUtilities;

/**
 * Some tests for the {@link AttributedStringUtilities} class.
 */
public class AttributedStringUtilitiesTests  extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(AttributedStringUtilitiesTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public AttributedStringUtilitiesTests(String name) {
        super(name);
    }

    /**
     * Some checks for the equal(AttributedString, AttributedString) method.
     */
    public void testEqual() {
        assertTrue(AttributedStringUtilities.equal(null, null));
  
        AttributedString s1 = new AttributedString("ABC");
        assertFalse(AttributedStringUtilities.equal(s1, null));
        assertFalse(AttributedStringUtilities.equal(null, s1));
        
        AttributedString s2 = new AttributedString("ABC");
        assertTrue(AttributedStringUtilities.equal(s1, s2));
        
        s1.addAttribute(TextAttribute.BACKGROUND, Color.blue, 1, 2);
        assertFalse(AttributedStringUtilities.equal(s1, s2));
        s2.addAttribute(TextAttribute.BACKGROUND, Color.blue, 1, 2);
        assertTrue(AttributedStringUtilities.equal(s1, s2));
    }

}
