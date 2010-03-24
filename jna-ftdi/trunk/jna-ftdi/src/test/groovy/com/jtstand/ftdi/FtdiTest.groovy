/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.ftdi

/**
 *
 * @author albert_kurucz
 */
class FtdiTest extends GroovyTestCase{
    public static final String TEST_SN = "A6008COr"
    void testFtdi(){
        def ftdi = new Ftdi()
        try{
            ftdi.open(TEST_SN)
            ftdi.setBitMode(0xff, Ftdi.BITMODE_BITBANG)
            println '0xff'
            ftdi.write(0xff)
            Thread.sleep(500)
            println '0'
            ftdi.write(0)
            Thread.sleep(500)
            println '0xff'
            ftdi.write(0xff)
            Thread.sleep(500)
            println '0'
            ftdi.write(0)
            ftdi.close()
        }catch(IOException ex){
            // no fail
            // so code can be built without the test hardware
            println ex.getMessage()
        }
    }
}

