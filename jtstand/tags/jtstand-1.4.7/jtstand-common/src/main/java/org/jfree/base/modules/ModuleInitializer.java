/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ModuleInitializer.java is part of JTStand.
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
 * The module initializer is used to separate the initialization process from
 * the module definition. An invalid classpath setup or an missing base module
 * may throw an ClassCastException if the module class references this missing
 * resource. Separating them is the best way to make sure that the classloader
 * does not interrupt the module loading process.
 *
 * @author Thomas Morgner
 */
public interface ModuleInitializer {
    
    /**
     * Performs the initalization of the module.
     *
     * @throws ModuleInitializeException if an error occurs which prevents the module
     * from being usable.
     */
    public void performInit() throws ModuleInitializeException;
}
