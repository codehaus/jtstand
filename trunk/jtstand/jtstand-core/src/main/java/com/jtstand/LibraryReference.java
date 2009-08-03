/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, LibraryReference.java is part of JTStand.
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

import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 *
 * @author albert_kurucz
 */
@Entity
public class LibraryReference extends FileRevisionReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @ManyToOne
    private TestProject testProject;
    private int libraryReferencePosition;

    @XmlTransient
    public int getPosition() {
        return libraryReferencePosition;
    }

    public void setPosition(int position) {
        this.libraryReferencePosition = position;
    }

    @XmlTransient
    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
        if (testProject != null) {
            setCreator(testProject.getCreator());
        }
    }

    @XmlTransient
    public Library getLibrary() throws IOException, JAXBException, ParserConfigurationException, SAXException, SVNException, URISyntaxException {
        return Library.unmarshal(getNormal(getTestProject().getCreator()));
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testProject != null ? testProject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LibraryReference)) {
            return false;
        }
        LibraryReference other = (LibraryReference) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testProject == null && other.getTestProject() != null) || (this.testProject != null && !this.testProject.equals(other.getTestProject()))) {
            return false;
        }
        return true;
    }
}
