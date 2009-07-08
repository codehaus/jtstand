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
    void testCOM1() {
        def visa = new Visa()
        def com1 = visa.open('COM1')
    }
}
