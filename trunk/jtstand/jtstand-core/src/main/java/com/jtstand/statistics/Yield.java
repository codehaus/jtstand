/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Yield.java is part of JTStand.
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
