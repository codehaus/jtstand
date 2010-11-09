/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ExtendedDrawable.java is part of JTStand.
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

import java.awt.Dimension;

/**
 * A drawable that has a preferred size and aspect ratio. Implement this interface to gain
 * some control over the rendering and layouting process for the drawable.
 *
 * @author Thomas Morgner
 */
public interface ExtendedDrawable extends Drawable
{
  /**
   * Returns the preferred size of the drawable. If the drawable is aspect ratio aware,
   * these bounds should be used to compute the preferred aspect ratio for this drawable.
   *
   * @return the preferred size.
   */
  public Dimension getPreferredSize ();

  /**
   * Returns true, if this drawable will preserve an aspect ratio during the drawing.
   *
   * @return true, if an aspect ratio is preserved, false otherwise.
   */
  public boolean isPreserveAspectRatio ();
}
