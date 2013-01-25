/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author albert_kurucz
 */
abstract public class AbstractTestSequenceInstanceNamedProcessor extends AbstractTestSequenceInstanceProcessor {

    private static final String defaultName = "Process";

    /**
     * please overwrite this method
     *
     * @return
     */
    public String getName() {
        return defaultName;
    }
   
}
