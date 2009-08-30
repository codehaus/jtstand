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
import com.jtstand.TestType;
import com.jtstand.TestTypeReference;
import java.util.HashMap;
import javax.script.Bindings;
import javax.script.ScriptException;

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
            bindings = (Bindings) new HashMap();
            bindings.put("starter", si);
        }
        return bindings;
    }

    @Override
    public Object getPropertyObject(String keyString, Bindings bindings) throws ScriptException {
        if (bindings != null) {
            bindings.put("starter", si);
        }
        TestTypeReference ttr = si.getTestType();
        if (ttr != null && ttr.getTestStation() != null && ttr.getTestStation().getTestProject() != null) {
            TestType testType = ttr.getTestStation().getTestProject().getTestType(ttr);
            if (testType != null) {
                return testType.getPropertyObject(keyString, bindings);
            }
        }
        if (si.getTestFixture() != null) {
            return si.getTestFixture().getPropertyObject(keyString, bindings);
        }
        if (si.getTestStation() != null) {
            return si.getTestStation().getPropertyObject(keyString, bindings);
        }
        return null;
    }
}
