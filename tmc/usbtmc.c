#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <limits.h>
#include <time.h>
#include <sys/types.h>

#include <usb.h>

#include "io.h"


#ifndef USB_CLASS_APP_SPEC
#define USB_CLASS_APP_SPEC 0xfe
#endif

#ifndef USB_SUBCLASS_USBTMC
#define USB_SUBCLASS_USBTMC 0x03
#endif

#ifndef USB_PROTOCOL_USB488
#define USB_PROTOCOL_USB488 1
#endif

#define SIZE_IOBUFFER (1024*1024)	/* memory is cheap ;-) */

#define DEV_DEP_MSG_OUT			1
#define DEV_DEP_MSG_IN			2

#define INITIATE_ABORT_BULK_OUT		1
#define CHECK_ABORT_BULK_OUT_STATUS	2
#define INITIATE_ABORT_BULK_IN		3
#define CHECK_ABORT_BULK_STATUS		4
#define INITIATE_CLEAR			5
#define CHECK_CLEAR_STATUS		6

#define STATUS_SUCCESS			0x01
#define STATUS_PENDING			0x02
#define STATUS_FAILED			0x80

struct usbtmc_dsc {
    usb_dev_handle *handle; /* USB device handle */
    uint8_t ep_int_in, ep_bulk_in, ep_bulk_out; /* endpoints */
    uint8_t bTag; /* sequence number */
    uint16_t max_packet; /* maximum ep_bulk_in packet size */
    unsigned long timeout; /* timeout in milliseconds */
    int retry; /* retry after timeouts */
    int rigol; /* enable Rigol-specific hacks */
    void *last_buf; /* last buffer sent */
    int last_len;
};

/* ----- Debugging --------------------------------------------------------- */


static void dump(const char *label, void *data, size_t size) {
    int i;

    if (!debug)
        return;
    fprintf(stderr, "%s (%d)", label, (int) size);
    for (i = 0; i != size; i++) {
        if (!(i & 15))
            fprintf(stderr, "\n%04x ", i);
        fprintf(stderr, " %02x", ((uint8_t *) data)[i]);
    }
    fprintf(stderr, "\n");
}

/* ----- Device selection -------------------------------------------------- */


static void multiple_ep(const char *type) {
    fprintf(stderr,
            "multiple %s endpoints found\n", type);
}

static void multiple_dev(void) {
    fprintf(stderr,
            "multiple devices found. Please add more qualifiers.\n");
}

static int get_eps(struct usbtmc_dsc *d,
        const struct usb_interface_descriptor *alt) {
    const struct usb_endpoint_descriptor *ep;
    uint8_t type_dir;

    for (ep = alt->endpoint;
            ep != alt->endpoint + alt->bNumEndpoints;
            ep++) {
        type_dir = (ep->bmAttributes & USB_ENDPOINT_TYPE_MASK) |
                (ep->bEndpointAddress & USB_ENDPOINT_DIR_MASK);
        switch (type_dir) {
            case USB_ENDPOINT_TYPE_INTERRUPT | USB_ENDPOINT_IN:
                if (d->ep_int_in) {
                    multiple_ep("Interrupt-IN");
                    return -1;
                }
                d->ep_int_in = ep->bEndpointAddress;
                break;
            case USB_ENDPOINT_TYPE_BULK | USB_ENDPOINT_IN:
                if (d->ep_bulk_in) {
                    multiple_ep("Bulk-IN");
                    return -1;
                }
                d->ep_bulk_in = ep->bEndpointAddress;
                d->max_packet = ep->wMaxPacketSize;
                break;
            case USB_ENDPOINT_TYPE_BULK:
                if (d->ep_bulk_out) {
                    multiple_ep("Bulk-OUT");
                    return -1;
                }
                d->ep_bulk_out = ep->bEndpointAddress;
                break;
            default:
                fprintf(stderr,
                        "endpoint type 0x%02x is not supported\n",
                        type_dir);
                return -1;
        }
    }
    return 0;
}

static int open_usbtmc_alt(struct usbtmc_dsc *d,
        struct usb_device *dev,
        const struct usb_config_descriptor *cfg,
        const struct usb_interface *itf,
        struct usb_interface_descriptor *alt) {
    int error;

    if (alt->bDescriptorType != USB_DT_INTERFACE)
        return 0;
    if (alt->bAlternateSetting)
        return 0;
    if (alt->bInterfaceClass != USB_CLASS_APP_SPEC)
        return 0;
    if (alt->bInterfaceSubClass != USB_SUBCLASS_USBTMC)
        return 0;
    if (alt->bInterfaceProtocol &&
            alt->bInterfaceProtocol != USB_PROTOCOL_USB488)
        return 0;

    if (d->handle) {
        multiple_dev();
        return -1;
    }
    d->handle = usb_open(dev);
    if (!d->handle) {
        fprintf(stderr, "cannot open device\n");
        return -1;
    }
    error = usb_set_configuration(d->handle, cfg->bConfigurationValue);
    if (error) {
        fprintf(stderr, "cannot set configuration (error %d)\n",
                error);
        return -1;
    }
    error = usb_claim_interface(d->handle, alt->bInterfaceNumber);
    if (error) {
        fprintf(stderr, "cannot claim interface (error %d)\n", error);
        return -1;
    }
    error = usb_set_altinterface(d->handle, alt->bAlternateSetting);
    if (error) {
        fprintf(stderr, "cannot set alternate setting (error %d)\n",
                error);
        return -1;
    }

    if (get_eps(d, alt) < 0)
        return -1;

    /* amazingly enough, after all this searching, we have a device */
    return 0;
}

static int open_usbtmc_dev(struct usbtmc_dsc *d,
        struct usb_device *dev) {
    const struct usb_config_descriptor *cfg;
    const struct usb_interface *itf;
    struct usb_interface_descriptor *alt;
    int valid;

    /* According to USB TMC spec */
    valid = dev->descriptor.bDeviceClass == USB_CLASS_PER_INTERFACE &&
            !dev->descriptor.bDeviceSubClass &&
            !dev->descriptor.bDeviceProtocol;

    /* According to Rigol Corp. */
    valid |= dev->descriptor.bDeviceClass == USB_CLASS_VENDOR_SPEC &&
            dev->descriptor.bDeviceSubClass == 0xff &&
            dev->descriptor.bDeviceProtocol == 0xff;

    if (!valid)
        return 0;

    /* seems that libusb doesn't provide the Device_Qualifier
       descriptor, so we can't sanity-check that one */

    for (cfg = dev->config;
            cfg != dev->config + dev->descriptor.bNumConfigurations;
            cfg++)
        for (itf = cfg->interface;
                itf != cfg->interface + cfg->bNumInterfaces;
                itf++)
            for (alt = itf->altsetting;
                    alt != itf->altsetting + itf->num_altsetting;
                    alt++)
                if (open_usbtmc_alt(d, dev, cfg, itf, alt) < 0)
                    return -1;
    return 0;
}

static int open_usbtmc(struct usbtmc_dsc *d,
        uint16_t vendor, uint16_t product, uint8_t bus_id, uint8_t dev_id) {
    const struct usb_bus *bus;
    struct usb_device *dev;

    usb_init();
    usb_find_busses();
    usb_find_devices();

    for (bus = usb_get_busses(); bus; bus = bus->next) {
        if (bus_id && atoi(bus->dirname) != bus_id)
            continue;
        for (dev = bus->devices; dev; dev = dev->next) {
            if (dev_id && dev->devnum != dev_id)
                continue;
            if (vendor && dev->descriptor.idVendor != vendor)
                continue;
            if (product && dev->descriptor.idProduct != product)
                continue;
            if (open_usbtmc_dev(d, dev) < 0)
                return -1;
            if (debug)
                fprintf(stderr,
                    "USBTMC device %04X:%04X at %s/%s\n",
                    dev->descriptor.idVendor,
                    dev->descriptor.idProduct,
                    bus->dirname, dev->filename);
        }
    }
    return 0;
}

/* ----- USBTMC Communication ---------------------------------------------- */


static void usbtmc_abort_in(struct usbtmc_dsc *d) {
    uint8_t buf[2];
    ssize_t got;

    if (debug)
        fprintf(stderr, "INITIATE_ABORT_BULK_IN\n");
    got = usb_control_msg(d->handle,
            USB_ENDPOINT_IN | USB_TYPE_CLASS | USB_RECIP_ENDPOINT,
            INITIATE_ABORT_BULK_IN,
            d->bTag,
            d->ep_bulk_in,
            (void *) buf, 2, d->timeout);
    if (got < 0) {
        fprintf(stderr, "INITIATE_ABORT_BULK_IN error %d\n",
                (int) got);
        return;
    }
    if (debug)
        fprintf(stderr, "status 0x%02x bTag 0x%02x\n", buf[0], buf[1]);

    /* depending on status, do more */
}

static void usbtmc_abort_out(struct usbtmc_dsc *d) {
    uint8_t buf[2];
    ssize_t got;

    if (debug)
        fprintf(stderr, "INITIATE_ABORT_BULK_OUT\n");
    got = usb_control_msg(d->handle,
            USB_ENDPOINT_IN | USB_TYPE_CLASS | USB_RECIP_ENDPOINT,
            INITIATE_ABORT_BULK_OUT,
            d->bTag,
            d->ep_bulk_out,
            (void *) buf, 2, d->timeout);
    if (got < 0) {
        fprintf(stderr, "INITIATE_ABORT_BULK_OUT error %d\n",
                (int) got);
        return;
    }
    if (debug)
        fprintf(stderr, "status 0x%02x bTag 0x%02x\n", buf[0], buf[1]);

    /* depending on status, do more */
}

static int usbtmc_clear(struct usbtmc_dsc *d) {
    uint8_t buf[SIZE_IOBUFFER];
    ssize_t got;
    int error;

    if (debug)
        fprintf(stderr, "INITIATE_CLEAR\n");
    got = usb_control_msg(d->handle,
            USB_ENDPOINT_IN | USB_TYPE_CLASS | USB_RECIP_INTERFACE,
            INITIATE_CLEAR,
            0,
            0, /* interface number */
            (void *) buf, 1, d->timeout);
    if (got < 0) {
        fprintf(stderr, "INITIATE_CLEAR error %d\n", (int) got);
        return -1;
    }

    if (debug)
        fprintf(stderr, "INITIATE_CLEAR status 0x%02x\n", buf[0]);
    if (buf[0] != STATUS_SUCCESS)
        return 0;

    while (1) {
        if (debug)
            fprintf(stderr, "CHECK_CLEAR_STATUS\n");
        got = usb_control_msg(d->handle,
                USB_ENDPOINT_IN | USB_TYPE_CLASS | USB_RECIP_INTERFACE,
                CHECK_CLEAR_STATUS,
                0,
                0, /* interface number */
                (void *) buf, 2, d->timeout);
        if (got < 0) {
            fprintf(stderr, "CHECK_CLEAR_STATUS error %d\n",
                    (int) got);
            return -1;
        }

        if (debug)
            fprintf(stderr,
                "CHECK_CLEAR_STATUS status 0x%02x, 0x%02x\n",
                buf[0], buf[1]);
        if (buf[0] != STATUS_PENDING)
            break;

        if (!(buf[1] & 1))
            continue;

        if (debug)
            fprintf(stderr, "%d vs %d\n",
                SIZE_IOBUFFER, d->max_packet);
        do {
            got = usb_bulk_read(d->handle, d->ep_bulk_in,
                    (void *) buf, SIZE_IOBUFFER, d->timeout);
            if (got < 0) {
                fprintf(stderr, "read error %d\n", (int) got);
                return -1;
            }
            dump("RECV: discarding", buf, got);
        } while (got != d->max_packet);
    }

    if (debug)
        fprintf(stderr, "usb_clear_halt\n");
    error = usb_clear_halt(d->handle, d->ep_bulk_out);
    if (error) {
        fprintf(stderr, "usb_clear_halt error %d\n", error);
        return -1;
    }

    return 0;
}

static void usage(void) {
    fprintf(stderr,
            "usage: \"usbtmc\" [, retry] [, rigol] [, timeout=N]\n"
            "       [, bus=N] [, device=N] [, vendor=N] [, product=N]\n");
}

static void *usbtmc_open(int argc, const char **argv) {
    struct usbtmc_dsc *d;
    unsigned long vendor = 0, product = 0;
    unsigned long bus = 0, device = 0;
    int i;
    char *end;

    d = malloc(sizeof (*d));
    if (!d) {
        perror("malloc");
        return NULL;
    }
    memset(d, 0, sizeof (*d));
    for (i = 0; i != argc; i++) {
        if (!strcmp(argv[i], "retry"))
            d->retry = 1;
        else if (!strncmp(argv[i], "timeout=", 8)) {
            d->timeout = strtoul(argv[i] + 8, &end, 0);
            if (*end || !d->timeout || d->timeout > INT_MAX / 1000)
                goto usage;
            d->timeout *= 1000;
        } else if (!strncmp(argv[i], "bus=", 4)) {
            bus = strtoul(argv[i] + 4, &end, 0);
            if (*end || !bus || bus > 0xff)
                goto usage;
        } else if (!strncmp(argv[i], "device=", 7)) {
            device = strtoul(argv[i] + 7, &end, 0);
            if (*end || !device || device > 0xff)
                goto usage;
        } else if (!strncmp(argv[i], "vendor=", 7)) {
            vendor = strtoul(argv[i] + 7, &end, 0);
            if (*end || !vendor || vendor > 0xffff)
                goto usage;
        } else if (!strncmp(argv[i], "product=", 8)) {
            product = strtoul(argv[i] + 8, &end, 0);
            if (*end || !product || product > 0xffff)
                goto usage;
        } else if (!strcmp(argv[i], "rigol"))
            d->rigol = 1;
        else goto usage;
    }
    if (open_usbtmc(d, vendor, product, bus, device) < 0)
        goto fail;
    if (!d->handle) {
        fprintf(stderr, "no device found\n");
        goto fail;
    }
    return d;

usage:
    usage();
fail:
    free(d);
    return NULL;
}

static void usbtmc_close(void *dsc) {
    struct usbtmc_dsc *d = dsc;
    int error;

    error = usb_close(d->handle);
    if (error)
        fprintf(stderr, "usb_close: %d\n", error);
    if (d->last_buf)
        free(d->last_buf);
}

static int bulk_write(void *dsc, const char *label, void *buf, int size) {
    struct usbtmc_dsc *d = dsc;
    int sent;

    dump(label, buf, size);
    sent = usb_bulk_write(d->handle, d->ep_bulk_out, buf, size, d->timeout);
    if (sent < 0) {
        fprintf(stderr, "write error %d\n", sent);
        return -1;
    }
    if (sent != size) {
        fprintf(stderr, "sent %d instead of %d bytes\n", sent, size);
        return -1;
    }
    return sent;
}

static int usbtmc_write(void *dsc, const void *buf, size_t len) {
    struct usbtmc_dsc *d = dsc;
    uint8_t tmp[SIZE_IOBUFFER];
    char *msg;
    size_t left, size, send_size;

    msg = malloc(len);
    if (!msg) {
        perror("malloc");
        return -1;
    }
    memcpy(msg, buf, len);
    if (d->last_buf)
        free(d->last_buf);
    d->last_buf = msg;
    d->last_len = len;
    /* "buf" may be d->last_buf, so use the copy ("tmp") from now on */

    for (left = len; left; left -= size) {
        tmp[8] = left <= SIZE_IOBUFFER - 12;
        size = tmp[8] ? left : SIZE_IOBUFFER - 12;

        d->bTag++;
        if (!d->bTag)
            d->bTag++;
        tmp[0] = DEV_DEP_MSG_OUT;
        tmp[1] = d->bTag;
        tmp[2] = ~d->bTag;
        tmp[3] = 0;
        tmp[4] = size;
        tmp[5] = size >> 8;
        tmp[6] = size >> 16;
        tmp[7] = size >> 24;
        tmp[9] = tmp[10] = tmp[11] = 0;

        if (!d->rigol) {
            memcpy(tmp + 12, msg, size);
            msg += size;

            send_size = 12 + ((size + 3) & ~3);

            if (bulk_write(d, "DEV_DEP_MSG_OUT", tmp, send_size)
                    < 0)
                return -1;
        } else {
            if (bulk_write(d, "SEND: DEV_DEP_MSG_OUT", tmp, 12) < 0)
                return -1;
            if (bulk_write(d, "SEND", msg, size) < 0)
                return -1;
        }
    }
    return 0;
}

static int usbtmc_read(void *dsc, io_push_fn *push, void *push_dsc) {
    struct usbtmc_dsc *d = dsc;
    uint8_t tmp[SIZE_IOBUFFER];
    size_t size;
    ssize_t got;
    uint32_t payload;
    struct timeval tv;
    int end = 0;

    do {
        size = SIZE_IOBUFFER - 12;
        d->bTag++;
        if (!d->bTag)
            d->bTag++;
        tmp[0] = DEV_DEP_MSG_IN;
        tmp[1] = d->bTag;
        tmp[2] = ~d->bTag;
        tmp[3] = 0;
        tmp[4] = size;
        tmp[5] = size >> 8;
        tmp[6] = size >> 16;
        tmp[7] = size >> 24;
        tmp[8] = tmp[9] = tmp[10] = tmp[11] = 0;

        if (bulk_write(d, "SEND: DEV_DEP_MSG_IN", tmp, 12) < 0)
            return -1;

        got = usb_bulk_read(d->handle, d->ep_bulk_in, (void *) tmp,
                d->rigol ? 64 : size, d->timeout);
        if (got < 0) {
            usbtmc_abort_in(d);
            fprintf(stderr, "read error %d\n", (int) got);
            if (d->retry) {
                if (usbtmc_write(d, d->last_buf, d->last_len)
                        < 0)
                    return -1;
                continue;
            }
            return got;
        }
        dump("RECV: DEV_DEP_MSG_IN", tmp, got);
        if (got < 12) {
            /* ABORT */
            fprintf(stderr,
                    "received %d bytes, need at least 12\n", (int) got);
            return -1;
        }
        if (got > size) {
            /* ABORT */
            fprintf(stderr,
                    "received %d bytes, wanted no more than %d\n",
                    (int) got, (int) size);
            return -1;
        }

        if (gettimeofday(&tv, NULL) < 0) {
            perror("gettimeofday");
            return -1;
        }

        if (tmp[0] != DEV_DEP_MSG_IN) {
            /* ABORT */
            fprintf(stderr, "unexpected message type 0x%02x\n",
                    tmp[0]);
            return -1;
        }
        if (tmp[1] != d->bTag || tmp[2] != (~d->bTag & 0xff)) {
            /* ABORT */
            fprintf(stderr,
                    "received bTag 0x%02x/0x%02x, "
                    "expected 0x%02x/0x%02x\n",
                    tmp[1], tmp[2], d->bTag, ~d->bTag & 0xff);
            return -1;
        }

        if (tmp[8] & 2) {
            fprintf(stderr, "unexpected TermChar\n");
            return -1;
        }

        payload =
                tmp[4] | (tmp[5] << 8) | (tmp[6] << 16) | (tmp[7] << 24);
        if (!payload) {
            fprintf(stderr,
                    "zero-length DEV_DEP_MSG_IN payload\n");
            return -1;
        }
        if (payload > size) {
            fprintf(stderr,
                    "DEV_DEP_MSG_IN payload has %lu bytes, "
                    "we have space for %d\n",
                    (unsigned long) payload, (int) size);
            return -1;
        }

        if (d->rigol && payload > 64 - 12) {
            got = usb_bulk_read(d->handle, d->ep_bulk_in,
                    (void *) tmp + 64, payload - (64 - 12), d->timeout);
            dump("RECV", tmp + 64, got);
        }

        end = tmp[8] & 1;
        push(push_dsc, &tv, tmp + 12, payload, end);
    } while (!end);
    return 0;
}

static int usbtmc_dci(void *dsc) {
    struct usbtmc_dsc *d = dsc;

    /*
     * @@@FIXME: This doesn't seem work yet. The goal is to abort an
     * on-going transfer, e.g., from a READ? with many samples. It succeeds
     * to interrupt the transfer sometimes, but not all the time.
     */
    if (debug)
        fprintf(stderr, "DCI\n");
    //usbtmc_clear(d);
    if (d->rigol)
        usb_reset(d->handle);
    else
        usbtmc_abort_in(d);
    //usbtmc_abort_out(d);
    return 0;
}


struct proto_ops usbtmc_ops = {
    .open = usbtmc_open,
    .close = usbtmc_close,
    .read = usbtmc_read,
    .write = usbtmc_write,
    .dci = usbtmc_dci,
};
