/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.jtsandbox.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.analysis.MultivariateMatrixFunction;
import org.apache.commons.math.analysis.MultivariateRealFunction;
import org.apache.commons.math.analysis.MultivariateVectorialFunction;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.optimization.general.GaussNewtonOptimizer;

/**
 *
 * @author albert_kurucz
 */
public class ErrorFunction implements DifferentiableMultivariateVectorialFunction {

    List<DataPoint> dataPoints;
    int[] factors;

    public ErrorFunction(List<DataPoint> dataPoints, int[] factors) {
        this.dataPoints = dataPoints;
        this.factors = factors;
    }

    @Override
    public double[] value(double[] c) {
        double sum = 0.0;
        for (DataPoint dp : dataPoints) {
            sum += Math.abs(dp.value(c));
        }
        return new double[]{sum / dataPoints.size()};
    }

    public double getValueWorst(double[] c) {
        double worst = 0.0;
        for (DataPoint dp : dataPoints) {
            double value = dp.value(c);
            if (Math.abs(value) > Math.abs(worst)) {
                worst = value;
            }
        }
        return worst;
    }

    public double[] getStart(RealPointValuePair dp1, RealPointValuePair dp2,
            int n, int[] factors) {
        double[] c = new double[DataPoint.getMulti(factors)];
        double scale = (dp1.getValue() - dp2.getValue())
                / (dp1.getPointRef()[n] - dp2.getPointRef()[n]);
        double offset = dp1.getValue()
                - (scale * dp2.getPointRef()[n]);
        for (int i = 0; i < c.length; i++) {
            int[] vector = DataPoint.getVector(i, factors);
            boolean isScale = true;
            for (int j = 0; j < vector.length; j++) {
                isScale &= (j == n) ? vector[j] == 1 : vector[j] == 0;
            }
            c[i] = isScale ? scale : i == 0 ? offset : 0.0;
        }
        return c;
    }

    public static double[] generateTestCalib(int[] factors) {
        double[] c = new double[DataPoint.getMulti(factors)];
        for (int i = 0; i < c.length; i++) {
            c[i] = 0.0;
        }
        c[0] = 1.0;
        c[1] = 2.0; //linear scale
        for (int i = 0; i < c.length; i++) {
            System.out.println("generated[" + i + "]:" + c[i]);
        }
        return c;
    }
    public static final Random r = new Random();

    public static List<DataPoint> generateTestData(int[] factors, int[] dataNums) {
        double[] c = generateTestCalib(factors);
        List<DataPoint> dplist = new ArrayList<DataPoint>();
        for (int i = 0; i < DataPoint.getMulti(dataNums); i++) {
            int[] f = DataPoint.getVector(i, dataNums);
            double[] point = new double[dataNums.length];
            for (int j = 0; j < point.length; j++) {
                //point[j] = r.nextDouble() * 0.000001 + (1.0 + f[j]) / dataNums[j];
                point[j] = (0.5 + f[j]) / dataNums[j];
            }
            double[] factorials = DataPoint.getFactorials(factors, point);
            for (int n = 0; n < factorials.length; n++) {
                System.out.println("factorials[" + n + "]:" + factorials[n]);
            }
            DataPoint dp = new DataPoint(point, (r.nextDouble() * 0.000001) + DataPoint.getMulti(c, factorials));
            System.out.println("dp#" + i);
            dp.print();
            dp.setFactors(factors);
            dplist.add(dp);
        }
        return dplist;
    }

    public double[] opt(DataPoint dp1, DataPoint dp2, int n) {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setMaxIterations(1000);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1.0e-5, 1.0e-5));
        double[] c = getStart(dp1, dp2, n, factors);
        for (int i = 0; i < c.length; i++) {
            System.out.println("start[" + i + "]:" + c[i]);
        }
        try {
            return optimizer.optimize(this, new double[]{0}, new double[]{1}, c).getPointRef();
        } catch (FunctionEvaluationException ex) {
            Logger.getLogger(ErrorFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OptimizationException ex) {
            Logger.getLogger(ErrorFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ErrorFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void main(String[] args) {
        int[] factors = {5, 5};
        int[] dataNums = {12, 12};
        List<DataPoint> testData = generateTestData(factors, dataNums);
        ErrorFunction ef = new ErrorFunction(testData, factors);
        double[] c = ef.opt(
                testData.get(0),
                testData.get(testData.size() - 1),
                0);
        for (int i = 0; i < c.length; i++) {
            System.out.println("c[" + i + "]:" + c[i]);
        }
    }

    public double partialDerivative(double[] c, int i) {
        double sum = 0.0;
        for (DataPoint dp : dataPoints) {
            if (dp.value(c) > 0) {
                sum += dp.partialDerivative(c, i);
            } else {
                sum -= dp.partialDerivative(c, i);
            }
        }
        return sum / dataPoints.size();
    }

    public MultivariateRealFunction partialDerivative(int k) {
        final int i = k;
        return new MultivariateRealFunction() {

            @Override
            public double value(double[] point) throws FunctionEvaluationException, IllegalArgumentException {
                return partialDerivative(point, i);
            }
        };
    }

    public MultivariateVectorialFunction gradient() {
        return new MultivariateVectorialFunction() {

            @Override
            public double[] value(double[] point) throws FunctionEvaluationException, IllegalArgumentException {
                double[] g = new double[point.length];
                for (int i = 0; i < g.length; i++) {
                    g[i] = partialDerivative(point, i);
                }
                return g;
            }
        };
    }

    @Override
    public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {

            @Override
            public double[][] value(double[] point) throws FunctionEvaluationException, IllegalArgumentException {
                double[][] j = new double[1][point.length];
                j[0] = gradient().value(point);
                return j;
            }
        };
    }
}
