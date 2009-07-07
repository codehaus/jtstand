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
import java.util.ArrayList;
import java.util.List;

/** Simple example of JNA interface mapping and usage. */
public class HelloNI845x {

    /* This is the standard, stable way of mapping, which supports extensive
     * customization and mapping of Java to native types.
     */
    public interface Ni845xLibrary extends Library {

        Ni845xLibrary INSTANCE = (Ni845xLibrary) Native.loadLibrary(
                Platform.isWindows()
                ? "ni845x" : "????",
                Ni845xLibrary.class);

        /*
         * General
         */
        int ni845xOpen(
                String resourceName,
                IntByReference deviceHandle);

        int ni845xClose(
                int deviceHandle);

        void ni845xStatusToString(
                int StatusCode,
                int MaxSize,
                byte[] statusString);

        int ni845xFindDevice(
                byte[] firstDevice,
                IntByReference findDeviceHandle,
                IntByReference numberFound);

        int ni845xFindDeviceNext(
                int findDeviceHandle,
                byte[] nextDevice);

        int ni845xCloseFindDeviceHandle(
                int FindDeviceHandle);

        /*
         * Basic
         */
        int ni845xI2cRead(
                int deviceHandle,
                int configurationHandle,
                int numBytesToRead,
                IntByReference readSize,
                byte[] readData);

        int ni845xI2cWrite(
                int deviceHandle,
                int configurationHandle,
                int writeSize,
                byte[] writeData);

        int ni845xI2cWriteRead(
                int DeviceHandle,
                int ConfigurationHandle,
                int WriteSize,
                byte[] pWriteData,
                int NumBytesToRead,
                IntByReference pReadSize,
                byte[] pReadData);
    }

    public static String getStatusString(int status) {
        byte[] statusString = new byte[1024];
        Ni845xLibrary.INSTANCE.ni845xStatusToString(status, 1024, statusString);
        return Integer.toString(status) + ": " + Native.toString(statusString);
    }

    public static List<String> getDevicesList() {
        int status;
        List<String> list = new ArrayList<String>();
        byte[] dev = new byte[256];

        IntByReference findDeviceHandle = new IntByReference();
        IntByReference numberFound = new IntByReference();
        status = Ni845xLibrary.INSTANCE.ni845xFindDevice(dev, findDeviceHandle, numberFound);
        if (status == 0) {
            list.add(Native.toString(dev));
            for (int i = 1; i < numberFound.getValue(); i++) {
                status = Ni845xLibrary.INSTANCE.ni845xFindDeviceNext(findDeviceHandle.getValue(), dev);
                if (status != 0) {
                    /* Not checking error condition on close this time */
                    Ni845xLibrary.INSTANCE.ni845xCloseFindDeviceHandle(findDeviceHandle.getValue());
                    throw new IllegalStateException("ni845xFindDeviceNext ERROR: " + getStatusString(status));
                }
                list.add(Native.toString(dev));
            }
            /* But we do check error condition on close this time */
            status = Ni845xLibrary.INSTANCE.ni845xCloseFindDeviceHandle(findDeviceHandle.getValue());
            if (status != 0) {
                throw new IllegalStateException("ni845xCloseFindDeviceHandle ERROR: " + getStatusString(status));
            }
            return list;
        } else {
            /* Not checking error condition on close this time */
            Ni845xLibrary.INSTANCE.ni845xCloseFindDeviceHandle(findDeviceHandle.getValue());
            if (status != -301701) {
                throw new IllegalStateException("ni845xFindDevice ERROR: " + getStatusString(status));
            }
            return list;
        }
    }

    public static void main(String[] args) {
        List<String> devicesList = getDevicesList();
        if (devicesList.size() == 0) {
            System.out.println("There are no devices found");
        } else {
            System.out.println("Devices list:");
            for (String dev : devicesList) {
                System.out.println(dev);
            }
            System.out.println("[]");
        }
    }
}
