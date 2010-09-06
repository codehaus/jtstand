/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, PasswordStore.java is part of JTStand.
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
 *  PasswordStore specifies a mechanism to store passwords used to authenticate
 *  using the <strong>LoginService</strong>. The actual mechanism used
 *  to store the passwords is left up to the implementation.
 *
 * @author Bino George
 * @author Jonathan Giles
 */
public abstract class PasswordStore {
    /**
     *  Saves a password for future use. 
     *
     *  @param username username used to authenticate.
     *  @param server server used for authentication
     *  @param password password to save. Password can't be null. Use empty array for empty password.
     */
    public abstract boolean set(String username, String server, char[] password);
    
    /** 
     * Fetches the password for a given server and username.
     *  @param username username
     *  @param server server
     *  @return <code>null</code> if not found, a character array representing the password 
     *  otherwise. Returned array can be empty if the password is empty.
     */
    public abstract char[] get(String username, String server);

    /**
     * This should attempt to remove the given username from the password store, as well as any associated password.
     * @param username The username to remove
     */
    public abstract void removeUserPassword(String username);
}
