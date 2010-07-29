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
public class VisaInstrument implements Instrument {

    private Visa visa = new Visa();
    private VisaInst inst;

    public VisaInstrument(String instAddress) {
        inst = (VisaInst) visa.openFirst(instAddress);
    }

    @Override
    public String getIdn() throws IOException {
        return getResponseString(1000, "*IDN?\n");
    }

    @Override
    public void send(String command) throws IOException {
        System.out.println("<" + command);
        inst.write(command);
    }

    @Override
    public void send(byte[] data) throws IOException {
        inst.writeBytes(data);
    }

    @Override
    public String getResponseString(int timeout, String command) throws IOException {
        inst.write(command);
        inst.setAttribute(Visa.getVI_ATTR_TMO_VALUE(), timeout);
        inst.setAttribute(Visa.getVI_ATTR_TERMCHAR(), 10);
        inst.setAttribute(Visa.getVI_ATTR_TERMCHAR_EN(), 1);
        return inst.read();
    }

    @Override
    public String getResponseStringTrimmed(int timeout, String command) throws IOException {
        String response = getResponseString(timeout, command).trim();
        int space = response.lastIndexOf(" ");
        if (space >= 0) {
            return response.substring(space + 1);
        }
        return response.startsWith("+") ? response.substring(1) : response;
    }

    @Override
    public int getResponseInteger(int timeout, String command) throws IOException {
        return Integer.parseInt(getResponseStringTrimmed(timeout, command));
    }

    @Override
    public double getResponseDouble(int timeout, String command) throws IOException {
        return Double.parseDouble(getResponseStringTrimmed(timeout, command));
    }

    @Override
    public byte[] getResponseBytes(int timeout, String command, int length) throws IOException {
        inst.write(command);
        return readBytes(timeout, length);
    }

    @Override
    public byte[] getResponseBytes(int timeout, String command, byte[] retval) throws IOException {
        inst.write(command);
        return readBytes(timeout, retval);
    }

    @Override
    public byte[] readBytes(int timeout, int length) throws IOException {
        return readBytes(timeout, new byte[length]);
    }

    @Override
    public byte[] readBytes(int timeout, byte[] retval) throws IOException {
        inst.setAttribute(Visa.getVI_ATTR_TMO_VALUE(), timeout);
        inst.setAttribute(Visa.getVI_ATTR_TERMCHAR_EN(), 0);
        return inst.readBytes(retval);
    }

    @Override
    public void close() throws IOException {
        inst.close();
    }
}
