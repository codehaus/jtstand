/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Stats.java is part of JTStand.
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
