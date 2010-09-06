/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, PainterIcon.java is part of JTStand.
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

package org.jdesktop.swingx.painter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

public class PainterIcon implements Icon {
    Dimension size;
    private Painter painter;
    public PainterIcon(Dimension size) {
        this.size = size;
    }
    
    public int getIconHeight() {
        return size.height;
    }
    
    public int getIconWidth() {
        return size.width;
    }
    
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (getPainter() != null && g instanceof Graphics2D) {
            g = g.create();
            
            try {
                g.translate(x, y);
                getPainter().paint((Graphics2D) g, c, size.width, size.height);
                g.translate(-x, -y);
            } finally {
                g.dispose();
            }
        }
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter painter) {
        this.painter = painter;
    }
}