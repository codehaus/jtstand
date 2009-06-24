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

import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
abstract public class AbstractVariables extends AbstractProperties implements Serializable, PropertiesInterface {

    private static final Logger LOGGER = Logger.getLogger(TestStepInstance.class.getCanonicalName());
    protected transient Map<String, Object> variablesMap = new HashMap<String, Object>();
    private transient Map<String, HashSet<Thread>> lockerThreads = new HashMap<String, HashSet<Thread>>();
    private transient Object variableLock = new Object();
    private transient Thread actualLockerThread;

    public void abort(Thread thread) {
        if (thread != null) {
            for (String variableName : lockerThreads.keySet()) {
                if (lockerThreads.get(variableName).contains(thread)) {
                    TestStepScript.abort(variablesMap.get(variableName));
                }
            }
        }
    }

    public void dispose() {
        synchronized (variableLock) {
//            for (Object o : variables.values()) {
//                TestStepScript.callDispose(o);
//            }
            Object[] vars = variablesMap.values().toArray();
            for (int i = vars.length - 1; i >= 0; i--) {
                TestStepScript.disposeOrClose(vars[i]);
            }
            variablesMap.clear();
            lockerThreads.clear();
        }
    }

    private Object readResolve() {
//        System.out.println("readResolve Variables");
        variableLock = new Object();
        return this;
    }

    public Object getVariable(String keyString, boolean wait, TestProperty tsp, TestStepInstance step) throws InterruptedException {
        if (wait) {
            while (true) {
                Object v = getVariable(keyString, tsp, step);
                if (v != null) {
                    return v;
                }
                Thread.sleep(1);
            }
        }
        Object v = getVariable(keyString, tsp, step);
        if (v != null) {
            return v;
        }
        v = tsp.getPropertyObject(step.getTestSequenceInstance().getTestProject().getGroovyClassLoader(), step.getBinding());
        if (v != null) {
            setVariable(keyString, v);
        }
        return v;
    }

    private Object getVariable(String keyString, TestProperty tsp, TestStepInstance step) throws InterruptedException {
        if (tsp.getPropertyValueAttribute() != null) {
//            System.out.println("propertyValueAttribute of '" + tsp.getName() + "' is: '" + tsp.getPropertyValueAttribute() + "'");
            return tsp.getPropertyValueAttribute();
        }
        if (tsp.isMutex() != null && tsp.isMutex()) {
            Set<Thread> deadThreads = new HashSet<Thread>();
            while (true) {
                synchronized (variableLock) {
                    boolean locked = false;
                    HashSet<Thread> lockerThread = lockerThreads.get(keyString);
                    if (lockerThread != null) {
                        deadThreads.clear();
                        Iterator<Thread> it = lockerThread.iterator();
                        while (it.hasNext() && !locked) {
                            Thread locker = it.next();
                            if (!locker.isAlive()) {
                                deadThreads.add(locker);
                            } else {
                                if (!step.isThreadInFamily(locker)) {
                                    locked = true;
                                    if (!locker.equals(actualLockerThread)) {
                                        actualLockerThread = locker;
//                                        LOGGER.info("Variable is locked by: " + actualLockerThread.getName());
                                    }
                                }
                            }
                        }
                        lockerThread.removeAll(deadThreads);
                    } else {
                        lockerThread = new HashSet<Thread>();
                        lockerThreads.put(keyString, lockerThread);
                    }
                    if (!locked) {
                        Thread t = step.getThisThread();
                        if (t != null && t.isAlive()) {
                            lockerThread.add(t);
                        } else {
                            throw new IllegalStateException("Trying to lock sequence variable '" + keyString + "' by a dead thread of step '" + step.getTestStepInstancePath() + "'");
                        }
                        return variablesMap.get(keyString);
                    }
                }
                Thread.sleep(1L);
            }
        }
        synchronized (variableLock) {
            return variablesMap.get(keyString);
        }
    }

    public void releaseVariable(String keyString, TestProperty tsp, TestStepInstance step) {
        if (tsp.isMutex() != null && tsp.isMutex()) {
            synchronized (variableLock) {
                //boolean locked = false;
                HashSet<Thread> lockerThread = lockerThreads.get(keyString);
                if (lockerThread != null) {
                    Set<Thread> deadThreads = new HashSet<Thread>();
                    Iterator<Thread> it = lockerThread.iterator();
//                    while (it.hasNext() && !locked) {
                    while (it.hasNext()) {
                        Thread locker = it.next();
                        if (!locker.isAlive()) {
                            deadThreads.add(locker);
                        }
                    }
                    lockerThread.removeAll(deadThreads);
                    Thread t = step.getThisThread();
                    if (t != null && t.isAlive()) {
                        lockerThread.remove(Thread.currentThread());
                    } else {
                        throw new IllegalStateException("Trying to release sequence variable '" + keyString + "' by a dead thread of step '" + step.getTestStepInstancePath() + "'");
                    }
                    if (lockerThread.isEmpty()) {
                        lockerThreads.remove(keyString);
                    }
                }
            }
        }
    }

    public void setVariable(String keyString, Object variableValue) {
        System.out.println("putting '" + keyString + "' value: " + variableValue);
        variablesMap.put(keyString, variableValue);
    }
}
