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

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestProjectPersistenceProperties extends PersistenceProperties implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @OneToOne(mappedBy = "persistenceProperties")
    private TestProject testProject;

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

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testProject != null ? testProject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestProjectPersistenceProperties)) {
            return false;
        }
        TestProjectPersistenceProperties other = (TestProjectPersistenceProperties) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testProject == null && other.getTestProject() != null) || (this.testProject != null && !this.testProject.equals(other.getTestProject()))) {
            return false;
        }
        return true;
    }
}
