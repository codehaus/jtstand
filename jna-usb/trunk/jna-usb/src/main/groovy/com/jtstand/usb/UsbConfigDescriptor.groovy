/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Pointer
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.Structure

/**
 *
 * @author albert
 */
class UsbConfigDescriptor extends Structure{
    /**
     * uint8_t bLength;
     */
    public byte bLength;
    /**
     * uint8_t bDescriptorType;
     */
    public byte bDescriptorType;
    /**
     * uint16_t wTotalLength;
     */
    public short wTotalLength;
    /**
     * uint8_t bNumInterfaces;
     */
    public byte bNumInterfaces;
    /**
     * uint8_t bConfigurationValue;
     */
    public byte bConfigurationValue;
    /**
     * uint8_t iConfiguration;
     */
    public byte iConfiguration;
    /**
     * uint8_t bmAttributes;
     */
    public byte bmAttributes;
    /**
     * uint8_t MaxPower;
     */
    public byte MaxPower;
    /**
     * struct usb_interface *interface;
     */
    public Pointer interf;
    /**
     * unsigned char *extra;
     */
    public ByteByReference extra;
    /**
     * int extralen;
     */
    public int extralen;

    def print(){
        println "  wTotalLength:         " + wTotalLength
        println "  bNumInterfaces:       " + bNumInterfaces
        println "  bConfigurationValue:  " + bConfigurationValue
        println "  iConfiguration:       " + iConfiguration
        println "  bmAttributes:         0x" + Integer.toHexString(0xFF & bmAttributes)
        println "  MaxPower:             " + MaxPower
    }
}

