/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, DomainUserList.java is part of JTStand.
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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class DomainUserList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private FileRevision creator;
    @OneToOne
    private Authentication authentication;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "domainUserList")
    @OrderBy("domainUserPosition ASC")
    private List<DomainUser> domainUsers = new ArrayList<DomainUser>();

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
        setDomainUsers(getDomainUsers());
    }

    @XmlTransient
    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
        if (authentication != null) {
            setCreator(authentication.getCreator());
        }
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
                domainUser.setDomainUserList(this);
                domainUser.setPosition(index);
            }
        }
    }

    public String getEmployeeNumber(String username, String password) {
        if (getDomainUsers() != null) {
            for (DomainUser domuser : getDomainUsers()) {
                if (domuser.getLoginName().equalsIgnoreCase(username) || domuser.getEmployeeNumber().equals(username)) {
                    return domuser.getEmployeeNumber(password);
                }
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DomainUserList)) {
            return false;
        }
        DomainUserList other = (DomainUserList) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }
}
