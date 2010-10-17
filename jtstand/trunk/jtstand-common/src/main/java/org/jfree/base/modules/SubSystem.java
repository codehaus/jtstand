/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SubSystem.java is part of JTStand.
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

package org.jfree.base.modules;

import org.jfree.util.Configuration;
import org.jfree.util.ExtendedConfiguration;

/**
 * A sub-system holds a separate collection of modules.
 * <p>
 * On a simple level, subsystems can be just libraries.
 * Libraries offering services need a controlled way to
 * initialize these services before dependent code starts
 * using the library. This can be achived by embedding the
 * library services into an own subsystem.
 *
 * @author Thomas Morgner
 */
public interface SubSystem {

    /**
     * Returns the global configuration.
     * 
     * @return The global configuration.
     */
    public Configuration getGlobalConfig();

    /**
     * Returns the global configuration as ExtendedConfiguration instance.
     *
     * @return the extended configuration.
     */
    public ExtendedConfiguration getExtendedConfig ();

    /**
     * Returns the package manager.
     * 
     * @return The package manager.
     */
    public PackageManager getPackageManager();

}
