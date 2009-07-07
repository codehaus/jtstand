/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ni845x

/**
 *
 * @author albert_kurucz
 */
class Ni845xDevice {
    final int sesn
    def base

    void setSesn(int newSesn){
        this.sesn = newSesn
        println "Ni845xDevice opened: $sesn"
    }

    void close(){
        ni845xClose(sesn)
        println "Ni845xDevice closed: $sesn"
    }

    def methodMissing(String name, args) {
        println("Ni845xDevice missing: $name")
        base."$name"(*args)
    }
	
}

