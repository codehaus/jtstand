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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DomainUsers domainUsers;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private LocalUsers localUsers;
    @OneToOne(mappedBy = "authentication")
    private TestProject testProject;
    private transient String operator;
    private transient PropertyChangeSupport support = new PropertyChangeSupport(this);

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

    @XmlElement(name = "localUsers")
    public LocalUsers getLocalUsers() {
        return localUsers;
    }

    public void setLocalUsers(LocalUsers localUsers) {
        this.localUsers = localUsers;
        if (localUsers != null) {
            localUsers.setCreator(creator);
        }
    }

    @XmlElement(name = "domainUsers")
    public DomainUsers getDomainUsers() {
        return domainUsers;
    }

    public void setDomainUsers(DomainUsers domainUsers) {
        this.domainUsers = domainUsers;
        if (domainUsers != null) {
            domainUsers.setCreator(creator);
        }
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
        if ((localUsers == null || localUsers.getLocalUsers().isEmpty()) && (domainUsers == null || domainUsers.getDomainUsers().isEmpty())) {
            return username;
        }
        if (domainUsers != null) {
            for (DomainUser domuser : domainUsers.getDomainUsers()) {
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
        if (localUsers != null) {
            try {
                for (LocalUser locuser : localUsers.getLocalUsers()) {
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
