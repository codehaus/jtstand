/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, PainterGlasspane.java is part of JTStand.
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


package org.jdesktop.swingx.painter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a glasspane which will draw the specified painter on
 * top of the specified target components. The PainterGlasspane would
 * commonly be used for drawing a translucent overlay or icon badge on top
 * of components which are invalid, indicating to the user what the problem
 * is. 
 * 
 * The PainterGlasspane can also be used to apply a Painter on top of a component
 * which does not already support painters on it's own.
 * 
 * @author joshy
 */
public class PainterGlasspane extends JComponent {
    private Painter painter;
    private List<JComponent>targets;
    
    /** Creates a new instance of ValidationOverlay */
    public PainterGlasspane() {
        targets = new ArrayList<JComponent>();
    }
    
    public void addTarget(JComponent comp) {
        targets.add(comp);
        repaint();
    }
    public void removeTarget(JComponent comp) {
        targets.remove(comp);
        repaint();
    }
    
    protected void paintComponent(Graphics gfx) {
        Graphics2D g = (Graphics2D)gfx;
        if(getPainter() != null) {
            for(JComponent target : targets) {
                Point offset = calcOffset(target);
                g.translate(offset.x,offset.y);
                getPainter().paint(g, target, target.getWidth(), target.getHeight());
                g.translate(-offset.x,-offset.y);
            }
        }
    }

    private Point calcOffset(JComponent target) {
        if(target == null) {
            return new Point(0,0);
        }
        // if the parent is the top then we must be the rootpane?
        if(target.getParent() == SwingUtilities.getWindowAncestor(target)) {
            return new Point(0,0);
        }
        
        Point parent = calcOffset((JComponent)target.getParent());
        Point self = target.getLocation();
        return new Point(parent.x + self.x, parent.y + self.y);
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter painter) {
        Painter old = getPainter();
        this.painter = painter;
        firePropertyChange("painter", old, getPainter());
        repaint();
    }
}
