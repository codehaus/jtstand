/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.Native
import com.sun.jna.Platform

/**
 *
 * @author albert_kurucz
 */

class UsbBus extends Structure {

    public UsbBus(){
        super()
        if(Platform.isWindows()){
            setAlignType(Structure.ALIGN_NONE)
        }
    }
    /**
     * struct usb_bus *next;
     */
    public Pointer next
    /**
     * struct usb_bus *prev;
     */    
    public Pointer prev
    /**
     * char dirname[PATH_MAX + 1];
     */
    public byte[] dirname = new byte[Usb.PATH_MAX + 1]
    /**
     * struct usb_device *devices;
     */
    public Pointer devices;
    /**
     * uint32_t location;
     */
    public int location;
    /**
     * struct usb_device *root_dev;
     */
    public Pointer root_dev;

    int getIndex(){
        (prev==null)?0:1+Structure.updateStructureByReference(UsbBus, null, prev).getIndex()
    }

    UsbDevice findSerialNumber(String sn){
        if(devices != null){
            UsbDevice device=Structure.updateStructureByReference(UsbDevice, null, devices)
            device = device?.findSerialNumber(sn)
            if(device != null){
                return device
            }
        }
        if(next != null){
            return Structure.updateStructureByReference(UsbBus, null, next).findSerialNumber(sn)
        }
        return null;
    }

    def print(){
        println 'Bus: ' + Native.toString(dirname) + '@' + getIndex()
        if(devices != null){
            Structure.updateStructureByReference(UsbDevice, null, devices)?.printDevices()
        }
    }

    def printBusses(){
        print()
        if(next!=null){
            Structure.updateStructureByReference(UsbBus, null, next)?.printBusses()
        }
    }

    static UsbBus getBusses(){
        (new Usb()).getBusses()
    }
}

