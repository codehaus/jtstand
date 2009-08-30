/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestType.java is part of JTStand.
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
import javax.script.ScriptException;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import javax.script.Bindings;
import javax.script.SimpleBindings;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"creator_id", "product_id", "name"}))
@XmlType(name = "testType", propOrder = {"name", "remark", "properties", "testSequence"})
public class TestType extends AbstractProperties implements Serializable {

    public static final long serialVersionUID = 20081114L;
    public static final String STR_SERIAL_NUMBER_CRITERIA = "SERIAL_NUMBER_CRITERIA";
    private String name;
    private String remark;
    @ManyToOne
    private Product product;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testType")
    @OrderBy("testTypePropertyPosition ASC")
    private List<TestTypeProperty> properties = new ArrayList<TestTypeProperty>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String subversionUrl;
//    private Long revision;
    @ManyToOne
    private FileRevision creator;
    private int testTypePosition;
    @OneToOne(cascade = CascadeType.ALL)
    private TestTypeSequenceReference testSequence;

    @XmlTransient
    public int getPosition() {
        return testTypePosition;
    }

    public void setPosition(int position) {
        this.testTypePosition = position;
    }

    @XmlElement
    public TestTypeSequenceReference getTestSequence() {
        return testSequence;
    }

    public void setTestSequence(TestTypeSequenceReference testSequence) {
        this.testSequence = testSequence;
        if (testSequence != null) {
            testSequence.setTestType(this);
        }
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        setProperties(getProperties());
        setTestSequence(getTestSequence());
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

//    public FileRevision getNormal(FileRevision creator) throws URISyntaxException {
//        if (revision == null || revision < 1) {
//            throw new IllegalArgumentException("Cannot normalize revision number:" + revision);
//        }
//        URI creatorURI = new URI(creator.getSubversionUrl());
//        URI newURI = creatorURI.resolve(subversionUrl);
//        String str = creatorURI.toString();
//        URI relativeURI = (new URI(str.substring(0, str.lastIndexOf("/")))).relativize(newURI);
//        if (!relativeURI.isAbsolute() && creator.getFile() != null) {
//            return new FileRevision(newURI.toString(), revision, new File(creator.getFile().toURI().resolve(relativeURI)));
//        } else {
//            return new FileRevision(newURI.toString(), revision);
//        }
//    }

//    @XmlAttribute
//    public String getSubversionUrl() {
//        return subversionUrl;
//    }
//
//    public void setSubversionUrl(String subversionUrl) {
//        this.subversionUrl = subversionUrl;
//    }
//
//    @Override
//    public String toString() {
//        return "com.jtstand.FileRevision[id=" + id + ", url=" + subversionUrl + ", rev=" + revision + "]";
//    }
//
//    @XmlAttribute
//    public Long getRevision() {
//        return revision;
//    }
//
//    public void setRevision(Long revision) {
//        this.revision = revision;
//    }
    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElement(name = "property")
    public List<TestTypeProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TestTypeProperty> properties) {
        this.properties = properties;
        if (properties != null) {
            for (ListIterator<TestTypeProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestTypeProperty testTypeProperty = iterator.next();
                testTypeProperty.setTestType(this);
                testTypeProperty.setPosition(index);
            }
        }
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            setCreator(product.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
//        hash += (subversionUrl != null ? subversionUrl.hashCode() : 0);
//        hash += (revision != null ? revision.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        hash += (product != null ? product.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestType)) {
            return false;
        }
        TestType other = (TestType) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
//        if ((this.subversionUrl == null && other.getSubversionUrl() != null) || (this.subversionUrl != null && !this.subversionUrl.equals(other.getSubversionUrl()))) {
//            return false;
//        }
//        if ((this.revision == null && other.getRevision() != null) || (this.revision != null && !this.revision.equals(other.getRevision()))) {
//            return false;
//        }
        if ((this.name == null && other.getName() != null) || (this.name != null && !this.name.equals(other.getName()))) {
            return false;
        }
        if ((this.product == null && other.getProduct() != null) || (this.product != null && !this.product.equals(other.getProduct()))) {
            return false;
        }
        return true;
    }

    @Override
    public Bindings getBindings() {
        if (bindings == null) {
            bindings = new SimpleBindings();
            bindings.put("testType", this);
        }
        return bindings;
    }

    @Override
    public Object getPropertyObject(String keyString, Bindings bindings) throws ScriptException {
        if (bindings != null) {
            bindings.put("testType", this);
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return tsp.getPropertyObject(getProduct().getTestProject().getGroovyClassLoader(), bindings);
            }
        }
        if (getProduct() != null) {
            return getProduct().getPropertyObject(keyString, bindings);
        }
        return null;
    }

    public boolean isSerialNumberOK(String sn) {
        return isMatchMulti(sn, getPropertyString(STR_SERIAL_NUMBER_CRITERIA, null));
    }

    public static boolean isMatchMulti(String sn, String snCriteria) {
        if (snCriteria == null) {
            return true;
        }
        StringTokenizer st = new StringTokenizer(snCriteria, ";");
        if (st.countTokens() > 0) {
            while (st.hasMoreTokens()) {
                if (isMatch(sn, st.nextToken())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isMatch(String sn, String snCriteria) {
        if (snCriteria == null || snCriteria.length() == 0) {
            return true;
        }
        if (sn.length() != snCriteria.length()) {
            return false;
        }
        for (int i = 0; i < sn.length(); i++) {
            char crit = snCriteria.charAt(i);
            char ch = sn.charAt(i);
            switch (crit) {
                case '#':
                    if (!Character.isDigit(ch)) {
                        return false;
                    }
                    break;
                case '$':
                    if (!Character.isLetter(ch)) {
                        return false;
                    }
                    break;
                case '*':
                    if (!Character.isLetterOrDigit(ch)) {
                        return false;
                    }
                    break;
                default:
                    if (ch != crit) {
                        return false;
                    }
            }
        }
        return true;
    }
}
