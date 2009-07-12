/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Product.java is part of JTStand.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import groovy.lang.Binding;

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
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"creator_id", "partNumber", "partRevision"}), @UniqueConstraint(columnNames = {"testproject_id", "partNumber", "partRevision"})})
@XmlType(name = "productType", propOrder = {"partRevision", "partNumber", "remark", "properties", "testTypes"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class Product extends AbstractProperties implements Serializable, PropertiesInterface {

    public static final long serialVersionUID = 20081114L;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestType> testTypes = new ArrayList<TestType>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String partNumber;
    private String partRevision;
    private String remark;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @OrderBy(TestProject.POSITION_ASC)
    private List<ProductProperty> properties = new ArrayList<ProductProperty>();
    @ManyToOne
    private TestProject testProject;
    @ManyToOne
    private FileRevision creator;
    private int position;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

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
        setProperties(getProperties());
        setTestTypes(getTestTypes());
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElement(name = "testType", required = true, nillable = false)
    public List<TestType> getTestTypes() {
        return testTypes;
    }

    public void setTestTypes(List<TestType> testTypes) {
        this.testTypes = testTypes;
        if (testTypes != null) {
            for (ListIterator<TestType> iterator = testTypes.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestType testType = iterator.next();
                testType.setProduct(this);
                testType.setPosition(index);
            }
        }
    }

    public TestType getTestType(String testTypeName) {
//        System.out.println("product: " + toString() + " testType: " + testTypeName);
        if (getTestTypes() != null && testTypeName != null) {
            for (TestType testType : getTestTypes()) {
//                System.out.println(testType.getName());
                if (testTypeName.equals(testType.getName())) {
//                    System.out.println("found!");
                    return testType;
                }
            }
        }
//        System.out.println("requested test type is not found in product");
        return null;
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

    @XmlAttribute(required = true)
    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    @XmlAttribute(required = true)
    public String getPartRevision() {
        return partRevision;
    }

    @XmlTransient
    public String getPartNumberWithRevision() {
        return getPartRevision() == null ? getPartNumber() : getPartNumber() + "@" + getPartRevision();
    }

    public void setPartRevision(String partRevision) {
        this.partRevision = partRevision;
    }

    @XmlElement(name = "property")
    public List<ProductProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ProductProperty> properties) {
        this.properties = properties;
        if (properties != null) {
            for (ListIterator<ProductProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                ProductProperty productProperty = iterator.next();
                productProperty.setProduct(this);
                productProperty.setPosition(index);
            }
        }
    }

//    public TestSequence getTestSequence() throws IOException, JAXBException, ParserConfigurationException, SAXException, URISyntaxException, SVNException {
//        return TestSequence.unmarshal(getTestSequenceFileRevision().getNormal(getCreator()));
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (partNumber != null ? partNumber.hashCode() : 0);
        hash += (partRevision != null ? partRevision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.partNumber == null && other.getPartNumber() != null) || (this.partNumber != null && !this.partNumber.equals(other.getPartNumber()))) {
            return false;
        }
        if ((this.partRevision == null && other.getPartRevision() != null) || (this.partRevision != null && !this.partRevision.equals(other.getPartRevision()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getPartNumber() + "@" + getPartRevision();
    }

    @Override
    public Binding getBinding() {
        if (binding == null) {
            binding = new Binding();
            binding.setVariable("product", this);
        }
        return binding;
    }

    @Override
    public Object getPropertyObject(String keyString, Binding binding) {
        if (binding != null) {
            binding.setVariable("product", this);
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return tsp.getPropertyObject(getTestProject().getGroovyClassLoader(), binding);
            }
        }
        if (getTestProject() != null) {
            return getTestProject().getPropertyObject(keyString, binding);
        }
        return null;
    }

    public boolean isSerialNumberOK(String sn) {
        for (TestType tt : testTypes) {
            if (tt.isSerialNumberOK(sn)) {
                return true;
            }
        }
        return false;
    }
}
