/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Size2D.java is part of JTStand.
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

import java.io.Serializable;

import org.jfree.util.PublicCloneable;

/**
 * A simple class for representing the dimensions of an object.  It would be
 * better to use <code>Dimension2D</code>, but this class is broken on various
 * JDK releases (particularly JDK 1.3.1, refer to bugs 4189446 and 4976448 on 
 * the Java bug parade).
 *
 * @author David Gilbert
 */
public class Size2D implements Cloneable, PublicCloneable, Serializable {

    /** For serialization. */ 
    private static final long serialVersionUID = 2558191683786418168L;
    
    /** The width. */
    public double width;

    /** The height. */
    public double height;

    /**
     * Creates a new instance with zero width and height.
     */
    public Size2D() {
        this(0.0, 0.0);
    }
    
    /**
     * Creates a new instance with the specified width and height.
     *
     * @param width  the width.
     * @param height  the height.
     */
    public Size2D(final double width, final double height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width.
     *
     * @return The width.
     */
    public double getWidth() {
        return this.width;
    }
    
    /**
     * Sets the width.
     * 
     * @param width  the width.
     */
    public void setWidth(final double width) {
        this.width = width;
    }

    /**
     * Returns the height.
     *
     * @return The height.
     */
    public double getHeight() {
        return this.height;
    }
    
    /**
     * Sets the height.
     * 
     * @param height  the height.
     */
    public void setHeight(final double height) {
        this.height = height;
    }
    
    /**
     * Returns a string representation of this instance, mostly used for 
     * debugging purposes.
     * 
     * @return A string.
     */
    public String toString() {
        return "Size2D[width=" + this.width + ", height=" + this.height + "]";   
    }

    /**
     * Compares this instance for equality with an arbitrary object.
     * 
     * @param obj  the object (<code>null</code> permitted).
     * 
     * @return A boolean.
     */
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Size2D)) {
            return false;
        }
        final Size2D that = (Size2D) obj;
        if (this.width != that.width) {
            return false;
        }
        if (this.height != that.height) {
            return false;
        }
        return true;
    }
    
    /**
     * Returns a clone of this object.
     * 
     * @return A clone.
     * 
     * @throws CloneNotSupportedException if the object cannot be cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
}
