/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestSequenceProperty.java is part of JTStand.
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

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
public class TestSequenceProperty extends TestProperty implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @ManyToOne
    private TestSequence testSequence;
    @Basic
    private int testSequencePropertyPosition;

    @XmlTransient
    public int getPosition() {
        return testSequencePropertyPosition;
    }

    public void setPosition(int position) {
        this.testSequencePropertyPosition = position;
    }

    @XmlTransient
    public TestSequence getTestSequence() {
        return testSequence;
    }

    public void setTestSequence(TestSequence testSequence) {
        this.testSequence = testSequence;
        if (testSequence != null) {
            setCreator(testSequence.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testSequence != null ? testSequence.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestSequenceProperty)) {
            return false;
        }
        TestSequenceProperty other = (TestSequenceProperty) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testSequence == null && other.getTestSequence() != null) || (this.testSequence != null && !this.testSequence.equals(other.getTestSequence()))) {
            return false;
        }
        return true;
    }
}
