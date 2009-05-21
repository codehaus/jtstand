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

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Hashtable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class DomainUser extends Operator implements Serializable {
    public static final long serialVersionUID = 20081114L;

    private String domainName;
    @ManyToOne
    private Authentication authentication;
    private int position;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    @XmlAttribute
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domain) {
        this.domainName = domain;
    }

    public void login(String password) throws NamingException {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        Hashtable<String, String> env = new Hashtable<String, String>();
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

        DirContext ctx = new InitialDirContext(env);
        ctx.lookup("");
        ctx.close();
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
