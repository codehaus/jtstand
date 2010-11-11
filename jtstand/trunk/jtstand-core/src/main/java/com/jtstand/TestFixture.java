/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestFixture.java is part of JTStand.
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

import java.util.Map;
import java.util.Set;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.script.Bindings;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.script.ScriptContext;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author albert_kurucz
 */
@Entity
//@XmlType(name = "testFixtureType", propOrder = {"remark", "properties", "testLimits", "testTypes", "initSequence"})
@XmlType(name = "testFixtureType", propOrder = {"remark", "properties", "testLimits", "testTypes", "initTypeReference"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestFixture extends AbstractVariables implements Bindings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fixtureName;
    private String remark;
    private Boolean disabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testFixture", fetch = FetchType.LAZY)
    @OrderBy("testFixturePropertyPosition ASC")
    private List<TestFixtureProperty> properties = new ArrayList<TestFixtureProperty>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testFixture", fetch = FetchType.LAZY)
    @OrderBy("testLimitPosition ASC")
    private List<TestFixtureLimit> testLimits = new ArrayList<TestFixtureLimit>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testFixture")
    @OrderBy("testTypeReferencePosition ASC")
    private List<FixtureTestTypeReference> testTypes = new ArrayList<FixtureTestTypeReference>();
    @ManyToOne
    private TestStation testStation;
    @ManyToOne
    private FileRevision creator;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "testFixture", fetch = FetchType.LAZY)
    private FixtureInitTestTypeReference initTypeReference;
    private int testFixturePosition;
    private String serialNumber;
    private transient final Object testLimitsLock = new Object();
    private transient final Object propertiesLock = new Object();
    private transient final Object testTypesLock = new Object();
    private transient Map<String, Object> localVariablesMap = new HashMap<String, Object>();

    public void initializeProperties() throws ScriptException {
        for (TestFixtureProperty tp : properties) {
            if (tp.isEager() != null && tp.isEager()) {
                System.out.println("Evaluating eager fixture property: " + tp.getName());
                put(tp.getName(), tp.getPropertyObject(getBindings()));
            }
        }
    }

    @XmlElement(name = "limit")
    public List<TestFixtureLimit> getTestLimits() {
        synchronized (testLimitsLock) {
            if (testLimits == null) {
                System.err.println("testLimits is null!");
            }
            return testLimits;
        }
    }

    public void setTestLimits(List<TestFixtureLimit> testLimits) {
        this.testLimits = testLimits;
        if (testLimits != null) {
            for (ListIterator<TestFixtureLimit> iterator = testLimits.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestFixtureLimit testLimit = iterator.next();
                testLimit.setTestFixture(this);
                testLimit.setPosition(index);
            }
        }
    }

    @XmlTransient
    public int getPosition() {
        return testFixturePosition;
    }

    public void setPosition(int position) {
        this.testFixturePosition = position;
    }

//    @XmlElement(name = "initSequence")
//    public FixtureInitSequenceReference getInitSequence() {
//        return initSequence;
//    }
//
//    public void setInitSequence(FixtureInitSequenceReference initSequence) {
//        this.initSequence = initSequence;
//        if (initSequence != null) {
//            initSequence.setCreator(getCreator());
//        }
//    }
    @XmlElement(name = "initType")
    public FixtureInitTestTypeReference getInitTypeReference() {
        return initTypeReference;
    }

    public void setInitTypeReference(FixtureInitTestTypeReference initTypeReference) {
        this.initTypeReference = initTypeReference;
    }

    @XmlAttribute
    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

//    @XmlTransient
//    public boolean isDisabled() {
//        return disabled != null && disabled;
//    }
    @XmlTransient
    public Long getId() {
        return id;
    }

    public FixtureTestTypeReference getTestType(String partNumber, String partRevision, String testTypeName) {
        for (FixtureTestTypeReference fttr : getTestTypes()) {
            if (fttr.getPartNumber().equals(partNumber)
                    && fttr.getPartRevision().equals(partRevision)
                    && fttr.getName().equals(testTypeName)) {
                return fttr;
            }
        }
        return null;
    }

    @XmlElement(name = "testType")
    public List<FixtureTestTypeReference> getTestTypes() {
        synchronized (testTypesLock) {
            return testTypes;
        }
    }

    public void setTestTypes(List<FixtureTestTypeReference> testTypes) {
        this.testTypes = testTypes;
        if (testTypes != null) {
            for (ListIterator<FixtureTestTypeReference> iterator = testTypes.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                FixtureTestTypeReference testTypeReference = iterator.next();
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
        synchronized (propertiesLock) {
            return properties;
        }
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
//        if (getInitSequence() != null) {
//            getInitSequence().setTestFixture(this);
//        }
        setProperties(getProperties());
        setTestLimits(getTestLimits());
        setTestTypes(getTestTypes());
        if (getInitTypeReference() != null) {
            getInitTypeReference().setTestFixture(this);
        }
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
    public Bindings getBindings() {
        return this;
    }

    @Override
    public Object getPropertyObjectUsingBindings(String keyString, Bindings bindings) throws ScriptException {
        if (bindings != null) {
            bindings.put("fixture", this);
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return tsp.getPropertyObject(bindings);
            }
        }
        if (testStation != null) {
            return testStation.getPropertyObjectUsingBindings(keyString, bindings);
        }
        return null;
    }

    public boolean containsProperty(String key) {
        if ("fixture".equals(key)) {
            return true;
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(key)) {
                return true;
            }
        }
        return getTestStation().containsProperty(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        for (Entry<? extends String, ? extends Object> variableEntry : toMerge.entrySet()) {
            put(variableEntry.getKey(), variableEntry.getValue());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        return "fixture".equals(key)
                || localVariablesMap.containsKey(key.toString())
                || containsProperty(key.toString());
    }

    @Override
    public Object get(Object key) {
        if ("$type$".equals(key)) {
            return getClass().getName();
        }
        if ("context".equals(key)) {
            return ScriptContext.ENGINE_SCOPE;
        }
        if ("fixture".equals(key)) {
            return this;
        }
        if ("id".equals(key)) {
            return id;
        }
        if ("testStation".equals(key)) {
            return testStation;
        }
        if ("testTypes".equals(key)) {
            return testTypes;
        }
        if ("position".equals(key)) {
            return testFixturePosition;
        }

        if (localVariablesMap.containsKey((String) key)) {
            return localVariablesMap.get((String) key);
        }
        try {
            return getVariable((String) key);
        } catch (ScriptException ex) {
            Logger.getLogger(TestFixture.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex.getMessage());
        } catch (InterruptedException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    @Override
    public Object remove(Object key) {
        return localVariablesMap.remove((String) key);
    }

    @Override
    public int size() {
        return keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {
        localVariablesMap.clear();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();
        for (String key : keySet()) {
            entries.add(new HashMap.SimpleEntry(key, get(key)));
        }
        return entries;
    }

    @Override
    public Set<String> keySet() {
        Set<String> keys = testStation.keySet();
        keys.add("fixture");
        keys.addAll(localVariablesMap.keySet());
        return keys;
    }

    @Override
    public boolean containsValue(Object value) {
        for (String key : keySet()) {
            if (value.equals(get(key))) {
                return true;
            }
        }
        return false;
    }

    public Object getVariable(String keyString) throws InterruptedException, ScriptException {
        for (TestFixtureProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return getVariable(keyString, tsp, this);
            }
        }
        if (getTestStation() != null) {
            return getTestStation().getVariable(keyString);
        }
        throw new IllegalArgumentException("Undefined variable:" + keyString);
    }

//    @Override
//    public Set<String> getPropertyNames() {
//        Set<String> propertyNames = new HashSet<String>();
//        for (TestProperty tp : getProperties()) {
//            propertyNames.add(tp.getName());
//        }
//        return propertyNames;
//    }
}
