/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, BooleanUtilities.java is part of JTStand.
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
 * Utility methods for working with <code>Boolean</code> objects.
 * 
 * @author David Gilbert
 */
public class BooleanUtilities {

    /**
     * Private constructor prevents object creation.
     */
    private BooleanUtilities() {
    }

    /**
     * Returns the object equivalent of the boolean primitive.
     * <p>
     * A similar method is provided by the Boolean class in JDK 1.4, but you can use this one
     * to remain compatible with earlier JDKs.
     * 
     * @param b  the boolean value.
     * 
     * @return <code>Boolean.TRUE</code> or <code>Boolean.FALSE</code>.
     */
    public static Boolean valueOf(final boolean b) {
        return (b ? Boolean.TRUE : Boolean.FALSE);
    }
    
}
