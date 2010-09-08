/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, AbstractMultiThumbModel.java is part of JTStand.
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

package org.jdesktop.swingx.multislider;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jm158417
 */
public abstract class AbstractMultiThumbModel<E> implements MultiThumbModel<E> {
    /** Creates a new instance of AbstractMultiThumbModel */
    public AbstractMultiThumbModel() {
    }
    
    protected float maximumValue = 1.0f;
    protected float minimumValue = 0.0f;
    
    public float getMaximumValue()    {
        return maximumValue;
    }
    
    public float getMinimumValue()    {
        return minimumValue;
    }
    
    public void setMaximumValue(float maximumValue) {
        this.maximumValue = maximumValue;
    }
    
    public void setMinimumValue(float minimumValue) {
        this.minimumValue = minimumValue;
    }
    
    protected List<ThumbDataListener> thumbDataListeners = new ArrayList<ThumbDataListener>();
    
    public void addThumbDataListener(ThumbDataListener listener) {
        thumbDataListeners.add(listener);
    }
    
    public void removeThumbDataListener(ThumbDataListener listener) {
        thumbDataListeners.remove(listener);
    }
    
    
    
    public void thumbPositionChanged(Thumb<E> thumb) {
        fireThumbPositionChanged(thumb);
    }
    
    protected void fireThumbPositionChanged(Thumb<E> thumb) {
        if(getThumbIndex(thumb) >= 0) {
            ThumbDataEvent evt = new ThumbDataEvent(this,-1,getThumbIndex(thumb),thumb);
            for(ThumbDataListener l : thumbDataListeners) {
                l.positionChanged(evt);
            }
        }
    }
    public void thumbValueChanged(Thumb<E> thumb) {
        fireThumbValueChanged(thumb);
    }
    
    protected void fireThumbValueChanged(Thumb<E> thumb) {
        ThumbDataEvent evt = new ThumbDataEvent(this,-1,getThumbIndex(thumb),thumb);
        for(ThumbDataListener l : thumbDataListeners) {
            l.valueChanged(evt);
        }
    }
    
}

