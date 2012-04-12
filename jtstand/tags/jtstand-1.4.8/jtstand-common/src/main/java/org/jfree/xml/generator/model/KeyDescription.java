/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, KeyDescription.java is part of JTStand.
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

/**
 * A key description.
 */
public class KeyDescription {
    
    /** The parameters. */
    private TypeInfo[] parameters;
    
    /** The comments. */
    private Comments comments;

    /**
     * Creates a new key description instance.
     * 
     * @param parameters  the parameters.
     */
    public KeyDescription(final TypeInfo[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the parameters.
     * 
     * @return The parameters.
     */
    public TypeInfo[] getParameters() {
        return this.parameters;
    }

    /**
     * Returns the comments.
     * 
     * @return The comments.
     */
    public Comments getComments() {
        return this.comments;
    }

    /**
     * Sets the comments.
     * 
     * @param comments  the comments.
     */
    public void setComments(final Comments comments) {
        this.comments = comments;
    }
}
