/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, HelloDirectVisa.java is part of JTStand.
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
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jtstand.jna;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

/** Simple example of JNA direct mapping.
 * Fewer automatic type conversions are supported with this method
 */
public class HelloDirectVisa {

    static {
        Native.register(Platform.isWindows() ? "visa32" : "????");
    }

    public static native int viOpenDefaultRM(Pointer sesn);

    public static native int viOpen(int sesn, Pointer rsrcName, int accessMode, int openTimeout, Pointer vi);

    public static void main(String[] args) {
        int status;
        System.out.println("viOpenDefaultRM...");
        IntByReference sesn = new IntByReference();
        status = viOpenDefaultRM(sesn.getPointer());
        System.out.println("Status:" + status);

        IntByReference vi = new IntByReference();
        Memory m = new Memory(10);
        m.setString(0, "COM1");
        System.out.println("viOpen...");
        status = viOpen(sesn.getValue(), m.share(0, 5), 0, 0, vi.getPointer());
        System.out.println("Status:" + status);
    }
}
