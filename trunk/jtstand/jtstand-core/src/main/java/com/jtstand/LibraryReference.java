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
    private int position;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
