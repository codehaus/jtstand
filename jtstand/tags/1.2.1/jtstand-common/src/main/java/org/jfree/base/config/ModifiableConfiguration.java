/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ModifiableConfiguration.java is part of JTStand.
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

package org.jfree.base.config;

import java.util.Enumeration;
import java.util.Iterator;

import org.jfree.util.Configuration;

/**
 * A modifiable configuration.
 *
 * @author Thomas Morgner
 */
public interface ModifiableConfiguration extends Configuration {
 
    /**
     * Sets the value of a configuration property.
     * 
     * @param key  the property key.
     * @param value  the property value.
     */
    public void setConfigProperty(final String key, final String value);
  
    /**
     * Returns the configuration properties.
     * 
     * @return The configuration properties.
     */
    public Enumeration getConfigProperties();
  
    /**
     * Returns an iterator for the keys beginning with the specified prefix.
     * 
     * @param prefix  the prefix.
     * 
     * @return The iterator.
     */
    public Iterator findPropertyKeys(final String prefix);

}
