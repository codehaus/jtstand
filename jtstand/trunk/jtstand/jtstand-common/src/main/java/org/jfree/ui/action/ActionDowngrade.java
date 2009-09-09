/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ActionDowngrade.java is part of JTStand.
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

package org.jfree.ui.action;

import javax.swing.Action;

/**
 * Defines the 2 new constants introduced by Sun in version 1.3 of the J2SDK.
 *
 * @author Thomas Morgner
 */
public interface ActionDowngrade extends Action {

    /**
     * The key used for storing a <code>KeyStroke</code> to be used as the
     * accelerator for the action.
     */
    public static final String ACCELERATOR_KEY = "AcceleratorKey";

    /**
     * The key used for storing an int key code to be used as the mnemonic
     * for the action.
     */
    public static final String MNEMONIC_KEY = "MnemonicKey";

}
