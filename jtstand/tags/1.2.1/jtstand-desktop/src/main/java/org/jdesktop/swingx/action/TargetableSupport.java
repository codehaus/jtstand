/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TargetableSupport.java is part of JTStand.
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
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;

/**
 *
 * @author rbair
 */
public class TargetableSupport {
    private JComponent component;
    
    /** Creates a new instance of TargetableSupport */
    public TargetableSupport(JComponent component) {
        this.component = component;
    }
    
    public boolean doCommand(Object command, Object value) {
        // Look at the internal component first.
        ActionMap map = component.getActionMap();
        Action action = map.get(command);

        if (action != null) {
            if (value instanceof ActionEvent) {
                action.actionPerformed( (ActionEvent) value);
            }
            else {
                // XXX should the value represent the event source?
                action.actionPerformed(new ActionEvent(value, 0,
                    command.toString()));
            }
            return true;
        }
        return false;
    }

    public Object[] getCommands() {
        ActionMap map = component.getActionMap();
        return map.allKeys();
    }

    public boolean hasCommand(Object command) {
        Object[] commands = getCommands();
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].equals(command)) {
                return true;
            }
        }
        return false;
    }
}
