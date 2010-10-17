/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, GutterIconInfo.java is part of JTStand.
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
package org.fife.ui.rtextarea;

import javax.swing.Icon;


/**
 * Information about an icon displayed in a {@link Gutter}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface GutterIconInfo {


	/**
	 * Returns the icon being rendered.
	 *
	 * @return The icon being rendered.
	 */
	public Icon getIcon();


	/**
	 * Returns the offset that is being tracked.  The line of this offset is
	 * where the icon is rendered.  This offset may change as the user types.
	 *
	 * @return The offset being tracked.
	 */
	public int getMarkedOffset();

}