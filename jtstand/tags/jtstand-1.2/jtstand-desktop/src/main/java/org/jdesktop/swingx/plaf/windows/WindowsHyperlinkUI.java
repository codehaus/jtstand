/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, WindowsHyperlinkUI.java is part of JTStand.
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
package org.jdesktop.swingx.plaf.windows;

import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.plaf.basic.BasicHyperlinkUI;

/**
 * Extends BasicHyperlinkUI and paints the text with an offset when mouse
 * pressed.<br>
 * 
 * @deprecated pre-0.9.4 PENDING JW: why is this deprecated? What to use
 *             instead? kgs: clearly installDefaults can go away, but I
 *             distinctly recall that setTextShiftOffset was at one point
 *             uncommented in parent, leaving no differences with child. That is
 *             no longer the case.
 */
@Deprecated
public class WindowsHyperlinkUI extends BasicHyperlinkUI {

  public static ComponentUI createUI(JComponent c) {
    return new WindowsHyperlinkUI();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
protected void paintButtonPressed(Graphics g, AbstractButton b) {
    setTextShiftOffset();
  }
  
    /**
     * {@inheritDoc}
     */
    @Override
    protected void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        if (b.getBorder() == null || b.getBorder() instanceof UIResource) {
            b.setBorder(new BorderUIResource(BorderFactory.createEmptyBorder(0, 1, 0, 0)));
        }
    }
}
