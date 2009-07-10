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
class Ni845xI2cConfiguration {
    final int sesn
    def base
    static final int Address7Bit = 0
    static final int Address10Bit = 1

    void setSesn(int newSesn){
        this.sesn = newSesn
        println "Ni845xI2cConfiguration opened: $sesn"
    }

    void close(){
        ni845xI2cConfigurationClose(sesn)
        println "Ni845xI2cConfiguration closed: $sesn"
    }

    def methodMissing(String name, args) {
        println("Ni845xI2cConfiguration missing: $name")
        base.invokeMethod(name, args)
    }

    int getAddress() {
        IntByReference address = new IntByReference()
        def status = ni845xI2cConfigurationGetAddress(sesn, address)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationGetAddress ERROR: " + toStatusString(status))
        }
        address.getValue()
    }

    int getAddressSize() {
        IntByReference addressSize = new IntByReference()
        def status = ni845xI2cConfigurationGetAddressSize(sesn, addressSize)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationGetAddressSize ERROR: " + getStatusString(status))
        }
        addressSize.getValue()
    }

    int getClockRate() {
        IntByReference clockRate = new IntByReference()
        def status = ni845xI2cConfigurationGetClockRate(sesn, clockRate)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationGetClockRate ERROR: " + getStatusString(status))
        }
        clockRate.getValue()
    }

    int getPort() {
        IntByReference port = new IntByReference()
        def status = ni845xI2cConfigurationGetPort(sesn, port)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationGetPort ERROR: " + getStatusString(status))
        }
        port.getValue()
    }

    void setAddress(address) {
        def status = ni845xI2cConfigurationSetAddress(sesn, address)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationSetAddress ERROR: " + getStatusString(status))
        }
    }

    void setAddressSize(addressSize) {
        def status = ni845xI2cConfigurationSetAddressSize(sesn, addressSize)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationSetAddressSize ERROR: " + getStatusString(status))
        }
    }

    int setClockRate(clockRate) {
        def status = ni845xI2cConfigurationSetClockRate(sesn, clockRate)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationSetClockRate ERROR: " + getStatusString(status))
        }
    }

    int setPort(port) {
        def status = ni845xI2cConfigurationSetPort(sesn, port)
        if (status != 0) {
            throw new IllegalStateException("ni845xI2cConfigurationSetPort ERROR: " + getStatusString(status))
        }
    }
	
}

