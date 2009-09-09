/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Library.java is part of JTStand.
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
 * Library specification moved to base package to allow more control
 * over the boot process.
 *
 * @author David Gilbert
 * @deprecated shadow class for deprecation
 */
public class Library extends org.jfree.base.Library {

    /**
     * Creates a new library reference.
     *
     * @param name    the name.
     * @param version the version.
     * @param licence the licence.
     * @param info    the web address or other info.
     */
    public Library(final String name, final String version, final String licence, final String info) {
        super(name, version, licence, info);
    }

    /**
     * Constructs a library reference from a ProjectInfo object.
     *
     * @param project  information about a project.
     */
    public Library(final ProjectInfo project) {

        this(project.getName(), project.getVersion(),
             project.getLicenceName(), project.getInfo());
    }
}
