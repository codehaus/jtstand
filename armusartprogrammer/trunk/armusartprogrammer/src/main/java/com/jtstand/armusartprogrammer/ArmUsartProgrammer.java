/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.armusartprogrammer;

import com.jtstand.intelhex.IntelHex;
import com.jtstand.intelhex.Memory;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class ArmUsartProgrammer {

    static final byte ACK = 0x79;
    static final byte NACK = 0x1F;
    String serialPortString;
    SerialPort sp;
    byte version;
    byte[] id;

    public ArmUsartProgrammer(String serialPortString) throws UnsupportedCommOperationException, PortInUseException, NoSuchPortException {
        this.serialPortString = serialPortString;
        open();
        sp.setRTS(false); //activate System Memory Boot Mode
        while (!initOK()) {
            System.out.println("Press Reset button on device and then hit <Enter>!");
        }
        close();
    }

    public int read(long time) throws IOException {
        long timeout = time + System.currentTimeMillis();
        while (System.currentTimeMillis() < timeout) {
            if (sp.getInputStream().available() > 0) {
                return sp.getInputStream().read();
            }
        }
        throw new IOException("Timeout");
    }

    private boolean initOK() {
        int inch;

        try {
            sp.getOutputStream().write(new byte[]{0x7f}, 0, 1);
            inch = read(50);
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
        if (0x79 == inch) {
            doID();
            return true;
        } else {
            return false;
        }
    }

    private void close() {
        if (sp != null) {
            sp.close();
        }
    }

    private void open() throws UnsupportedCommOperationException, PortInUseException, NoSuchPortException {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortString);
        if (portIdentifier.getPortType() != CommPortIdentifier.PORT_SERIAL) {
            throw new IllegalArgumentException("Only serial ports are allowed:" + serialPortString);
        }
        if (portIdentifier.isCurrentlyOwned()) {
            throw new IllegalArgumentException(serialPortString + " is currently in use");
        }
        sp = (SerialPort) portIdentifier.open(getClass().getName(), 5000);
        sp.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
        sp.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
    }

    public void writeUInt5(int num) throws IOException {
        byte[] retval = new byte[5];
        retval[0] = (byte) (num >> 24);
        retval[1] = (byte) (num >> 16);
        retval[2] = (byte) (num >> 8);
        retval[3] = (byte) num;
        retval[4] = (byte) (retval[0] ^ retval[1] ^ retval[2] ^ retval[3]);
        sp.getOutputStream().write(retval);
        sp.getOutputStream().flush();
    }

    public void writeUInt1(int num) throws IOException {
        byte[] retval = new byte[1];
        retval[0] = (byte) num;
        sp.getOutputStream().write(retval);
        sp.getOutputStream().flush();
    }

    public boolean doEraseWriteVerifyMemory(IntelHex hexFile) {
        return doErase() && doWriteMemory(hexFile) && doVerifyMemory(hexFile);
    }

    public boolean doWriteMemory(IntelHex hexFile) {
        for (Memory mem : hexFile.memorySegments) {
            if (!doWriteMemory(mem)) {
                close();
                return false;
            }
        }
        return true;
    }

    public boolean doVerifyMemory(IntelHex hexFile) {
        for (Memory mem : hexFile.memorySegments) {
            if (!doVerifyMemory(mem)) {
                close();
                return false;
            }
        }
        return true;
    }

    public boolean doWriteMemory(Memory mem) {
        int remainingLength = mem.data.length;
        int address = mem.address;
        System.out.println("Writing memory segment at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(remainingLength));
        for (int offset = 0; remainingLength > 0;) {
            int length = (remainingLength > 256) ? 256 : remainingLength;
            if (!doWriteMemory(address, mem.data, offset, length)) {
                return false;
            }
            offset += length;
            address += length;
            remainingLength -= length;
        }
        return true;
    }

    public boolean doVerifyMemory(Memory mem) {
        int remainingLength = mem.data.length;
        int address = mem.address;
        System.out.println("Verify memory segment at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(remainingLength));
        for (int offset = 0; remainingLength > 0;) {
            int length = (remainingLength > 256) ? 256 : remainingLength;
            if (!doVerifyMemory(address, mem.data, offset, length)) {
                return false;
            }
            offset += length;
            address += length;
            remainingLength -= length;
        }
        return true;
    }

    public boolean doWriteMemory(int address, byte[] data, int offset, int length) {
        try {
            if (length < 1 || length > 256) {
                throw new IllegalArgumentException("Length should be between 1 and 256 bytes");
            }
            System.out.println("Writing memory at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(length));
            sp.getOutputStream().write(new byte[]{(byte) 0x31, (byte) 0xce});
            sp.getOutputStream().flush();
            if (ACK == read(25)) {
                writeUInt5(address);
                if (ACK == read(25)) {
                    writeUInt1(length - 1);
                    sp.getOutputStream().write(data, offset, length);
                    sp.getOutputStream().flush();
                    int checksum = length - 1;
                    for (int i = 0; i < length; i++) {
                        checksum ^= data[offset + i];
                    }
                    writeUInt1(checksum);
                    if (ACK == read(25)) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean doVerifyMemory(int address, byte[] data, int offset, int length) {
        try {
            if (length < 1 || length > 256) {
                throw new IllegalArgumentException("Length should be between 1 and 256 bytes");
            }
            System.out.println("Verify memory at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(length));
            sp.getOutputStream().write(new byte[]{(byte) 0x11, (byte) 0xee});
            sp.getOutputStream().flush();
            if (ACK == read(25)) {
                //System.out.println("address..");
                writeUInt5(address);
                if (ACK == read(25)) {
                    //System.out.println("length..");
                    writeUInt1(length - 1);
                    writeUInt1((byte) ((length - 1) ^ 255));
                    if (ACK == read(25)) {
                        for (int i = 0; i < length; i++) {
                            //System.out.println("read..");
                            byte data_read = (byte) read(25);
                            if (data_read != data[offset + i]) {
                                System.out.println("Mismatch at address:0x" + Integer.toString(address + i, 16) + " expected:0x" + Integer.toString(data[offset + i], 16) + " received:0x" + Integer.toString(data_read, 16));
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean doID() {
        try {
            sp.getOutputStream().write(new byte[]{(byte) 0, (byte) 0xff});
            sp.getOutputStream().flush();
            if (ACK == read(25)) {
                byte nr = (byte) read(25);
                System.out.println("Number of bytes:" + nr);
                version = (byte) read(25);
                System.out.println("Booloader version: 0x" + Integer.toString(version, 16));
                id = new byte[nr];
                for (int i = 0; i < nr; i++) {
                    id[i] = (byte) read(25);
                }
                if (ACK == read(25)) {
                    System.out.println("ID:");
                    for (int i = 0; i < nr; i++) {
                        if (i > 0) {
                            System.out.println(",");
                        }
                        System.out.println("0x" + Integer.toString(id[i], 16));
                    }
                    System.out.println();
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean doErase() {
        try {
            sp.getOutputStream().write(new byte[]{(byte) 0x43, (byte) 0xbc});
            sp.getOutputStream().flush();
            if (ACK == read(25)) {
                sp.getOutputStream().write(new byte[]{(byte) 0xff, (byte) 0});
                sp.getOutputStream().flush();
                if (ACK == read(25)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
