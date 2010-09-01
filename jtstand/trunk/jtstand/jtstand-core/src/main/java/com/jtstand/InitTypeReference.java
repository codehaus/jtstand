/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestTypeReference.java is part of JTStand.
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
package com.jtstand;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
//@XmlType(name = "initTypeReferenceType", propOrder = {"name", "partRevision", "partNumber"})
//@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class InitTypeReference extends TestTypeReference implements Serializable {
    public static final long serialVersionUID = 20081114L;
}
