/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Contributor.java is part of JTStand.
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

package org.jfree.ui.about;

/**
 * A simple class representing a contributor to a software project.
 * <P>
 * Used in the AboutFrame class.
 *
 * @author David Gilbert
 */
public class Contributor {

    /** The name. */
    private String name;

    /** The e-mail address. */
    private String email;

    /**
     * Creates a new contributor.
     *
     * @param name  the name.
     * @param email  the e-mail address.
     */
    public Contributor(final String name, final String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the contributor's name.
     *
     * @return the contributor's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the contributor's e-mail address.
     *
     * @return the contributor's e-mail address.
     */
    public String getEmail() {
        return this.email;
    }

}
