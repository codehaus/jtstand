/*
 * OutputSession.groovy
 * Copyright (C) 2009  Albert Kurucz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand.io

/**
 *
 * @author albert_kurucz
 */
class OutputSession implements java.io.Closeable {
    OutputStream outputStream
    String charactersetName
    Thread thread
    Long slow
    final Object sendLock = new Object()
    boolean aborted
    boolean closed = false

    OutputSession(OutputStream outputStream) {
        setOutputStream outputStream
    }

    OutputSession(OutputStream outputStream, String charactersetName) {
        setCharactersetName(charactersetName)
        setOutputStream outputStream
    }

    void send(String outStr) throws Exception {
        send outStr, 0
    }

    void send(String outStr, long timeout) throws Exception {
        send charactersetName != null ? outStr.getBytes(charactersetName) : outStr.getBytes(), timeout
    }

    void send(byte[] outBytes, long timeout) {
        send outBytes, 0, outBytes.length, timeout
    }

    void send(final byte[] outBytes, final int outPtr, final int outLen, final long timeout) {
        synchronized (sendLock) {
            aborted = false
            if (closed) {
                throw new Exception("OutputSession is closed, cannot send out string: '$charactersetName==null?new String(outBytes,outPtr,outLen):new String(outBytes,outPtr,outLen,charactersetName)'")
            }
            if (outputStream == null) {
                throw new Exception("OutputStream is null, cannot send out string: '$charactersetName==null?new String(outBytes,outPtr,outLen):new String(outBytes,outPtr,outLen,charactersetName)'")
            }
            if (outLen > 0) {
                thread = Thread.startDaemon(
                        {
                            if (slow == null) {
                                outputStream.write(outBytes, outPtr, outLen)
                                outputStream.flush()
                                outBytes = null
                                outLen = 0
                            } else {
                                int ptr = outPtr
                                int len = outLen
                                while (len > 0) {
                                    outputStream.write(outBytes, ptr, 1)
                                    outputStream.flush()
                                    ptr++
                                    len--
                                    if (len != 0 && slow != null) {
                                        sleep slow
                                    }
                                }
                            }
                        }
                )
            }
            try {
                if (timeout == 0) {
                    thread.join()
                } else {
                    thread.join(timeout)
                }
            } catch (InterruptedException ex) {
            }
            if (closed) {
                throw new IllegalStateException("OutputSession was closed while sending string: '$charactersetName==null?new String(outBytes,outPtr,outLen):new String(outBytes,outPtr,outLen,charactersetName)'")
            }
            if (aborted) {
                return;
            }
            if (outBytes != null) {
                throw new Exception("Timeout while sending string: '$charactersetName==null?new String(outBytes,outPtr,outLen):new String(outBytes,outPtr,outLen,charactersetName)'")
            }
        }
    }

    void abort() {
        aborted = true
        thread?.interrupt()
    }

    void close() {
        closed = true
        abort()
    }
}

