/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.visa

/**
 *
 * @author albert_kurucz
 */
class VisaInst {
    final int sesn
    def base

    void setSesn(int newSesn){
        this.sesn = newSesn
        println "VisaInst opened: $sesn"
    }

    void close(){
        viClose(sesn)
        println "VisaInst closed: $sesn"
    }

    def methodMissing(String name, args) {
        println("VisaInst missing: $name")
        base."$name"(*args)
    }

    String getRsrcClass(){
        return getRsrcClass(sesn)
    }
}
