/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ftdi

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native
/**
 *
 * @author albert_kurucz
 */
class Ftdi {
    static def libftdi = NativeLibrary.getInstance(Platform.isWindows() ? "ftd2xx" : "libftdi")
}

