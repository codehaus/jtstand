package com.jtstand.usb;

import com.sun.jna.Pointer;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        int retval;

        UsbtmcDevice dev = UsbtmcDevice.findSerialNumber("MY49001053");
        if (dev != null) {
            dev.device.print();
            Pointer udev = dev.device.open();

            retval = dev.device.setConfiguration(udev, 1);
            System.out.println("setConfiguration 1 retval: " + retval);

//            retval = dev.device.detachKernelDriver(udev,0);
//            System.out.println("detachKernelDriver retval: " + retval);

            retval = dev.device.claimInterface(udev, 0);            
            System.out.println("claimInterface retval: " + retval);
            if(retval!=0){
                System.out.println("error:"+dev.device.usbError(udev,0));
            }

            retval = dev.send(udev, "*IDN?\n");
            System.out.println("send retval: " + retval);

            retval = dev.device.releaseInterface(udev, 0);
            System.out.println("releaseInterface retval: " + retval);

            retval = dev.device.close(udev);
            System.out.println("close retval: " + retval);
        } else {
            System.out.println("Device not found");
        }
    }
}
