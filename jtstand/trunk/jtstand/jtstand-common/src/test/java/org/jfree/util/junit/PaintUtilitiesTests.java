/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PaintUtilitiesTests.java is part of JTStand.
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
import java.awt.GradientPaint;
import java.awt.Paint;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.util.PaintUtilities;

/**
 * Some tests for the {@link PaintUtilities} class.
 */
public class PaintUtilitiesTests  extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(PaintUtilitiesTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public PaintUtilitiesTests(String name) {
        super(name);
    }

    /**
     * Some checks for the equal(Paint, Paint) method.
     */
    public void testEqual() {
        Paint p1 = Color.red;
        Paint p2 = Color.blue;
        Paint p3 = new Color(1, 2, 3, 4);
        Paint p4 = new Color(1, 2, 3, 4);
        Paint p5 = new GradientPaint(
            1.0f, 2.0f, Color.red, 3.0f, 4.0f, Color.yellow
        );
        Paint p6 = new GradientPaint(
            1.0f, 2.0f, Color.red, 3.0f, 4.0f, Color.yellow
        );
        Paint p7 = new GradientPaint(
            1.0f, 2.0f, Color.red, 3.0f, 4.0f, Color.blue
        );
        assertTrue(PaintUtilities.equal(null, null));
        assertFalse(PaintUtilities.equal(p1, null));
        assertFalse(PaintUtilities.equal(null, p1));
        assertFalse(PaintUtilities.equal(p1, p2));
        assertTrue(PaintUtilities.equal(p3, p3));
        assertTrue(PaintUtilities.equal(p3, p4));
        assertTrue(PaintUtilities.equal(p5, p6));
        assertFalse(PaintUtilities.equal(p5, p7));
    }

}
