#ifndef IO_H
#define IO_H

#include <sys/types.h>
#include <sys/time.h>


typedef void io_push_fn(void *dsc, const struct timeval *tv,
        const void *buf, size_t len, int end);

void *io_setup_async(const char *file);
void io_push_async(void *dsc, const struct timeval *tv, const void *buf,
        size_t len, int end);
void io_end_async(void *dsc);

void *io_create_buf(void);
void io_push_buf(void *dsc, const struct timeval *tv, const void *buf,
        size_t len, int end);
size_t io_read_buf(void *dsc, void *buf, size_t size);
void io_cancel_buf(void *dsc);

struct proto_ops {
    void *(*open)(int argc, const char **argv);
    void (*close)(void *dsc);
    int (*write)(void *dsc, const void *buf, size_t len);
    int (*read)(void *dsc, io_push_fn *push, void *push_dsc);
    int (*dci)(void *dsc);
};


extern int debug;


//extern struct proto_ops usbtmc_ops; /* USBTMC */

#endif /* !IO_H */
