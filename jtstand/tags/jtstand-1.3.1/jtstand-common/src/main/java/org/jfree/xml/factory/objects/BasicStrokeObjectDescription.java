/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, BasicStrokeObjectDescription.java is part of JTStand.
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

import java.awt.BasicStroke;

/**
 * An object-description for a <code>BasicStroke</code> object.
 *
 * @author Thomas Morgner
 */
public class BasicStrokeObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public BasicStrokeObjectDescription() {
        super(BasicStroke.class);
        setParameterDefinition("value", String.class);
        setParameterDefinition("width", Float.class);
        setParameterDefinition("dashes", Float[].class);
    }

    /**
     * Returns a parameter as a float.
     *
     * @param param  the parameter name.
     *
     * @return The float value.
     */
    private float getFloatParameter(final String param) {
        final String p = (String) getParameter(param);
        if (p == null) {
            return 0;
        }
        try {
            return Float.parseFloat(p);
        }
        catch (Exception e) {
            return 0;
        }
    }

    /**
     * Creates a new <code>BasicStroke</code> object based on this description.
     *
     * @return The <code>BasicStroke</code> object.
     */
    public Object createObject() {

        final float width = getFloatParameter("value");
        if (width > 0) {
          return new BasicStroke(width);
        }

        Float realWidth = (Float) getParameter("width");
        Float[] dashes = (Float[]) getParameter("dashes");
        if (realWidth == null || dashes == null) {
            return null;
        }
        float[] dashesPrimitive = new float[dashes.length];
        for (int i = 0; i < dashes.length; i++) {
          Float dash = dashes[i];
          dashesPrimitive[i] = dash.floatValue();
        }
        return new BasicStroke(realWidth.floatValue(),
                BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                10.0f, dashesPrimitive, 0.0f);
    }

    /**
     * Sets the parameters for this description to match the supplied object.
     *
     * @param o  the object (instance of <code>BasicStroke</code> required).
     *
     * @throws ObjectFactoryException if the supplied object is not an instance of
     *         <code>BasicStroke</code>.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof BasicStroke)) {
            throw new ObjectFactoryException("Expected object of type BasicStroke");
        }
        final BasicStroke bs = (BasicStroke) o;
        setParameter("value", String.valueOf(bs.getLineWidth()));
    }
}
