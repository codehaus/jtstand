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
        usb.usb_set_debug(0)
        usb.usb_find_busses()
        usb.usb_find_devices()
        UsbBus bus = usb.getBusses()
        //println bus
        bus?.print()
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
