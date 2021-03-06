/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ni845x

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native
/**
 *
 * @author albert_kurucz
 */
class Ni845x {
    static def libc = NativeLibrary.getInstance(Platform.isWindows() ? "ni845x" : "ni845x")

    static String toStatusString(int status) {
        byte[] statusString = new byte[1024]
        ni845xStatusToString(status, 1024, statusString)
        Integer.toString(status) + ": " + Native.toString(statusString)
    }

    static List<String> findDevices() {
        int status
        List<String> list = new ArrayList<String>()
        byte[] dev = new byte[256]

        IntByReference findDeviceHandle = new IntByReference()
        IntByReference numberFound = new IntByReference()
        status = ni845xFindDevice(dev, findDeviceHandle, numberFound)
        if (status == 0) {
            list.add(Native.toString(dev))
            for (int i = 1; i < numberFound.getValue(); i++) {
                status = ni845xFindDeviceNext(findDeviceHandle.getValue(), dev)
                if (status != 0) {
                    /* Not checking error condition on close this time */
                    ni845xCloseFindDeviceHandle(findDeviceHandle.getValue())
                    throw new IllegalStateException("ni845xFindDeviceNext ERROR: " + toStatusString(status))
                }
                list.add(Native.toString(dev))
            }
            /* But we do check error condition on close this time */
            status = ni845xCloseFindDeviceHandle(findDeviceHandle.getValue())
            if (status != 0) {
                throw new IllegalStateException("ni845xCloseFindDeviceHandle ERROR: " + toStatusString(status))
            }            
        } else {
            /* Not checking error condition on close this time */
            ni845xCloseFindDeviceHandle(findDeviceHandle.getValue())
            if (status != -301701) {
                throw new IllegalStateException("ni845xFindDevice ERROR: " + toStatusString(status))
            }            
        }
        list
    }

    static Ni845xDevice openFirst() {
        def devices = findDevices()
        if (devices.isEmpty()){
            throw new IllegalStateException("No Ni845x device was found")
        }
        open(devices[0])
    }

    static Ni845xDevice open(String rsrcName) {
        IntByReference deviceHandle = new IntByReference()
        def status = ni845xOpen(rsrcName, deviceHandle)
        if (0 != status){
            throw new IllegalStateException("Cannot open: '$rsrcName', ni845xOpen ERROR: " + toStatusString(status))
        }
        new Ni845xDevice(sesn:deviceHandle.getValue(), base:this)
    }

    static Ni845xI2cConfiguration openI2cConfiguration() {
        IntByReference configurationHandle = new IntByReference()
        def status = ni845xI2cConfigurationOpen(configurationHandle)
        if (0 != status){
            throw new IllegalStateException("Cannot open a configuration: " + toStatusString(status))
        }
        new Ni845xI2cConfiguration(sesn:configurationHandle.getValue(), base:this)
    }

    static def $static_methodMissing(String name, args) {
        //println("NI845x missing: $name")

        def method = libc.getFunction(name)
        if (null == method) {
            throw new MissingMethodException(name, getClass(), args)
        }
        method.invokeInt(args)
    }    
}

