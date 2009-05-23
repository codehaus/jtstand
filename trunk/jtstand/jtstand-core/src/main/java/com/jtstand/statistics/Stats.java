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
package com.jtstand.statistics;

import java.util.Hashtable;

/**
 *
 * @author albert_kurucz
 */
public class Stats extends Hashtable<Object,Stat> {

    @Override
    public Stat get(Object name) {
        Stat stat = super.get(name);
        if (stat == null) {
            stat = new Stat();
            put(name, stat);
        }
        return stat;
    }

    public void addValue(String name, Object value) {
        if (value == null) {
            return;
        }
        if (value.getClass().equals(Boolean.class)) {
            addValue(name, ((Boolean) value).booleanValue() ? 1.0 : 0.0);
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            addValue(name, ((Number) value).doubleValue());
        }
    }

    public void substractValue(Object name, Object value) {
        if (value == null) {
            return;
        }
        if (value.getClass().equals(Boolean.class)) {
            substractValue(name, ((Boolean) value).booleanValue() ? 1.0 : 0.0);
        }
        if (Number.class.isAssignableFrom(value.getClass())) {
            substractValue(name, ((Number) value).doubleValue());
        }
    }

    public void addValue(Object name, double val) {
        get(name).addValue(val);
    }

    public void substractValue(Object name, double val) {
        get(name).substractValue(val);
    }

    public int getN(Object name) {
        return get(name).getN();
    }

    public double getAverage(Object name) {
        return get(name).getAverage();
    }

    public double getStandardDeviation(Object name) {
        return get(name).getStandardDeviation();
    }

    public double getCPK(Object name, Double LSL, Double USL) {
        return get(name).getCPK(LSL, USL);
    }
}
