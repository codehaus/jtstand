//
// Generated from archetype; please customize.
//

package com.jtstand.ni845x

import groovy.util.GroovyTestCase

/**
 * Tests for the {@link Example} class.
 */
class Ni845xTest extends GroovyTestCase
{
    void testList() {
        List<String> devices = Ni845x.findDevices()
        if (devices.isEmpty()) {
            System.out.println("There are no Ni845x devices found.")
        } else {
            System.out.println("Ni845x devices list:")
            for (String dev : devices) {
                System.out.println(dev)
            }
            System.out.println("[]")
        }
    }
}
