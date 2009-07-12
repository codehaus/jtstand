/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, HelloVisa.java is part of JTStand.
 *
 * JTStand is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JTStand is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand.jna;

/**
 *
 * @author albert_kurucz
 */
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.IntByReference;

/** Simple example of JNA interface mapping and usage. */
public class HelloVisa {

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.
    public interface NiVisaLibrary extends Library {

        NiVisaLibrary INSTANCE = (NiVisaLibrary) Native.loadLibrary((Platform.isWindows() ? "visa32" : "????"),
                NiVisaLibrary.class);

        //void printf(String format, Object... args);
        int viOpenDefaultRM(IntByReference sesn);

        int viOpen(int sesn, String rsrcName, int accessMode, int openTimeout, IntByReference vi);

        int viGetAttribute(int sesn, int attribute, byte[] attrState);
    }

    public static void main(String[] args) {
        int status;
        System.out.println("viOpenDefaultRM...");
        IntByReference sesn = new IntByReference();
        status = NiVisaLibrary.INSTANCE.viOpenDefaultRM(sesn);
        System.out.println("Status: " + status);

        System.out.println("viOpen...");
        IntByReference vi = new IntByReference();
        status = NiVisaLibrary.INSTANCE.viOpen(sesn.getValue(), "COM1", 0, 0, vi);
        System.out.println("Status: " + status);

        System.out.println("viGetAttribute...");
        byte[] retval = new byte[256];
        status = NiVisaLibrary.INSTANCE.viGetAttribute(vi.getValue(), 0xBFFF0001, retval);
        System.out.println("Status: " + status);
        System.out.println("Attribute: " + Native.toString(retval));

    }
}
