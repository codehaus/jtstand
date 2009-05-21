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

import javax.naming.NamingException;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    @OneToOne(mappedBy = "authentication")
    private TestProject testProject;
    private transient String operator;
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authentication")
    @OrderBy(TestProject.POSITION_ASC)
    private List<DomainUser> domainUsers = new ArrayList<DomainUser>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "authentication")
    @OrderBy(TestProject.POSITION_ASC)
    private List<LocalUser> localUsers = new ArrayList<LocalUser>();


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
                domainUser.setAuthentication(this);
                domainUser.setPosition(index);
            }
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
                localUser.setAuthentication(this);
                localUser.setPosition(index);
            }
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
        setLocalUsers(getLocalUsers());
        setDomainUsers(getDomainUsers());
    }

    public static String encryptString(String x) throws NoSuchAlgorithmException {
        return byteArrayToHexString(encrypt(x));
    }

    public static byte[] encrypt(String x) throws NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        return d.digest();
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (byte aB : b) {
            int v = aB & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
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
        if ((getLocalUsers() == null || getLocalUsers().isEmpty()) && (getDomainUsers() == null || getDomainUsers().isEmpty())) {
            return username;
        }
        if (getDomainUsers() != null) {
            for (DomainUser domuser : getDomainUsers()) {
                if (domuser.getLoginName().equalsIgnoreCase(username) || domuser.getEmployeeNumber().equals(username)) {
                    try {
                        domuser.login(password);
                        return domuser.getEmployeeNumber();
                    } catch (NamingException ex) {
                        Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (getLocalUsers() != null) {
            try {
                for (LocalUser locuser : getLocalUsers()) {
                    if (locuser.getLoginName().equalsIgnoreCase(username) || locuser.getEmployeeNumber().equals(username)) {
                        if (locuser.getPassword().equals(encryptString(password))) {
                            return locuser.getEmployeeNumber();
                        }
                    }
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Authentication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
