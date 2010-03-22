/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    public static final int  FT_OK = 0;

    //
    // FT_OpenEx Flags
    //

    public static final int  FT_OPEN_BY_SERIAL_NUMBER = 1;
    public static final int  FT_OPEN_BY_DESCRIPTION   = 2;
    public static final int  FT_OPEN_BY_LOCATION      =	4;

    //
    // FT_ListDevices Flags (used in conjunction with FT_OpenEx Flags
    //

    public static final int  FT_LIST_NUMBER_ONLY = 0x80000000;
    public static final int  FT_LIST_BY_INDEX    = 0x40000000;
    public static final int  FT_LIST_ALL         = 0x20000000;

    public static final int  FT_LIST_MASK =(FT_LIST_NUMBER_ONLY|FT_LIST_BY_INDEX|FT_LIST_ALL);

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
    OTHER_ERROR = 18;
    /* openEx flags */
    public static final int OPEN_BY_SERIAL_NUMBER = 1 << 0,
    OPEN_BY_DESCRIPTION = 1 << 1,
    OPEN_BY_LOCATION = 1 << 2;
    /* listDevices flags (used in conjunction with openEx flags) */
    public static final int LIST_NUMBER_ONLY = 1 << 31,
    LIST_BY_INDEX = 1 << 30,
    LIST_ALL = 1 << 29,
    LIST_MASK = LIST_NUMBER_ONLY | LIST_BY_INDEX | LIST_ALL;
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
    BAUD_921600 = 921600;
    /* Word lengths */
    public static final int BITS_8 = 8,
    BITS_7 = 7,
    BITS_6 = 6,
    BITS_5 = 5;
    /* Stop bits */
    public static final int STOP_BITS_1 = 0,
    STOP_BITS_1_5 = 1,
    STOP_BITS_2 = 2;
    /* Parity */
    public static final int PARITY_NONE = 0,
    PARITY_ODD = 1,
    PARITY_EVEN = 2,
    PARITY_MARK = 3,
    PARITY_SPACE = 4;
    /* Flow control */
    public static final int FLOW_NONE = 0,
    FLOW_RTS_CTS = 1 << 8,
    FLOW_DTR_DSR = 1 << 9,
    FLOW_XON_XOFF = 1 << 10;
    /* Purge rx and tx `s */
    public static final int PURGE_RX = 1 << 0,
    PURGE_TX = 1 << 1;
    /* Events */
    public static final int EVENT_RXCHAR = 1 << 0,
    EVENT_MODEM_STATUS = 1 << 1;
    /* Timeouts */
    public static final int DEFAULT_RX_TIMEOUT = 300,
    DEFAULT_TX_TIMEOUT = 300;
    /* Device types */
    public static final int DEVICE_BM = 0,
    DEVICE_AM = 1,
    DEVICE_100AX = 2,
    DEVICE_UNKNOWN = 3,
    DEVICE_2232C = 4,
    DEVICE_232R = 5;

    static def libftdi = NativeLibrary.getInstance(Platform.isWindows() ? "ftd2xx" : "libftdi")
    final Pointer context

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

    public void setBitMode(byte[] sn, int mask, int mode){
        IntByReference handle=new IntByReference()
        int status = FT_OpenEx(sn, FT_OPEN_BY_SERIAL_NUMBER,handle)
        if(FT_OK == status){
            FT_SetBitMode(handle.getValue(), mask, mode)
            FT_Close(handle.getValue())
        }
    }

    public void setBitMode(String sn, int mask, int mode){
        setBitMode(Native.toByteArray(sn), mask, mode)
    }

    public int write(byte[] sn, byte[] buffer){
        IntByReference handle = new IntByReference()
        IntByReference written = new IntByReference()
        int status = FT_OpenEx(sn, FT_OPEN_BY_SERIAL_NUMBER,handle)
        if(FT_OK == status){
            FT_Write(handle.getValue(), buffer, buffer.length, written)
            FT_Close(handle.getValue())
        }
        return written.getValue()
    }

    public int write(byte[] sn, int data){
        byte[] buffer = new byte[1]
        buffer[0] = data
        return write(sn, buffer)
    }

    public int write(String sn, int data){
        return write(Native.toByteArray(sn), data)
    }

    public List<String> getSerialNumberList() throws IOException {
        List<String> serialNumbers=new ArrayList<String>();
        if(Platform.isWindows()){
            byte[] buffer = new byte[64]
            int retval

            IntByReference numberOfDevices = new IntByReference()
            retval = FT_CreateDeviceInfoList(numberOfDevices)

            if(FT_OK == retval){
                println "number of devices:" + numberOfDevices.getValue()
            }
            IntByReference flags = new IntByReference()
            IntByReference type = new IntByReference()
            IntByReference id = new IntByReference()
            IntByReference locId = new IntByReference()
            byte[] sn = new byte[64]            
            byte[] desc = new byte[64]
            IntByReference handle = new IntByReference()
            for(int i=0; i<numberOfDevices.getValue(); i++){
                retval = FT_GetDeviceInfoDetail(i, flags, type, id, locId, sn, desc, handle)
                if(FT_OK == retval){
                    if(0 == (1 & flags.getValue())){
                        println ""
                        println "i:" + i
                        println "flags:" + flags.getValue()
                        println "type:" + type.getValue()
                        println "id:" + id.getValue()
                        println "locId:" + locId.getValue()
                        println "sn:" + Native.toString(sn)
                        println "desc:" + Native.toString(desc)
                        println "handle:" + handle.getValue()
                        serialNumbers.add(Native.toString(sn))
                    }
                }
            }
        }else{
            int numberOfDevices
            DeviceList[] deviceList = new DeviceList[1];
            Pointer[] devlist = new Pointer[1]
            byte[] manufacturer = new byte[64]
            byte[] description = new byte[64]
            byte[] serialNumber = new byte[64]
            int retval;

            numberOfDevices = ftdi_usb_find_all(context, deviceList, 0x0403, 0x6001)
            println "numberOfDevices:" + numberOfDevices
            if(numberOfDevices<0){
                throw new IOException("ftdi_usb_find_all")
            }
            DeviceList list = null
            for(int i=0; i < numberOfDevices; i++){
                println i + ":" + deviceList[0]
                list = (list == null) ? deviceList[0] : list.nextDevice
                retval = ftdi_usb_get_strings(
                    context,
                    list.device,
                    manufacturer,
                    64,
                    description,
                    64,
                    serialNumber,
                    64)
                println "retval:" + retval
                println "manufacturer:" + Native.toString(manufacturer)
                println "description:" + Native.toString(description)
                println "serialNumber:" + Native.toString(serialNumber)
            }
            ftdi_list_free(devicelist)
        }
        return serialNumbers;
    }


    void close(){
        if(Platform.isWindows()){

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

