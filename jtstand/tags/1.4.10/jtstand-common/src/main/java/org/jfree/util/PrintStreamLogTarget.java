/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PrintStreamLogTarget.java is part of JTStand.
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

import java.io.PrintStream;
import java.io.Serializable;

/**
 * A log target that sends output to a {@link PrintStream}.
 *
 * @author Thomas Morgner
 */
public class PrintStreamLogTarget implements LogTarget, Serializable {

  /** For serialization. */
  private static final long serialVersionUID = 6510564403264504688L;

  /** The printstream we use .. */
  private PrintStream printStream;

  /**
   * The default constructor. Initializes this target with the system.out 
   * stream.
   * <p>
   * All {@link org.jfree.util.LogTarget} implementations need a default 
   * constructor.
   */
  public PrintStreamLogTarget() {
    this (System.out);
  }

  /**
   * The default constructor. Initializes this target with the given stream.
   * <p>
   * @param printStream the print stream that is used to write the content.
   */
  public PrintStreamLogTarget(final PrintStream printStream) {
    if (printStream == null) {
      throw new NullPointerException();
    }
    this.printStream = printStream;
  }

  /**
   * Logs a message to the main log stream. All attached logStreams will also
   * receive this message. If the given log-level is higher than the given 
   * debug-level in the main config file, no logging will be done.
   *
   * @param level log level of the message.
   * @param message text to be logged.
   */
  public void log(int level, final Object message) {
    if (level > 3) {
      level = 3;
    }
    this.printStream.print(LEVELS[level]);
    this.printStream.println(message);
    if (level < 3) {
      System.out.flush();
    }
  }

  /**
   * logs an message to the main-log stream. All attached logStreams will also
   * receive this message. If the given log-level is higher than the given 
   * debug-level in the main config file, no logging will be done.
   *
   * The exception's stacktrace will be appended to the log-stream
   *
   * @param level log level of the message.
   * @param message text to be logged.
   * @param e the exception, which should be logged.
   */
  public void log(int level, final Object message, final Exception e) {
    if (level > 3) {
      level = 3;
    }
    this.printStream.print(LEVELS[level]);
    this.printStream.println(message);
    e.printStackTrace(this.printStream);
    if (level < 3) {
      System.out.flush();
    }
  }
}
