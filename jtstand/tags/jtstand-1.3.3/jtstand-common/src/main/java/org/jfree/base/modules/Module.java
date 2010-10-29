/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Module.java is part of JTStand.
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
 * A module encapsulates optional functionality within a project. Modules can
 * be used as an easy way to make projects more configurable.
 * <p>
 * The module system provides a controled way to check dependencies and to initialize
 * the modules in a controlled way.
 *
 * @author Thomas Morgner 
 */
public interface Module extends ModuleInfo
{
  /**
   * Returns an array of all required modules. If one of these modules is missing
   * or cannot be initialized, the module itself will be not available.
   *
   * @return an array of the required modules.
   */
  public ModuleInfo[] getRequiredModules();

  /**
   * Returns an array of optional modules. Missing or invalid modules are non fatal
   * and will not harm the module itself.
   *
   * @return an array of optional module specifications.
   */
  public ModuleInfo[] getOptionalModules();

  /**
   * Initializes the module. Use this method to perform all initial setup operations.
   * This method is called only once in a modules lifetime. If the initializing cannot
   * be completed, throw a ModuleInitializeException to indicate the error,. The module
   * will not be available to the system.
   *
   * @param subSystem  the subSystem.
   * 
   * @throws ModuleInitializeException if an error ocurred while initializing the module.
   */
  public void initialize(SubSystem subSystem) throws ModuleInitializeException;

  /**
   * Configures the module. This should load the default settings of the module.
   *
   * @param subSystem  the subSystem.
   */
  public void configure(SubSystem subSystem);

  /**
   * Returns a short description of the modules functionality.
   *
   * @return a module description.
   */
  public String getDescription();

  /**
   * Returns the name of the module producer.
   *
   * @return the producer name
   */
  public String getProducer();

  /**
   * Returns the module name. This name should be a short descriptive handle of the
   * module.
   *
   * @return the module name
   */
  public String getName();

  /**
   * Returns the modules subsystem. If this module is not part of an subsystem
   * then return the modules name, but never null.
   *
   * @return the name of the subsystem.
   */
  public String getSubSystem ();


}
