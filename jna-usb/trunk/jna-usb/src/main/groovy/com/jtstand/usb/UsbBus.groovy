/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.Native

/**
 *
 * @author albert_kurucz
 */

class UsbBus extends Structure {
    public UsbBus(){
        super()
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

    def print(){
        println 'Bus: ' + Native.toString(dirname) + '@' + getIndex()
        //        println this
        //        if(root_dev != null){
        //            Structure.updateStructureByReference(UsbDevice, null, root_dev)?.print()
        //        }
        if(devices != null){
            Structure.updateStructureByReference(UsbDevice, null, devices)?.print()
        }
        if(next!=null){
            Structure.updateStructureByReference(UsbBus, null, next)?.print()
        }
    }
}

