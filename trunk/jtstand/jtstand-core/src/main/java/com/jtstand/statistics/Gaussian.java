/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Gaussian.java is part of JTStand.
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

/**
 *
 * @author albert_kurucz
 */
public class Gaussian {

    private double mean;
    private double stddev;

    public Gaussian(double mean, double stddev) {
        this.mean = mean;
        this.stddev = stddev;
    }

    public double fx(double x) {
        return getGaussian(x, mean, stddev);
    }

    public static double getGaussian(double x, double mean, double stddev) {
        double d = x - mean;
        double c = 2 * stddev * stddev;
        return Math.exp(-d * d / c) / Math.sqrt(Math.PI * c);
    }
}
