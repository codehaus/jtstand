/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AboutPanel.java is part of JTStand.
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

package org.jfree.ui.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jfree.ui.RefineryUtilities;

/**
 * A standard panel for displaying information about an application.
 *
 * @author David Gilbert
 */
public class AboutPanel extends JPanel {

    /**
     * Constructs a panel.
     *
     * @param application  the application name.
     * @param version  the version.
     * @param copyright  the copyright statement.
     * @param info  other info.
     */
    public AboutPanel(final String application,
                      final String version,
                      final String copyright,
                      final String info) {

        this(application, version, copyright, info, null);

    }

    /**
     * Constructs a panel.
     *
     * @param application  the application name.
     * @param version  the version.
     * @param copyright  the copyright statement.
     * @param info  other info.
     * @param logo  an optional logo.
     */
    public AboutPanel(final String application,
                      final String version,
                      final String copyright,
                      final String info,
                      final Image logo) {

        setLayout(new BorderLayout());

        final JPanel textPanel = new JPanel(new GridLayout(4, 1, 0, 4));

        final JPanel appPanel = new JPanel();
        final Font f1 = new Font("Dialog", Font.BOLD, 14);
        final JLabel appLabel = RefineryUtilities.createJLabel(application, f1, Color.black);
        appLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        appPanel.add(appLabel);

        final JPanel verPanel = new JPanel();
        final Font f2 = new Font("Dialog", Font.PLAIN, 12);
        final JLabel verLabel = RefineryUtilities.createJLabel(version, f2, Color.black);
        verLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        verPanel.add(verLabel);

        final JPanel copyrightPanel = new JPanel();
        final JLabel copyrightLabel = RefineryUtilities.createJLabel(copyright, f2, Color.black);
        copyrightLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        copyrightPanel.add(copyrightLabel);

        final JPanel infoPanel = new JPanel();
        final JLabel infoLabel = RefineryUtilities.createJLabel(info, f2, Color.black);
        infoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        infoPanel.add(infoLabel);

        textPanel.add(appPanel);
        textPanel.add(verPanel);
        textPanel.add(copyrightPanel);
        textPanel.add(infoPanel);

        add(textPanel);

        if (logo != null) {
            final JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.add(new javax.swing.JLabel(new javax.swing.ImageIcon(logo)));
            imagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
            final JPanel imageContainer = new JPanel(new BorderLayout());
            imageContainer.add(imagePanel, BorderLayout.NORTH);
            add(imageContainer, BorderLayout.WEST);
        }

    }

}
