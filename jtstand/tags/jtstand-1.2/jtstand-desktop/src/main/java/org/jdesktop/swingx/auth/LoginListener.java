/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, LoginListener.java is part of JTStand.
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

import java.util.EventListener;

/*
 * <b>LoginListener</b> provides a listener for the actual login
 * process.
 *
 * @author Bino George
 * @author Shai Almog
 */
public interface LoginListener extends EventListener {
    
    /**
     *  Called by the <strong>JXLoginPane</strong> in the event of a login failure
     *
     * @param source panel that fired the event
     */
    public void loginFailed(LoginEvent source);
    /**
     *  Called by the <strong>JXLoginPane</strong> when the Authentication
     *  operation is started.
     * @param source panel that fired the event
     */
    public void loginStarted(LoginEvent source);
    /**
     *  Called by the <strong>JXLoginPane</strong> in the event of a login
     *  cancellation by the user.
     *
     * @param source panel that fired the event
     */
    public void loginCanceled(LoginEvent source);
    /**
     *  Called by the <strong>JXLoginPane</strong> in the event of a
     *  successful login.
     *
     * @param source panel that fired the event
     */
    public void loginSucceeded(LoginEvent source);
}
