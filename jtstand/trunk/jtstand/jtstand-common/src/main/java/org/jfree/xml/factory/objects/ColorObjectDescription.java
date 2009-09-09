/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ColorObjectDescription.java is part of JTStand.
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

import java.awt.Color;

import org.jfree.util.PaintUtilities;

/**
 * An object-description for a <code>Color</code> object.
 *
 * @author Thomas Morgner
 */
public class ColorObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public ColorObjectDescription() {
        super(Color.class);
        setParameterDefinition("value", String.class);
    }

    /**
     * Creates an object based on this description.
     *
     * @return The object.
     */
    public Object createObject() {
        final String value = (String) getParameter("value");
        return PaintUtilities.stringToColor(value);
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object (should be an instance of <code>Color</code>).
     *
     * @throws ObjectFactoryException if there is a problem while reading the
     * properties of the given object.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof Color)) {
            throw new ObjectFactoryException("Is no instance of java.awt.Color");
        }
        final Color c = (Color) o;
        setParameter("value", PaintUtilities.colorToString(c));
    }
}
