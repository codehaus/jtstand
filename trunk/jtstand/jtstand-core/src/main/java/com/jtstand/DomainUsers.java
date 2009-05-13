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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class DomainUsers implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "domainUsers")
    @OrderBy(TestProject.POSITION_ASC)
    private List<DomainUser> domainUsers = new ArrayList<DomainUser>();
    @ManyToOne
    private FileRevision creator;

    @XmlTransient
    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DomainUsers)) {
            return false;
        }
        DomainUsers other = (DomainUsers) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DomainUsers.class.getCanonicalName() + "[id=" + id + "]";
    }

    @XmlElement(name = "domainUser")
    public List<DomainUser> getDomainUsers() {
        return domainUsers;
    }

    public void setDomainUsers(List<DomainUser> domainUsers) {
        this.domainUsers = domainUsers;
        if (domainUsers != null) {
            for (ListIterator<DomainUser> iterator = domainUsers.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                DomainUser domainUser = iterator.next();
                domainUser.setDomainUsers(this);
                domainUser.setPosition(index);
            }
        }
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        setDomainUsers(getDomainUsers());
    }
}
