/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectTableTests.java is part of JTStand.
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.util.ObjectTable;

/**
 * Tests for the {@link ObjectTable} class.
 */
public class ObjectTableTests extends TestCase {

    /**
     * Basic object table.
     */
    public class TObjectTable extends ObjectTable {

        /**
         * Constructor.
         */
        public TObjectTable() {
            super();
        }

        /**
         * Returns the object from a particular cell in the table.
         * Returns null, if there is no object at the given position.
         *
         * @param row  the row index (zero-based).
         * @param column  the column index (zero-based).
         *
         * @return The object.
         */
        public Object getObject(final int row, final int column) {
            return super.getObject(row, column);
        }

        /**
         * Sets the object for a cell in the table.  The table is expanded if necessary.
         *
         * @param row  the row index (zero-based).
         * @param column  the column index (zero-based).
         * @param object  the object.
         */
        public void setObject(final int row, final int column, final Object object) {
            super.setObject(row, column, object);
        }
    }

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(ObjectTableTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public ObjectTableTests(final String name) {
        super(name);
    }

    /**
     * When an ObjectTable is created, it should be empty and return null for all lookups.
     */
    public void testCreate() {

        final TObjectTable t = new TObjectTable();

        // the new table should have zero rows and zero columns...
        assertEquals(t.getColumnCount(), 0);
        assertEquals(t.getRowCount(), 0);

        // ...and should return null for any lookup
        assertNull(t.getObject(0, 0));
        assertNull(t.getObject(12, 12));

    }

    /**
     * When an object is added to the table outside the current bounds, the table
     * should resize automatically.
     */
    public void testSetObject1() {

        final TObjectTable t = new TObjectTable();
        t.setObject(8, 5, Color.red);
        assertEquals(6, t.getColumnCount());
        assertEquals(9, t.getRowCount());
        assertNull(t.getObject(7, 4));
        assertEquals(Color.red, t.getObject(8, 5));

    }

}
