/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DateChooserPanelDemo.java is part of JTStand.
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


package org.jfree.demo;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.DateChooserPanel;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demo showing the {@link DateChooserPanel}.
 */
public class DateChooserPanelDemo extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public DateChooserPanelDemo(String title) {
        super(title);
        setContentPane(new DateChooserPanel());
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        DateChooserPanelDemo demo = new DateChooserPanelDemo(
            "DateChooserPanel Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
