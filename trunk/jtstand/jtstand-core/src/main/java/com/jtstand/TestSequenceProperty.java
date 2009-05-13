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
    private int position;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
