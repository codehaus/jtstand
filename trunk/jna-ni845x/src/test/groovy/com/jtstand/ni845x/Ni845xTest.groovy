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
        List<String> devicesList = Ni845x.getDevicesList()
        if (devicesList.isEmpty()) {
            System.out.println("There are no devices found")
        } else {
            System.out.println("Devices list:")
            for (String dev : devicesList) {
                System.out.println(dev)
            }
            System.out.println("[]")
        }
    }
}
