/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ClassFactoryCollector.java is part of JTStand.
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

package org.jfree.xml.factory.objects;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.util.Configuration;

/**
 * A class factory collector.
 *
 * @author Thomas Morgner
 */
public class ClassFactoryCollector extends ClassFactoryImpl {

    /** Storage for the class factories. */
    private ArrayList factories;

    /**
     * Creates a new class factory collector.
     */
    public ClassFactoryCollector() {
        this.factories = new ArrayList();
    }

    /**
     * Adds a class factory to the collection.
     *
     * @param factory  the factory.
     */
    public void addFactory(final ClassFactory factory) {
        this.factories.add(factory);
        if (getConfig() != null) {
            factory.configure(getConfig());
        }
    }

    /**
     * Returns an iterator the provides access to all the factories in the collection.
     *
     * @return The iterator.
     */
    public Iterator getFactories() {
        return this.factories.iterator();
    }

    /**
     * Returns an object description for a class.
     *
     * @param c  the class.
     *
     * @return The object description.
     */
    public ObjectDescription getDescriptionForClass(final Class c) {
        for (int i = 0; i < this.factories.size(); i++) {
            final ClassFactory f = (ClassFactory) this.factories.get(i);
            final ObjectDescription od = f.getDescriptionForClass(c);
            if (od != null) {
                return od;
            }
        }
        return super.getDescriptionForClass(c);
    }

    /**
     * Returns an object-description for the super class of a class.
     *
     * @param d  the class.
     * @param knownSuperClass the last known super class or null.
     * @return The object description.
     */
    public ObjectDescription getSuperClassObjectDescription
        (final Class d, ObjectDescription knownSuperClass) {
        for (int i = 0; i < this.factories.size(); i++) {
            final ClassFactory f = (ClassFactory) this.factories.get(i);
            final ObjectDescription od = f.getSuperClassObjectDescription(d, knownSuperClass);
            if (od != null) {
                if (knownSuperClass == null) {
                    knownSuperClass = od;
                }
                else {
                    if (getComparator().isComparable(knownSuperClass.getObjectClass(),
                        od.getObjectClass())) {
                        if (getComparator().compare(knownSuperClass.getObjectClass(),
                            od.getObjectClass()) < 0) {
                            knownSuperClass = od;
                        }
                    }
                }
            }
        }
        return super.getSuperClassObjectDescription(d, knownSuperClass);
    }

    /**
     * Returns an iterator that provices access to the registered classes.
     *
     * @return The iterator.
     */
    public Iterator getRegisteredClasses() {
        final ArrayList list = new ArrayList();
        for (int i = 0; i < this.factories.size(); i++) {
            final ClassFactory f = (ClassFactory) this.factories.get(i);
            final Iterator iterator = f.getRegisteredClasses();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        return list.iterator();
    }

    /**
     * Configures this factory. The configuration contains several keys and
     * their defined values. The given reference to the configuration object
     * will remain valid until the report parsing or writing ends.
     * <p>
     * The configuration contents may change during the reporting.
     *
     * @param config the configuration, never null
     */
    public void configure(final Configuration config) {
        if (getConfig() != null) {
            // already configured ...
            return;
        }
        super.configure(config);

        final Iterator it = this.factories.iterator();
        while (it.hasNext()) {
            final ClassFactory od = (ClassFactory) it.next();
            od.configure(config);
        }
    }

    /**
     * Tests for equality.
     * 
     * @param o  the object to test.
     * 
     * @return A boolean.
     */
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassFactoryCollector)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final ClassFactoryCollector classFactoryCollector = (ClassFactoryCollector) o;

        if (!this.factories.equals(classFactoryCollector.factories)) {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code for the object.
     * 
     * @return The hash code.
     */
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + this.factories.hashCode();
        return result;
    }
}
