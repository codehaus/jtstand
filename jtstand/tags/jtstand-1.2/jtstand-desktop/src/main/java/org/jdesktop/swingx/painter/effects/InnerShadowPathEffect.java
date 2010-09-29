/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, InnerShadowPathEffect.java is part of JTStand.
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


package org.jdesktop.swingx.painter.effects;

import java.awt.Color;
import java.awt.Point;

/**
 * An effect which draws a shadow inside the path painter.
 * @author joshy
 */
public class InnerShadowPathEffect extends AbstractAreaEffect {
    
    /** Creates a new instance of InnerShadowPathEffect */
    public InnerShadowPathEffect() {
        super();
        setRenderInsideShape(true);
        setBrushColor(Color.BLACK);
        setOffset(new Point(2,2));
    }
    
}
