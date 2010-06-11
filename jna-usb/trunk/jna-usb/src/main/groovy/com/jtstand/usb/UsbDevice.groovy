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
import com.sun.jna.Platform
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
    public Pointer children

    UsbDevice(){
        super()
        if(Platform.isWindows()){
            setAlignType(Structure.ALIGN_NONE)
        }
    }

    Pointer open(){
        usb_open(getPointer())
    }

    //    static close(Pointer udev){
    //        usb_close(udev)
    //    }

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

    String getManufacturer(Pointer udev){
        if(descriptor.iManufacturer != null){
            byte[] manufacturer = new byte[256]
            def ret = usb_get_string_simple(udev,
                descriptor.iManufacturer,
                manufacturer,
                manufacturer.length)
            if (ret > 0){
                return Native.toString(manufacturer)
            }
        }
        return null
    }

    String getProduct(Pointer udev){
        if(descriptor.iProduct!=null){
            byte[] product = new byte[256]
            def ret = usb_get_string_simple(udev,
                descriptor.iProduct,
                product,
                product.length)
            if (ret > 0){
                return Native.toString(product)
            }
        }
        return null
    }

    String getSerialNumber(){
        Pointer udev=open()
        String sn=(udev==null)?null:getSerialNumber(udev)
        usb_close(udev)
        return sn
    }

    String getSerialNumber(Pointer udev){
        if(descriptor.iSerialNumber!=null){
            byte[] sn = new byte[256]
            def ret = usb_get_string_simple(udev,
                descriptor.iSerialNumber,
                sn,
                sn.length)
            if (ret > 0){
                return Native.toString(sn)
            }
        }
        return null;
    }

    static String hex4(int i){
        if(i<0x10){
            return '000' + Integer.toHexString(i)
        }else if(i<0x100){
            return '00' + Integer.toHexString(i)
        }else if(i<0x1000){
            return '0' + Integer.toHexString(i)
        }
        return Integer.toHexString(i)
    }

    UsbDevice findSerialNumber(String sn){
        if(sn.equals(getSerialNumber())){
            return this
        }
        if(next != null){
            return Structure.updateStructureByReference(UsbDevice, null, next).findSerialNumber(sn)
        }
        return null
    }

    def print(){
        print 'Device: '
        print Native.toString(filename)
        Pointer udev = open()
        if(udev != null){
            print ' #'
            print devnum
            print ' '
            print getInstrumentAddress(udev)
            print ' '
            print getManufacturer(udev)
            print '::'
            print getProduct(udev)
            usb_close(udev)
        }
        //        print ' '
        //        print num_children
        println ''

        print "  bDeviceClass:         "
        println 0xff & descriptor.bDeviceClass
        print "  bDeviceSubClass:      "
        println 0xff & descriptor.bDeviceSubClass
        print "  bDeviceProtocol:      "
        println 0xff & descriptor.bDeviceProtocol

        getConfigDescriptor()?.print()
    }

    UsbConfigDescriptor getConfigDescriptor(){
        (config==null) ? null : Structure.updateStructureByReference(UsbConfigDescriptor, null, config)
    }

    def printDevices(){
        print()
        if(next != null){
            Structure.updateStructureByReference(UsbDevice, null, next)?.printDevices()
        }
    }

    String getInstrumentAddress(Pointer udev){
        String address='USB'
        address += Structure.updateStructureByReference(UsbBus, null, bus).getIndex()
        address += '::0x'
        address +=  hex4(0xFFFF & descriptor.idVendor)
        address += '::0x'
        address +=  hex4(0xFFFF & descriptor.idProduct)
        address += '::'
        address += getSerialNumber(udev)
        address += '::INSTR'
    }

}

