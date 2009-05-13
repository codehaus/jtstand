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
        System.out.println("Processing output directory '" + saveDirectory.toString() + "' started...");
        while (!aborted) {
//            System.out.println("toDatabase checking directory: " + saveDirectory.toString());
            try {
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
//                                                    if (MainFrame.isMemoryEnough()) {
//                                                        System.out.println("Replace...");
//                                                        model.replace(seq.getCreateTime(), seq.getHostName());
//                                                    } else {
//                                                        System.out.println("Remove...");
//                                                        model.replace(seq.getCreateTime(), seq.getHostName());
//                                                    }
                                                    }
                                                    if (file.delete()) {
                                                        LOGGER.fine("Output file successfully deleted : " + file.getName());
                                                        System.out.println("Processing file: " + file.getName() + " successfuly completed in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
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
                                } catch (Exception ex) {
                                    LOGGER.log(Level.SEVERE, "Exception : " + ex);
//                                    aborted = true;
                                }
                            }
                            sleep(1);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
                sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
