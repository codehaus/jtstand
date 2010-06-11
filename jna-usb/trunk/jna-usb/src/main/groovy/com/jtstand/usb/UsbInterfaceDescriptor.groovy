/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.Pointer
import com.sun.jna.Platform
/**
 *
 * @author albert_kurucz
 */
class UsbInterfaceDescriptor extends Structure{
    /**
     * uint8_t bLength;
     */
    public byte bLength
    /**
     * uint8_t bDescriptorType;
     */
    public byte bDescriptorType
    /**
     * uint8_t bInterfaceNumber;
     */
    public byte bInterfaceNumber
    /**
     * uint8_t bAlternateSetting;
     */
    public byte bAlternateSetting
    /**
     * uint8_t bNumEndpoints;
     */
    public byte bNumEndpoints
    /**
     * uint8_t bInterfaceClass;
     */
    public byte bInterfaceClass
    /**
     * uint8_t bInterfaceSubClass;
     */
    public byte bInterfaceSubClass
    /**
     * uint8_t bInterfaceProtocol;
     */
    public byte bInterfaceProtocol
    /**
     * uint8_t iInterface;
     */
    public byte iInterface
    /**
     * struct usb_endpoint_descriptor *endpoint;
     */
    //public UsbEndpointDescriptor.ByReference endpoint;
    public Pointer endpoint
    /**
     * unsigned char *extra;
     */
    public ByteByReference extra
    /**
     * int extralen;
     */
    public int extralen

    UsbInterfaceDescriptor(){
        super()
        if(Platform.isWindows()){
            setAlignType(Structure.ALIGN_NONE)
        }
    }

    UsbEndpointDescriptor[] getUsbEndpointDescriptors(){
        ((bNumEndpoints==0)||(endpoint == null)) ? null : Structure.updateStructureByReference(UsbEndpointDescriptor, null, endpoint)?.toArray(bNumEndpoints)
    }

    def print(){
        println 'Interface:'
        print "    bInterfaceNumber:   "
        println 0xff & bInterfaceNumber
        print "    bAlternateSetting:  "
        println 0xff & bAlternateSetting
        print "    bNumEndpoints:      "
        println 0xff & bNumEndpoints
        print "    bInterfaceClass:    "
        println 0xff & bInterfaceClass
        print "    bInterfaceSubClass: "
        println 0xff & bInterfaceSubClass
        print "    bInterfaceProtocol: "
        println 0xff & bInterfaceProtocol
        print "    iInterface:         "
        println 0xff & iInterface

        //        if(bNumEndpoints > 0){
        //            if(endpoint != null){
        //                if(bNumEndpoints > 1){
        //                    UsbEndpointDescriptor[] descriptors=Structure.updateStructureByReference(UsbEndpointDescriptor, null, endpoint)?.toArray(bNumEndpoints)
        //                    for(int i=0;i<bNumEndpoints;i++){
        //                        println 'Endpoint #'+ i
        //                        descriptors[i].print()
        //                    }
        //                }else{
        //                    Structure.updateStructureByReference(UsbEndpointDescriptor, null, endpoint).print()
        //                }
        //            }
        //        }
        getUsbEndpointDescriptors()?.each({it.print()})
    }
}

