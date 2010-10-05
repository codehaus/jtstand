/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ObjectList.java is part of JTStand.
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


/**
 * A list of objects that can grow as required.
 * <p>
 * When cloning, the objects in the list are NOT cloned, only the references. 
 *
 * @author Thomas Morgner
 */
public class ObjectList extends AbstractObjectList {
    
    /**
     * Default constructor.
     */
    public ObjectList() {
    }
    
    /**
     * Creates a new list.
     * 
     * @param initialCapacity  the initial capacity.
     */
    public ObjectList(final int initialCapacity) {
        super(initialCapacity);
    }
    
    // NOTE:  the methods below look redundant, but their purpose is to provide public
    // access to the the get(), set() and indexOf() methods defined in the 
    // AbstractObjectList class, for this class only.  For other classes 
    // (e.g. PaintList, ShapeList etc) we don't want the Object versions of these 
    // methods to be visible in the public API.
    
    /**          
     * Returns the object at the specified index, if there is one, or <code>null</code>.         
     *   
     * @param index  the object index.   
     *   
     * @return The object or <code>null</code>.          
     */          
    public Object get(final int index) {         
        return super.get(index);         
    }    
         
    /**          
     * Sets an object reference (overwriting any existing object).       
     *   
     * @param index  the object index.   
     * @param object  the object (<code>null</code> permitted).          
     */          
    public void set(final int index, final Object object) {      
        super.set(index, object);        
    }    
         
    /**          
     * Returns the index of the specified object, or -1 if the object is not in the list.        
     *   
     * @param object  the object.        
     *   
     * @return The index or -1.          
     */          
    public int indexOf(final Object object) {    
        return super.indexOf(object);    
    }    
         
}
