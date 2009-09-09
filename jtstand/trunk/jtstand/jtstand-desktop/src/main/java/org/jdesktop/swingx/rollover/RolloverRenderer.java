/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, RolloverRenderer.java is part of JTStand.
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
package org.jdesktop.swingx.rollover;

/**
 * Interface to mark renderers as "live". <p>
 * 
 * PENDING: probably need methods to enabled/click taking a similar
 *   set of parameters as getXXComponent because the actual 
 *   outcome might depend on the given value. If so, we'll need
 *   to extend the XXRenderer interfaces.
 *   
 * @author Jeanette Winzenburg
 */
public interface RolloverRenderer {
    /**
     * 
     * @return true if rollover effects are on and clickable.
     */
    boolean isEnabled();
    
    /**
     * Same as AbstractButton.doClick(). It's up to client
     * code to prepare the renderer's component before calling
     * this method.
     *
     */
    void doClick();
}
