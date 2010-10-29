/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JCommon.java is part of JTStand.
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

package org.jfree;

import org.jfree.ui.about.ProjectInfo;

/**
 * This class contains static information about the JCommon class library.
 *
 * @author David Gilbert
 */
public final class JCommon {

    /** Information about the project. */
    public static final ProjectInfo INFO = JCommonInfo.getInstance();
    
    /**
     * Hidden constructor.
     */
    private JCommon() {
        super();
    }
    
    /**
     * Prints information about JCommon to standard output.
     *
     * @param args  no arguments are honored.
     */
    public static void main(final String[] args) {
        System.out.println(JCommon.INFO.toString());
    }

}
