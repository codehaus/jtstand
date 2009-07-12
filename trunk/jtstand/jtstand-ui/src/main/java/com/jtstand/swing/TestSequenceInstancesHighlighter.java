/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestSequenceInstancesHighlighter.java is part of JTStand.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand.swing;

import com.jtstand.swing.MainFrame.SequencesColumn;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstancesHighlighter implements Highlighter {

    private static Object bgc = UIManager.get("Panel.background");
//    public TestSequenceInstancesHighlighter() {
//         (Color) UIManager.get("Panel.background");
//    }

    @Override
    public Component highlight(
            Component c, ComponentAdapter ca) {
        if (JLabel.class.isAssignableFrom(c.getClass())) {
            ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
            if (ca.column == SequencesColumn.STATUS.ordinal()) {
                c.setForeground(MainFrame.getColor(((JLabel) c).getText()));
            } else if (ca.column == SequencesColumn.ROW.ordinal() && bgc != null && Color.class.isAssignableFrom(bgc.getClass())) {
                c.setForeground(Color.black);
                c.setBackground((Color) bgc);
            }
        }
        return c;
    }

    @Override
    public void addChangeListener(ChangeListener arg0) {
    }

    @Override
    public void removeChangeListener(ChangeListener arg0) {
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        return null;
    }
}
