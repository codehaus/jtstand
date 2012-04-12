/*
 * InputSession.groovy
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

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author albert_kurucz
 */
class InputSession implements java.io.Closeable {
    static final int buffersize = 2048
    byte[] inputData = new byte[buffersize]
    Thread thread
    StringBuffer stringBuffer = new StringBuffer()
    int caret = 0
    String charactersetName
    InputStream inputStream
    Set<OutputSession> pipes = new HashSet<OutputSession>()
    boolean aborted
    boolean closed = false
    boolean timedOut
    long expirationTime
    String group

    String receivedUntil
    Object receivedObject
    Object expectations

    final Object lock = new Object()

    InputSession(InputStream inputStream, String charactersetName) {
        setCharactersetName charactersetName
        setInputStream inputStream
    }

    InputSession(InputStream inputStream) {
        setInputStream inputStream
    }

    void setExpectations(Object expectations) {
        if (expectations instanceof Collection || expectations instanceof Map) {
            this.expectations = expectations
        } else {
            this.expectations = new ArrayList()
            ((ArrayList) this.expectations).add(expectations)
        }
    }

    String expect(Object expectations, long timeout) {
        expirationTime = System.currentTimeMillis() + timeout
        synchronized (lock) {
            if (closed) {
                throw new IllegalStateException("Cannot expect on a closed InputSession")
            }
            setExpectations(expectations)
            timedOut = false
            aborted = false
            receivedObject = null
            receivedUntil = null
            if (stringBuffer.length() > caret) {
                if (check(null)) {
                    return group
                }
            }
            while (this.expectations != null) {
                if(isClosed()){
                    throw new IllegalStateException("InputSession was closed while expecting: $expectations")
                }
                if (isAborted()) {
                    return null
                }
                long timeToWait = expirationTime - System.currentTimeMillis()
                if (timeToWait > 0L) {
                    println "waiting $timeToWait ms"
                    lock.wait(timeToWait)
                } else {
                    println "timed out"
                    this.expectations=null
                    timedOut = true
                    return null
                }
            }
            return group
        }
    }

    void setInputStream(final InputStream inputStream) {
        if (inputStream != this.inputStream) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Cannot set InputStream to null")
            }
            thread?.interrupt()
            this.inputStream = inputStream
            thread = Thread.startDaemon(
                    {
                        int total = 0
                        while (total >= 0) {
                            total = inputStream.read(inputData, 0, buffersize)
                            if (total > 0) {
                                add(inputData, 0, total)
                            }
                        }
                    }
            )
        }
    }

    void add(byte[] data, int start, int total) {
        if (total > 0) {
            if (charactersetName != null) {
                add(new String(data, start, total, charactersetName))
            }
            add(new String(data, start, total))
        }
    }

    void add(String string) {
        if (string != null && string.length() > 0) {
            System.out.println("inputStream: " + string)
            for (OutputSession pipe: pipes) {
                pipe.send string
            }
            check string
        }
    }

/*
    String getCurrentString() {
        synchronized (lock) {
            return stringBuffer.substring(caret)
        }
    }
*/

/*
    String getCurrentString(int length) {
        synchronized (lock) {
            return stringBuffer.substring(caret, caret + length)
        }
    }
*/

    /*StringBuffer getCurrentStringBuffer(){
        synchronized (lock) {
            return stringBuffer.subSequence(caret, stringBuffer.length())
        }        
    }*/

/*
    StringBuffer getCurrentStringBuffer(int length){
        synchronized (lock) {
            return stringBuffer.subSequence(caret, stringBuffer.length())
        }        
    }
*/

    protected boolean check(String string) {
        synchronized (lock) {
            if (string != null) {
                stringBuffer.append(string)
            }
            if (expectations != null) {
                Integer pos = null
                Object received = null
                expectations.each {Object expectation ->
                    if (pos != 0) {
                        def o = expectation instanceof Map.Entry ? ((Map.Entry) expectation).key : expectation
                        int p = -1
                        String g = null
                        if (o instanceof Pattern) {
                            Matcher m = ((Pattern) o).matcher(stringBuffer)
                            if (m.find(caret)) {
                                p = m.start()
                                g = m.group()
                            }
                        } else {
                            g = o.toString()
                            p = stringBuffer.indexOf(g, caret)
                            //println "looking for '$g' in '$stringBuffer' from: $caret index:$p"
                        }
                        if (p >= 0 && (pos == null || p < pos)) {
                            received = expectation
                            group = g
                            pos = p
                        }
                    }
                }
                if (pos != null) {
                    //println "Received: $received"
                    receivedUntil = stringBuffer.substring(caret, pos)
                    caret += pos + group.length()
                    receivedObject = received
                    expectations = null
                    lock.notify()
                    return true
                }
            }
        }
        return false
    }

    void abort() {
        aborted = true
        thread?.interrupt()
    }

    void close() {
        synchronized (lock) {
            closed = true
            abort();
        }
    }
}

