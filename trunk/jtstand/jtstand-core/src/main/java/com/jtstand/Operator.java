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

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class Operator implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginName;
    private String employeeNumber;
    @ManyToOne
    private FileRevision creator;

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
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @XmlAttribute(required = true)
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (employeeNumber != null ? employeeNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Operator)) {
            return false;
        }
        Operator other = (Operator) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.employeeNumber == null && other.getEmployeeNumber() != null) || (this.employeeNumber != null && !this.employeeNumber.equals(other.getEmployeeNumber()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jtstand.DomainUser[id=" + id + "]";
    }
}
