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
