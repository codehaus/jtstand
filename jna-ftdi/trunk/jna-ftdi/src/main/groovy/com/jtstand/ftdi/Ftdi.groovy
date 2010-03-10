/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ftdi

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.Function
/**
 *
 * @author albert_kurucz
 */
class Ftdi {
    static def libftdi = NativeLibrary.getInstance(Platform.isWindows() ? "ftd2xx" : "libftdi")
    final Pointer context
    
    Ftdi(){
        Function f = libftdi.getFunction("ftdi_new")
        if (f == null) {
            throw new MissingMethodException("ftdi_new", getClass(), args)
        }
        context = f.invokePointer()
        if (null == context) {
            throw new IllegalStateException('Cannot initialize FTDI driver')
        }
    }

    void close(){
        ftdi_free(context)
        println "Ftdi closed"
    }

    def methodMissing(String name, args) {
        //println "Ftdi methodMissing: $name, with args: $args"
        def method = libftdi.getFunction(name)
        if (method==null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        method.invokeInt(args)
    }
}

