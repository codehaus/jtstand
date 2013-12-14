/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Drawable.java is part of JTStand.
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

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * An interface for an object that can draw itself within an area on a Graphics2D.
 *
 * @author David Gilbert
 */
public interface Drawable {

    /**
     * Draws the object.
     *
     * @param g2  the graphics device.
     * @param area  the area inside which the object should be drawn.
     */
    public void draw(Graphics2D g2, Rectangle2D area);

}
