/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.ptr.PointerByReference
import com.sun.jna.Native
import com.sun.jna.Function

/**
 *
 * @author albert_kurucz
 */
class UsbDevice extends Structure{
    /**
     * struct usb_device *next;
     */
    //public UsbDevice.ByReference next;
    public Pointer next;
    /**
     * struct usb_device *prev;
     */
    //public UsbDevice.ByReference prev;
    public Pointer prev;
    /**
     * char filename[PATH_MAX + 1];
     */
    public byte[] filename = new byte[Usb.PATH_MAX + 1];
    /**
     * struct usb_bus *bus;
     */
    // usb_bus.ByReference did not work, I'm not sure why.
    //public Pointer /* usb_bus.ByReference */bus;
    //public UsbBus.ByReference bus;
    public Pointer bus;
    /**
     * struct usb_device_descriptor descriptor;
     */
    public UsbDeviceDescriptor descriptor
    /**
     * struct usb_config_descriptor *config;
     */
    //public UsbConfigDescriptor.ByReference config;
    public Pointer config

    /**
     * void *dev; Darwin support
     */
    public Pointer dev
    /**
     * uint8_t devnum;
     */
    public byte devnum
    /**
     * unsigned char num_children;
     */
    public byte num_children
    /**
     * struct usb_device **children;
     */
    public PointerByReference children

    Pointer open(){
        usb_open(getPointer())
    }

    def methodMissing(String name, args) {
        println "UsbDevice methodMissing: $name, with args: $args"
        Function f = Usb.libusb.getFunction(name)
        if (f == null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        if("usb_open".equals(name)){
            f.invokePointer(args)
        }else{
            f.invokeInt(args)
        }
    }

    def print(){
        println 'usbDevice:' + Native.toString(filename)
        Pointer udev = open()
        println "udev:" + udev
        if(udev != null){
            println Native.toString(descriptor.iManufacturer)
            usb_close(udev)
        }
        if(next != null){
            Structure.updateStructureByReference(UsbDevice, this, next)?.print()
        }
    }
}

