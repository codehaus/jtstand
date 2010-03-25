/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * put chmod o+w -R /dev/bus/usb into /etc/init.d/rc shell script!
 * 
 * modprobe -r ftdi-sio
 *
 * sudo gedit /etc/modprobe.d/blacklist

 * add the following lines:

 * blacklist usbserial
 * blacklist usb-serial
 * blacklist ftdi_sio

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
class Usb {
    static def libusb = NativeLibrary.getInstance(Platform.isWindows() ? "usb" : "usb")

    Usb(){
        usb_init()
    }    

    def methodMissing(String name, args) {
        //println "Ftdi methodMissing: $name, with args: $args"
        def method = libusb.getFunction(name)
        if (method==null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        method.invokeInt(args)
    }
}

