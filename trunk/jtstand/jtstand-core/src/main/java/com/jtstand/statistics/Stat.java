/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Stat.java is part of JTStand.
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

/**
 *
 * @author albert_kurucz
 */
public class Stat {

    private int n = 0;
    private double sum = 0.0;
    private double sum2 = 0.0;
    private double min = 0.0;
    private double max = 0.0;

    public boolean isDistribution() {
        return n > 1 && getStandardDeviation() != 0.0;
    }

    public void addValue(double val) {
        if (n++ == 0) {
            min = val;
            max = val;
        } else {
            min = Math.min(min, val);
            max = Math.max(max, val);
        }
        sum += val;
        sum2 += val * val;
    }

    public void substractValue(double val) {
        n--;
        sum -= val;
        sum2 -= val * val;
    }

    public int getN() {
        return n;
    }

    public double getAverage() {
        return sum / n;
    }

    public double getStandardDeviation() {
        if (n < 2) {
            return 0;
        }
        //System.out.println("N: "+N);
        return Math.sqrt((n * sum2 - sum * sum) / (((double) n) * (n - 1)));
//        if ((N * sum2 - sum * sum) < 0.0) {
//            return 0.0;
//        }
//        return Math.sqrt(N * sum2 - sum * sum) / N;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public Double getCP(Double LSL, Double USL) {
        if (LSL == null) {
//            throw new IllegalArgumentException("LSL is null");
            return null;
        }
        if (USL == null) {
//            throw new IllegalArgumentException("USL is null");
            return null;
        }
        /* (USL - LSL)  / 6*Std.Dev */
        if (isDistribution()) {
            return (USL - LSL) / (6 * getStandardDeviation());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getCPL(Double LSL) {
        if (LSL == null) {
            return Double.MAX_VALUE;
        }
        if (isDistribution()) {
            return (getAverage() - LSL) / (3 * getStandardDeviation());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getCPU(Double USL) {
        if (USL == null) {
            return Double.MAX_VALUE;
        }
        if (isDistribution()) {
            return (USL - getAverage()) / (3 * getStandardDeviation());
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getCPK(Double LSL, Double USL) {
        if (LSL == null && USL == null) {
            return Double.MAX_VALUE;
        }
        if (USL == null) {
            return getCPL(LSL);
        }
        if (LSL == null) {
            return getCPU(USL);
        }
        return Math.min(getCPL(LSL), getCPU(USL));
    }
}
