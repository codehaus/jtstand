//
// Generated from archetype; please customize.
//

package com.jtstand.visa

import groovy.util.GroovyTestCase

/**
 * Tests for the {@link Example} class.
 */
class VisaTest extends GroovyTestCase
{
    //    void testCOM1() {
    //        def visa = new Visa()
    //        VisaInst comPort = visa.open('COM1')
    //        println 'write'
    //        comPort.write("Hello")
    //        println 'read'
    //        System.out.println(comPort.read())
    //    }
    static void printHex(byte[] it){
        for(int i=0;i<it.length;i++){
            if (i != 0) {
                print(',')
            }
            print(Integer.toString((0xff & it[i]),16))
        }
        println()
    }

    void testUSB() {
        def visa = new Visa()
        println 'open USB'
        VisaRaw raw = visa.open('USB0::0x04B4::0x0100::NI-VISA-1::RAW')
        raw.printPipeInfo()

        println 'controlIn'
        byte[] inputData = raw.controlIn(0x80, 0, 0, 0, 2)
        printHex(inputData)
        assertEquals([2, 0] as byte[], inputData)

        byte[] descriptor = raw.controlIn(0x80, 6, 256, 0, 18)
        printHex(descriptor)
        assertEquals([0x12, 1, 1, 1, 0, 0, 0, 8, 0xb4, 4, 0, 1, 3, 3, 1, 2, 0, 1] as byte[], descriptor)

    }
}
