/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Rectangle2DObjectDescription.java is part of JTStand.
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

import java.awt.geom.Rectangle2D;

/**
 * An object-description for a <code>Rectangle2D</code> object.
 *
 * @author Thomas Morgner
 */
public class Rectangle2DObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public Rectangle2DObjectDescription() {
        super(Rectangle2D.class);
        setParameterDefinition("width", Float.class);
        setParameterDefinition("height", Float.class);
        setParameterDefinition("x", Float.class);
        setParameterDefinition("y", Float.class);
    }

    /**
     * Creates an object based on this description.
     *
     * @return The object.
     */
    public Object createObject() {
        final Rectangle2D rect = new Rectangle2D.Float();

        final float w = getFloatParameter("width");
        final float h = getFloatParameter("height");
        final float x = getFloatParameter("x");
        final float y = getFloatParameter("y");
        rect.setRect(x, y, w, h);
        return rect;
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
     * @param o  the object (should be an instance of <code>Rectangle2D</code>).
     *
     * @throws ObjectFactoryException if the object is not an instance of <code>Rectangle2D</code>.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof Rectangle2D)) {
            throw new ObjectFactoryException("The given object is no java.awt.geom.Rectangle2D.");
        }

        final Rectangle2D rect = (Rectangle2D) o;
        final float x = (float) rect.getX();
        final float y = (float) rect.getY();
        final float w = (float) rect.getWidth();
        final float h = (float) rect.getHeight();

        setParameter("x", new Float(x));
        setParameter("y", new Float(y));
        setParameter("width", new Float(w));
        setParameter("height", new Float(h));
    }

}
