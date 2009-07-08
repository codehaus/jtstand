/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.visa

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native

public class Visa {
    static def libc = NativeLibrary.getInstance(Platform.isWindows() ? "visa32" : "visa32")
    final int sesn

    void setSesn(int newSesn){
        this.sesn = newSesn
        println "Visa opened: $sesn"
    }

    void close(){
        viClose(sesn)
        println "Visa closed: $sesn"
    }

    Visa(){
        IntByReference defaultRM = new IntByReference()
        if (0 == viOpenDefaultRM(defaultRM)) {
            setSesn(defaultRM.getValue())
        } else {
            throw new IllegalStateException('Cannot open default RM')
        }
    }

    def open(String rsrcName){
        return open(rsrcName, 0, 0)
    }

    def open(String rsrcName, int accessMode, int openTimeout){
        IntByReference vi = new IntByReference()
        if (0 == viOpen(sesn, rsrcName, accessMode, openTimeout, vi)){
            String name = getRsrcClass(vi.getValue())
            if (name.equals('INSTR')){
                return new VisaInst(sesn:vi.getValue(), base:this)
            } else {
                viClose(vi.getValue())
                throw new IllegalArgumentException("Not supported class: $name")
            }
        } else {
            throw new IllegalStateException("Cannot open: $rsrcName")
        }
    }

    int getAttributeInt(int attr){
        return getAttributeInt(sesn, attr)
    }

    int getAttributeInt(int inst, int attr){
        IntByReference retval = new IntByReference()
        def status = viGetAttribute(inst, (int)attr, retval)
        if (0 == status){
            return retval.getValue()
        } else {
            throw new IllegalStateException("Error reading integer attribute of instrument: $status")
        }
    }

    String getAttributeString(int attr){
        return getAttributeString(sesn, attr)
    }

    String getAttributeString(int inst, int attr){
        byte[] retval = new byte[256]
        def status = viGetAttribute(inst, (int)attr, retval)
        if (0 == status){
            return Native.toString(retval)
        } else {
            throw new IllegalStateException("Error reading string attribute of instrument: $status")
        }
    }

    String getRsrcClass(int inst){
        return getAttributeString(inst,(int)0xBFFF0001)
    }

    def methodMissing(String name, args) {
        println("Visa missing: $name")

        def method = libc.getFunction(name)
        if (method) {
            return method.invokeInt(args)
        } else {
            throw new MissingMethodException(name, getClass(), args)
        }
    }
}
