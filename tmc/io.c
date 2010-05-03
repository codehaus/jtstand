#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>
#include <sys/types.h>
#include <sys/time.h>

#include "io.h"


int debug = 0;


/* ----- Asynchronous I/O -------------------------------------------------- */


#define BUF_SIZE 8192	/* this should rarely exceed ~16 bytes */

struct io_async_buf {
    int fd;
    char buf[BUF_SIZE];
    size_t len;
    struct timeval tv;
};

void *io_setup_async(const char *file) {
    struct io_async_buf *d;

    d = malloc(sizeof (*d));
    if (!d) {
        perror("malloc");
        return NULL;
    }
    d->len = 0;
    d->fd = creat(file, 0666);
    if (d->fd >= 0)
        return d;
    perror(file);
    free(d);
    return NULL;
}

static int do_write(int fd, const void *buf, size_t len) {
    ssize_t wrote;

    while (len) {
        wrote = write(fd, buf, len);
        if (wrote < 0) {
            perror("write");
            return -1;
        }
        if (!wrote) {
            fprintf(stderr, "can't write\n");
            return -1;
        }
        buf += wrote;
        len -= wrote;
    }
    return 0;
}

static int do_tv(int fd, const struct timeval *tv) {
    char buf[20];
    ssize_t len;

    len = sprintf(buf, "%lu.%06lu ",
            (unsigned long) tv->tv_sec, (unsigned long) tv->tv_usec);
    return do_write(fd, buf, len);
}

void io_push_async(void *dsc, const struct timeval *tv, const void *buf,
        size_t len, int end) {
    struct io_async_buf *d = dsc;
    const void *p;

    //fprintf(stderr, "(%*s) %d\n", len, buf, end);
    while (len) {
        p = memchr(buf, ',', len);
        if (!p && end)
            p = buf + len;
        if (!p) {
            if (d->len + len > BUF_SIZE) {
                fprintf(stderr, "buffer overflow (%u bytes)\n",
                        (int) (d->len + len));
                abort();
            }
            if (!d->len)
                d->tv = *tv;
            memcpy(d->buf + d->len, buf, len);
            d->len += len;
            return;
        }
        /* @@@FIXME: consider using pwrite */
        if (!d->len)
            do_tv(d->fd, tv);
        else {
            do_tv(d->fd, &d->tv);
            do_write(d->fd, d->buf, d->len);
            d->len = 0;
        }
        do_write(d->fd, buf, p - buf);
        do_write(d->fd, "\n", 1);
        if (p == buf + len)
            return;
        len -= p - buf + 1;
        buf = p + 1;
    }
}

void io_end_async(void *dsc) {
    struct io_async_buf *d = dsc;

    if (close(d->fd) < 0)
        perror("close");
    free(d);
}

/* ----- Synchronous I/O --------------------------------------------------- */


struct io_sync_buf {
    char *buf;
    size_t size;
};

void *io_create_buf(void) {
    struct io_sync_buf *d;

    d = malloc(sizeof (*d));
    if (!d) {
        perror("malloc");
        return NULL;
    }
    d->buf = NULL;
    d->size = 0;
    return d;
}

void io_push_buf(void *dsc, const struct timeval *tv, const void *buf,
        size_t len, int end) {
    struct io_sync_buf *d = dsc;

    //fprintf(stderr, "(%.*s) %d %d\n", len, buf, len, end);
    if (!len)
        return;
    d->buf = realloc(d->buf, d->size + len);
    if (!d->buf) {
        perror("realloc");
        abort();
    }
    memcpy(d->buf + d->size, buf, len);
    d->size += len;
}

size_t io_read_buf(void *dsc, void *buf, size_t size) {
    struct io_sync_buf *d = dsc;
    size_t len = d->size;

    if (len > size) {
        fprintf(stderr,
                "io_read_buf: %u byte buffer too small for %u byte "
                "message\n", (unsigned) size, (unsigned) len);
        return -1;
    }
    memcpy(buf, d->buf, len);
    //fprintf(stderr, "(%.*s) %d\n", len, d->buf, len);
    free(d->buf);
    free(d);
    return len;
}

void io_cancel_buf(void *dsc) {
    struct io_sync_buf *d = dsc;

    free(d->buf);
    free(d);
}
