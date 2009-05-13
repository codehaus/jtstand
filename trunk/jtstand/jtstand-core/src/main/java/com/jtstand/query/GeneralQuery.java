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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class GeneralQuery implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(GeneralQuery.class.getCanonicalName());
    private EntityManager em;
    private String queryString;
    private Integer maxResults;
    private List result;

    public GeneralQuery(EntityManagerFactory emf, String queryString) {
        this(emf.createEntityManager(), queryString);
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    public GeneralQuery(EntityManager em, String queryString) {
        this(em, queryString, null);
    }

    public GeneralQuery(EntityManager em, String queryString, Integer maxResults) {
        this.em = em;
        this.queryString = queryString;
        this.maxResults = maxResults;
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(this);
        t.setName(getClass().getCanonicalName());
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneralQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(getClass().getCanonicalName() + " took " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void run() {
        try {
            LOGGER.log(Level.FINE, getClass().getCanonicalName() + " \"" + queryString + "\"" + "...");
            Query q = em.createQuery(queryString);
            if (maxResults != null) {
                q.setMaxResults(maxResults);
            }
            result = q.getResultList();
            LOGGER.log(Level.FINE, Integer.toString(result.size()) + " instances loaded from database!");
        } catch (Exception ex) {
//            logger.log(Level.SEVERE, "Exception: " + ex.getMessage());
        }
    }

    public List query() {
        return result;
    }
}
