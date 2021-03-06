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
class VisaInst {
    final Visa base
    final int sesn
    boolean loggingEnabled=false;

    VisaInst(def newBase, int newSesn){
        base = newBase
        sesn = newSesn
    }

    void close(){
        viClose(sesn)
//        println "VisaInst closed: $sesn"
    }

    def methodMissing(String name, args) {
        //println("VisaInst methodMissing: $name")
        base.invokeMethod(name, args)
    }
    
    def propertyMissing(String name) {
        //println("VisaInst propertyMissing: $name")
        base."$name"
    }

    void writeBytes(byte[] outputString) {
        IntByReference retCount = new IntByReference()
        def status = viWrite(sesn, outputString, outputString.size(), retCount)
        if(status != 0){
            throw new IllegalStateException("write '$outputString': viWrite ERROR: " + toStatusString(status))
        }
    }

    void write(String outputString) {
        if(loggingEnabled){
            println "<" + outputString
        }
        IntByReference retCount = new IntByReference()
        def status = viWrite(sesn, outputString, outputString.length(), retCount)
        if(status != 0){
            throw new IllegalStateException("write '$outputString': viWrite ERROR: " + toStatusString(status))
        }
    }

    void setAttribute(int attribute, int value){
        def status = viSetAttribute(sesn, attribute, value)
        if(status != 0){
            throw new IllegalStateException("viSetAttribute ERROR: " + toStatusString(status))
        }
    }    

    String read(){
        read(1024)
    }

    String readMax(int count, int max){
        byte[] buff = new byte[count]
        IntByReference retCount = new IntByReference()
        int pos=0;
        while(count>0)
        {
            byte[] b=new byte[MAX_READ];
            def status = viRead((int)sesn, b, Math.min(MAX_READ, count), retCount)
            if (status != 0 &&
                status != VI_SUCCESS_TERM_CHAR &&
                status != VI_SUCCESS_MAX_CNT) {
                throw new IllegalStateException("viRead ERROR: " + toStatusString(status))
            }
            System.arraycopy(b, 0, buff, pos, retCount.getValue());
            count-=retCount.getValue();
            pos+=retCound.getValue();
        }
        Native.toString(buff)
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

    byte[] readBytes(byte[] buff, int count){
        IntByReference retCount = new IntByReference()
        def status = viRead(sesn, buff, count, retCount)
        if (status != 0 &&
            status != VI_SUCCESS_MAX_CNT) {
            throw new IllegalStateException("viRead ERROR: " + toStatusString(status))
        }
        buff
    }

    String getRsrcClass(){
        getRsrcClass(sesn)
    }
}
