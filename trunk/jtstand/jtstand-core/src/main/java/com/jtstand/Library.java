/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Library.java is part of JTStand.
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

import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.persistence.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlRootElement(name = "library")
@XmlType(name = "libraryType", propOrder = {"remark", "classes"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class Library implements Serializable {

    @ManyToMany(mappedBy = "libraries")
    private List<TestProject> testProjects;
    private static final long serialVersionUID = 1L;
    private final static Object JAXB_LOCK = new Object();

    public List<TestProject> getTestProjects() {
        return testProjects;
    }

    public static Library query(EntityManager em, FileRevision creator) {
        if (em == null || creator == null) {
            return null;
        }
        FileRevision c = FileRevision.query(em, creator);
        if (c == null) {
            return null;
        }
        try {
            Query q = em.createQuery("select ts from Library ts where ts.creator = :creator");
            q.setParameter("creator", c);
            Library library = (Library) q.getSingleResult();
            Library.getMarshaller().marshal(library, TestProject.NULL_OUTPUT_STREAM);
            return library;
        } catch (Exception ex) {
            return null;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    private FileRevision creator;
    private String remark;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "library")
    @OrderBy(TestProject.POSITION_ASC)
    private List<LibraryClass> classes = new ArrayList<LibraryClass>();
    private transient Object classesLock = new Object();
    private static transient ConcurrentHashMap<FileRevision, Library> cache = new java.util.concurrent.ConcurrentHashMap<FileRevision, Library>();
    final private static transient Object CACHE_LOCK = new Object();
    private static JAXBContext jc;
    private static Unmarshaller um;
    private static Marshaller m;

    private static JAXBContext getJAXBContext()
            throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(Library.class);
        }
        return jc;
    }

    public static Marshaller getMarshaller()
            throws JAXBException {
        if (m == null) {
            m = getJAXBContext().createMarshaller();
            if (TestProject.getSchemaLocation() != null) {
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, TestProject.getSchemaLocation());
            }
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        return m;
    }

    public static Unmarshaller getUnmarshaller()
            throws JAXBException, SAXException {
        if (um == null) {
            um = getJAXBContext().createUnmarshaller();
            um.setSchema(TestProject.getSchema());
        }
        return um;
    }

    public static Library unmarshal(FileRevision fileRevision)
            throws JAXBException, SAXException, SVNException {
        Library library;
        synchronized (CACHE_LOCK) {
            library = cache.get(fileRevision);
        }
        if (library != null) {
            return library;
        }
        synchronized (JAXB_LOCK) {
            library = (Library) fileRevision.unmarshal(getUnmarshaller());
        }
        synchronized (CACHE_LOCK) {
            cache.put(fileRevision, library);
        }
        library.setCreator(fileRevision);
        return library;
    }

    private Object readResolve() {
        classesLock = new Object();
        return this;
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElement(name = "class")
    public List<LibraryClass> getClasses() {
        synchronized (classesLock) {
            return classes;
        }
    }

    public void setClasses(List<LibraryClass> classes) {
        this.classes = classes;
        if (classes != null) {
            for (ListIterator<LibraryClass> iterator = classes.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                LibraryClass libraryClass = iterator.next();
                libraryClass.setLibrary(this);
                libraryClass.setPosition(index);
            }
        }
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        setClasses(getClasses());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Library)) {
            return false;
        }
        Library other = (Library) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }
}
