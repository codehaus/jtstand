/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import com.jtstand.TestStepInstance;
import java.util.Random;

/**
 *
 * @author albert_kurucz
 */
public class TestRandom implements Runnable {

    public static Random r = new Random();
    public static final String STR_AVERAGE = "AVERAGE";
    public static final String STR_SIGMA = "SIGMA";
    public static final String[][][] ps = {{{}, {STR_AVERAGE, STR_SIGMA}}};
    public static final String UNIT = "V";
    private TestStepInstance step;

    @Override
    public void run() {
        step.setValue(step.getPropertyDouble(STR_AVERAGE, 0.0) + step.getPropertyDouble(STR_SIGMA, 1.0) * TestRandom.r.nextGaussian());
    }
}
