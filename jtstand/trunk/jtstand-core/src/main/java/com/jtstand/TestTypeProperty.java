/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestTypeProperty.java is part of JTStand.
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
@UniqueConstraint(columnNames = {"testtype_id", "name"}))
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestTypeProperty extends TestProperty {

    @ManyToOne
    private TestType testType;
    private int testTypePropertyPosition;

    @XmlTransient
    public int getPosition() {
        return testTypePropertyPosition;
    }

    public void setPosition(int position) {
        this.testTypePropertyPosition = position;
    }

    @XmlTransient
    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
        if (testType != null) {
            setCreator(testType.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testType != null ? testType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestTypeProperty)) {
            return false;
        }
        TestTypeProperty other = (TestTypeProperty) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testType == null && other.getTestType() != null) || (this.testType != null && !this.testType.equals(other.getTestType()))) {
            return false;
        }
        return true;
    }
}
