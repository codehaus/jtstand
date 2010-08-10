/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.armusartprogrammer;

import java.util.ArrayList;

/**
 *
 * @author albert_kurucz
 */
public class ArmUsartProgrammers extends ArrayList<ArmUsartProgrammer> {

    public void close() {
        for (ArmUsartProgrammer p : this) {
            try {
                p.close();
            } catch (Exception ex) {
                //ignore
            }
        }
    }
}
