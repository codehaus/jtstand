/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference

/**
 *
 * @author albert_kurucz
 */
class UsbEndpointDescriptor extends Structure{
    /**
     * uint8_t bLength;
     */
    public byte bLength;
    /**
     * uint8_t bDescriptorType;
     */
    public byte bDescriptorType;
    /**
     * uint8_t bEndpointAddress;
     */
    public byte bEndpointAddress;
    /**
     * uint8_t bmAttributes;
     */
    public byte bmAttributes;
    /**
     * uint16_t wMaxPacketSize;
     */
    public short wMaxPacketSize;
    /**
     * uint8_t bInterval;
     */
    public byte bInterval;
    /**
     * uint8_t bRefresh;
     */
    public byte bRefresh;
    /**
     * uint8_t bSynchAddress;
     */
    public byte bSynchAddress;
    /**
     * unsigned char *extra;
     */
    public ByteByReference extra;
    /**
     * int extralen;
     */
    public int extralen;
}

