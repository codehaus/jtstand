/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PaintTests.java is part of JTStand.
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

package org.jfree.junit;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the {@link Paint} interface and known subclasses.
 */
public class PaintTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return the test suite.
     */
    public static Test suite() {
        return new TestSuite(PaintTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public PaintTests(final String name) {
        super(name);
    }

    /**
     * Check that the equals() method distinguishes all fields.
     */
    public void testColorEquals() {
        final Paint p1 = new Color(0xFF, 0xEE, 0xDD);
        final Paint p2 = new Color(0xFF, 0xEE, 0xDD);
        assertEquals(p1, p2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode. 
     */
    public void testColorHashcode() {
        final Paint p1 = new Color(0xFF, 0xEE, 0xDD);
        final Paint p2 = new Color(0xFF, 0xEE, 0xDD);
        assertTrue(p1.equals(p2));
        final int h1 = p1.hashCode();
        final int h2 = p2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Check that the equals() method distinguishes all fields.
     */
    public void testGradientPaintEquals() {
        final Paint p1 = new GradientPaint(10.0f, 20.0f, Color.blue, 30.0f, 40.0f, Color.red);
        final Paint p2 = new GradientPaint(10.0f, 20.0f, Color.blue, 30.0f, 40.0f, Color.red);
        assertEquals(p1, p2);
    }

    /**
     * Check that the equals() method distinguishes all fields.
     */
    public void testTexturePaintEquals() {
        final Paint p1 = new TexturePaint(
            new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB), new Rectangle2D.Double()
        );
        final Paint p2 = new TexturePaint(
            new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB), new Rectangle2D.Double()
        );
        assertEquals(p1, p2);
    }

}
