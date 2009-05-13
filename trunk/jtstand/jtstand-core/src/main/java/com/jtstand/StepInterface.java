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
