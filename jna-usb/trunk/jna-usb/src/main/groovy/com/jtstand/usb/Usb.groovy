/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * put chmod o+w -R /dev/bus/usb into /etc/init.d/rc shell script!
 * 
 * modprobe -r ftdi-sio
 *
 * sudo gedit /etc/modprobe.d/blacklist

 * add the following lines:

 * blacklist usbserial
 * blacklist usb-serial
 * blacklist ftdi_sio

#include <stdio.h>
#include <string.h>
#include <usb.h>

int verbose = 0;

void print_endpoint(struct usb_endpoint_descriptor *endpoint)
{
  printf("      bEndpointAddress: %02xh\n", endpoint->bEndpointAddress);
  printf("      bmAttributes:     %02xh\n", endpoint->bmAttributes);
  printf("      wMaxPacketSize:   %d\n", endpoint->wMaxPacketSize);
  printf("      bInterval:        %d\n", endpoint->bInterval);
  printf("      bRefresh:         %d\n", endpoint->bRefresh);
  printf("      bSynchAddress:    %d\n", endpoint->bSynchAddress);
}

void print_altsetting(struct usb_interface_descriptor *interface)
{
  int i;

  printf("    bInterfaceNumber:   %d\n", interface->bInterfaceNumber);
  printf("    bAlternateSetting:  %d\n", interface->bAlternateSetting);
  printf("    bNumEndpoints:      %d\n", interface->bNumEndpoints);
  printf("    bInterfaceClass:    %d\n", interface->bInterfaceClass);
  printf("    bInterfaceSubClass: %d\n", interface->bInterfaceSubClass);
  printf("    bInterfaceProtocol: %d\n", interface->bInterfaceProtocol);
  printf("    iInterface:         %d\n", interface->iInterface);

  for (i = 0; i < interface->bNumEndpoints; i++)
    print_endpoint(&interface->endpoint[i]);
}

void print_interface(struct usb_interface *interface)
{
  int i;

  for (i = 0; i < interface->num_altsetting; i++)
    print_altsetting(&interface->altsetting[i]);
}

void print_configuration(struct usb_config_descriptor *config)
{
  int i;

  printf("  wTotalLength:         %d\n", config->wTotalLength);
  printf("  bNumInterfaces:       %d\n", config->bNumInterfaces);
  printf("  bConfigurationValue:  %d\n", config->bConfigurationValue);
  printf("  iConfiguration:       %d\n", config->iConfiguration);
  printf("  bmAttributes:         %02xh\n", config->bmAttributes);
  printf("  MaxPower:             %d\n", config->MaxPower);

  for (i = 0; i < config->bNumInterfaces; i++)
    print_interface(&config->interface[i]);
}

int print_device(struct usb_device *dev, int level)
{
  usb_dev_handle *udev;
  char description[256];
  char string[256];
  int ret, i;

  udev = usb_open(dev);
  if (udev) {
    if (dev->descriptor.iManufacturer) {
      ret = usb_get_string_simple(udev, dev->descriptor.iManufacturer, string,
                                  sizeof(string));
      if (ret > 0)
        snprintf(description, sizeof(description), "%s - ", string);
      else
        snprintf(description, sizeof(description), "%04X - ",
                 dev->descriptor.idVendor);
    } else
      snprintf(description, sizeof(description), "%04X - ",
               dev->descriptor.idVendor);

    if (dev->descriptor.iProduct) {
      ret = usb_get_string_simple(udev, dev->descriptor.iProduct, string,
                                  sizeof(string));
      if (ret > 0)
        snprintf(description + strlen(description), sizeof(description) -
                 strlen(description), "%s", string);
      else
        snprintf(description + strlen(description), sizeof(description) -
                 strlen(description), "%04X", dev->descriptor.idProduct);
    } else
      snprintf(description + strlen(description), sizeof(description) -
               strlen(description), "%04X", dev->descriptor.idProduct);

  } else
    snprintf(description, sizeof(description), "%04X - %04X",
             dev->descriptor.idVendor, dev->descriptor.idProduct);
        print ' '
//        print num_children

  printf("%.*sDev #%d: %s\n", level * 2, "                    ", dev->devnum,
         description);

  if (udev && verbose) {
    if (dev->descriptor.iSerialNumber) {
      ret = usb_get_string_simple(udev, dev->descriptor.iSerialNumber, string,
                                  sizeof(string));
      if (ret > 0)
        printf("%.*s  - Serial Number: %s\n", level * 2,
               "                    ", string);
    }
  }

  if (udev)
    usb_close(udev);

  if (verbose) {
    if (!dev->config) {
      printf("  Couldn't retrieve descriptors\n");
      return 0;
    }

    for (i = 0; i < dev->descriptor.bNumConfigurations; i++)
      print_configuration(&dev->config[i]);
  } else {
    for (i = 0; i < dev->num_children; i++)
      print_device(dev->children[i], level + 1);
  }

  return 0;
}

int main(int argc, char *argv[])
{
  struct usb_bus *bus;

  if (argc > 1 && !strcmp(argv[1], "-v"))
    verbose = 1;

  usb_init();
  usb_set_debug(0);

  usb_find_busses();
  usb_find_devices();

  for (bus = usb_get_busses(); bus; bus = bus->next) {
    if (bus->root_dev && !verbose)
      print_device(bus->root_dev, 0);
    else {
      struct usb_device *dev;

      for (dev = bus->devices; dev; dev = dev->next)
        print_device(dev, 0);
    }
  }

  return 0;
}

 */

package com.jtstand.usb

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native
import com.sun.jna.Structure
import com.sun.jna.Pointer
import com.sun.jna.Function
/**
 *
 * @author albert_kurucz
 */
class Usb {
    static final int PATH_MAX = Platform.isWindows() ? 511 : 4096;
    //static final int PATH_MAX = 511;
    /*
     * Device and/or Interface Class codes
     */
    static final byte USB_CLASS_PER_INTERFACE = 0
    /* for DeviceClass */
    static final byte USB_CLASS_AUDIO = 1
    static final byte USB_CLASS_COMM = 2
    static final byte USB_CLASS_HID = 3
    static final byte USB_CLASS_PRINTER = 7
    static final byte USB_CLASS_PTP = 6
    static final byte USB_CLASS_MASS_STORAGE = 8
    static final byte USB_CLASS_HUB = 9
    static final byte USB_CLASS_DATA = 10
    static final byte USB_CLASS_VENDOR_SPEC = (byte)0xff
    /*
     * Descriptor types
     */
    static final byte USB_DT_DEVICE = 0x01
    static final byte USB_DT_CONFIG = 0x02
    static final byte USB_DT_STRING = 0x03
    static final byte USB_DT_INTERFACE = 0x04
    static final byte USB_DT_ENDPOINT = 0x05
    static final byte USB_DT_HID = 0x21
    static final byte USB_DT_REPORT = 0x22
    static final byte USB_DT_PHYSICAL = 0x23
    static final byte USB_DT_HUB = 0x29
    /*
     * Descriptor sizes per descriptor type
     */
    static final byte USB_DT_DEVICE_SIZE = 18
    static final byte USB_DT_CONFIG_SIZE = 9
    static final byte USB_DT_INTERFACE_SIZE = 9
    static final byte USB_DT_ENDPOINT_SIZE = 7
    static final byte USB_DT_ENDPOINT_AUDIO_SIZE = 9
    /* Audio extension */
    static final byte USB_DT_HUB_NONVAR_SIZE = 7
    static final byte USB_MAXENDPOINTS = 32
    static final byte USB_ENDPOINT_ADDRESS_MASK = 0x0f
    /* in bEndpointAddress */
    static final byte USB_ENDPOINT_DIR_MASK = (byte)0x80
    static final byte USB_ENDPOINT_TYPE_MASK = 0x03
    /* in bmAttributes */
    static final byte USB_ENDPOINT_TYPE_CONTROL = 0
    static final byte USB_ENDPOINT_TYPE_ISOCHRONOUS = 1
    static final byte USB_ENDPOINT_TYPE_BULK = 2
    static final byte USB_ENDPOINT_TYPE_INTERRUPT = 3
    /* Interface descriptor */
    static final byte USB_MAXINTERFACES = 32
    static final byte USB_MAXALTSETTING = (byte)128 /* Hard limit */

    static final byte USB_MAXCONFIG = 8
    /*
     * Standard requests
     */
    static final byte USB_REQ_GET_STATUS = 0x00
    static final byte USB_REQ_CLEAR_FEATURE = 0x01
    /* 0x02 is reserved */
    static final byte USB_REQ_SET_FEATURE = 0x03
    /* 0x04 is reserved */
    static final byte USB_REQ_SET_ADDRESS = 0x05
    static final byte USB_REQ_GET_DESCRIPTOR = 0x06
    static final byte USB_REQ_SET_DESCRIPTOR = 0x07
    static final byte USB_REQ_GET_CONFIGURATION = 0x08
    static final byte USB_REQ_SET_CONFIGURATION = 0x09
    static final byte USB_REQ_GET_INTERFACE = 0x0A
    static final byte USB_REQ_SET_INTERFACE = 0x0B
    static final byte USB_REQ_SYNCH_FRAME = 0x0C
    static final byte USB_TYPE_STANDARD = (0x00 << 5)
    static final byte USB_TYPE_CLASS = (0x01 << 5)
    static final byte USB_TYPE_VENDOR = (0x02 << 5)
    static final byte USB_TYPE_RESERVED = (0x03 << 5)
    static final byte USB_RECIP_DEVICE = 0x00
    static final byte USB_RECIP_INTERFACE = 0x01
    static final byte USB_RECIP_ENDPOINT = 0x02
    static final byte USB_RECIP_OTHER = 0x03
    /*
     * Various libusb API related stuff
     */
    static final byte USB_ENDPOINT_IN = (byte)0x80
    static final byte USB_ENDPOINT_OUT = 0x00
    /* Error codes */
    static final int USB_ERROR_BEGIN = 500000
    
    static public NativeLibrary libusb = NativeLibrary.getInstance(Platform.isWindows() ? "libusb0" : "usb-0.1")

    Usb(){
        usb_init()
    }

    def getBusses(){
        return usb_get_busses()
    }

    def methodMissing(String name, args) {
        //println "Usb methodMissing: $name, with args: $args"
        Function f = libusb.getFunction(name)
        if (f == null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        if("usb_get_busses".equals(name)){
            f.invoke(UsbBus.class, null)
        }else{
            f.invokeInt(args)
        }
    }
}

