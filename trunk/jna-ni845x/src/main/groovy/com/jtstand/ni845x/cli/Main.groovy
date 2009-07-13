/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ni845x.cli
import com.jtstand.ni845x.Ni845x
import com.jtstand.ni845x.Ni845xDevice
import com.jtstand.ni845x.Ni845xI2cConfiguration

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
            print(Integer.toString((0xff & it[i]),16))
        }
        println()
    }

    static void main(String[] args){
        Ni845xDevice dev = args.length > 0 ? Ni845x.open(args[0]) : Ni845x.openFirst()
        Ni845xI2cConfiguration config = Ni845x.openI2cConfiguration()
        try{
            config.setAddress(0x74)
            dev.write(config, [(byte)0] as byte[])
            byte[] data0 = dev.read(config,1)
            printHex(data0)
            dev.write(config, [(byte)0x3f] as byte[])
            byte[] data3f = dev.read(config,1)
            printHex(data3f)
        }catch(Exception ex){
            println "Exception: " + ex.getMessage()
        }
//        try{
//            config.setAddress(0x20)
//            byte[] data = dev.read(config,1)
//            printHex(data)
//        }catch(Exception ex){
//            println "Exception: " + ex.getMessage()
//        }
//        try{
//            config.setAddress(0x21)
//            byte[] data = dev.read(config,1)
//            printHex(data)
//        }catch(Exception ex){
//            println "Exception: " + ex.getMessage()
//        }
        try{
            config.setAddress(0x2c)

            println 'LM81 setup'
            dev.write(config, [(byte)0, (byte)1] as byte[])

//            print 'dataDummy: '
//            byte[] dataDummy = dev.read(config,1)
//            printHex(dataDummy)

            print 'data22: '
//            dev.write(config, [(byte)0x22] as byte[])
//            byte[] data22 = dev.read(config, 1)
            byte[] data22 = dev.writeRead(config, [(byte)0x22] as byte[], 1)
            printHex(data22)
            println "3.3V: " + Double.toString(0.01719 * (data22[0] & 0xff)) + " [V]"

            print 'data23: '
            byte[] data23 = dev.writeRead(config, [(byte)0x23] as byte[], 1)
            printHex(data23)
            println "5V: " + Double.toString(0.02604 * (data23[0] & 0xff)) + " [V]"

            print 'data24: '
            byte[] data24 = dev.writeRead(config, [(byte)0x24] as byte[], 1)
            printHex(data24)
            println "12V: " + Double.toString(0.0625 * (data24[0] & 0xff)) + " [V]"
        }catch(Exception ex){
            println "Exception: " + ex.getMessage()
        }

    }
}
