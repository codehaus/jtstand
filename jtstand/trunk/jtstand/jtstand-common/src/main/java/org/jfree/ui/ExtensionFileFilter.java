/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ExtensionFileFilter.java is part of JTStand.
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
package org.jfree.ui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A filter for JFileChooser that filters files by extension.
 *
 * @author David Gilbert
 */
public class ExtensionFileFilter extends FileFilter {

    /** A description for the file type. */
    private String description;

    /** The extension (for example, "png" for *.png files). */
    private String extension;

    /**
     * Standard constructor.
     *
     * @param description  a description of the file type;
     * @param extension  the file extension;
     */
    public ExtensionFileFilter(final String description, final String extension) {
        this.description = description;
        this.extension = extension;
    }

    /**
     * Returns true if the file ends with the specified extension.
     *
     * @param file  the file to test.
     *
     * @return A boolean that indicates whether or not the file is accepted by the filter.
     */
    public boolean accept(final File file) {

        if (file.isDirectory()) {
            return true;
        }

        final String name = file.getName().toLowerCase();
        if (name.endsWith(this.extension)) {
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * Returns the description of the filter.
     *
     * @return a description of the filter.
     */
    public String getDescription() {
        return this.description;
    }

}
