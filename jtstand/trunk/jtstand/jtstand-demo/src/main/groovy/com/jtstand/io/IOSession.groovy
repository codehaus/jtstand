/*
 * IOSession.groovy
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
class IOSession implements java.io.Closeable {
    static final int buffersize = 2048
    Map<Object, InputSession> inputSessions = new HashMap<Object, InputSession>()
    Map<Object, OutputSession> outputSessions = new HashMap<Object, OutputSession>()

    Map pipes = new HashMap()

    InputSession inputSession
    OutputSession outputSession
    Thread outputThread
    Thread inputThread
    String charactersetName
    long timeout = 10000

    boolean closed = false

    final sendLock = new Object()

    IOSession(Object object) {
        add object
    }

    IOSession(Object object, String charactersetName) {
        add object, charactersetName
    }

    void setSlow(Long slow) {
        if (outputSession == null) {
            throw new IllegalStateException("Cannot set slow")
        }
        outputSession.setSlow slow
    }

    void add(Object object, String charactersetName) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot add null to Session")
        }
        if (!addOutputStream(object, charactersetName) && !addInputStream(object, charactersetName) && !addProcess(object, charactersetName)) {
            throw new IllegalArgumentException("Cannot add '$object' to Session")
        }
    }

    void add(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot add null to Session")
        }
        if (!addOutputStream(object) && !addInputStream(object) && !addProcess(object)) {
            throw new IllegalArgumentException("Cannot add '$object' to Session")
        }
    }

    boolean addOutputStream(Object object) {
        if (object == null) {
            return false
        }
        if (object instanceof File) {
            return addOutputStreamEntry(new MapEntry(object, new FileOutputStream((File) object)))
        }
        if (object instanceof String) {
            return addOutputStreamEntry(new MapEntry(object, new FileOutputStream((String) object)))
        }
        if (object instanceof OutputStream) {
            return addOutputStreamEntry(new MapEntry(object, (OutputStream) object))
        }
        if (object instanceof Map.Entry) {
            return addOutputStreamEntry((Map.Entry) object)
        }
        return false
    }

    boolean addOutputStream(Object object, String charactersetName) {
        if (object == null) {
            return false
        }
        if (object instanceof File) {
            return addOutputStreamEntry(new MapEntry(object, new FileOutputStream((File) object)), charactersetName)
        }
        if (object instanceof String) {
            return addOutputStreamEntry(new MapEntry(object, new FileOutputStream((String) object)), charactersetName)
        }
        if (object instanceof OutputStream) {
            return addOutputStreamEntry(new MapEntry(object, (OutputStream) object), charactersetName)
        }
        if (object instanceof Map.Entry) {
            return addOutputStreamEntry((Map.Entry) object, charactersetName)
        }
        return false
    }

    boolean addOutputStreamEntry(Map.Entry entry) {
        if (entry.value instanceof OutputStream) {
            OutputSession old = outputSessions.get(entry.key)
            if (old != null) {
                old.setOutputStream((OutputStream) entry.value)
            } else {
                old = new OutputSession((OutputStream) entry.value)
                outputSessions.put entry.key, old
            }
            if (outputSession == null) {
                outputSession = old
            }
            return true
        }
        return false
    }

    boolean addOutputStreamEntry(Map.Entry entry, String charactersetName) {
        if (entry.value instanceof OutputStream) {
            OutputSession old = outputSessions.get(entry.key)
            if (old != null) {
                old.setCharactersetName charactersetName
                old.setOutputStream((OutputStream) entry.value)
            } else {
                old = new OutputSession((OutputStream) entry.value, charactersetName)
                outputSessions.put entry.key, old
            }
            if (outputSession == null) {
                outputSession = old
            }
            return true
        }
        return false
    }

    boolean addProcess(Object object) {
        boolean added = false
        if (object.metaClass.respondsTo(object, "getOutputStream")) {
            added |= addOutputStream(object.getOutputStream())
        }
        if (object.metaClass.respondsTo(object, "getInputStream")) {
            added |= addInputStream(object.getInputStream())
        }
        if (object.metaClass.respondsTo(object, "getErrorStream")) {
            added |= addInputStream(object.getErrorStream())
        }
        return added
    }

    boolean addProcess(Object object, String charactersetName) {
        boolean added = false
        if (object.metaClass.respondsTo(object, "getOutputStream")) {
            added |= addOutputStream(object.getOutputStream(), charactersetName)
        }
        if (object.metaClass.respondsTo(object, "getInputStream")) {
            added |= addInputStream(object.getInputStream(), charactersetName)
        }
        if (object.metaClass.respondsTo(object, "getErrorStream")) {
            added |= addInputStream(object.getErrorStream(), charactersetName)
        }
        return added
    }

    boolean addInputStream(Object object) {
        if (object == null) {
            return false
        }
        if (object instanceof File) {
            return addInputStreamEntry(new MapEntry(object, new FileInputStream((File) object)))
        }
        if (object instanceof String) {
            return addInputStreamEntry(new MapEntry(object, new FileInputStream((String) object)))
        }
        if (object instanceof InputStream) {
            return addInputStreamEntry(new MapEntry(object, (InputStream) object))
        }
        if (object instanceof Map.Entry) {
            return addInputStreamEntry((Map.Entry) object)
        }
        return false
    }

    boolean addInputStream(Object object, String charactersetName) {
        if (object == null) {
            return false
        }
        if (object instanceof File) {
            return addInputStreamEntry(new MapEntry(object, new FileInputStream((File) object)), charactersetName)
        }
        if (object instanceof String) {
            return addInputStreamEntry(new MapEntry(object, new FileInputStream((String) object)), charactersetName)
        }
        if (object instanceof InputStream) {
            return addInputStreamEntry(new MapEntry(object, (InputStream) object), charactersetName)
        }
        if (object instanceof Map.Entry) {
            return addInputStreamEntry((Map.Entry) object, charactersetName)
        }
        return false
    }

    boolean addInputStreamEntry(Map.Entry entry) {
        if (entry.value instanceof InputStream) {
            InputSession old = inputSessions.get(entry.key)
            if (old != null) {
                old.setInputStream((InputStream) entry.value)
            } else {
                old = new InputSession((InputStream) entry.value)
                inputSessions.put(entry.key, old)
            }
            if (inputSession == null) {
                inputSession = old
            }
            return true

        }
        return false
    }

    boolean addInputStreamEntry(Map.Entry entry, String charactersetName) {
        if (entry.value instanceof InputStream) {
            InputSession old = inputSessions.get(entry.key)
            if (old != null) {
                old.setCharactersetName charactersetName
                old.setInputStream((InputStream) entry.value)
            } else {
                old = new InputSession((InputStream) entry.value, charactersetName)
                inputSessions.put(entry.key, old)
            }
            if (inputSession == null) {
                inputSession = old
            }
            return true

        }
        return false
    }

    void send(Closure outStr) throws Exception {
        send(outStr.call(this).toString())
    }

    void send(String str) throws Exception {
        if (outputSession == null) {
            throw new IllegalStateException("Cannot send, because outputSession is not set")
        } else {
            outputSession.send(str, timeout)
        }
    }

    void send(byte[] outBytes) {
        if (outputSession == null) {
            throw new IllegalStateException("Cannot send, because outputSession is not set")
        } else {
            outputSession.send(outBytes, timeout)
        }
    }

    void send(byte[] outBytes, int outPtr, int outLen) {
        if (outputSession == null) {
            p
            throw new IllegalStateException("Cannot send, because outputSession is not set")
        } else {
            outputSession.send(outBytes, outPtr, outLen, timeout)
        }
    }

    void fail(String failureMessage) {
        throw new IllegalStateException(failureMessage)
    }

    void fail(Closure failureMessage) {
        fail(failureMessage.call(this).toString())
    }

    String expect(Object expectations, long timeout, Closure onTimeout) {
        if (inputSession == null) {
            throw new IllegalStateException("Cannot expect, because inputSession is not set")
        }
        inputSession.expect(expectations, timeout)
        if (inputSession.isClosed()) {
            throw new IllegalStateException("InputSession was closed while expecting: $expectations")
        }
        if (inputSession.isAborted()) {
            return null
        }
        if (inputSession.isTimedOut()) {
            return onTimeout?.call(this)
        }
        Object o = inputSession.getReceivedObject()
        if (o == null) {
            throw new IllegalStateException("InputSession ended without receiving: $expectations")
        }
        if (o instanceof Map.Entry && ((Map.Entry) o).value instanceof Closure) {
            return ((Closure) ((Map.Entry) o).value).call(this)
        }
        return inputSession.getGroup()
    }


    String expect(Object expectations, Closure onTimeout) {
        return expect(expectations, timeout, onTimeout)
    }

    String expect(Object expectations, long timeout) {
        return expect(expectations, timeout, {IOSession it -> it.fail "Timeout while expecting: $expectations"})
    }

    String expect(Object expectations) {
        return expect(expectations, timeout)
    }

    void abort() {
        outputSessions.each {OutputSession it -> it.abort()}
        inputSessions.each {InputSession it -> it.abort()}
    }

    void close() throws java.io.IOException {
        closed = true
        outputSessions.each {OutputSession it -> it.close()}
        inputSessions.each {InputSession it -> it.close()}
    }

    public static void main(String[] args) {
        println "main..."
        ServerSocket serverSocket = new ServerSocket(8995)
        Thread.start {
            IOSession client = new IOSession(new Socket('127.0.0.1', 8995))
            println 'client sending GET...'
            client.send 'GET'
            client.expect 'HTML'
            println Double.parseDouble(client.expect(~/[-+]?[0-9]*\.?[0-9]+\s/).toString().trim())
            client.send 'PUT'
            println "Client OK"
        }
        Thread.start {
            IOSession server = new IOSession(serverSocket.accept())
            println 'server expecting...'
            println "expect on server returned: " + server.expect([(~/G?T/): {IOSession session -> println 'server has received a GET request'; session.send 'HTML2.34\n'}, 'PUT': {println 'server has received a PUT request'}])
            println "expect on server returned: " + server.expect([new MapEntry('GET', {println 'server has received a GET'}), 'PUT'])
            println "Server OK"
        }
    }
}