/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, HyperlinkAddon.java is part of JTStand.
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


import org.jdesktop.swingx.JXHyperlink;

/**
 * Addon for <code>JXHyperlink</code>.<br>
 *
 */
public class HyperlinkAddon extends AbstractComponentAddon {

  public HyperlinkAddon() {
    super("JXHyperlink");
  }

  @Override
  protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
      super.addBasicDefaults(addon, defaults);
      
    if (isMetal(addon)) {
        defaults.add(JXHyperlink.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicHyperlinkUI");
    } else {
        defaults.add(JXHyperlink.uiClassID, "org.jdesktop.swingx.plaf.windows.WindowsHyperlinkUI");
    }
  }

}
