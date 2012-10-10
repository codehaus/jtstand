/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MultiplexMappingInfo.java is part of JTStand.
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

import java.util.Arrays;

/**
 * Defines the multiplex entries for a certain base class. Multiplexers are
 * used to select a specific handler if more than one class will match the
 * property type.
 * <p>
 * Multiplexers override automatic mappings and can be redefined using manual
 * mappings.
 */
public class MultiplexMappingInfo {
    
    /** The base class. */
    private Class baseClass;
    
    /** The type attribute. */
    private String typeAttribute;
    
    /** The child classes. */
    private TypeInfo[] childClasses;
    
    /** The comments. */
    private Comments comments;
    
    /** The source. */
    private String source;

    /**
     * Creates a new instance for the specified class.
     * 
     * @param baseClass  the base class.
     */
    public MultiplexMappingInfo(final Class baseClass) {
        this(baseClass, "type");
    }

    /**
     * Creates a new instance for the specified class.
     * 
     * @param baseClass  the base class (<code>null</code> not permitted).
     * @param typeAttribute  the type attribute (<code>null</code> not permitted).
     */
    public MultiplexMappingInfo(final Class baseClass, final String typeAttribute) {
        if (baseClass == null) {
            throw new NullPointerException("BaseClass");
        }
        if (typeAttribute == null) {
            throw new NullPointerException("TypeAttribute");
        }
        this.baseClass = baseClass;
        this.typeAttribute = typeAttribute;
    }

    /**
     * Returns the base class.
     * 
     * @return The base class.
     */
    public Class getBaseClass() {
        return this.baseClass;
    }

    /**
     * Returns the type attribute.
     * 
     * @return The type attribute.
     */
    public String getTypeAttribute() {
        return this.typeAttribute;
    }

    /**
     * Returns the child classes.
     * 
     * @return The child classes.
     */
    public TypeInfo[] getChildClasses() {
        return this.childClasses;
    }

    /**
     * Sets the child classes.
     * 
     * @param childClasses  the child classes.
     */
    public void setChildClasses(final TypeInfo[] childClasses) {
        this.childClasses = childClasses;
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

    /**
     * Returns the source.
     * 
     * @return The source.
     */
    public String getSource() {
        return this.source;
    }

    /**
     * Sets the source.
     * 
     * @param source  the source.
     */
    public void setSource(final String source) {
        this.source = source;
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
        if (!(o instanceof MultiplexMappingInfo)) {
            return false;
        }

        final MultiplexMappingInfo multiplexMappingInfo = (MultiplexMappingInfo) o;

        if (!this.baseClass.equals(multiplexMappingInfo.baseClass)) {
            return false;
        }
        if (!Arrays.equals(this.childClasses, multiplexMappingInfo.childClasses)) {
            return false;
        }
        if (!this.typeAttribute.equals(multiplexMappingInfo.typeAttribute)) {
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
        result = this.baseClass.hashCode();
        result = 29 * result + this.typeAttribute.hashCode();
        return result;
    }

}
