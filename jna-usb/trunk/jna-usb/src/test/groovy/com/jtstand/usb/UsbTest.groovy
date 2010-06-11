/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Pointer
import com.sun.jna.Native
import com.sun.jna.Structure

/**
 *
 * @author albert_kurucz
 */
class UsbTest extends GroovyTestCase{
    public static final String TEST_SN = "A6008COr"

    void testUsb(){
        UsbBus.getBusses()?.printBusses()
    }
}

