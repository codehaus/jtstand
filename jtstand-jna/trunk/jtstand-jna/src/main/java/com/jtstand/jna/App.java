/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, App.java is part of JTStand.
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

/** Simple example of JNA direct mapping.
 * Fewer automatic type conversions are supported with this method
 */
public class App {

    static {
        Native.register(Platform.isWindows() ? "msvcrt" : "m");
    }

    public static native double cos(double x);

    public static native double sin(double x);

    public static void main(String[] args) {
        System.out.println("cos(0)=" + cos(0));
        System.out.println("sin(0)=" + sin(0));
    }
}
