/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, GeneralQuery.java is part of JTStand.
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

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.jboss.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class GeneralQuery implements Runnable {

    private static final Logger log = Logger.getLogger(GeneralQuery.class.getCanonicalName());
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
            log.warn("Exception", ex);
        }
//        System.out.println(getClass().getCanonicalName() + " took " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
    }

    @Override
    public void run() {
        try {
            log.trace("\"" + queryString + "\"" + "...");
            Query q = em.createQuery(queryString);
            if (maxResults != null) {
                q.setMaxResults(maxResults);
            }
            result = q.getResultList();
            log.info(Integer.toString(result.size()) + " instances loaded from database!");
        } catch (Exception ex) {
//            logger.log(Level.SEVERE, "Exception: " + ex.getMessage());
        }
    }

    public List query() {
        return result;
    }
}
