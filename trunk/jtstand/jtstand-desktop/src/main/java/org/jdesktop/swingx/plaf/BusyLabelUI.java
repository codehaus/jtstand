/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, BusyLabelUI.java is part of JTStand.
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

import java.awt.Dimension;

import org.jdesktop.swingx.painter.BusyPainter;

/**
 *
 * @author rah003
 */
public interface BusyLabelUI {
    /**
     * @return The BusyPainter for the JXBusyLabel. If
     * this method returns null, then no progress indication will be shown by busy label.
     */
    public BusyPainter getBusyPainter(Dimension dim);
    
    /**
     * Delay between moving from one point to another. The exact timing will be close to the selected value but is not guearantied to be precise (subject to the timing precision of underlaying jvm).
     * @return Delay in ms.
     */
    public int getDelay();
}
