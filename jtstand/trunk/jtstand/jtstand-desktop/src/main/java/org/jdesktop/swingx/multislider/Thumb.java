/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, Thumb.java is part of JTStand.
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

/**
 *
 * @author jm158417
 */
public class Thumb<E> {
    private float position;
    private E object;
    private MultiThumbModel<E> model;

    /** Creates a new instance of Thumb */
    public Thumb(MultiThumbModel<E> model) {
        this.model = model;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
        model.thumbPositionChanged(this);
    }

    public E getObject() {
        return object;
    }

    public void setObject(E object) {
        this.object = object;
        model.thumbValueChanged(this);
    }
}