/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure

/**
 *
 * @author albert_kurucz
 */
class UsbDeviceDescriptor extends Structure{
    /**
     * uint8_t bLength;
     */
    public byte bLength;
    /**
     * uint8_t bDescriptorType;
     */
    public byte bDescriptorType;
    /**
     * uint16_t bcdUSB;
     */
    public short bcdUSB;
    /**
     * uint8_t bDeviceClass;
     */
    public byte bDeviceClass;
    /**
     * uint8_t bDeviceSubClass;
     */
    public byte bDeviceSubClass;
    /**
     * uint8_t bDeviceProtocol;
     */
    public byte bDeviceProtocol;
    /**
     * uint8_t bMaxPacketSize0;
     */
    public byte bMaxPacketSize0;
    /**
     * uint16_t idVendor;
     */
    public short idVendor;
    /**
     * uint16_t idProduct;
     */
    public short idProduct;
    /**
     * uint16_t bcdDevice;
     */
    public short bcdDevice;
    /**
     * uint8_t iManufacturer;
     */
    public byte iManufacturer;
    /**
     * uint8_t iProduct;
     */
    public byte iProduct;
    /**
     * uint8_t iSerialNumber;
     */
    public byte iSerialNumber;
    /**
     * uint8_t bNumConfigurations;
     */
    public byte bNumConfigurations;	
}
