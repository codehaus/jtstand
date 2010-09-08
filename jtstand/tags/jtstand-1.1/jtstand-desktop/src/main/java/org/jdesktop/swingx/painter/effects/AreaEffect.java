/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, AreaEffect.java is part of JTStand.
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
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jdesktop.swingx.painter.effects;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * An effect which works on AbstractPathPainters or any thing else which can provide a shape to be drawn.
 * @author joshy
 */
public interface AreaEffect {
    /*
     * Applies the shape effect. This effect will be drawn on top of the graphics context.
     */
    /**
     * Draws an effect on the specified graphics and path using the specified width and height.
     * @param g 
     * @param clipShape 
     * @param width 
     * @param height 
     */
    public abstract void apply(Graphics2D g, Shape clipShape, int width, int height);
}
