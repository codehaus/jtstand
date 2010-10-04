/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, MappedValue.java is part of JTStand.
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
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jdesktop.swingx.renderer;

import javax.swing.Icon;

/**
 * Compound implementation of XXValue. Currently, XX stands for String, 
 * Icon, Boolean. <p>
 * 
 * Quick hack around #590-swingx: LabelProvider should respect StringValue
 * when formatting (instead of going clever with icons).
 * 
 * Note: this will change!
 * 
 * @see CheckBoxProvider
 */
public class MappedValue implements StringValue, IconValue, BooleanValue {

    private StringValue stringDelegate;
    private IconValue iconDelegate;
    private BooleanValue booleanDelegate;

    public MappedValue(StringValue stringDelegate, IconValue iconDelegate) {
        this(stringDelegate, iconDelegate, null);
    }
    
    public MappedValue(StringValue stringDelegate, IconValue iconDelegate, 
            BooleanValue booleanDelegate) {
        this.stringDelegate = stringDelegate;
        this.iconDelegate = iconDelegate;
        this.booleanDelegate = booleanDelegate;
    }
    
    /**
     * {@inheritDoc}<p>
     * 
     * This implementation delegates to the contained StringValue if available or
     * returns an empty String, if not.
     *  
     */
    public String getString(Object value) {
        if (stringDelegate != null) {
            return stringDelegate.getString(value);
        }
        return "";
    }

    /**
     * {@inheritDoc}<p>
     * 
     * This implementation delegates to the contained IconValue if available or
     * returns null, if not.
     *  
     */
    public Icon getIcon(Object value) {
        if (iconDelegate != null) {
            return iconDelegate.getIcon(value);
        }
        return null;
    }
    
    /**
     * {@inheritDoc}<p>
     * 
     * This implementation delegates to the contained BooleanValue if available or
     * returns false, if not.
     *  
     */
    public boolean getBoolean(Object value) {
        if (booleanDelegate != null) {
            return booleanDelegate.getBoolean(value);
        }
        return false;
    }
    
}