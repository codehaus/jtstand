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

    public static String getStatusString(int status) {
        byte[] statusString = new byte[1024]
        ni845xStatusToString(status, 1024, statusString)
        return Integer.toString(status) + ": " + Native.toString(statusString)
    }

    public static List<String> getDevicesList() {
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
                    throw new IllegalStateException("ni845xFindDeviceNext ERROR: " + getStatusString(status))
                }
                list.add(Native.toString(dev))
            }
            /* But we do check error condition on close this time */
            status = ni845xCloseFindDeviceHandle(findDeviceHandle.getValue())
            if (status != 0) {
                throw new IllegalStateException("ni845xCloseFindDeviceHandle ERROR: " + getStatusString(status))
            }
            return list
        } else {
            /* Not checking error condition on close this time */
            ni845xCloseFindDeviceHandle(findDeviceHandle.getValue())
            if (status != -301701) {
                throw new IllegalStateException("ni845xFindDevice ERROR: " + getStatusString(status))
            }
            return list
        }
    }

    static def openDeviceFirst() {
        def devicesList = getDevicesList()
        if (devicesList.isEmpty()){
            throw new IllegalStateException("No NI835x device found")
        }
        return open(devicesList[0])
    }

    static def openDevice(String rsrcName) {
        IntByReference deviceHandle = new IntByReference()
        def status = ni845xOpen(rsrcName, deviceHandle)
        if (0 == status){
            return new Ni845xDevice(sesn:deviceHandle.getValue(), base:this)
        } else {
            throw new IllegalStateException("Cannot open: $rsrcName")
        }
    }

    static def openI2cConfiguration() {
        IntByReference configurationHandle
        def status = ni845xI2cConfigurationOpen(configurationHandle)
        if (0 == status){
            return new Ni845xI2cConfiguration(sesn:configurationHandle.getValue(), base:this)
        } else {
            throw new IllegalStateException("Cannot open a configuration: " + getStatusString(status))
        }
    }

    static $static_methodMissing(String name, args) {
        println("NI845x missing: $name")

        def method = libc.getFunction(name)
        if (method) {
            return method.invokeInt(args)
        } else {
            throw new MissingMethodException(name, getClass(), args)
        }
    }

    static void main(args){
        List<String> devicesList = getDevicesList()
        if (devicesList.isEmpty()) {
            System.out.println("There are no devices found")
            System.exit(-1)
        } else {
            System.out.println("Devices list:")
            for (String dev : devicesList) {
                System.out.println(dev)
            }
            System.out.println("[]")
        }
    }
}

