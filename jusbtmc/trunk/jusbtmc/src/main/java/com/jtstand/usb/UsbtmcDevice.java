/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.usb;

/**
 *
 * @author albert_kurucz
 */
public class UsbtmcDevice {

    UsbDevice device;
    UsbConfigDescriptor configDescriptor;
    UsbEndpointDescriptor interruptIn;
    UsbEndpointDescriptor bulkIn;
    UsbEndpointDescriptor bulkOut;

    public UsbtmcDevice(String sn) {
        this(UsbBus.getBusses().findSerialNumber(sn));
    }

    public UsbtmcDevice(UsbDevice device) {
        this.device = device;
        configDescriptor = device.getConfigDescriptor();
//        if (configDescriptor.bNumInterfaces != 1) {
//            throw new IllegalArgumentException("USBTMC device should have one interface, but found:" + Integer.toString(configDescriptor.bNumInterfaces));
//        }
        UsbInterface[] usbInterfaces = configDescriptor.getInterfaces();
        for (int i = 0; i < usbInterfaces.length; i++) {
            UsbInterface usbInterface = usbInterfaces[i];
            UsbInterfaceDescriptor[] usbInterfaceDescriptors = usbInterface.getUsbInterfaceDescriptors();
            for (int j = 0; j < usbInterfaceDescriptors.length; j++) {
                UsbInterfaceDescriptor usbInterfaceDescriptor = usbInterfaceDescriptors[j];
                if (usbInterfaceDescriptor.bDescriptorType == Usb.getUSB_DT_INTERFACE()
                        && usbInterfaceDescriptor.bAlternateSetting == 0
                        && usbInterfaceDescriptor.bInterfaceClass == Usb.getUSB_CLASS_APP_SPEC()
                        && usbInterfaceDescriptor.bInterfaceSubClass == 3
                        && usbInterfaceDescriptor.bInterfaceProtocol == 1) {
                    UsbEndpointDescriptor[] usbEndpointDescriptors = usbInterfaceDescriptor.getUsbEndpointDescriptors();
                    for (int k = 0; k < usbEndpointDescriptors.length; k++) {
                        UsbEndpointDescriptor usbEndpointDescriptor = usbEndpointDescriptors[k];
                        if(((usbEndpointDescriptor.bmAttributes & Usb.getUSB_ENDPOINT_TYPE_MASK()) == Usb.getUSB_ENDPOINT_TYPE_INTERRUPT())
                               && ((usbEndpointDescriptor.bEndpointAddress & Usb.getUSB_ENDPOINT_DIR_MASK()) == Usb.getUSB_ENDPOINT_IN()) ){
                            interruptIn = usbEndpointDescriptor;
                            System.out.println("interruptIn found!");
                        }else if(((usbEndpointDescriptor.bmAttributes & Usb.getUSB_ENDPOINT_TYPE_MASK()) == Usb.getUSB_ENDPOINT_TYPE_BULK())
                               && ((usbEndpointDescriptor.bEndpointAddress & Usb.getUSB_ENDPOINT_DIR_MASK()) == Usb.getUSB_ENDPOINT_IN()) ){
                            bulkIn = usbEndpointDescriptor;
                            System.out.println("bulkIn found!");
                        }else if(((usbEndpointDescriptor.bmAttributes & Usb.getUSB_ENDPOINT_TYPE_MASK()) == Usb.getUSB_ENDPOINT_TYPE_BULK())
                               && ((usbEndpointDescriptor.bEndpointAddress & Usb.getUSB_ENDPOINT_DIR_MASK()) == Usb.getUSB_ENDPOINT_OUT()) ){
                            bulkOut = usbEndpointDescriptor;
                            System.out.println("bulkOut found!");
                        }
                    }
                }
            }
        }
    }
}
