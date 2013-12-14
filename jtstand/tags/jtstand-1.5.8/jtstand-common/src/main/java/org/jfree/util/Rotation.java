/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Rotation.java is part of JTStand.
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
 * Represents a direction of rotation (<code>CLOCKWISE</code> or 
 * <code>ANTICLOCKWISE</code>).
 * @author David Gilbert
 */
public final class Rotation implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -4662815260201591676L;
    
    /** Clockwise. */
    public static final Rotation CLOCKWISE 
        = new Rotation("Rotation.CLOCKWISE", -1.0);

    /** The reverse order renders the primary dataset first. */
    public static final Rotation ANTICLOCKWISE 
        = new Rotation("Rotation.ANTICLOCKWISE", 1.0);

    /** The name. */
    private String name;
    
    /** 
     * The factor (-1.0 for <code>CLOCKWISE</code> and 1.0 for 
     * <code>ANTICLOCKWISE</code>). 
     */
    private double factor;

    /**
     * Private constructor.
     *
     * @param name  the name.
     * @param factor  the rotation factor.
     */
    private Rotation(final String name, final double factor) {
        this.name = name;
        this.factor = factor;
    }

    /**
     * Returns a string representing the object.
     *
     * @return the string (never <code>null</code>).
     */
    public String toString() {
        return this.name;
    }

    /**
     * Returns the rotation factor, which is -1.0 for <code>CLOCKWISE</code> 
     * and 1.0 for <code>ANTICLOCKWISE</code>.
     * 
     * @return the rotation factor.
     */
    public double getFactor() {
        return this.factor;
    }

    /**
     * Compares this object for equality with an other object.
     * Implementation note: This simply compares the factor instead
     * of the name.
     *
     * @param o the other object
     * @return true or false
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rotation)) {
            return false;
        }

        final Rotation rotation = (Rotation) o;

        if (this.factor != rotation.factor) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return the hashcode
     */
    public int hashCode() {
        final long temp = Double.doubleToLongBits(this.factor);
        return (int) (temp ^ (temp >>> 32));
    }

    /**
     * Ensures that serialization returns the unique instances.
     * 
     * @return the object.
     * 
     * @throws ObjectStreamException if there is a problem.
     */
    private Object readResolve() throws ObjectStreamException {
        if (this.equals(Rotation.CLOCKWISE)) {
            return Rotation.CLOCKWISE;
        }
        else if (this.equals(Rotation.ANTICLOCKWISE)) {
            return Rotation.ANTICLOCKWISE;
        }      
        return null;
    }

}
