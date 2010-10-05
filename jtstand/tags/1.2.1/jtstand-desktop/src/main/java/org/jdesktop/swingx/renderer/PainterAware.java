/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, PainterAware.java is part of JTStand.
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
package org.jdesktop.swingx.renderer;

import org.jdesktop.swingx.painter.Painter;

/**
 * Temporary hook to allow painters in rendering. <p>
 * 
 * NOTE: this will be removed as soon as the painter_work enters
 * main.
 * 
 * @author Jeanette Winzenburg
 */
public interface PainterAware {
    void setPainter(Painter painter);
    Painter getPainter();
}
