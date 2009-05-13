/*
 * Copyright 2009 Albert Kurucz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
