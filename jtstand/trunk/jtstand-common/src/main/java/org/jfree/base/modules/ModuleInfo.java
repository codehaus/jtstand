/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ModuleInfo.java is part of JTStand.
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

/**
 * The Module info class encapsulates metadata about a given module. It holds the
 * list of dependencies and the module version and description.
 *
 * @author Thomas Morgner
 */
public interface ModuleInfo {

    /**
     * Returns the module class of the desired base module.
     *
     * @return The module class.
     */
    public String getModuleClass();

    /**
     * Returns the major version of the base module. The string should
     * contain a compareable character sequence so that higher versions
     * of the module are considered greater than lower versions.
     *
     * @return The major version of the module.
     */
    public String getMajorVersion();

    /**
     * Returns the minor version of the base module. The string should
     * contain a compareable character sequence so that higher versions
     * of the module are considered greater than lower versions.
     *
     * @return The minor version of the module.
     */
    public String getMinorVersion();

    /**
     * Returns the patchlevel version of the base module. The patch level
     * should be used to mark bugfixes. The string should
     * contain a compareable character sequence so that higher versions
     * of the module are considered greater than lower versions.
     *
     * @return The patch level version of the module.
     */
    public String getPatchLevel();
  
}
