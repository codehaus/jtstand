/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, Authentication.java is part of JTStand.
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

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class Authentication implements Serializable {

    public static final long serialVersionUID = 20081114L;
    public static final String OPERATOR_PROPERTY = "operator";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private FileRevision creator;
    @OneToOne
    private TestProject testProject;
    private transient String operator;
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "authentication")
    private DomainUserList domainUserList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "authentication")
    private LocalUserList localUserList;

    @XmlElement(name = "domainUsers")
    public DomainUserList getDomainUserList() {
        return domainUserList;
    }

    public void setDomainUserList(DomainUserList domainUserList) {
        this.domainUserList = domainUserList;
        if (domainUserList != null) {
            domainUserList.setAuthentication(this);
        }
    }

    @XmlElement(name = "localUsers")
    public LocalUserList getLocalUserList() {
        return localUserList;
    }

    public void setLocalUserList(LocalUserList localUserList) {
        this.localUserList = localUserList;
        if (localUserList != null) {
            localUserList.setAuthentication(this);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        support.removePropertyChangeListener(l);
    }

    @XmlTransient
    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
        if (testProject != null) {
            setCreator(testProject.getCreator());
        }
    }

    public enum AuthenticationMode {

        PASSWORD, NO_PASSWORD
    }
    private AuthenticationMode authenticatonMode = null;

    @XmlAttribute
    public AuthenticationMode getAuthenticatonMode() {
        return authenticatonMode;
    }

    public void setAuthenticatonMode(AuthenticationMode authenticatonMode) {
        this.authenticatonMode = authenticatonMode;
    }

    @XmlTransient
    public boolean isPassword() {
        return authenticatonMode == null || authenticatonMode.equals(AuthenticationMode.PASSWORD);
    }

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
        if (!(object instanceof Authentication)) {
            return false;
        }
        Authentication other = (Authentication) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Authentication.class.getCanonicalName() + "[id=" + id + "]";
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        setLocalUserList(getLocalUserList());
        setDomainUserList(getDomainUserList());
    }

    @XmlTransient
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        if (((operator == null) && (this.operator != null)) || ((operator != null) && !operator.equals(this.operator))) {
            String oldOperator = this.operator;
            if (operator != null) {
                System.out.println("Operator '" + operator + "' is logging in.");
            } else {
                System.out.println("Operator '" + oldOperator + "' is logging out.");
            }
            this.operator = operator;
            support.firePropertyChange(OPERATOR_PROPERTY, oldOperator, this.operator);
        }
    }

    public void login(String username, String password) throws Exception {
        String op = getEmployeeNumber(username, password);
        setOperator(op);
        if (op == null) {
            throw new Exception("Could not login");
        }
    }

    public void logout() {
        setOperator(null);
    }

    private String getEmployeeNumber(String username, String password) {
        if ((getLocalUserList() == null || getLocalUserList().getLocalUsers().isEmpty()) && (getDomainUserList() == null || getDomainUserList().getDomainUsers().isEmpty())) {
            return username;
        }
        String empNr = null;
        if (getDomainUserList() != null) {
            empNr = getDomainUserList().getEmployeeNumber(username, password);
        }
        if (empNr == null && getLocalUserList() != null) {
            empNr = getLocalUserList().getEmployeeNumber(username, password);
        }
        return empNr;
    }
}
