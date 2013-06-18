/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestProjectProperty.java is part of JTStand.
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

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"testproject_id", "name"}))
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestProjectProperty extends TestProperty {

    @ManyToOne
    private TestProject testProject;
    private int testProjectPropertyPosition;

    @XmlTransient
    public int getPosition() {
        return testProjectPropertyPosition;
    }

    public void setPosition(int position) {
        this.testProjectPropertyPosition = position;
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

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testProject != null ? testProject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestProjectProperty)) {
            return false;
        }
        TestProjectProperty other = (TestProjectProperty) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testProject == null && other.getTestProject() != null) || (this.testProject != null && !this.testProject.equals(other.getTestProject()))) {
            return false;
        }
        return true;
    }
}
