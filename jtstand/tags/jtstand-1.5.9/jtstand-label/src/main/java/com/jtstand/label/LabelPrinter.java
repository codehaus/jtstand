/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.label;

import com.jtstand.AbstractTestSequenceInstanceProcessor;
import com.jtstand.TestSequenceInstance;

/**
 *
 * @author albert_kurucz
 */
public class LabelPrinter extends AbstractTestSequenceInstanceProcessor {

    private TestSequenceInstanceLabel tsil = new TestSequenceInstanceLabel();

    @Override
    public void process(TestSequenceInstance seq) {
        tsil.setTestSequenceInstance(seq);
        tsil.print();
    }
}
