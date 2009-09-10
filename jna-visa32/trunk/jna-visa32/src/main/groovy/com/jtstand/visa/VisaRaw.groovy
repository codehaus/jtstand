/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.visa
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native

/**
 *
 * @author albert_kurucz
 */
class VisaRaw {
    def base
    final int sesn

    VisaRaw(def base, int sesn){
        setBase(base)
        setSesn(sesn)
        println "VisaRaw init..."

    }
    
    void setBase(def newBase){
        this.base = newBase
        println "Base is set"
    }

    void setSesn(int newSesn){
        this.sesn = newSesn
        println "VisaRaw opened: $sesn"
    }


    void close(){
        viClose(sesn)
        println "VisaRaw closed: $sesn"
    }

    def methodMissing(String name, args) {
        //println("VisaInst methodMissing: $name")
        base.invokeMethod(name, args)
    }

    def propertyMissing(String name) {
        //println("VisaInst propertyMissing: $name")
        base."$name"
    }

    void write(String outputString) {
        IntByReference retCount = new IntByReference()
        def status = viWrite(sesn, outputString, outputString.length(), retCount)
        if(status != 0){
            throw new IllegalStateException("write '$outputString': viWrite ERROR: " + toStatusString(status))
        }
    }

    String read(){
        read(1024)
    }

    String read(int count){
        byte[] buff = new byte[count]
        IntByReference retCount = new IntByReference()
        def status = viRead((int)sesn, buff, count, retCount)
        if (status != 0 &&
            status != VI_SUCCESS_TERM_CHAR &&
            status != VI_SUCCESS_MAX_CNT) {
            throw new IllegalStateException("viRead ERROR: " + toStatusString(status))
        }
        Native.toString(buff)
    }

    byte[] readBytes(int count){
        byte[] buff = new byte[count]
        readBytes(buff, count)
    }

    byte[] readBytes(byte[] buff){
        readBytes(buff, buff.length)
    }

    byte[] readBytes(byte[] buff, int count) {
        IntByReference retCount = new IntByReference()
        def status = viRead(sesn, buff, count, retCount)
        if (status != 0 &&
            status != VI_SUCCESS_MAX_CNT) {
            throw new IllegalStateException("viRead ERROR: " + toStatusString(status))
        }
        buff
    }

    String getRsrcClass() {
        getRsrcClass(sesn)
    }

    int getNumPipes() {
        getAttributeInt(sesn, VI_ATTR_USB_NUM_PIPES)
    }

    int getBulkInPipe() {
        getAttributeInt(sesn, VI_ATTR_USB_BULK_IN_PIPE)
    }

    int getBulkOutPipe() {
        getAttributeInt(sesn, VI_ATTR_USB_BULK_OUT_PIPE)
    }

    int getCtrlPipe() {
        getAttributeInt(sesn, VI_ATTR_USB_CTRL_PIPE)
    }

    int getIntrInPipe() {
        getAttributeInt(sesn, VI_ATTR_USB_INTR_IN_PIPE)
    }

    int getRecvIntrSize() {
        getAttributeInt(sesn, VI_ATTR_USB_RECV_INTR_SIZE)        
    }

    void printPipeInfo() {
        print 'Number of pipes: '
        println getNumPipes()
        print 'Input pipe: '
        println getBulkInPipe()
        print 'Output pipe: '
        println getBulkOutPipe()
        print 'Control pipe: '
        println getCtrlPipe()
        print 'Interrupt pipe: '
        println getIntrInPipe()
        //        print 'Receive Interrupt size: '
        //        println getRecvIntrSize()
    }

    void controlOut(
        int requestType,
        int request,
        int value,
        int index,
        int length,
        byte[] buf) {

        int status = viUsbControlOut (
            sesn,
            requestType,
            request,
            value,
            index,
            length,
            buf);

        if (status != 0) {
            throw new IllegalStateException("viUsbControlOut ERROR: " + toStatusString(status))
        }
    }

    byte[] controlIn(
        int requestType,
        int request,
        int value,
        int index,
        int length) {

        byte[] buf = new byte[length]
        IntByReference retCnt = new IntByReference()

        int status = viUsbControlIn (
            sesn,
            requestType,
            request,
            value,
            index,
            length,
            buf,
            retCnt)

        if (status != 0) {
            throw new IllegalStateException("viUsbControlIn ERROR: " + toStatusString(status))
        }
        buf
    }
}
