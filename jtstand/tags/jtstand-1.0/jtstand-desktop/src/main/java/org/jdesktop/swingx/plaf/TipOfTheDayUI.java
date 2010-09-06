/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TipOfTheDayUI.java is part of JTStand.
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

import javax.swing.JDialog;
import javax.swing.plaf.PanelUI;

import org.jdesktop.swingx.JXTipOfTheDay;

/**
 * Pluggable UI for <code>JXTipOfTheDay</code>.
 *  
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public abstract class TipOfTheDayUI extends PanelUI {
  
  /**
   * Creates a new JDialog to display a JXTipOfTheDay panel. If
   * <code>choice</code> is not null then the window will offer a way for the
   * end-user to not show the tip of the day dialog.
   * 
   * @param parentComponent
   * @param choice
   * @return a new JDialog to display a JXTipOfTheDay panel
   */
  public abstract JDialog createDialog(Component parentComponent,
    JXTipOfTheDay.ShowOnStartupChoice choice);
  
}
