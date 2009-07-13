/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ni845x
import com.sun.jna.ptr.IntByReference

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
        //println("Ni845xDevice missing: $name")
        base.invokeMethod(name, args)
    }

    byte[] read(Ni845xI2cConfiguration config, int numBytesToRead){
        byte[] readData = new byte[numBytesToRead]

        IntByReference readSize = new IntByReference()
        int status = ni845xI2cRead (
            sesn,
            config.sesn,
            numBytesToRead,
            readSize,
            readData
        )
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cRead ERROR: " + toStatusString(status))
        }
        return readData
    }

    void write(Ni845xI2cConfiguration config, byte[] writeData){
        int status = ni845xI2cWrite (
            sesn,
            config.sesn,
            writeData.length,
            writeData
        )
    }
}

