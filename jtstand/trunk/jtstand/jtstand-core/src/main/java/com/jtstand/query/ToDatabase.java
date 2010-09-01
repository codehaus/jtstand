/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ToDatabase.java is part of JTStand.
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
package com.jtstand.query;

import com.jtstand.TestSequenceInstance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class ToDatabase extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ToDatabase.class.getCanonicalName());
    private File saveDirectory;
    private boolean aborted = false;
    private FrameInterface model;

    public ToDatabase(File saveDirectory, FrameInterface model) {
        super();
        this.saveDirectory = saveDirectory;
        this.model = model;
        setDaemon(true);
        setPriority(MIN_PRIORITY);
        start();
    }

    public void abort() throws InterruptedException {
        aborted = true;
        this.join();
    }

    @Override
    public void run() {
        int num = 0;
        System.out.println("Processing output directory '" + saveDirectory.toString() + "' started...");
        while (!aborted) {
//            System.out.println("toDatabase checking directory: " + saveDirectory.toString());
            try {
                if (!saveDirectory.canRead()) {
                    throw new IllegalArgumentException("Output directory does not exist and cannot be read: " + saveDirectory);
                }
                File[] files = saveDirectory.listFiles();
                if (files.length > 0) {
                    for (int i = 0; !aborted && i < files.length; i++) {
                        File file = files[i];
                        if (!file.canWrite()) {
                            LOGGER.log(Level.SEVERE, "Output file cannot be written : " + file.getName());
                        } else {
                            try {
                                long startTime = System.currentTimeMillis();
                                if (file.getName().endsWith(".state")) {
                                    System.out.println("Processing file: " + file.getName());
                                    TestSequenceInstance seq = TestSequenceInstance.fromFile(file);
                                    if (seq == null) {
                                        System.out.println("Output file cannot be Unmarshalled");
                                    } else {
                                        EntityManagerFactory emf = seq.getTestStation().getEntityManagerFactory();
                                        if (emf == null) {
                                            System.out.println("Entity Manager Factory cannot be obtained");
                                        } else {
                                            EntityManager em = emf.createEntityManager();
                                            if (em == null) {
                                                System.out.println("Entity Manager cannot be obtained");
                                            } else {
                                                seq.connect(em);
                                                if (seq.merge(em)) {
//                                                Log.log("Output file successfully persisted : " + file.getName());
                                                    if (model != null) {
                                                        System.out.println("Replace...");
                                                        model.replace(seq.getCreateTime(), seq.getHostName());
                                                    }
                                                    if (file.delete()) {
                                                        LOGGER.fine("Output file successfully deleted : " + file.getName());
                                                        num++;
                                                        System.out.println("Processing file: " + file.getName() + " successfuly completed in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
                                                        System.out.println("Free Memory after processing " + Integer.toString(num) + " times: " + Runtime.getRuntime().freeMemory());
                                                    } else {
                                                        LOGGER.log(Level.SEVERE, "Output file cannot be deleted : " + file.getName());
                                                    }
                                                } else {
                                                    LOGGER.log(Level.SEVERE, "Output file cannot be persisted : " + file.getName());
                                                }
                                                em.close();
                                            }
                                        }
                                    }
                                } else if (file.getName().endsWith(".xml")) {
                                    System.out.println("Processing file: " + file.getName());
                                    TestSequenceInstance seq = TestSequenceInstance.unmarshal(file);
                                    file.delete();
                                }
                            } catch (Exception ex) {
                                LOGGER.log(Level.SEVERE, "Exception: " + ex);
//                                    aborted = true;
                            }
                        }
                    }
                }
                sleep(2500);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
