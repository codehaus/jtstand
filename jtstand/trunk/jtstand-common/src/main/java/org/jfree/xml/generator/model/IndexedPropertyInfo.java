/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, IndexedPropertyInfo.java is part of JTStand.
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

package org.jfree.xml.generator.model;

/**
 * Indexed property info.
 */
public class IndexedPropertyInfo extends PropertyInfo {

    /** The key. */
    private KeyDescription key;

    /**
     * Creates a new instance.
     * 
     * @param name  the name.
     * @param type  the type.
     */
    public IndexedPropertyInfo(final String name, final Class type) {
        super(name, type);
    }

    /**
     * Returns the key.
     * 
     * @return the key.
     */
    public KeyDescription getKey() {
        return this.key;
    }

    /**
     * Sets the key.
     * 
     * @param key  the key.
     */
    public void setKey(final KeyDescription key) {
        this.key = key;
    }
}
