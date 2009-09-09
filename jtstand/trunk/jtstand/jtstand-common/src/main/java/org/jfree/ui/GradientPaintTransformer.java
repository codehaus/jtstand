/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, GradientPaintTransformer.java is part of JTStand.
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

import java.awt.GradientPaint;
import java.awt.Shape;

/**
 * The interface for a class that can transform a <code>GradientPaint</code> to
 * fit an arbitrary shape.
 * 
 * @author David Gilbert
 */
public interface GradientPaintTransformer {
    
    /**
     * Transforms a <code>GradientPaint</code> instance to fit some target 
     * shape.  Classes that implement this method typically return a new
     * instance of <code>GradientPaint</code>.
     * 
     * @param paint  the original paint (not <code>null</code>).
     * @param target  the reference area (not <code>null</code>).
     * 
     * @return A transformed paint.
     */
    public GradientPaint transform(GradientPaint paint, Shape target);

}
