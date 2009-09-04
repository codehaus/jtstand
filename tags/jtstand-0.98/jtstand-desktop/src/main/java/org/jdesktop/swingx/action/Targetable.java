/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, Targetable.java is part of JTStand.
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

package org.jdesktop.swingx.action;


/**
 * An interface which exposes the allowable actions to a TargetManager. 
 * The getCommands method will expose the allowable actions to another class
 * and the doCommand method is called to invoke an action on the class.
 * <p>
 * Usually, the command key will be the key value of the Action. For components
 * This could be the ActionMap keys. For actions managed with the ActionManager,
 * this will be the value of an actions Action.ACTION_COMMAND_KEY
 * 
 * @see TargetManager
 * @author Mark Davidson
 */
public interface Targetable {

    /**
     * Perform the command using the object value.
     *
     * @param command is a Action.ACTION_COMMAND_KEY
     * @param value an arbitrary value. Usually this will be
     *              EventObject which trigered the command.
     */
    boolean doCommand(Object command, Object value);

    /**
     * Return a flag that indicates if a command is supported.
     * 
     * @param command is a Action.ACTION_COMMAND_KEY
     * @return true if command is supported; false otherwise
     */
    boolean hasCommand(Object command);

    /**
     * Returns an array of supported commands. If this Targetable 
     * doesn't support any commands (which is unlikely) then an 
     * empty array is returned.
     *
     * @return array of supported commands
     */
    Object[] getCommands();
}
