/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand.intelhex;

import java.util.Formatter;

/**
 *
 * @author albert_kurucz
 */
public class Memory {

    public int address;
    public byte[] data;

    public Memory(int address, byte[] data) {
        this.address = address;
        this.data = data;
    }

    public boolean canAppendMemory(Memory append) {
        return append.address == (address + data.length);
    }

    public void appendMemory(Memory append) {
        if (!canAppendMemory(append)) {
            throw new IllegalArgumentException("Cannot append memory");
        }
        byte[] newdata = new byte[data.length + append.data.length];
        System.arraycopy(data, 0, newdata, 0, data.length);
        System.arraycopy(append.data, 0, newdata, data.length, append.data.length);
        data = newdata;
    }

    public void writeMemoryHex(Formatter f) {
        int i = 0;
        while (i < data.length) {
            f.format(":");
            byte lineLength = (byte) Math.min(data.length - i, 16);
            byte sum = lineLength;
            f.format("%02X", lineLength);
            int lineAddress = (i + address) & 0xffff;
            f.format("%04X", lineAddress);
            f.format("00");//record type is data, there is no need for adding 0 to check sum
            sum += (byte) lineAddress;
            sum += (byte) (lineAddress >> 8);
            for (int j = 0; j < lineLength; j++) {
                f.format("%02X", data[i]);
                sum += data[i];
                i++;
            }
            sum = (byte) (0 - sum);
            f.format("%02X\n", sum);
        }
    }

    public static void main(String[] args) {
        Memory m = new Memory(0x8000000, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
        m.writeMemoryHex(new Formatter(System.out));
    }
}
