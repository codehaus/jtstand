/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestTypeReference.java is part of JTStand.
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

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType(name = "productReferenceType", propOrder = {"partRevision", "partNumber"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestTypeReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String partNumber;
    private String partRevision;
    private String name;
    @ManyToOne
    private TestStation testStation;
    @ManyToOne
    private TestFixture testFixture;
    @ManyToOne
    private FileRevision creator;
    private int position;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
    }

    @XmlTransient
    public TestStation getTestStation() {
        return testStation;
    }

    public void setTestStation(TestStation testStation) {
        this.testStation = testStation;
        if (testStation != null) {
            setCreator(testStation.getCreator());
        }
    }

    @XmlTransient
    public TestFixture getTestFixture() {
        return testFixture;
    }

    public void setTestFixture(TestFixture testFixture) {
        this.testFixture = testFixture;
        if (testFixture != null) {
            this.setCreator(testFixture.getCreator());
        }
    }

    @XmlTransient
    public TestStation getTestStationReally() {
        return getTestFixture() == null ? getTestStation() : getTestFixture().getTestStation();
    }

    @XmlAttribute(required = true)
    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @XmlAttribute(required = true)
    public String getPartRevision() {
        return partRevision;
    }

    public void setPartRevision(String partRevision) {
        this.partRevision = partRevision;
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += creator == null ? 0 : creator.hashCode();
        hash += partNumber == null ? 0 : partNumber.hashCode();
        hash += partRevision == null ? 0 : partRevision.hashCode();
        hash += name == null ? 0 : name.hashCode();
        hash += testStation == null ? 0 : testStation.hashCode();
        hash += testFixture == null ? 0 : testFixture.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestTypeReference)) {
            return false;
        }
        TestTypeReference other = (TestTypeReference) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.partNumber == null && other.getPartNumber() != null) || (this.partNumber != null && !this.partNumber.equals(other.getPartNumber()))) {
            return false;
        }
        if ((this.partRevision == null && other.getPartRevision() != null) || (this.partRevision != null && !this.partRevision.equals(other.getPartRevision()))) {
            return false;
        }
        if ((this.name == null && other.getName() != null) || (this.name != null && !this.name.equals(other.getName()))) {
            return false;
        }
        if ((this.testStation == null && other.getTestStation() != null) || (this.testStation != null && !this.testStation.equals(other.getTestStation()))) {
            return false;
        }
        if ((this.testFixture == null && other.getTestFixture() != null) || (this.testFixture != null && !this.testFixture.equals(other.getTestFixture()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return TestTypeReference.class.getCanonicalName() + "[id=" + id + "]";
    }
}
