/*
 *  Copyright 2009 albert_kurucz.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
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
