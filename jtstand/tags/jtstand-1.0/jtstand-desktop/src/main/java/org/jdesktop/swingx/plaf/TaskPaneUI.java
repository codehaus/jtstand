/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TaskPaneUI.java is part of JTStand.
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

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.plaf.PanelUI;

/**
 * Pluggable UI for <code>JXTaskPane</code>.
 *  
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public abstract class TaskPaneUI extends PanelUI {

  /**
   * Called by the component when an action is added to the component through
   * the {@link org.jdesktop.swingx.JXTaskPane#add(Action)} method.
   * 
   * @param action
   * @return a component built from the action.
   */
  public Component createAction(Action action) {
    return new JButton(action);
  }

}
