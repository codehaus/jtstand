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
public class LocalUsers implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localUsers")
    @OrderBy(TestProject.POSITION_ASC)
    private List<LocalUser> localUsers = new ArrayList<LocalUser>();
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
        if (!(object instanceof LocalUsers)) {
            return false;
        }
        LocalUsers other = (LocalUsers) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return LocalUsers.class.getCanonicalName() + "[id=" + id + "]";
    }

    @XmlElement(name = "localUser")
    public List<LocalUser> getLocalUsers() {
        return localUsers;
    }

    public void setLocalUsers(List<LocalUser> localUsers) {
        this.localUsers = localUsers;
        if (localUsers != null) {
            for (ListIterator<LocalUser> iterator = localUsers.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                LocalUser localUser = iterator.next();
                localUser.setLocalUsers(this);
                localUser.setPosition(index);
            }
        }
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        setLocalUsers(getLocalUsers());
    }
}
