/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, EmptyIcon.java is part of JTStand.
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
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;

/**
 * An empty icon with arbitrary width and height.
 */
public final class EmptyIcon implements Icon, Serializable {

  private int width;
  private int height;
  
  public EmptyIcon() {
    this(0, 0);
  }
  
  public EmptyIcon(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public int getIconHeight() {
    return height;
  }

  public int getIconWidth() {
    return width;
  }

  public void paintIcon(Component c, Graphics g, int x, int y) {
  }

}
