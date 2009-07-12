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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand.swing;

import com.jtstand.AbstractProperties;
import com.jtstand.PropertiesInterface;
import com.jtstand.TestType;
import com.jtstand.TestTypeReference;
import groovy.lang.Binding;

/**
 *
 * @author albert_kurucz
 */
public class StarterProperties extends AbstractProperties implements PropertiesInterface {

    public static final long serialVersionUID = 20080507L;
    private StarterInterface si;

    public StarterProperties(StarterInterface si) {
        this.si = si;
    }

    @Override
    public Binding getBinding() {
        if (binding == null) {
            binding = new Binding();
            binding.setVariable("starter", si);
        }
        return binding;
    }

    @Override
    public Object getPropertyObject(String keyString, Binding binding) {
        if (binding != null) {
            binding.setVariable("starter", si);
        }
        TestTypeReference ttr = si.getTestType();
        if (ttr != null && ttr.getTestStation() != null && ttr.getTestStation().getTestProject() != null) {
            TestType testType = ttr.getTestStation().getTestProject().getTestType(ttr);
            if (testType != null) {
                return testType.getPropertyObject(keyString, binding);
            }
        }
        if (si.getTestFixture() != null) {
            return si.getTestFixture().getPropertyObject(keyString, binding);
        }
        if (si.getTestStation() != null) {
            return si.getTestStation().getPropertyObject(keyString, binding);
        }
        return null;
    }
}
