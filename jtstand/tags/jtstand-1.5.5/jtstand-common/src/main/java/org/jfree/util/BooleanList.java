/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, BooleanList.java is part of JTStand.
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

package org.jfree.util;

/**
 * A list of <code>Boolean</code> objects.
 *
 * @author David Gilbert
 */
public class BooleanList extends AbstractObjectList {

    /** For serialization. */
    private static final long serialVersionUID = -8543170333219422042L;
    
    /**
     * Creates a new list.
     */
    public BooleanList() {
    }

    /**
     * Returns a {@link Boolean} from the list.
     *
     * @param index the index (zero-based).
     *
     * @return a {@link Boolean} from the list.
     */
    public Boolean getBoolean(final int index) {
        return (Boolean) get(index);
    }

    /**
     * Sets the value for an item in the list.  The list is expanded if 
     * necessary.
     *
     * @param index  the index (zero-based).
     * @param b  the boolean.
     */
    public void setBoolean(final int index, final Boolean b) {
        set(index, b);
    }

    /**
     * Tests the list for equality with another object (typically also a list).
     *
     * @param o  the other object.
     *
     * @return A boolean.
     */
    public boolean equals(final Object o) {

        if (o instanceof BooleanList) {
            return super.equals(o);
        }
        return false;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return the hashcode
     */
    public int hashCode() {
        return super.hashCode();
    }
}
