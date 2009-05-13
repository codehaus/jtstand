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
public class Yields {

    private Hashtable<String, Yield> yieldMap = new Hashtable<String, Yield>();

    public Hashtable<String, Yield> getYields() {
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
