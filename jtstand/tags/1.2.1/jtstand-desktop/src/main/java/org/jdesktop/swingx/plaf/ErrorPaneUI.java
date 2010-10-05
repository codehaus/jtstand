/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, ErrorPaneUI.java is part of JTStand.
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

package org.jdesktop.swingx.plaf;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.plaf.PanelUI;

/**
 * The ComponentUI for a JXErrorPane.
 * <p>
 * 
 * @author rbair
 */
public abstract class ErrorPaneUI extends PanelUI {
    /**
     * Creates new ErrorPane wrapped in the frame window centered at provided owner component. 
     * @param owner component to center created error frame at.
     * @return New ErrorPane instance wrapped in JFrame.
     */
    public abstract JFrame getErrorFrame(Component owner);
    
    /**
     * Creates new ErrorPane wrapped in the dialog window centered at provided owner component. 
     * @param owner component to center created error dialog at.
     * @return New ErrorPane instance wrapped in JDialog.
     */
    public abstract JDialog getErrorDialog(Component owner);
    
    /**
     * Creates new ErrorPane wrapped in the internal frame window centered at provided owner component. 
     * @param owner component to center created error frame at.
     * @return New ErrorPane instance wrapped in JInternalFrame.
     */
    public abstract JInternalFrame getErrorInternalFrame(Component owner);
    /**
     * Calculates default prefered size for JXErrorPane on given platform/LAF.
     * @return Preferred size.
     */
    public abstract Dimension calculatePreferredSize();
}
