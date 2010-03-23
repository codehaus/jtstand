/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ftdi
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference

/**
 *
 * @author albert_kurucz
 */
class DeviceList extends Structure{
     public int device
     public int nextDevice
}

