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
@XmlType(name = "testFixtureType", propOrder = {"remark", "properties", "testTypes", "initSequence"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestFixture extends AbstractVariables implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fixtureName;
    private String remark;
    private Boolean disabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testFixture")
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestFixtureProperty> properties = new ArrayList<TestFixtureProperty>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testFixture")
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestTypeReference> testTypes = new ArrayList<TestTypeReference>();
    @ManyToOne
    private TestStation testStation;
    @ManyToOne
    private FileRevision creator;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FixtureInitSequenceReference initSequence;
    private int position;
    private String serialNumber;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @XmlElement(name = "initSequence")
    public FixtureInitSequenceReference getInitSequence() {
        return initSequence;
    }

    public void setInitSequence(FixtureInitSequenceReference initSequence) {
        this.initSequence = initSequence;
        if (initSequence != null) {
            initSequence.setCreator(getCreator());
        }
    }

    @XmlAttribute
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled != null && disabled;
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlElement(name = "testType")
    public List<TestTypeReference> getTestTypes() {
        return testTypes;
    }

    public void setTestTypes(List<TestTypeReference> testTypes) {
        this.testTypes = testTypes;
        if (testTypes != null) {
            for (ListIterator<TestTypeReference> iterator = testTypes.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestTypeReference testTypeReference = iterator.next();
                testTypeReference.setTestFixture(this);
                testTypeReference.setPosition(index);
            }
        }
    }

    @XmlAttribute(required = true)
    public String getFixtureName() {
        return fixtureName;
    }

    public void setFixtureName(String fixtureName) {
        this.fixtureName = fixtureName;
    }

    @XmlAttribute
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElement(name = "property")
    public List<TestFixtureProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TestFixtureProperty> properties) {
        this.properties = properties;
        if (properties != null) {
            for (ListIterator<TestFixtureProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestFixtureProperty testFixtureProperty = iterator.next();
                testFixtureProperty.setTestFixture(this);
                testFixtureProperty.setPosition(index);
            }
        }
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        if (getInitSequence() != null) {
            getInitSequence().setTestFixture(this);
        }
        setProperties(getProperties());
        setTestTypes(getTestTypes());
    }

    @XmlTransient
    public TestStation getTestStation() {
        return testStation;
    }

    public void setTestStation(TestStation testStation) {
        this.testStation = testStation;
        if (testStation != null) {
            setCreator(testStation.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (fixtureName != null ? fixtureName.hashCode() : 0);
        hash += (testStation != null ? testStation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestFixture)) {
            return false;
        }
        TestFixture other = (TestFixture) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.fixtureName == null && other.getFixtureName() != null) || (this.fixtureName != null && !this.fixtureName.equals(other.getFixtureName()))) {
            return false;
        }
        if ((this.testStation == null && other.getTestStation() != null) || (this.testStation != null && !this.testStation.equals(other.getTestStation()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return TestFixture.class.getCanonicalName() + "[id=" + id + "]";
    }

    @Override
    public Binding getBinding() {
        if (binding == null) {
            binding = new Binding();
            binding.setVariable("fixture", this);
        }
        return binding;
    }

    @Override
    public Object getPropertyObject(String keyString, Binding binding) {
        if (binding != null) {
            binding.setVariable("fixture", this);
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return tsp.getPropertyObject(getTestStation().getTestProject().getGroovyClassLoader(), binding);
            }
        }
        if (testStation != null) {
            return testStation.getPropertyObject(keyString, binding);
        }
        return null;
    }
}
