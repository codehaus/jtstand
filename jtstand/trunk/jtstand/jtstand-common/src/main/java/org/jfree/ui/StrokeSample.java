/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StrokeSample.java is part of JTStand.
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

package org.jfree.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A panel that displays a stroke sample.
 *
 * @author David Gilbert
 */
public class StrokeSample extends JComponent implements ListCellRenderer {

    /** The stroke being displayed (may be null). */
    private Stroke stroke;

    /** The preferred size of the component. */
    private Dimension preferredSize;

    /**
     * Creates a StrokeSample for the specified stroke.
     *
     * @param stroke  the sample stroke (<code>null</code> permitted).
     */
    public StrokeSample(final Stroke stroke) {
        this.stroke = stroke;
        this.preferredSize = new Dimension(80, 18);
        setPreferredSize(this.preferredSize);
    }

    /**
     * Returns the current Stroke object being displayed.
     *
     * @return The stroke (possibly <code>null</code>).
     */
    public Stroke getStroke() {
        return this.stroke;
    }

    /**
     * Sets the stroke object being displayed and repaints the component.
     *
     * @param stroke  the stroke (<code>null</code> permitted).
     */
    public void setStroke(final Stroke stroke) {
        this.stroke = stroke;
        repaint();
    }

    /**
     * Returns the preferred size of the component.
     *
     * @return the preferred size of the component.
     */
    public Dimension getPreferredSize() {
        return this.preferredSize;
    }

    /**
     * Draws a line using the sample stroke.
     *
     * @param g  the graphics device.
     */
    public void paintComponent(final Graphics g) {

        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final double xx = insets.left;
        final double yy = insets.top;
        final double ww = size.getWidth() - insets.left - insets.right;
        final double hh = size.getHeight() - insets.top - insets.bottom;

        // calculate point one
        final Point2D one =  new Point2D.Double(xx + 6, yy + hh / 2);
        // calculate point two
        final Point2D two =  new Point2D.Double(xx + ww - 6, yy + hh / 2);
        // draw a circle at point one
        final Ellipse2D circle1 = new Ellipse2D.Double(one.getX() - 5,
                one.getY() - 5, 10, 10);
        final Ellipse2D circle2 = new Ellipse2D.Double(two.getX() - 6,
                two.getY() - 5, 10, 10);

        // draw a circle at point two
        g2.draw(circle1);
        g2.fill(circle1);
        g2.draw(circle2);
        g2.fill(circle2);

        // draw a line connecting the points
        final Line2D line = new Line2D.Double(one, two);
        if (this.stroke != null) {
            g2.setStroke(this.stroke);
            g2.draw(line);
        }

    }

    /**
     * Returns a list cell renderer for the stroke, so the sample can be
     * displayed in a list or combo.
     *
     * @param list  the list.
     * @param value  the value.
     * @param index  the index.
     * @param isSelected  selected?
     * @param cellHasFocus  focussed?
     *
     * @return the component for rendering.
     */
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Stroke) {
            setStroke((Stroke) value);
        }
        else {
            setStroke(null);
        }
        return this;
    }

}
