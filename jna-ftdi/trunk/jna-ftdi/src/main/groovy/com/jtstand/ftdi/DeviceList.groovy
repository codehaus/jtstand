/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ftdi
import com.sun.jna.Structure
import com.sun.jna.Pointer

/**
 *
 * @author albert_kurucz
 */
class DeviceList extends Structure{
     public Pointer device
     public Pointer nextDevice
}

