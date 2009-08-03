/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, LocalUserList.java is part of JTStand.
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
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class LocalUserList implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private FileRevision creator;
    @OneToOne
    private Authentication authentication;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localUserList")
    @OrderBy("localUserPosition ASC")
    private List<LocalUser> localUsers = new ArrayList<LocalUser>();

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
        setLocalUsers(getLocalUsers());
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
                localUser.setLocalUserList(this);
                localUser.setPosition(index);
            }
        }
    }

    public String getEmployeeNumber(String username, String password) {
        if (getLocalUsers() != null) {
            for (LocalUser locuser : getLocalUsers()) {
                if (locuser.getLoginName().equalsIgnoreCase(username) || locuser.getEmployeeNumber().equals(username)) {
                    return locuser.getEmployeeNumber(password);
                }
            }
        }
        return null;
    }
}
