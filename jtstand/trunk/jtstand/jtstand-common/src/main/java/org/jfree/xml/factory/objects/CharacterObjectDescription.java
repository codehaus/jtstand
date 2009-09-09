/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, CharacterObjectDescription.java is part of JTStand.
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
 * An object-description for a <code>Character</code> object.
 *
 * @author Thomas Morgner
 */
public class CharacterObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public CharacterObjectDescription() {
        super(Character.class);
        setParameterDefinition("value", String.class);
    }

    /**
     * Creates a new object (<code>Character</code>) based on this description object.
     *
     * @return The <code>Character</code> object.
     */
    public Object createObject() {
        final String o = (String) getParameter("value");
        if (o == null) {
            return null;
        }
        if (o.length() > 0) {
            return new Character(o.charAt(0));
        }
        else {
            return null;
        }
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object (should be an instance of <code>Character</code>).
     * @throws ObjectFactoryException if there is a
     * problem while reading the properties of the given object.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof Character)) {
            throw new ObjectFactoryException("The given object is no java.lang.Character.");
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

        if (Character.TYPE.equals(abstractObjectDescription.getObjectClass())) {
            return true;
        }
        if (Character.class.equals(abstractObjectDescription.getObjectClass())) {
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
