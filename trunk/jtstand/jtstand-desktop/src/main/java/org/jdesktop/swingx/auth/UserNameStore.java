/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, UserNameStore.java is part of JTStand.
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

import org.jdesktop.beans.AbstractBean;

/**
 * <b>UsernameStore</b> is a class that implements persistence of usernames
 *
 * @author Bino George
 * @author rbair
 */
public abstract class UserNameStore extends AbstractBean {
    /**
     * Gets the current list of users.
     */
    public abstract String[] getUserNames();
    /**
     */
    public abstract void setUserNames(String[] names);
    /**
     * lifecycle method for loading names from persistent storage
     */
    public abstract void loadUserNames();
    /**
     * lifecycle method for saving name to persistent storage
     */
    public abstract void saveUserNames();
    /**
     */
    public abstract boolean containsUserName(String name);
    
    /**
     * Add a username to the store.
     * @param userName
     */
    public abstract void addUserName(String userName);
    
    /**
     * Removes a username from the list.
     * @param userName
     */
    public abstract void removeUserName(String userName);
}
