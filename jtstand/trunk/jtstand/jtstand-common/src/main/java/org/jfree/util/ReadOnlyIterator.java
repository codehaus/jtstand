/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ReadOnlyIterator.java is part of JTStand.
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

package org.jfree.util;

import java.util.Iterator;

/**
 * Protects an given iterator by preventing calls to remove().
 *
 * @author Thomas Morgner
 */
public class ReadOnlyIterator implements Iterator {

    /** The base iterator which we protect. */
    private Iterator base;

    /**
     * Creates a new read-only iterator for the given iterator.
     *
     * @param it the iterator.
     */
    public ReadOnlyIterator(final Iterator it) {
        if (it == null) {
            throw new NullPointerException("Base iterator is null.");
        }
        this.base = it;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return this.base.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     * Throws NoSuchElementException when iteration has no more elements.
     * 
     * @return the next element in the iteration.
     */
    public Object next() {
        return this.base.next();
    }

    /**
     * Throws <code>UnsupportedOperationException</code>.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
