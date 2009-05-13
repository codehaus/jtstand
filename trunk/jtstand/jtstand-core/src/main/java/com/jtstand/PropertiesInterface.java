/*
 * Copyright 2009 Albert Kurucz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
