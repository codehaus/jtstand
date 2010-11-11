/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SortOrder.java is part of JTStand.
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

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Defines tokens used to indicate sorting order (ascending or descending).
 *
 * @author David Gilbert
 */
public final class SortOrder implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -2124469847758108312L;
    
    /** Ascending order. */
    public static final SortOrder ASCENDING 
        = new SortOrder("SortOrder.ASCENDING");

    /** Descending order. */
    public static final SortOrder DESCENDING 
        = new SortOrder("SortOrder.DESCENDING");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private SortOrder(final String name) {
        this.name = name;
    }

    /**
     * Returns a string representing the object.
     *
     * @return The string.
     */
    public String toString() {
        return this.name;
    }

    /**
     * Returns <code>true</code> if this object is equal to the specified 
     * object, and <code>false</code> otherwise.
     *
     * @param obj  the other object.
     *
     * @return A boolean.
     */
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SortOrder)) {
            return false;
        }

        final SortOrder that = (SortOrder) obj;
        if (!this.name.equals(that.toString())) {
            return false;
        }

        return true;
    }
    
    /**
     * Returns a hash code value for the object.
     *
     * @return The hashcode
     */
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * Ensures that serialization returns the unique instances.
     * 
     * @return The object.
     * 
     * @throws ObjectStreamException if there is a problem.
     */
    private Object readResolve() throws ObjectStreamException {
        if (this.equals(SortOrder.ASCENDING)) {
            return SortOrder.ASCENDING;
        }
        else if (this.equals(SortOrder.DESCENDING)) {
            return SortOrder.DESCENDING;
        }
        return null;
    }
}
