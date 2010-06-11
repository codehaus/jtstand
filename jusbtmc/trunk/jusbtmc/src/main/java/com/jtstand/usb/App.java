package com.jtstand.usb;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        UsbtmcDevice dev = new UsbtmcDevice("C000580");
        dev.device.print();
    }
}
