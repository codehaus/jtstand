/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StarterProperties.java is part of JTStand.
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

import com.jtstand.AbstractProperties;
//import com.jtstand.PropertiesInterface;
import com.jtstand.FixtureTestTypeReference;
import com.jtstand.Product;
import com.jtstand.TestFixture;
import com.jtstand.TestProject;
import com.jtstand.TestProperty;
import com.jtstand.TestStation;
import com.jtstand.TestType;
import javax.script.Bindings;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

/**
 *
 * @author albert_kurucz
 */
public class StarterProperties extends AbstractProperties {

    public static final long serialVersionUID = 20080507L;
    private StarterInterface si;

    public StarterProperties(StarterInterface si) {
        this.si = si;
    }

    @Override
    public Bindings getBindings() {
        if (bindings == null) {
            bindings = new SimpleBindings();
            bindings.put("starter", si);
        }
        return bindings;
    }

    @Override
    public Object getPropertyObjectUsingBindings(String keyString, Bindings bindings) throws ScriptException {
        //System.out.println("Getting " + keyString + "...");
        if (bindings != null) {
            bindings.put("starter", si);
        }
        FixtureTestTypeReference ttr = si.getTestType();
        if (ttr != null && ttr.getTestFixture() != null && ttr.getTestFixture().getTestStation() != null && ttr.getTestFixture().getTestStation().getTestProject() != null) {
            TestType testType = ttr.getTestFixture().getTestStation().getTestProject().getTestType(ttr);
            if (testType != null) {
                if (bindings != null) {
                    bindings.put("testType", testType);
                }
                for (TestProperty tsp : testType.getProperties()) {
                    if (tsp.getName().equals(keyString)) {
                        return tsp.getPropertyObject(bindings);
                    }
                }
                Product product = testType.getProduct();
                if (product != null) {
                    if (bindings != null) {
                        bindings.put("product", product);
                    }
                    for (TestProperty tsp : product.getProperties()) {
                        if (tsp.getName().equals(keyString)) {
                            return tsp.getPropertyObject(bindings);
                        }
                    }
                }
            }
        }
        TestFixture testFixture = si.getTestFixture();
        if (testFixture != null) {
            if (bindings != null) {
                bindings.put("fixture", testFixture);
            }
            for (TestProperty tsp : testFixture.getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        TestStation testStation = si.getTestStation();
        if (testStation != null) {
            if (bindings != null) {
                bindings.put("station", testStation);
            }
            for (TestProperty tsp : testStation.getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        TestProject testProject = si.getTestProject();
        if (testProject != null) {
            if (bindings != null) {
                bindings.put("project", testProject);
            }
            for (TestProperty tsp : testProject.getProperties()) {
                if (tsp.getName().equals(keyString)) {
                    return tsp.getPropertyObject(bindings);
                }
            }
        }
        return null;
    }
}
