/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TypeInfo.java is part of JTStand.
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
 * Retains information about a type.
 */
public class TypeInfo {
    
    /** The type name. */
    private String name;
    
    /** The class. */
    private Class type;
    
    /** A flag indicating whether or not the type can take a null value. */
    private boolean nullable;
    
    /** ??. */
    private boolean constrained;
    
    /** A description. */
    private String description;
    
    /** Comments. */
    private Comments comments;

    /**
     * Creates a new instance.
     * 
     * @param name  the type name (<code>null</code> not permitted).
     * @param type  the class.
     */
    public TypeInfo(final String name, final Class type) {
        if (name == null) {
            throw new NullPointerException("Name");
        }
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the class.
     * 
     * @return The class.
     */
    public Class getType() {
        return this.type;
    }

    /**
     * Returns the nullable status.
     * 
     * @return A boolean.
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * Sets the nullable flag.
     * 
     * @param nullable  the flag.
     */
    public void setNullable(final boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * Returns <code>true</code> if the type is constrained, and <code>false</code> otherwise.
     * 
     * @return A boolean.
     */
    public boolean isConstrained() {
        return this.constrained;
    }

    /**
     * Sets the flag that indicates whether or not the type is constrained.
     * 
     * @param constrained  the flag.
     */
    public void setConstrained(final boolean constrained) {
        this.constrained = constrained;
    }

    /**
     * Returns the type description.
     * 
     * @return The type description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the type description.
     * 
     * @param description  the description.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the type name.
     * 
     * @return The type name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the comments for this type info.
     * 
     * @return The comments.
     */
    public Comments getComments() {
        return this.comments;
    }

    /**
     * Sets the comments for this type info.
     * 
     * @param comments  the comments.
     */
    public void setComments(final Comments comments) {
        this.comments = comments;
    }
    /**
     * Tests this object for equality with another object.
     * 
     * @param o  the other object.
     * 
     * @return A boolean.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeInfo)) {
            return false;
        }

        final TypeInfo typeInfo = (TypeInfo) o;

        if (!this.name.equals(typeInfo.name)) {
            return false;
        }
        if (!this.type.equals(typeInfo.type)) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code for this object.
     * 
     * @return A hash code.
     */
    public int hashCode() {
        int result;
        result = this.name.hashCode();
        result = 29 * result + this.type.hashCode();
        result = 29 * result + (this.nullable ? 1 : 0);
        result = 29 * result + (this.constrained ? 1 : 0);
        result = 29 * result + (this.description != null ? this.description.hashCode() : 0);
        return result;
    }

}