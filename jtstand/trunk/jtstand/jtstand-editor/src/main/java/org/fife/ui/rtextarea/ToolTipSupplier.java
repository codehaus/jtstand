/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, ToolTipSupplier.java is part of JTStand.
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

import java.awt.event.MouseEvent;


/**
 * A <tt>ToolTipSupplier</tt> can create tool tip text for an <tt>RTextArea</tt>
 * on its behalf.  A text area will check its <tt>ToolTipSupplier</tt> for a
 * tool tip before calling the super class's implementation of
 * {@link RTextArea#getToolTipText()}.  This allows
 * applications to intercept tool tip events and provide the text for a tool
 * tip without subclassing <tt>RTextArea</tt>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ToolTipSupplier {


	/**
	 * Returns the tool tip text to display for a given mouse event.
	 *
	 * @param textArea The text area.
	 * @param e The mouse event.
	 * @return The tool tip, or <code>null</code> if none.
	 */
	public String getToolTipText(RTextArea textArea, MouseEvent e);


}