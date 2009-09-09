/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JCommonResources.java is part of JTStand.
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

package org.jfree.resources;

import java.util.ListResourceBundle;

/**
 * Localised resources for the JCommon Class Library.
 */
public class JCommonResources extends ListResourceBundle {

    /**
     * Default constructor.
     */
    public JCommonResources() {
    }

    /**
     * Returns the array of strings in the resource bundle.
     *
     * @return The resources.
     */
    public Object[][] getContents() {
        return CONTENTS;
    }

    /** The resources to be localised. */
    private static final Object[][] CONTENTS = {

        {"project.name",      "JCommon"},
        {"project.version",   "1.0.15"},
        {"project.info",      "http://www.jfree.org/jcommon/"},
        {"project.copyright",
            "(C)opyright 2000-2008, by Object Refinery Limited and Contributors"}

    };

}
