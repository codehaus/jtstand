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
        def usb = new Usb()
        println usb.usb_set_debug(0)
        println usb.usb_find_busses()
        println usb.usb_find_devices()
        def bus = usb.getBusses()
        println bus
        while (bus != null) {
            println Native.toString(bus.dirname)
            bus=(bus.next!=null)?Structure.updateStructureByReference(UsbBus,bus,bus.next):null
            println bus
        }
        //    if (bus->root_dev && !verbose)
        //      print_device(bus->root_dev, 0);
        //    else {
        //      struct usb_device *dev;
        //
        //      for (dev = bus->devices; dev; dev = dev->next)
        //        print_device(dev, 0);
        //    }
        //  }

    }
}

