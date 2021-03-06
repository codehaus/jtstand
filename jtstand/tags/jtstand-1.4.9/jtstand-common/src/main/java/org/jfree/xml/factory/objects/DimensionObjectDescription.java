/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DimensionObjectDescription.java is part of JTStand.
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

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

/**
 * An object-description for a <code>java.awt.Dimension</code> object.
 *
 * @author Thomas Morgner
 */
public class DimensionObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public DimensionObjectDescription() {
        super(Dimension.class);
        setParameterDefinition("width", Float.class);
        setParameterDefinition("height", Float.class);
    }

    /**
     * Creates an object based on the description.
     *
     * @return The object.
     */
    public Object createObject() {
        final Dimension2D dim = new Dimension();

        final float width = getFloatParameter("width");
        final float height = getFloatParameter("height");
        dim.setSize(width, height);
        return dim;
    }

    /**
     * Returns a parameter value as a float.
     *
     * @param param  the parameter name.
     *
     * @return The float value.
     */
    private float getFloatParameter(final String param) {
        final Float p = (Float) getParameter(param);
        if (p == null) {
            return 0;
        }
        return p.floatValue();
    }


    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object (should be an instance of <code>Dimension2D</code>).
     *
     * @throws ObjectFactoryException if the object is not an instance of <code>Point2D</code>.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof Dimension)) {
            throw new ObjectFactoryException("The given object is no java.awt.geom.Dimension2D.");
        }

        final Dimension dim = (Dimension) o;
        final float width = (float) dim.getWidth();
        final float height = (float) dim.getHeight();

        setParameter("width", new Float(width));
        setParameter("height", new Float(height));
    }
}
