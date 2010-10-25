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
 * along with GTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import org.hibernate.annotations.DiscriminatorOptions;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType(name = "productReferenceType", propOrder = {"name", "partRevision", "partNumber"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
@DiscriminatorOptions(force = true)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class TestTypeReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String partNumber;
    private String partRevision;
    private String name;
    @ManyToOne
    private FileRevision creator;
    private int testTypeReferencePosition;

    public TestTypeReference() {
        super();
    }

    public TestTypeReference(String partNumber, String partRevision, String name) {
        super();
        this.partNumber = partNumber;
        this.partRevision = partRevision;
        this.name = name;
    }

    @XmlTransient
    public int getPosition() {
        return testTypeReferencePosition;
    }

    public void setPosition(int position) {
        this.testTypeReferencePosition = position;
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
        return true;
    }

    @Override
    public String toString() {
        return partNumber + ":" + partRevision + ":" + name;
    }
}
