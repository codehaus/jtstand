/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.visa;

import java.io.IOException;

/**
 *
 * @author albert_kurucz
 */
public interface Instrument {

    public String getIdn() throws IOException;

    public void send(String command) throws IOException;

    public void send(byte[] data) throws IOException;

    public String getResponseString(int timeout, String command) throws IOException;

    public String getResponseStringTrimmed(int timeout, String command) throws IOException;

    public int getResponseInteger(int timeout, String command) throws IOException;

    public double getResponseDouble(int timeout, String command) throws IOException;

    public byte[] getResponseBytes(int timeout, String command, int length) throws IOException;

    public byte[] getResponseBytes(int timeout, String command, byte[] retval) throws IOException;

    public byte[] readBytes(int timeout, int length) throws IOException;

    public byte[] readBytes(int timeout, byte[] retval) throws IOException;

    public void close() throws IOException;
}
