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

import java.text.DecimalFormat;

/**
 *
 * @author albert_kurucz
 */
public class Yield {

    public static final DecimalFormat decimalFormat = new DecimalFormat("##.#%");
    private long total = 0L;
    private long passed = 0L;

    public long getN() {
        return total;
    }

    public void pass() {
        total++;
        passed++;
    }

    public void fail() {
        total++;
    }

    @Override
    public String toString() {
        if (total == 0L) {
            return "";
        }
        StringBuffer out=new StringBuffer();
        out.append("Yield ");
        out.append(Long.toString(passed));
        out.append('/');
        out.append(Long.toString(total));
        if (total > 1L) {
            out.append(": ");
            out.append(getYieldPercent());
        }
        return out.toString();
    }

    public String getYieldPercent() {
        if (total == 0L) {
            return "";
        }
        return decimalFormat.format(getYield());
    }

    public double getYield() {
        if (total == 0L) {
            return 0.0;
        }
        return (double) passed / (double) total;
    }

}
