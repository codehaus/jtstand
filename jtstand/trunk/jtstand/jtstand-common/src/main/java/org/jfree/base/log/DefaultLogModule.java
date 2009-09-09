/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DefaultLogModule.java is part of JTStand.
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

package org.jfree.base.log;

import org.jfree.base.modules.AbstractModule;
import org.jfree.base.modules.ModuleInitializeException;
import org.jfree.base.modules.SubSystem;
import org.jfree.util.Log;
import org.jfree.util.PrintStreamLogTarget;

/**
 * The module definition for the System.out-Logging. This is the default log
 * implementation and is provided to insert the logging initialisation in the
 * module loading process.
 *
 * @author Thomas Morgner
 */
public class DefaultLogModule extends AbstractModule
{
  /**
   * DefaultConstructor. Loads the module specification.
   *
   * @throws ModuleInitializeException if an error occured.
   */
  public DefaultLogModule() throws ModuleInitializeException
  {
    loadModuleInfo();
  }

  /**
   * Initalizes the module. This method initializes the logging system, if the
   * System.out logtarget is selected.
   *
   * @param subSystem the sub-system.
   * @throws ModuleInitializeException if an error occured.
   */
  public void initialize(final SubSystem subSystem)
          throws ModuleInitializeException
  {
    if (LogConfiguration.isDisableLogging())
    {
      return;
    }

    if (LogConfiguration.getLogTarget().equals
            (PrintStreamLogTarget.class.getName()))
    {
      DefaultLog.installDefaultLog();
      Log.getInstance().addTarget(new PrintStreamLogTarget());

      if ("true".equals(subSystem.getGlobalConfig().getConfigProperty
              ("org.jfree.base.LogAutoInit")))
      {
        Log.getInstance().init();
      }
      Log.info("Default log target started ... previous log messages " +
              "could have been ignored.");
    }
  }
}
