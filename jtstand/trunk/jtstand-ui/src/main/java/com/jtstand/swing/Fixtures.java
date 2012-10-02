/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Fixtures.java is part of JTStand.
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
package com.jtstand.swing;

import com.jtstand.TestFixture;
import com.jtstand.TestSequenceInstance;
import com.jtstand.TestStation;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author albert_kurucz
 */
public class Fixtures extends JPanel {

    public static final long serialVersionUID = 20081114L;
    public static final String STR_FIXTURE_COLUMNS = "FIXTURE_COLUMNS";
    public static final String STR_FIXTURE_ROWS = "FIXTURE_ROWS";
    public static final String STR_FIXTURE_GAP = "FIXTURE_GAP";
    private Hashtable<String, Fixture> fxs = null;
    private int rows = 1;
    private int cols = 1;
    private int gap = 0;
    private Fixture firstFixture;

//    public Collection<Fixture> getFixtures() {
//        return fxs.values();
//    }
    public void init() {
        System.out.println("Fixtures init..." + fxs.size());
        for (Fixture fx : fxs.values()) {
            System.out.println(fx.getTestFixture().getFixtureName() + " init...");
            fx.init();
            System.out.println(fx.getTestFixture().getFixtureName() + " init completed.");
        }
    }

    public void replace(TestSequenceInstance seq) {
        for (Fixture fx : fxs.values()) {
            fx.replace(seq);
        }
    }

    public void remove(TestSequenceInstance seq) {
        for (Fixture fx : fxs.values()) {
            fx.remove(seq);
        }
    }

    public void removeAll(Collection<?> seqList) {
        for (Fixture fx : fxs.values()) {
            fx.removeAll(seqList);
        }
    }

    public Fixtures(TestStation testStation, MainFrame fi) {
        cols = testStation.getPropertyInteger(STR_FIXTURE_COLUMNS, 1);
        rows = testStation.getPropertyInteger(STR_FIXTURE_ROWS, 1);
        gap = testStation.getPropertyInteger(STR_FIXTURE_GAP, 1);

        System.out.println("Number of fixtures: " + testStation.getFixtures().size());
        if (testStation.getFixtures().size() > 0) {
            setLayout(new GridLayout(0, 1, 0, gap));
            fxs = new Hashtable<String, Fixture>();
            Iterator<TestFixture> it = testStation.getFixtures().iterator();
            for (int row = 0; row < rows; row++) {
                if (it.hasNext()) {
                    JPanel panel = new JPanel();
                    panel.setLayout(new GridLayout(1, 0, gap, 0));
                    for (int col = 0; col < cols; col++) {
                        System.out.println("col: " + col + " of " + cols);
                        if (it.hasNext()) {
                            TestFixture fxt = it.next();
                            Fixture fx = new Fixture(fxt, fi);
                            if (firstFixture == null) {
                                firstFixture = fx;
                            }
                            fxs.put(fx.getName(), fx);
                            panel.add(fx);
                        } else {
                            System.out.println("No more fixtures!");
                        }
                    }
                    add(panel);
                }
            }
        } else {
            /* fixtureless configuration */
            //...
        }
    }
}
