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
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;


import java.util.logging.Logger;
import javax.script.ScriptException;

/**
 *
 * @author albert_kurucz
 */
public interface StepInterface {

    Object getVariable(String keyString) throws InterruptedException, ScriptException;

    Object getVariableWait(String keyString) throws InterruptedException, ScriptException;

    void releaseVariable(String keyString);

    TestLimit getTestLimit();

    Object getValue();

    void setValue(Object value);

    boolean isAborted();

    void abort();

    Logger getLogger();

    String getName();

}
