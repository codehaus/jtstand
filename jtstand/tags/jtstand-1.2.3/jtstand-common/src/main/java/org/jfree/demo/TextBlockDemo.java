/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TextBlockDemo.java is part of JTStand.
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

import java.awt.Font;
import javax.swing.JPanel;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo of the TextBlock class.
 *
 * @author David Gilbert
 */
public class TextBlockDemo extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public TextBlockDemo(final String title) {
        super(title);
        setContentPane(createContentPane());
    }


    /**
     * Creates the content pane for the demo frame.
     *
     * @return The content pane.
     */
    private JPanel createContentPane() {
        final JPanel content = new TextBlockPanel("This is some really long text that we will use "
            + "for testing purposes.  You'll need to resize the window to see how the TextBlock "
            + "is dynamically updated.  Also note what happens when there is a really long "
            + "word like ABCDEFGHIJKLMNOPQRSTUVWXYZ (OK, that's not really a word).", 
            new Font("Serif", Font.PLAIN, 14));
        return content;
    }

    /**
     * The starting point for the demo.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final TextBlockDemo demo = new TextBlockDemo("TextBlock Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
