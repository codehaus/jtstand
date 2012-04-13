/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ContributorsPanel.java is part of JTStand.
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
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * A panel containing a table that lists the contributors to a project.
 * <P>
 * Used in the AboutFrame class.
 *
 * @author David Gilbert
 */
public class ContributorsPanel extends JPanel {

    /** The table. */
    private JTable table;

    /** The data. */
    private TableModel model;

    /**
     * Creates a new contributors panel.
     *
     * @param contributors  a list of contributors (represented by Contributor objects).
     */
    public ContributorsPanel(final List contributors) {

        setLayout(new BorderLayout());
        this.model = new ContributorsTableModel(contributors);
        this.table = new JTable(this.model);
        add(new JScrollPane(this.table));

    }

}
