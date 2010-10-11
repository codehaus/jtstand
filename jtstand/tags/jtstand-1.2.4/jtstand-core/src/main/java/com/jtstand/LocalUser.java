/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, LocalUser.java is part of JTStand.
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
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class LocalUser extends Operator implements Serializable {

    public static final long serialVersionUID = 20081114L;
    private String realName;
    private String password;
    private int localUserPosition;
    @ManyToOne
    private LocalUserList localUserList;

    @XmlTransient
    public int getPosition() {
        return localUserPosition;
    }

    public void setPosition(int position) {
        this.localUserPosition = position;
    }

    @XmlTransient
    public LocalUserList getLocalUserList() {
        return localUserList;
    }

    public void setLocalUserList(LocalUserList localUserList) {
        this.localUserList = localUserList;
        if (localUserList != null) {
            setCreator(localUserList.getCreator());
        }
    }

    @XmlAttribute
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @XmlAttribute
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeNumber(String password) {
        try {
            if (getPassword().equals(encryptString(password))) {
                return getEmployeeNumber();
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LocalUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
}
