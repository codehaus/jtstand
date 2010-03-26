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
class UsbInterface extends Structure{
    /**
     * struct usb_interface_descriptor *altsetting;
     */
    //public UsbInterfaceDescriptor.ByReference altsetting;
    public Pointer altsetting;
    /**
     * int num_altsetting;
     */
    public int num_altsetting;
}

