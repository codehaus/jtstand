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
