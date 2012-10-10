/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PaintList.java is part of JTStand.
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

import java.awt.Paint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.io.SerialUtilities;

/**
 * A table of {@link Paint} objects.
 *
 * @author David Gilbert
 */
public class PaintList extends AbstractObjectList {

    /**
     * Creates a new list.
     */
    public PaintList() {
        super();
    }

    /**
     * Returns a {@link Paint} object from the list.
     *
     * @param index the index (zero-based).
     *
     * @return The object.
     */
    public Paint getPaint(final int index) {
        return (Paint) get(index);
    }

    /**
     * Sets the {@link Paint} for an item in the list.  The list is expanded if necessary.
     *
     * @param index  the index (zero-based).
     * @param paint  the {@link Paint}.
     */
    public void setPaint(final int index, final Paint paint) {
        set(index, paint);
    }

    /**
     * Tests the list for equality with another object (typically also a list).
     *
     * @param obj  the other object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof PaintList) {
            PaintList that = (PaintList) obj;
            int listSize = size();
            for (int i = 0; i < listSize; i++) {
               if (!PaintUtilities.equal(getPaint(i), that.getPaint(i))) {
                   return false;
               }
            }
        }
        return true;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return the hashcode
     */
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(final ObjectOutputStream stream) throws IOException {

        stream.defaultWriteObject();
        final int count = size();
        stream.writeInt(count);
        for (int i = 0; i < count; i++) {
            final Paint paint = getPaint(i);
            if (paint != null) {
                stream.writeInt(i);
                SerialUtilities.writePaint(paint, stream);
            }
            else {
                stream.writeInt(-1);
            }
        }

    }
    
    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {

        stream.defaultReadObject();
        final int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            final int index = stream.readInt();
            if (index != -1) {
                setPaint(index, SerialUtilities.readPaint(stream));
            }
        }
        
    }

}

