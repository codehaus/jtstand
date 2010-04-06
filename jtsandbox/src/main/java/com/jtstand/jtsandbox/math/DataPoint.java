/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.jtsandbox.math;

import org.apache.commons.math.optimization.RealPointValuePair;

/**
 *
 * @author albert_kurucz
 */
public class DataPoint extends RealPointValuePair {

    private int[] factors;
    double[] factorials;

    public static double getMulti(double[] c, double[] f) {
        if (f.length != c.length) {
            throw new IllegalArgumentException("Dimensional mismatch");
        }
        double sum = 0.0;
        for (int i = 0; i < f.length; i++) {
            sum += f[i] * c[i];
        }
        return sum;
    }

    public double value(double[] c) {
        return getMulti(c, factorials) / (getValue() * getValue());
    }

    public double partialDerivative(double[] c, int i) {
        double sum = 0.0;
        sum += 2.0
                * c[i]
                * getFactorial(i)
                * getFactorial(i);

        for (int j = 0; j < c.length; j++) {
            if (i != j) {
                sum += c[j]
                        * getFactorial(i)
                        * getFactorial(j);
            }
        }
        return sum / (getValue() * getValue());
    }

    public DataPoint(final double[] point, final double value) {
        super(point, value);
    }

    public static double[] getFactorials(int[] factors, double[] point) {
        double[] f = new double[getMulti(factors)];
        f[0] = 1.0;
        for (int i = 1; i < f.length; i++) {
//            System.out.println("i:" + i);
            int[] vector = getVector(i, factors);
//            for (int v = 0; v < vector.length; v++) {
//                System.out.println("v[" + v + "]:" + vector[v]);
//            }
            int j = 0;
            while (vector[j] == 0) {
                j++;
            }
            vector[j]--;
            int s = getScalar(vector, factors);
//            System.out.println("s:" + s);
            f[i] = f[s] * point[j];
        }
        return f;
    }

    public void setFactors(int[] factors) {
        if (factors.length != getPointRef().length) {
            throw new IllegalArgumentException("Dimensional mismatch");
        }
        this.factors = factors;

        factorials = getFactorials(factors, getPointRef());
    }

    public static int getMulti(int[] vector) {
        int m = 1;
        for (int i = 0; i < vector.length; i++) {
            m *= vector[i];
        }
        return m;
    }

    public static int getScalar(int[] vector, int[] f) {
        if (f.length != vector.length) {
            throw new IllegalArgumentException("Dimensional mismatch");
        }
        int scalar = 0;
        for (int i = 0; i < f.length; i++) {
            scalar *= f[i];
            scalar += vector[i];
        }
        return scalar;
    }

    public int getScalar(int[] vector) {
        return getScalar(vector, factors);
    }

    public static int[] getVector(int scalar, int[] f) {
        int[] retval = new int[f.length];
        for (int i = f.length - 1; i >= 0; i--) {
            retval[i] = scalar % f[i];
            scalar /= f[i];
        }
        if (scalar != 0) {
            throw new IllegalArgumentException("Dimensional mismatch");
        }
        return retval;
    }

    public int[] getVector(int scalar) {
        return getVector(scalar, factors);
    }

    public double getFactorial(int i) {
        return factorials[i];
    }

    public double getFactorial(int[] i) {
        return getFactorial(getScalar(i));
    }

    public void print() {
        for (int i = 0; i < getPointRef().length; i++) {
            System.out.println("point[" + i + "]:" + getPointRef()[i]);
        }
        System.out.println("value:" + getValue());
    }

    public void printFactorials() {
        for (int i = 0; i < factorials.length; i++) {
            System.out.println("f[" + i + "]:" + factorials[i]);
        }
    }

    public static void main(String[] args) {
        DataPoint dp = new DataPoint(new double[]{2.0, 3.0}, 4.0);
        dp.setFactors(new int[]{3, 3});
        dp.printFactorials();

        dp = new DataPoint(new double[]{1.0, 0.5}, 4.0);
        dp.setFactors(new int[]{5, 5});
        dp.printFactorials();
    }
}
