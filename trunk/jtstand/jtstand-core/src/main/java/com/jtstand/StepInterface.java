/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StepInterface.java is part of JTStand.
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

import groovy.lang.Script;

import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public interface StepInterface extends PropertiesInterface {

    Object getVariable(String keyString) throws InterruptedException;

    Object getVariableWait(String keyString) throws InterruptedException;

    void releaseVariable(String keyString);

    void setVariable(String keyString, Object variableValue);

    TestLimit getTestLimit();

    Object getValue();

    void setValue(Object value);

    boolean isAborted();

    void abort();

    Logger getLogger();

    String getName();

    void runGroovyScript(String scriptText);

    Script parse(String scriptText);
}
