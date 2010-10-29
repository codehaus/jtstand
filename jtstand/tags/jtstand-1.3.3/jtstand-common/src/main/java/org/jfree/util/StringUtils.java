/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StringUtils.java is part of JTStand.
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

package org.jfree.util;

/**
 * String utilities.
 * 
 * @author Thomas Morgner.
 */
public class StringUtils {

    /**
     * Private constructor prevents object creation. 
     */
    private StringUtils() {
    }

    /**
     * Helper functions to query a strings start portion. The comparison is case insensitive.
     *
     * @param base  the base string.
     * @param start  the starting text.
     *
     * @return true, if the string starts with the given starting text.
     */
    public static boolean startsWithIgnoreCase(final String base, final String start) {
        if (base.length() < start.length()) {
            return false;
        }
        return base.regionMatches(true, 0, start, 0, start.length());
    }

    /**
     * Helper functions to query a strings end portion. The comparison is case insensitive.
     *
     * @param base  the base string.
     * @param end  the ending text.
     *
     * @return true, if the string ends with the given ending text.
     */
    public static boolean endsWithIgnoreCase(final String base, final String end) {
        if (base.length() < end.length()) {
            return false;
        }
        return base.regionMatches(true, base.length() - end.length(), end, 0, end.length());
    }

    /**
     * Queries the system properties for the line separator. If access
     * to the System properties is forbidden, the UNIX default is returned.
     *
     * @return the line separator.
     */
    public static String getLineSeparator() {
        try {
            return System.getProperty("line.separator", "\n");
        }
        catch (Exception e) {
            return "\n";
        }
    }


}
