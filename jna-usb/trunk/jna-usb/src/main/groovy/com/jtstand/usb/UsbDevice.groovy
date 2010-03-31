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
        //println "UsbDevice methodMissing: $name, with args: $args"
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
        if(udev != null){
            if(descriptor.iManufacturer!=null){
                byte[] manufacturer = new byte[256]
                def ret = usb_get_string_simple(udev,
                    descriptor.iManufacturer,
                    manufacturer,
                    manufacturer.length);
                if (ret > 0){
                    print 'manufacturer:'
                    println Native.toString(manufacturer)
                }
            }
            if(descriptor.iProduct!=null){
                byte[] product = new byte[256]
                def ret = usb_get_string_simple(udev,
                    descriptor.iProduct,
                    product,
                    product.length);
                if (ret > 0){
                    print 'product:'
                    println Native.toString(product)
                }
            }
            if(descriptor.iSerialNumber!=null){
                byte[] sn = new byte[256]
                def ret = usb_get_string_simple(udev,
                    descriptor.iSerialNumber,
                    sn,
                    sn.length);
                if (ret > 0){
                    print 'Serial number:'
                    println Native.toString(sn)
                }
            }
            print 'vendor id - product id:'
            print Integer.toHexString(descriptor.idVendor)
            print '-'
            println Integer.toHexString(descriptor.idProduct)

            print 'Device #'
            println devnum
            usb_close(udev)
        }
        if(next != null){
            Structure.updateStructureByReference(UsbDevice, this, next)?.print()
        }
    }
}

