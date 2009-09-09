/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ByteObjectDescription.java is part of JTStand.
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

package org.jfree.xml.factory.objects;

/**
 * An object-description for a <code>Byte</code> object.
 *
 * @author Thomas Morgner
 */
public class ByteObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public ByteObjectDescription() {
        super(Byte.class);
        setParameterDefinition("value", String.class);
    }

    /**
     * Creates a new object (<code>Byte</code>) based on this description object.
     *
     * @return The <code>Byte</code> object.
     */
    public Object createObject() {
        final String o = (String) getParameter("value");
        return Byte.valueOf(o);
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object (should be an instance of <code>Byte</code>.
     * @throws ObjectFactoryException if there is a problem
     * while reading the properties of the given object.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof Byte)) {
            throw new ObjectFactoryException("The given object is no java.lang.Byte.");
        }
        setParameter("value", String.valueOf(o));
    }

    /**
     * Tests for equality.
     * 
     * @param o  the object to test.
     * 
     * @return A boolean.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractObjectDescription)) {
            return false;
        }

        final AbstractObjectDescription abstractObjectDescription = (AbstractObjectDescription) o;

        if (Byte.TYPE.equals(abstractObjectDescription.getObjectClass())) {
            return true;
        }
        if (Byte.class.equals(abstractObjectDescription.getObjectClass())) {
            return true;
        }
        return false;
    }

    /**
     * Returns a hash code for the object.
     * 
     * @return The hash code.
     */
    public int hashCode() {
        return getObjectClass().hashCode();
    }
    
}
