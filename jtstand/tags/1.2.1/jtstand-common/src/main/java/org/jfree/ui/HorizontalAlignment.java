/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, HorizontalAlignment.java is part of JTStand.
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

package org.jfree.ui;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * An enumeration of the horizontal alignment types (<code>LEFT</code>, 
 * <code>RIGHT</code> and <code>CENTER</code>).
 *
 * @author David Gilbert
 */
public final class HorizontalAlignment implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -8249740987565309567L;
    
    /** Left alignment. */
    public static final HorizontalAlignment LEFT 
        = new HorizontalAlignment("HorizontalAlignment.LEFT");

    /** Right alignment. */
    public static final HorizontalAlignment RIGHT 
        = new HorizontalAlignment("HorizontalAlignment.RIGHT");

    /** Center alignment. */
    public static final HorizontalAlignment CENTER 
        = new HorizontalAlignment("HorizontalAlignment.CENTER");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private HorizontalAlignment(final String name) {
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
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HorizontalAlignment)) {
            return false;
        }
        final HorizontalAlignment that = (HorizontalAlignment) obj;
        if (!this.name.equals(that.name)) {
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
        HorizontalAlignment result = null;
        if (this.equals(HorizontalAlignment.LEFT)) {
            result = HorizontalAlignment.LEFT;
        }
        else if (this.equals(HorizontalAlignment.RIGHT)) {
            result = HorizontalAlignment.RIGHT;
        }
        else if (this.equals(HorizontalAlignment.CENTER)) {
            result = HorizontalAlignment.CENTER;
        }
        return result;
    }
    
}