/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, LoginAdapter.java is part of JTStand.
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

package org.jdesktop.swingx.auth;

/**
 *
 * @author rbair
 */
public abstract class LoginAdapter implements LoginListener {
    /**
     * @inheritDoc
     */
    public void loginSucceeded(LoginEvent source) {}

    /**
     * @inheritDoc
     */
    public void loginStarted(LoginEvent source) {}

    /**
     * @inheritDoc
     */
    public void loginFailed(LoginEvent source) {}

    /**
     * @inheritDoc
     */
    public void loginCanceled(LoginEvent source) {}
}
