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

    public static void eval(String notused, Bindings bindings) {
        bindings.put("value", 1.0 + TestRandom.r.nextGaussian());
    }
}
