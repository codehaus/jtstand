/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ni845x.cli
import com.jtstand.ni845x.*

/**
 *
 * @author albert_kurucz
 */
class Main{
    static void printHex(byte[] it){
        for(int i=0;i<it.length;i++){
            if (i != 0) {
                print(',')
            }
            print(Integer.toString(it[i],16))
        }
        println()
    }

    static void main(String[] args){
        Ni845xDevice dev = args.length > 0 ? Ni845x.open(args[0]) : Ni845x.openFirst()
        Ni845xI2cConfiguration config = Ni845x.openI2cConfiguration()
        try{
            config.setAddress(0x74)
            byte[] data = dev.read(config,1)
            printHex(data)
        }catch(Exception ex){
            println "Exception: " + ex.getMessage()
        }
        try{
            config.setAddress(0x2c)
            byte[] data = dev.read(config,1)
            printHex(data)
        }catch(Exception ex){
            println "Exception: " + ex.getMessage()
        }
    }
}
