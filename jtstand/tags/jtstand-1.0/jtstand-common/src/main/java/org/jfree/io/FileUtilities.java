/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FileUtilities.java is part of JTStand.
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

package org.jfree.io;

import java.io.File;
import java.util.StringTokenizer;

/**
 * A class containing useful utility methods relating to files.
 *
 * @author David Gilbert
 */
public class FileUtilities {

    /**
     * To prevent unnecessary instantiation.
     */
    private FileUtilities() {
    }

    /**
     * Returns a reference to a file with the specified name that is located
     * somewhere on the classpath.  The code for this method is an adaptation
     * of code supplied by Dave Postill.
     *
     * @param name  the filename.
     *
     * @return a reference to a file or <code>null</code> if no file could be found.
     */
    public static File findFileOnClassPath(final String name) {

        final String classpath = System.getProperty("java.class.path");
        final String pathSeparator = System.getProperty("path.separator");

        final StringTokenizer tokenizer = new StringTokenizer(classpath, pathSeparator);

        while (tokenizer.hasMoreTokens()) {
            final String pathElement = tokenizer.nextToken();

            final File directoryOrJar = new File(pathElement);
            final File absoluteDirectoryOrJar = directoryOrJar.getAbsoluteFile();

            if (absoluteDirectoryOrJar.isFile()) {
                final File target = new File(absoluteDirectoryOrJar.getParent(), name);
                if (target.exists()) {
                    return target;
                }
            }
            else {
                final File target = new File(directoryOrJar, name);
                if (target.exists()) {
                    return target;
                }
            }

        }
        return null;

    }

}
