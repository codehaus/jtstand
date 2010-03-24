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
        List<String> serialNumberList = ftdi.getSerialNumberList()
        serialNumberList.each({println it})
        if(serialNumberList.contains(TEST_SN)){
            println 'TEST_SN found!'
            ftdi.setBitMode(TEST_SN, 0xff, Ftdi.BITMODE_BITBANG)
            ftdi.write(TEST_SN, 0xff)
            Thread.sleep(500)
            ftdi.write(TEST_SN, 0)
            Thread.sleep(500)
            ftdi.write(TEST_SN, 0xff)
            Thread.sleep(500)
            ftdi.write(TEST_SN, 0)
        }
        ftdi.close()
    }
}

