/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, SafeTagList.java is part of JTStand.
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
 
package org.jfree.xml.writer;

import java.util.HashMap;

/**
 * A container for information relating to the tags in the JFreeReport XML report files.  Some tags
 * cannot be spread across multiple lines, because it causes problems for the parser.
 *
 * @author Thomas Morgner.
 */
public class SafeTagList {
    
    /** Storage for the tag information. */
    private HashMap safeTags;

    /**
     * A tag description.
     */
    private static class SafeDescription {
        
        /** A flag indicating whether or not it is safe to put a new line after the open tag. */
        private boolean open;

        /** A flag indicating whether or not it is safe to put a new line before the close tag. */
        private boolean close;

        /**
         * Creates a new tag description.
         *
         * @param open  the 'open' flag.
         * @param close  the 'close' flag.
         */
        public SafeDescription(final boolean open, final boolean close) {
            this.open = open;
            this.close = close;
        }

        /**
         * Returns the 'open' flag.
         *
         * @return <code>true</code> or <code>false</code>.
         */
        public boolean isOpen() {
            return this.open;
        }

        /**
         * Returns the 'close' flag.
         *
         * @return <code>true</code> or <code>false</code>.
         */
        public boolean isClose() {
            return this.close;
        }
    }

    /**
     * Creates a new list.
     */
    public SafeTagList() {
        this.safeTags = new HashMap();
    }

    /**
     * Adds a tag with both the 'open' and 'close' flags set to <code>true</code>.
     *
     * @param tag  the tag name.
     */
    public void add (final String tag) {
        this.safeTags.put(tag, new SafeDescription(true, true));
    }

    /**
     * Adds a tag.
     *
     * @param tag  the tag name.
     * @param open  the 'open' flag.
     * @param closed  the 'close' flag.
     */
    public void add (final String tag, final boolean open, final boolean closed) {
        this.safeTags.put(tag, new SafeDescription(open, closed));
    }

    /**
     * Returns <code>true</code> if it is safe to start a new line
     * immediately after an open tag.
     *
     * @param tag  the tag name.
     *
     * @return A boolean.
     */
    public boolean isSafeForOpen (final String tag) {
        final SafeDescription sd = (SafeDescription) this.safeTags.get(tag);
        if (sd == null) {
            return false;
        }
        return sd.isOpen();
    }

    /**
     * Returns <code>true</code> if it is safe to start a new
     * line immediately after a close tag.
     *
     * @param tag  the tag name.
     *
     * @return A boolean.
     */
    public boolean isSafeForClose (final String tag) {
        final SafeDescription sd = (SafeDescription) this.safeTags.get(tag);
        if (sd == null) {
            return false;
        }
        return sd.isClose();
    }
    
}
