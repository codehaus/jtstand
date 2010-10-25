/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, DomainUser.java is part of JTStand.
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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Hashtable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class DomainUser extends Operator {

    private String domainName;
    @ManyToOne
    private DomainUserList domainUserList;
    private int domainUserPosition;

    @XmlTransient
    public int getPosition() {
        return domainUserPosition;
    }

    public void setPosition(int position) {
        this.domainUserPosition = position;
    }

    @XmlTransient
    public DomainUserList getDomainUserList() {
        return domainUserList;
    }

    public void setDomainUserList(DomainUserList domainUserList) {
        this.domainUserList = domainUserList;
        if (domainUserList != null) {
            setCreator(domainUserList.getCreator());
        }
    }

    @XmlAttribute
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domain) {
        this.domainName = domain;
    }

    public String getEmployeeNumber(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        Hashtable<String, String> env = new Hashtable<String, String>();
        DirContext ctx = null;
        try {
            env.put("java.naming.referral", "ignore");
//      env.put("java.naming.security.authentication", "simple");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            String provider = "com.sun.jndi.ldap.LdapCtxFactory";
            env.put("java.naming.factory.initial", provider);
            /* For handling Uncontinued reference found message of partial result exception */
            env.put(Context.REFERRAL, "follow");
            env.put("java.naming.ldap.derefAliases", "always");
            env.put("java.naming.ldap.deleteRDN", "false");
            env.put("java.naming.ldap.attributes.binary", "");
            env.put(Context.PROVIDER_URL, "ldap://" + domainName + ": " + "389");
            env.put(Context.SECURITY_PRINCIPAL, getLoginName() + "@" + domainName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            ctx = new InitialDirContext(env);
            ctx.lookup("");
            ctx.close();
            return getEmployeeNumber();
        } catch (NamingException ex) {
            Logger.getLogger(DomainUser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                    Logger.getLogger(DomainUser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
//    public static void main(String args[]) {
//        DomainUser du = new DomainUser();
//        du.setDomainName(args[0]);
//        du.setLoginName(args[1]);
//
//        try {
//            du.login(args[2]);
//            System.out.println("Logged in OK");
//        } catch (NamingException ex) {
//            System.out.println("Could not login to '" + args[0] + "' domain. Exception: " + ex.getMessage());
//        }
//    }
}
