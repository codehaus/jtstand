/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DateUtilities.java is part of JTStand.
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

package org.jfree.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Some useful date methods.
 *
 * @author David Gilbert.
 */
public class DateUtilities {

    /**
     * Private constructor to prevent object creation.
     */
    private DateUtilities() {
    }

    /** A working calendar. */
    private static final Calendar CALENDAR = Calendar.getInstance();

    /**
     * Creates a date.
     *
     * @param yyyy  the year.
     * @param month  the month (1 - 12).
     * @param day  the day.
     *
     * @return a date.
     */
    public static synchronized Date createDate(final int yyyy, final int month, final int day) {
        CALENDAR.clear();
        CALENDAR.set(yyyy, month - 1, day);
        return CALENDAR.getTime();
    }

    /**
     * Creates a date.
     *
     * @param yyyy  the year.
     * @param month  the month (1 - 12).
     * @param day  the day.
     * @param hour  the hour.
     * @param min  the minute.
     *
     * @return a date.
     */
    public static synchronized Date createDate(final int yyyy, final int month, final int day, final int hour, final int min) {

        CALENDAR.clear();
        CALENDAR.set(yyyy, month - 1, day, hour, min);
        return CALENDAR.getTime();

    }


}
