/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, DefaultSplitPaneModel.java is part of JTStand.
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
package org.jdesktop.swingx.multisplitpane;

import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Split;
/**
 * A simplified SplitPaneLayout for common split pane needs. A common multi splitpane
 * need is:
 *
 * +-----------+-----------+
 * |           |           |
 * |           +-----------+
 * |           |           |
 * +-----------+-----------+
 *
 * @author rbair
 */
public class DefaultSplitPaneModel extends Split {
    public static final String LEFT = "left";
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    
    /** Creates a new instance of DefaultSplitPaneLayout */
    public DefaultSplitPaneModel() {
        Split row = new Split();
        Split col = new Split();
        col.setRowLayout(false);
        setChildren(new Leaf(LEFT), new Divider(), col);
        col.setChildren(new Leaf(TOP), new Divider(), new Leaf(BOTTOM));
    }
    
}
