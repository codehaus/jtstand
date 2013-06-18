/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Yields.java is part of JTStand.
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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author albert_kurucz
 */
public class Yields {

    private Map<String, Yield> yieldMap = new HashMap<String, Yield>();

    public Map<String, Yield> getYields() {
        return yieldMap;
    }

    public int size() {
        return yieldMap.size();
    }

    private Yield get(String name) {
        Yield stat = yieldMap.get(name);
        if (stat == null) {
            stat = new Yield();
            yieldMap.put(name, stat);
        }
        return stat;
    }

    public long getN(String name) {
        return get(name).getN();
    }

    public void pass(String name) {
        get(name).pass();
    }

    public void fail(String name) {
        get(name).fail();
    }
}
