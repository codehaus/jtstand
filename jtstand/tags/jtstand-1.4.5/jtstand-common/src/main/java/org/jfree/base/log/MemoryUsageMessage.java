/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MemoryUsageMessage.java is part of JTStand.
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

package org.jfree.base.log;

/**
 * A helper class to print memory usage message if needed.
 *
 * @author Thomas Morgner
 */
public class MemoryUsageMessage {

    /** The message. */
    private final String message;

    /**
     * Creates a new message.
     *
     * @param message  the message.
     */
    public MemoryUsageMessage(final String message) {
        this.message = message;
    }

    /**
     * Returns a string representation of the message (useful for debugging).
     *
     * @return the string.
     */
    public String toString() {
        return this.message + "Free: " + Runtime.getRuntime().freeMemory() + "; "
            + "Total: " + Runtime.getRuntime().totalMemory();
    }
    
}
