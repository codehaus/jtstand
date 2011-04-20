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
import java.util.ArrayList;
import java.util.List;
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
    Thread thread;
    Boolean success = null;

    public ArmUsartProgrammer(String serialPortString) throws UnsupportedCommOperationException, PortInUseException, NoSuchPortException {
        this.serialPortString = serialPortString;
        open();
        sp.setRTS(false); //activate System Memory Boot Mode
    }

    private int read(long time) throws IOException {
        long timeout = time + System.currentTimeMillis();
        while (System.currentTimeMillis() < timeout) {
            if (sp.getInputStream().available() > 0) {
                return sp.getInputStream().read();
            }
        }
        throw new IOException("Timeout");
    }

    private void flushInput() throws IOException {
        int a = sp.getInputStream().available();
        while (a > 0) {
            sp.getInputStream().skip(a);
            a = sp.getInputStream().available();
        }
    }

    public void check() {
        if (!isPresent()) {
            throw new IllegalStateException("Check failed");
        }
    }

    private boolean isPresent() {
        int inch;

        try {
            flushInput();
            sp.getOutputStream().write(new byte[]{0x7f}, 0, 1);
            sp.getOutputStream().flush();
            inch = read(50);
            if (0x79 == inch) {
                readId();
                return true;
            }
        } catch (IOException e) {
            //System.out.println(e);
        }
        System.out.println("Bootloader on " + serialPortString + " is not present");
        return false;
    }

    public static boolean arePresent(List<ArmUsartProgrammer> programmers) {
        for (ArmUsartProgrammer programmer : programmers) {
            if (!programmer.isPresent()) {
                return false;
            }
        }
        return true;
    }

    public static void reset() {
        System.out.println("Reset the device and hit <Enter>!");
        System.console().readLine();
    }

    public void close() {
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

    private void write5(int num) throws IOException {
        byte[] retval = new byte[5];
        retval[0] = (byte) (num >> 24);
        retval[1] = (byte) (num >> 16);
        retval[2] = (byte) (num >> 8);
        retval[3] = (byte) num;
        retval[4] = (byte) (retval[0] ^ retval[1] ^ retval[2] ^ retval[3]);
        sp.getOutputStream().write(retval);
        sp.getOutputStream().flush();
    }

    private void write1(int num) throws IOException {
        byte[] retval = new byte[1];
        retval[0] = (byte) num;
        sp.getOutputStream().write(retval);
        sp.getOutputStream().flush();
    }

    public boolean eraseWriteVerify(IntelHex hexFile) {
        return erase() && write(hexFile) && verify(hexFile);
    }

    public void writeOffline(final IntelHex hexFile) throws InterruptedException {
        if (thread != null) {
            thread.join();
        }
        thread = new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        write(hexFile);
                    }
                },
                serialPortString + " WRITE");
        thread.start();
    }

    public boolean write(IntelHex hexFile) {
        System.out.println(serialPortString + " WRITE...");
        for (Memory mem : hexFile.memorySegments) {
            if (!write(mem)) {
                System.out.println(serialPortString + " WRITE failed.");
                success = false;
                return false;
            }
        }
        System.out.println(serialPortString + " WRITE done.");
        success = true;
        return true;
    }

    public boolean verify(IntelHex hexFile) {
        for (Memory mem : hexFile.memorySegments) {
            if (!verify(mem)) {
                return false;
            }
        }
        return true;
    }

    private boolean write(Memory mem) {
        int remainingLength = mem.data.length;
        int address = mem.address;
        System.out.println(serialPortString + " Writing memory segment at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(remainingLength));
        for (int offset = 0; remainingLength > 0;) {
            int length = (remainingLength > 256) ? 256 : remainingLength;
            if (!write(address, mem.data, offset, length)) {
                return false;
            }
            offset += length;
            address += length;
            remainingLength -= length;
        }
        return true;
    }

    private boolean verify(Memory mem) {
        int remainingLength = mem.data.length;
        int address = mem.address;
        System.out.println(serialPortString + " Verify memory segment at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(remainingLength));
        for (int offset = 0; remainingLength > 0;) {
            int length = (remainingLength > 256) ? 256 : remainingLength;
            if (!verify(address, mem.data, offset, length)) {
                return false;
            }
            offset += length;
            address += length;
            remainingLength -= length;
        }
        return true;
    }

    private boolean write(int address, byte[] data, int offset, int length) {
        try {
            if (length < 1 || length > 256) {
                throw new IllegalArgumentException("Length should be between 1 and 256 bytes");
            }
            System.out.println(serialPortString + " Writing memory at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(length));
            sp.getOutputStream().write(new byte[]{(byte) 0x31, (byte) 0xce});
            sp.getOutputStream().flush();
            if (ACK == read(100)) {
                write5(address);
                if (ACK == read(100)) {
                    write1(length - 1);
                    sp.getOutputStream().write(data, offset, length);
                    sp.getOutputStream().flush();
                    int checksum = length - 1;
                    for (int i = 0; i < length; i++) {
                        checksum ^= data[offset + i];
                    }
                    write1(checksum);
                    if (ACK == read(500)) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean verify(int address, byte[] data, int offset, int length) {
        try {
            if (length < 1 || length > 256) {
                throw new IllegalArgumentException("Length should be between 1 and 256 bytes");
            }
            System.out.println(serialPortString + " Verify memory at address:0x" + Integer.toString(address, 16) + " size:" + Integer.toString(length));
            sp.getOutputStream().write(new byte[]{(byte) 0x11, (byte) 0xee});
            sp.getOutputStream().flush();
            if (ACK == read(100)) {
                //System.out.println("address..");
                write5(address);
                if (ACK == read(100)) {
                    //System.out.println("length..");
                    write1(length - 1);
                    write1((byte) ((length - 1) ^ 255));
                    if (ACK == read(100)) {
                        for (int i = 0; i < length; i++) {
                            //System.out.println("read..");
                            byte data_read = (byte) read(100);
                            if (data_read != data[offset + i]) {
                                System.out.println("Mismatch at address:0x" + Integer.toString(address + i, 16) + " expected:0x" + Integer.toString(0xff & data[offset + i], 16) + " received:0x" + Integer.toString(0xff & data_read, 16));
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

    public boolean readId() {
        try {
            sp.getOutputStream().write(new byte[]{(byte) 0, (byte) 0xff});
            sp.getOutputStream().flush();
            if (ACK == read(100)) {
                byte nr = (byte) read(100);
                //System.out.println("Number of bytes:" + nr);
                version = (byte) read(100);
                System.out.println(serialPortString + " Booloader version: 0x" + Integer.toString(0xff & version, 16));
                id = new byte[nr];
                for (int i = 0; i < nr; i++) {
                    id[i] = (byte) read(100);
                }
                if (ACK == read(100)) {
                    String msg = serialPortString + " ID:";
                    for (int i = 0; i < nr; i++) {
                        if (i > 0) {
                            msg += ",";
                        }
                        msg += "0x" + Integer.toString(0xff & id[i], 16);
                    }
                    System.out.println(msg);
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean erase() {
        System.out.println(serialPortString + " ERASE...");
        try {
            sp.getOutputStream().write(new byte[]{(byte) 0x43, (byte) 0xbc});
            sp.getOutputStream().flush();
            if (ACK == read(100)) {
                sp.getOutputStream().write(new byte[]{(byte) 0xff, (byte) 0});
                sp.getOutputStream().flush();
                if (ACK == read(1000)) {
                    System.out.println(serialPortString + " ERASE done.");
                    success = true;
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        success = false;
        return false;
    }

    public boolean erasePages(int pageFirst, int pageLast) {
        System.out.println(serialPortString + " ERASE " + pageFirst + " " + pageLast + "...");
        int numberOfPages = pageLast - pageFirst;
        byte[] pages = new byte[numberOfPages + 3];
        pages[0] = (byte) (numberOfPages);
        int pos = 1;
        int checksum = numberOfPages;
        for (int page = pageFirst; page <= pageLast; page++) {
            pages[pos] = (byte) page;
            pos++;
            checksum ^= page;
        }
        pages[pos] = (byte) checksum;
        try {
            sp.getOutputStream().write(new byte[]{(byte) 0x43, (byte) 0xbc});
            sp.getOutputStream().flush();
            if (ACK == read(100)) {
                sp.getOutputStream().write(pages);
                sp.getOutputStream().flush();
                if (ACK == read(10000)) {
                    System.out.println(serialPortString + " ERASE done.");
                    success = true;
                    return true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
        }
        success = false;
        return false;
    }

    public static void printUsage() {
        System.out.println("Usage:");
        System.out.println("ERASEPAGES firstPage lastPage portlist");
        System.out.println("or");
        System.out.println("ERASE portlist");
        System.out.println("or");
        System.out.println("WRITE filename portlist");
        System.out.println("where");
        System.out.println("portlist for example: COM1 COM2 COM3 COM4");
    }

    public static List<ArmUsartProgrammer> prepareProgrammers(String[] args, int startIndex) throws UnsupportedCommOperationException, PortInUseException, NoSuchPortException {
        List<ArmUsartProgrammer> programmers = new ArrayList<ArmUsartProgrammer>();
        for (int i = startIndex; i < args.length; i++) {
            programmers.add(new ArmUsartProgrammer(args[i]));
        }
        if (!arePresent(programmers)) {
            reset();
        }
        if (!arePresent(programmers)) {
            throw new IllegalStateException("Some Bootloaders are not present even after reset");
        }
        return programmers;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
        } else if ("ERASE".equals(args[0].toUpperCase())) {
            try {
                List<ArmUsartProgrammer> programmers = prepareProgrammers(args, 1);
                for (ArmUsartProgrammer p : programmers) {
                    if (!p.erase()) {
                        throw new Exception("Erase on " + p.serialPortString + " could not succeed");
                    }
                }
                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        } else if ("ERASEPAGES".equals(args[0].toUpperCase())) {
            try {
                List<ArmUsartProgrammer> programmers = prepareProgrammers(args, 3);
                int firstPage = Integer.parseInt(args[1]);
                int lastPage = Integer.parseInt(args[2]);
                for (ArmUsartProgrammer p : programmers) {
                    if (!p.erasePages(firstPage, lastPage)) {
                        throw new Exception("Erase pages on " + p.serialPortString + " could not succeed");
                    }
                }
                System.exit(0);
            } catch (Exception ex) {
                Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        } else if ("WRITE".equals(args[0].toUpperCase())) {
            if (args.length < 3) {
                printUsage();
            } else {
                try {
                    List<ArmUsartProgrammer> programmers = prepareProgrammers(args, 2);
                    final IntelHex hexFile = new IntelHex(args[1]);
                    for (ArmUsartProgrammer p : programmers) {
                        p.writeOffline(hexFile);
                    }
                    for (ArmUsartProgrammer p : programmers) {
                        p.thread.join();
                        if (!p.success) {
                            throw new Exception("Write on " + p.serialPortString + " could not succeed");
                        }
                    }
                    System.exit(0);
                } catch (Exception ex) {
                    Logger.getLogger(ArmUsartProgrammer.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
            }
        } else {
            printUsage();
        }
    }
}
