/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Comments.java is part of JTStand.
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
 * A set of comments for a model.
 */
public class Comments {
    
    /** Open tag comments. */
    private String[] openTagComment;
    
    /** Close tag comments. */
    private String[] closeTagComment;
 
    /**
     * Creates a new set of comments.
     * 
     * @param openTagComment  the open tag comment.
     * @param closeTagComment  the close tag comment.
     */
    public Comments(final String[] openTagComment, final String[] closeTagComment) {
        this.openTagComment = openTagComment;
        this.closeTagComment = closeTagComment;
    }

    /**
     * Returns the open tag comments.
     * 
     * @return The open tag comments.
     */
    public String[] getOpenTagComment() {
        return this.openTagComment;
    }

    /**
     * Returns the close tag comments.
     * 
     * @return The close tag comments.
     */
    public String[] getCloseTagComment() {
        return this.closeTagComment;
    }

    /**
     * Returns a string representation of the set of comments.
     * 
     * @return A string.
     */
    public String toString() {
        final StringBuffer b = new StringBuffer();
        b.append ("Comments:={open=");
        if (this.openTagComment == null) {
            b.append("null");
        }
        else {
            b.append("{");
            for (int i = 0; i < this.openTagComment.length; i++) {
                b.append("[");
                b.append(this.openTagComment[i]);
                b.append("]");
            }
            b.append("}");
        }
        b.append(", close=");
        if (this.closeTagComment == null) {
            b.append("null");
        }
        else {
            b.append("{");
            for (int i = 0; i < this.closeTagComment.length; i++) {
                b.append("[");
                b.append(this.closeTagComment[i]);
                b.append("]");
            }
            b.append("}");
        }
        b.append("}");
        return b.toString();
    }
    
}
