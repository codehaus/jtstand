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

import com.jtstand.TestFixture;
import com.jtstand.TestSequenceInstance;
import com.jtstand.TestStation;
import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

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
