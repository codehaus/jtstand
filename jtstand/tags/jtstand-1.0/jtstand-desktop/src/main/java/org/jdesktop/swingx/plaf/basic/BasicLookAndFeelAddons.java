/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, BasicLookAndFeelAddons.java is part of JTStand.
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
package org.jdesktop.swingx.plaf.basic;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.UIManagerExt;

/**
 * Install simple pluggable UI. Usually not used directly, subclasses should be
 * preferred as this addon may not provide complete implementation of the
 * additional pluggable UIs.
 */
public class BasicLookAndFeelAddons extends LookAndFeelAddons {
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        super.initialize();
        //must add resource bundle before adding component values
        UIManagerExt.addResourceBundle(
                "org.jdesktop.swingx.plaf.basic.resources.swingx");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninitialize() {
        //must remove resource bundle before adding component values
        UIManagerExt.removeResourceBundle(
                "org.jdesktop.swingx.plaf.basic.resources.swingx");
        super.uninitialize();
    }
    
}
