package com.jtstand.usb;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        UsbtmcDevice dev = UsbtmcDevice.findSerialNumber("MY49520107");
        dev.device.print();
        dev.send("*IDN?\n");
    }
}
