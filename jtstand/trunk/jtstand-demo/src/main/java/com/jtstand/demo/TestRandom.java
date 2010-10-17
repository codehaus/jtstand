/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.demo;

import java.util.Random;
import javax.script.Bindings;

/**
 *
 * @author albert_kurucz
 */
public class TestRandom {

    public static Random r = new Random();

    public static Object eval(String notused, Bindings bindings) {
        Double retval = 1.0 + r.nextGaussian();
        bindings.put("value", retval);
        return retval;
    }
}
