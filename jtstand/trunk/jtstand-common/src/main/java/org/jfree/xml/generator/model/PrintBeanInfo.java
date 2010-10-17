/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PrintBeanInfo.java is part of JTStand.
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
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jfree.xml.generator.model;

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * A utility class for printing information about a class.
 */
public class PrintBeanInfo {

  private PrintBeanInfo ()
  {
  }

    /**
     * Prints the information for a class.
     * 
     * @param c  the class.
     */
    public static void print (final Class c) {
        try {
            System.out.println("Class: " + c.getName());
            System.out.println(
                "========================================================================"
            );
            final BeanInfo bi = Introspector.getBeanInfo(c, c.getSuperclass());
            final PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                System.out.println ("Property: " + pd[i].getDisplayName());
                System.out.println(
                    "---------------------------------------------------------------------"
                );
                System.out.println (" ( " + pd[i].getShortDescription() + ")");
                if (pd[i] instanceof IndexedPropertyDescriptor) {
                    final IndexedPropertyDescriptor id = (IndexedPropertyDescriptor) pd[i];
                    System.out.println ("  - idx-type   : " + id.getIndexedPropertyType());
                    System.out.println ("  - idx-read   : " + id.getIndexedReadMethod());
                    System.out.println ("  - idx-write  : " + id.getIndexedWriteMethod());
                }
                else {
                    System.out.println ("  - type       : " + pd[i].getPropertyType());
                    System.out.println ("  - read       : " + pd[i].getReadMethod());
                    System.out.println ("  - write      : " + pd[i].getWriteMethod());
                }
                System.out.println ("  - bound      : " + pd[i].isBound());
                System.out.println ("  - constrained: " + pd[i].isConstrained());
            }
        }
        catch (IntrospectionException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Entry point for this utility application.
     * 
     * @param args  the class names.
     * 
     * @throws Exception if there is a problem.
     */
    public static void main(final String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            print(Class.forName(args[i]));
        }
    }

}
