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
import com.jtstand.TestStation;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.script.ScriptException;
import org.jboss.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class ToDatabase extends Thread {

    private static final Logger log = Logger.getLogger(ToDatabase.class.getName());
    private File saveDirectory;
    private File savedDirectory;
    private File savedErrorDirectory;
    private boolean aborted = false;
    private FrameInterface model;
    private Set<File> ignoredFiles = new HashSet<File>();
    private TestStation testStation;
    private transient int num = 0;

    public ToDatabase(TestStation testStation, FrameInterface model) throws ScriptException {
        super();
        this.testStation = testStation;
        saveDirectory = testStation.getSaveDirectory();
        savedDirectory = testStation.getSavedDirectory();
        savedErrorDirectory = testStation.getSavedErrorDirectory();
        this.model = model;
        if (saveDirectory == null) {
            log.error("Save directory is not defined");
            return;
        }
        if (savedDirectory == null) {
            log.error("Saved directory is not defined");
            return;
        }
        if (savedErrorDirectory == null) {
            log.error("Saved error directory is not defined");
            return;
        }
        setDaemon(true);
        setPriority(MIN_PRIORITY);
        start();
    }

    public void abort() throws InterruptedException {
        aborted = true;
        this.join();
    }

    public void saveSequenceFileToDatabase(File file) {
        EntityManagerFactory emf;
        EntityManager em = null;
        if (ignoredFiles.contains(file)) {
            return;
        }
        try {
            TestSequenceInstance seq = null;
            if (!file.canWrite()) {
                log.error("Output file cannot be written : " + file.getName());
            } else {
                if (file.getName().endsWith(".xml")) {
                    log.trace("Processing file: " + file.getPath() + " ...");
                    seq = TestSequenceInstance.unmarshal(file);
                }
            }
            if (seq != null) {
                long startTime = System.currentTimeMillis();
                emf = seq.getTestStation().getEntityManagerFactory();
                if (emf == null) {
                    log.error("Entity Manager Factory cannot be obtained");
                } else {
                    em = emf.createEntityManager();
                    if (em == null) {
                        log.error("Entity Manager cannot be obtained");
                    } else {
//                        seq.connect(em);
                        if (seq.merge(em)) {
                            if (model != null) {
//                                System.out.println("Replace...");
                                model.replace(seq.getCreateTime(), seq.getHostName());
                            }
                            if (file.renameTo(new File(savedDirectory.getPath() + File.separator + file.getName()))) {
//                                System.out.println("Output file successfully moved to: " + file.getName());
                                num++;
                                log.info("Processing file: " + file.getPath() + " successfuly completed in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
//                                System.out.println("Free Memory after processing " + Integer.toString(num) + " times: " + Runtime.getRuntime().freeMemory());
                            } else {
                                log.error("Output file cannot be moved: " + file.getName());
                                ignoredFiles.add(file);
                            }
                        } else {
                            log.error("Output file cannot be persisted: " + file.getName());
                        }
                    }
                }
            }
        } catch (Throwable ex) {
            log.warn("Exception", ex);
            //ex.printStackTrace();
            if (file.renameTo(new File(savedErrorDirectory.getPath() + File.separator + file.getName()))) {
//                System.out.println("Output file successfully moved to: " + file.getName());
            } else {
                log.error("Output file cannot be moved: " + file.getName());
                ignoredFiles.add(file);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void run() {
        log.info("Processing output directory '" + saveDirectory.toString() + "' started...");
        try {
            while (!aborted) {
                if (!saveDirectory.canRead()) {
                    throw new IllegalArgumentException("Output directory does not exist and cannot be read: " + saveDirectory);
                }
                File[] files = saveDirectory.listFiles();
                for (int i = 0; !aborted && i < files.length; i++) {
                    saveSequenceFileToDatabase(files[i]);
                }
                if (!aborted) {
                    sleep(2500);
                }
            }
        } catch (InterruptedException ex) {
            log.warn("Exception", ex);
        }
    }
}
