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
