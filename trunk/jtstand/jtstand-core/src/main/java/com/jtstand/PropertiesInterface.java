/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, PropertiesInterface.java is part of JTStand.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

/**
 *
 * @author albert_kurucz
 */
public interface PropertiesInterface {

    Boolean getPropertyBoolean(String keyString, boolean defaultValue);

    Boolean getPropertyBoolean(String keyString);

    Byte getPropertyByte(String keyString, Byte defaultValue);

    Byte getPropertyByte(String keyString);

    Short getPropertyShort(String keyString, Short defaultValue);

    Short getPropertyShort(String keyString);

    Integer getPropertyInteger(String keyString, Integer defaultValue);

    Integer getPropertyInteger(String keyString);

    Long getPropertyLong(String keyString, Long defaultValue);

    Long getPropertyLong(String keyString);

    Double getPropertyDouble(String keyString, Double defaultValue);

    Double getPropertyDouble(String keyString);

    Character getPropertyCharacter(String keyString, Character defaultValue);

    Character getPropertyCharacter(String keyString);

    String getPropertyString(String keyString, String defaultValue);

    String getPropertyString(String keyString);

    Object getPropertyObject(String keyString);
}
