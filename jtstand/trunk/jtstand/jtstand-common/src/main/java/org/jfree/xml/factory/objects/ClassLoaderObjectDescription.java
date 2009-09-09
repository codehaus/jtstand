/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ClassLoaderObjectDescription.java is part of JTStand.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.jfree.util.ObjectUtilities;

/**
 * An object-description for a class loader.
 *
 * @author Thomas Morgner
 */
public class ClassLoaderObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public ClassLoaderObjectDescription() {
        super(Object.class);
        setParameterDefinition("class", String.class);
    }

    /**
     * Creates an object based on this object description.
     *
     * @return The object.
     */
    public Object createObject() {
        try {
            final String o = (String) getParameter("class");
            return ObjectUtilities.getClassLoader(getClass()).loadClass(o).newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the parameters of the object description to match the supplied object.
     *
     * @param o  the object.
     *
     * @throws ObjectFactoryException if there is a problem while reading the
     * properties of the given object.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (o == null) {
            throw new ObjectFactoryException("The Object is null.");
        }
        try {
            final Constructor c = o.getClass().getConstructor(new Class[0]);
            if (!Modifier.isPublic(c.getModifiers())) {
                throw new ObjectFactoryException
                    ("The given object has no public default constructor. [" + o.getClass() + "]");
            }
            setParameter("class", o.getClass().getName());
        }
        catch (Exception e) {
            throw new ObjectFactoryException
                ("The given object has no default constructor. [" + o.getClass() + "]", e);
        }
    }
}
