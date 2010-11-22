/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TextMeasurer.java is part of JTStand.
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
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jfree.text;

/**
 * An object that can measure text.
 *
 * @author David Gilbert
 */
public interface TextMeasurer {

    /**
     * Calculates the width of a <code>String</code> in the current 
     * <code>Graphics</code> context.
     *
     * @param text  the text.
     * @param start  the start position of the substring to be measured.
     * @param end  the position of the last character to be measured.
     *
     * @return The width of the string in Java2D units.
     */
    public float getStringWidth(String text, int start, int end);
    
}
