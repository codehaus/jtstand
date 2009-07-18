/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, ErrorLevel.java is part of JTStand.
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

package org.jdesktop.swingx.error;

import java.util.logging.Level;

/**
 * <p>Extends {@link java.util.logging.Level} adding the <code>FATAL</code> error level.
 * Fatal errors are those unrecoverable errors that must result in the termination
 * of the application.</p>
 *
 * @status REVIEWED
 * @author rbair
 */
public class ErrorLevel extends Level {
    /**
     * FATAL is a message level indicating a catastrophic failure that should
     * result in the immediate termination of the application.
     * <p>
     * In general FATAL messages should describe events that are
     * of considerable critical and which will prevent
     * program execution.   They should be reasonably intelligible
     * to end users and to system administrators.
     * This level is initialized to <CODE>1100</CODE>.
     */    
    public static final ErrorLevel FATAL = new ErrorLevel("FATAL", 1100);
    
    /** Creates a new instance of ErrorLevel */
    protected ErrorLevel(String name, int value) {
        super(name, value);
    }
}
