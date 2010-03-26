/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.Pointer

/**
 *
 * @author albert_kurucz
 */
class UsbInterfaceDescriptor extends Structure{
    /**
     * uint8_t bLength;
     */
    public byte bLength;
    /**
     * uint8_t bDescriptorType;
     */
    public byte bDescriptorType;
    /**
     * uint8_t bInterfaceNumber;
     */
    public byte bInterfaceNumber;
    /**
     * uint8_t bAlternateSetting;
     */
    public byte bAlternateSetting;
    /**
     * uint8_t bNumEndpoints;
     */
    public byte bNumEndpoints;
    /**
     * uint8_t bInterfaceClass;
     */
    public byte bInterfaceClass;
    /**
     * uint8_t bInterfaceSubClass;
     */
    public byte bInterfaceSubClass;
    /**
     * uint8_t bInterfaceProtocol;
     */
    public byte bInterfaceProtocol;
    /**
     * uint8_t iInterface;
     */
    public byte iInterface;
    /**
     * struct usb_endpoint_descriptor *endpoint;
     */
    //public UsbEndpointDescriptor.ByReference endpoint;
    public Pointer endpoint;
    /**
     * unsigned char *extra;
     */
    public ByteByReference extra;
    /**
     * int extralen;
     */
    public int extralen;
}

