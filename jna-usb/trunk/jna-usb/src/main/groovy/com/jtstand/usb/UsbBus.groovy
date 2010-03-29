/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.Pointer

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
}

