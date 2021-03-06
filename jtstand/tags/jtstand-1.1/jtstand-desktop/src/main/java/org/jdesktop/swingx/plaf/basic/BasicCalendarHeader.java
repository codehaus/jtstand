/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, BasicCalendarHeader.java is part of JTStand.
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

/*
 * Created on 30.10.2008
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;

/**
 * Active header for a JXMonthView in zoomable mode.<p>
 * 
 *  PENDING JW: very much work-in-progress.
 *  PENDING JW: CO problem ... exchange arrows on RToL
 *  PENDING JW: when used in picker dropdown not CO not updated
 *      
 * @author Jeanette Winzenburg
 * 
 * @deprecated moved into BasicCalendarHeaderHandler
 */
@Deprecated
class BasicCalendarHeader extends JXPanel {

    protected AbstractButton prevButton;
    protected AbstractButton nextButton;
    protected JXHyperlink zoomOutLink;

    public BasicCalendarHeader() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        prevButton = createNavigationButton();
        nextButton = createNavigationButton();
        zoomOutLink = createZoomLink();
        add(prevButton);
        add(Box.createHorizontalGlue());
        add(zoomOutLink);
        add(Box.createHorizontalGlue());
        add(nextButton);
        setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    }

    /**
     * Sets the actions for backward, forward and zoom out navigation.
     * 
     * @param prev
     * @param next
     * @param zoomOut
     */
    public void setActions(Action prev, Action next, Action zoomOut) {
        prevButton.setAction(prev);
        nextButton.setAction(next);
        zoomOutLink.setAction(zoomOut);
    }
    
    
    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the font of the zoom hyperlink.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (zoomOutLink != null)
            zoomOutLink.setFont(font);
    }

    private JXHyperlink createZoomLink() {
        JXHyperlink zoomOutLink = new JXHyperlink();
        Color textColor = new Color(16, 66, 104);
        zoomOutLink.setUnclickedColor(textColor);
        zoomOutLink.setClickedColor(textColor);
        zoomOutLink.setFocusable(false);
        return zoomOutLink;
    }

    private AbstractButton createNavigationButton() {
        JXHyperlink b = new JXHyperlink();
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setRolloverEnabled(true);
        b.setFocusable(false);
        return b;
    }
}
