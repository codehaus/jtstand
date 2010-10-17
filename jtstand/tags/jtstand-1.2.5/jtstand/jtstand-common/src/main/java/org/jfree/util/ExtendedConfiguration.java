/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ExtendedConfiguration.java is part of JTStand.
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
package org.jfree.util;

/**
 * The extended configuration provides methods to make using the
 * configuration easier.
 *
 * @author Thomas Morgner
 */
public interface ExtendedConfiguration extends Configuration
{
  /**
   * Checks, whether a given property is defined.
   *
   * @param name the name of the property
   * @return true, if the property is defined, false otherwise.
   */
  public boolean isPropertySet (String name);

  /**
   * Returns a given property as int value. Zero is returned if the
   * property value is no number or the property is not set.
   *
   * @param name the name of the property
   * @return the parsed number value or zero
   */
  public int getIntProperty (String name);

  /**
   * Returns a given property as int value. The specified default value is returned if the
   * property value is no number or the property is not set.
   *
   * @param name the name of the property
   * @param defaultValue the value to be returned if the property is no integer value
   * @return the parsed number value or the specified default value
   */
  public int getIntProperty (String name, int defaultValue);

  /**
   * Returns the boolean value of a given configuration property. The boolean value true
   * is returned, if the contained string is equal to 'true'.
   *
   * @param name the name of the property
   * @return the boolean value of the property.
   */
  public boolean getBoolProperty (String name);

  /**
   * Returns the boolean value of a given configuration property. The boolean value true
   * is returned, if the contained string is equal to 'true'. If the property is not set,
   * the default value is returned.
   *
   * @param name the name of the property
   * @param defaultValue the default value to be returned if the property is not set
   * @return the boolean value of the property.
   */
  public boolean getBoolProperty (String name, boolean defaultValue);
}
