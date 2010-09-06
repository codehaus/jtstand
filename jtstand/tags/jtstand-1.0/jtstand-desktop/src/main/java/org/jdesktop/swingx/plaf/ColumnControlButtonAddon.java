/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, ColumnControlButtonAddon.java is part of JTStand.
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

import javax.swing.plaf.InsetsUIResource;

import org.jdesktop.swingx.icon.ColumnControlIcon;

/**
 * Addon to load LF specific properties for the ColumnControlButton.
 * 
 * @author Jeanette Winzenburg
 */
public class ColumnControlButtonAddon extends AbstractComponentAddon {

    /**
     * Instantiates the addon for ColumnControlButton.
     */
    public ColumnControlButtonAddon() {
        super("ColumnControlButton");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon,
            DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        defaults.add("ColumnControlButton.actionIcon", new ColumnControlIcon());
        defaults.add("ColumnControlButton.margin", new InsetsUIResource(1, 2, 2, 1)); 
    }

    
}
