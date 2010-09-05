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
import java.io.IOException;
import java.net.URISyntaxException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

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
    private int num = 0;

    public void saveSequenceFileToDatabase(File file) throws JAXBException, SVNException {
        TestSequenceInstance seq = null;
        if (!file.canWrite()) {
            LOGGER.log(Level.SEVERE, "Output file cannot be written : " + file.getName());
        } else {
            if (file.getName().endsWith(".state")) {
                System.out.println("Processing file: " + file.getName());
                seq = TestSequenceInstance.fromFile(file);
            } else if (file.getName().endsWith(".xml")) {
                System.out.println("Processing file: " + file.getName());
                seq = TestSequenceInstance.unmarshal(file);
            }
        }
        if (seq != null) {
            long startTime = System.currentTimeMillis();
            EntityManagerFactory emf = seq.getTestStation().getEntityManagerFactory();
            if (emf == null) {
                System.out.println("Entity Manager Factory cannot be obtained");
            } else {
                EntityManager em = emf.createEntityManager();
                if (em == null) {
                    System.out.println("Entity Manager cannot be obtained");
                } else {
                    try {
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
                    } catch (Exception ex) {
                        Logger.getLogger(ToDatabase.class.getName()).log(Level.SEVERE, null, ex);
                        file.renameTo(new File(file.getPath() + ".error"));
                    }
                }
            }
        }
    }

    @Override
    public void run() {
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
                        saveSequenceFileToDatabase(files[i]);
                    }
                }
                sleep(2500);
            } catch (Exception ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
    }
}
