/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * put chmod o+w -R /dev/bus/usb into /etc/init.d/rc shell script!
 * 
 * modprobe -r ftdi-sio
 *
 * sudo gedit /etc/modprobe.d/blacklist

 * add the following lines:

 * blacklist usbserial
 * blacklist usb-serial
 * blacklist ftdi_sio

 */

package com.jtstand.ftdi

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.Function
/**
 *
 * @author albert_kurucz
 */
class Ftdi {
    public static final int  FT_OK = 0

    //
    // FT_OpenEx Flags
    //

    public static final int  FT_OPEN_BY_SERIAL_NUMBER = 1
    public static final int  FT_OPEN_BY_DESCRIPTION   = 2
    public static final int  FT_OPEN_BY_LOCATION      =	4

    //
    // FT_ListDevices Flags (used in conjunction with FT_OpenEx Flags
    //

    public static final int  FT_LIST_NUMBER_ONLY = 0x80000000
    public static final int  FT_LIST_BY_INDEX    = 0x40000000
    public static final int  FT_LIST_ALL         = 0x20000000

    public static final int  FT_LIST_MASK =(FT_LIST_NUMBER_ONLY|FT_LIST_BY_INDEX|FT_LIST_ALL)

    /* Device status */
    public static final int OK = 0,
    INVALID_HANDLE = 1,
    DEVICE_NOT_FOUND = 2,
    DEVICE_NOT_OPENED = 3,
    IO_ERROR = 4,
    INSUFFICIENT_RESOURCES = 5,
    INVALID_PARAMETER = 6,
    INVALID_BAUD_RATE = 7,
    DEVICE_NOT_OPENED_FOR_ERASE = 8,
    DEVICE_NOT_OPENED_FOR_WRITE = 9,
    FAILED_TO_WRITE_DEVICE = 10,
    EEPROM_READ_FAILED = 11,
    EEPROM_WRITE_FAILED = 12,
    EEPROM_ERASE_FAILED = 13,
    EEPROM_NOT_PRESENT = 14,
    EEPROM_NOT_PROGRAMMED = 15,
    INVALID_ARGS = 16,
    NOT_SUPPORTED = 17,
    OTHER_ERROR = 18
    /* openEx flags */
    public static final int OPEN_BY_SERIAL_NUMBER = 1 << 0,
    OPEN_BY_DESCRIPTION = 1 << 1,
    OPEN_BY_LOCATION = 1 << 2
    /* listDevices flags (used in conjunction with openEx flags) */
    public static final int LIST_NUMBER_ONLY = 1 << 31,
    LIST_BY_INDEX = 1 << 30,
    LIST_ALL = 1 << 29,
    LIST_MASK = LIST_NUMBER_ONLY | LIST_BY_INDEX | LIST_ALL
    /* Baud rates */
    public static final int BAUD_300 = 300,
    BAUD_600 = 600,
    BAUD_1200 = 1200,
    BAUD_2400 = 2400,
    BAUD_4800 = 4800,
    BAUD_9600 = 9600,
    BAUD_14400 = 14400,
    BAUD_19200 = 19200,
    BAUD_38400 = 38400,
    BAUD_57600 = 57600,
    BAUD_115200 = 115200,
    BAUD_230400 = 230400,
    BAUD_460800 = 460800,
    BAUD_921600 = 921600
    /* Word lengths */
    public static final int BITS_8 = 8,
    BITS_7 = 7,
    BITS_6 = 6,
    BITS_5 = 5
    /* Stop bits */
    public static final int STOP_BITS_1 = 0,
    STOP_BITS_1_5 = 1,
    STOP_BITS_2 = 2
    /* Parity */
    public static final int PARITY_NONE = 0,
    PARITY_ODD = 1,
    PARITY_EVEN = 2,
    PARITY_MARK = 3,
    PARITY_SPACE = 4
    /* Flow control */
    public static final int FLOW_NONE = 0,
    FLOW_RTS_CTS = 1 << 8,
    FLOW_DTR_DSR = 1 << 9,
    FLOW_XON_XOFF = 1 << 10
    /* Purge rx and tx `s */
    public static final int PURGE_RX = 1 << 0,
    PURGE_TX = 1 << 1;
    /* Events */
    public static final int EVENT_RXCHAR = 1 << 0,
    EVENT_MODEM_STATUS = 1 << 1
    /* Timeouts */
    public static final int DEFAULT_RX_TIMEOUT = 300,
    DEFAULT_TX_TIMEOUT = 300
    /* Device types */
    public static final int DEVICE_BM = 0,
    DEVICE_AM = 1,
    DEVICE_100AX = 2,
    DEVICE_UNKNOWN = 3,
    DEVICE_2232C = 4,
    DEVICE_232R = 5

    public static final int VENDOR=0x0403,
    PRODUCT=0x6001

    public static final int BITMODE_RESET  = 0x00,
    BITMODE_BITBANG= 0x01,
    BITMODE_MPSSE  = 0x02,
    BITMODE_SYNCBB = 0x04,
    BITMODE_OPTO   = 0x10,
    BITMODE_CBUS   = 0x20,
    BITMODE_SYNCFF = 0x40


    static def libftdi = NativeLibrary.getInstance(Platform.isWindows() ? "ftd2xx" : "libftdi")
    Pointer context
    int handle

    Ftdi(){
        if(!Platform.isWindows()){
            Function f = libftdi.getFunction("ftdi_new")
            if (f == null) {
                throw new MissingMethodException("ftdi_new", getClass(), args)
            }
            context = f.invokePointer()
            if (null == context) {
                throw new IllegalStateException('Cannot initialize FTDI driver')
            }
        }
    }

    public void open(byte[] sn) throws IOException{
        if(Platform.isWindows()){
            IntByReference handleByRef = new IntByReference()
            int status = FT_OpenEx(sn, FT_OPEN_BY_SERIAL_NUMBER, handleByRef)
            if(FT_OK == status){
                handle=handleByRef.getValue()
            }else{
                throw new IOException("FT_OpenEx error:"+status)
            }
        }else{
            int status = ftdi_usb_open_desc(context,
                VENDOR,
                PRODUCT,
                null,
                sn)
            if(0 != status){
                throw new IOException("ftdi_usb_open_desc error:"+status)
            }
        }
    }

    public void open(String sn) throws IOException{
        open(Native.toByteArray(sn))
    }

    public void setBitMode(int mask, int mode) throws IOException {
        if(Platform.isWindows()){
            int status = FT_SetBitMode(handle, mask, mode)
            if(FT_OK != status){
                throw new IOException("FT_SetBitMode error:"+status)
            }
        }
        else{
            int status= ftdi_set_bitmode(context, mask, mode)
            if(status < 0){
                throw new IOException("ftdi_set_bitmode error:"+status)
            }
        }
    }

    public int write(byte[] buffer) throws IOException {
        if(Platform.isWindows()){
            IntByReference written = new IntByReference()
            int status = FT_Write(handle, buffer, buffer.length, written)
            if(FT_OK != status){
                throw new IOException("FT_Write error:" + status)
            }
            return written.getValue()
        }else{
            int status = ftdi_write_data_set_chunksize(context, Math.min(4096, buffer.length))
            if(status == 0){
                status = ftdi_write_data(context, buffer, buffer.length)
                if(status < 0){
                    throw new IOException("ftdi_write_data error:" + status)
                }
                return status
            }
            else{
                throw new IOException("ftdi_write_data_set_chunksize error:" + status)
            }
        }
    }

    public int write(int data) throws IOException {
        byte[] buffer = new byte[1]
        buffer[0] = data
        return write(buffer)
    }

    void close(){
        if(Platform.isWindows()){
            FT_Close(handle)
        }else{
            ftdi_free(context)
        }
        println "Ftdi closed"
    }

    def methodMissing(String name, args) {
        //println "Ftdi methodMissing: $name, with args: $args"
        def method = libftdi.getFunction(name)
        if (method==null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        method.invokeInt(args)
    }
}

