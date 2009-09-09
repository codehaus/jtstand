/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, MappingModel.java is part of JTStand.
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

import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.util.Log;

/**
 * A mapping model.
 */
public class MappingModel {
    
    /** Mapping infos. */
    private HashMap mappingInfos;
    
    /** The manual mappings. */
    private ArrayList manualMappings;
    
    /** The multiplex mappings. */
    private ArrayList multiplexMappings;

    /**
     * Creates a new instance.
     */
    public MappingModel() {
        this.mappingInfos = new HashMap();
        this.manualMappings = new ArrayList();
        this.multiplexMappings = new ArrayList();
    }

    /**
     * Returns the multiplex mapping info.
     * 
     * @return The multiplex mapping info.
     */
    public MultiplexMappingInfo[] getMultiplexMapping() {
        return (MultiplexMappingInfo[]) this.multiplexMappings.toArray(
            new MultiplexMappingInfo[this.multiplexMappings.size()]
        );
    }

    /**
     * Returns the manual mapping info.
     * 
     * @return The manual mapping info.
     */
    public ManualMappingInfo[] getManualMapping() {
        return (ManualMappingInfo[]) this.manualMappings.toArray(
            new ManualMappingInfo[this.manualMappings.size()]
        );
    }

    /**
     * Adds a manual mapping.
     * 
     * @param mappingInfo  the mapping.
     */
    public void addManualMapping(final ManualMappingInfo mappingInfo) {
        if (!this.mappingInfos.containsKey(mappingInfo.getBaseClass())) {
            this.manualMappings.add(mappingInfo);
            this.mappingInfos.put(mappingInfo.getBaseClass(), mappingInfo);
        }
        else {
            final Object o = this.mappingInfos.get(mappingInfo.getBaseClass());
            if (o instanceof ManualMappingInfo) {
                Log.info ("Duplicate manual mapping: " + mappingInfo.getBaseClass());
            }
            else {
                throw new IllegalArgumentException
                    ("This mapping is already a multiplex mapping.");
            }
        }
    }

    /**
     * Adds a multiplex mapping.
     * 
     * @param mappingInfo  the mapping.
     */
    public void addMultiplexMapping(final MultiplexMappingInfo mappingInfo) {
        if (!this.mappingInfos.containsKey(mappingInfo.getBaseClass())) {
            this.multiplexMappings.add(mappingInfo);
            this.mappingInfos.put(mappingInfo.getBaseClass(), mappingInfo);
        }
        else {
            final Object o = this.mappingInfos.get(mappingInfo.getBaseClass());
            if (o instanceof ManualMappingInfo) {
                throw new IllegalArgumentException
                    ("This mapping is already a manual mapping.");
            }
            else {
                Log.info(
                    "Duplicate Multiplex mapping: " + mappingInfo.getBaseClass(), new Exception()
                );
            }
        }

    }

    /**
     * Returns a multiplex mapping for the specified class.
     * 
     * @param baseClass  the base class.
     * 
     * @return The mapping.
     */
    public MultiplexMappingInfo lookupMultiplexMapping(final Class baseClass) {
        final Object o = this.mappingInfos.get(baseClass);
        if (o instanceof MultiplexMappingInfo) {
            return (MultiplexMappingInfo) o;
        }
        return null;
    }

}
