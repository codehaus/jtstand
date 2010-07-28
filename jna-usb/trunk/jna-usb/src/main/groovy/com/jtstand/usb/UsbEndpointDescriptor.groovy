/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.usb
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.Pointer
import com.sun.jna.Platform
import com.sun.jna.Function

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

    UsbEndpointDescriptor(){
        super()
        if(Platform.isWindows()){
            setAlignType(Structure.ALIGN_NONE)
        }
    }

    void write(Pointer handle, String data, int timeout){
        write(data.getBytes())
    }

    void write(Pointer handle, byte[] data, int timeout){
        int requestedLength = data.size()
        int writtenLenght = usb_bulk_write(handle, bEndpointAddress, data, requestedLength, timeout)
        if(requestedLength != writtenLenght)
        {
            throw new IllegalStateException((writtenLenght<0)?"Cannot write (error "+writtenLenght+")":"Wrote "+writtenLenght+" instead of "+requestedLength+" bytes")
        }
    }

    String readString(Pointer handle, int timeout){
        readString(handle, (byte)10, timeout)
    }

    String readString(Pointer handle, byte separator, int timeout){
        String retval=""
        long timeoutEnd=System.currentTimeMillis() + timeout
        long remaining
        byte[] data = new byte[1]
        while((remaining=timeoutEnd-System.currentTimeMillis())>0)
        {
            byte inByte=readBytes(handle, data, (int)remaining)[0]
            if(inByte==separator){
                return retval
            }else{
                retval+=(char)inByte
            }
        }
    }

    byte readByte(Pointer handle, int timeout){
        readBytes(handle,1,timeout)[0]
    }

    byte[] readBytes(Pointer handle, int requestedLength, int timeout){
        readBytes(handle, new byte[requestedLength], timeout)
    }

    byte[] readBytes(Pointer handle, byte[] data, int timeout){
        int requestedLength = data.size()
        int readLenght = usb_bulk_read(handle, bEndpointAddress, data, requestedLength, timeout)
        if(requestedLength != readLenght)
        {
            throw new IllegalStateException((readLenght<0)?"Cannot read (error "+readLenght+")":"Read "+readLenght+" instead of "+requestedLength+" bytes")
        }
        data;
    }

    def methodMissing(String name, args) {
        //println "UsbDevice methodMissing: $name, with args: $args"
        Function f = Usb.libusb.getFunction(name)
        if (f == null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        f.invokeInt(args)
    }

    def print(){
        print "      bEndpointAddress: 0x"
        println Integer.toHexString(0xff & bEndpointAddress)
        print "      bmAttributes:     0x"
        println Integer.toHexString(0xff & bmAttributes)
        print "      wMaxPacketSize:   "
        println 0xffff & wMaxPacketSize
        print "      bInterval:        "
        println 0xff & bInterval
        print "      bRefresh:         "
        println 0xff & bRefresh
        print "      bSynchAddress:    "
        println 0xff & bSynchAddress
    }
}

