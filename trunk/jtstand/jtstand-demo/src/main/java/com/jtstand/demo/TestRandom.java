/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.demo;

import com.jtstand.StepInterface;
import java.util.Random;

/**
 *
 * @author albert_kurucz
 */
public class TestRandom implements Runnable {

    public static Random r = new Random();
    private StepInterface step;

    public TestRandom(StepInterface step) {
        this.step = step;
    }

    @Override
    public void run() {
        step.setValue(1.0 + TestRandom.r.nextGaussian());
    }
}
