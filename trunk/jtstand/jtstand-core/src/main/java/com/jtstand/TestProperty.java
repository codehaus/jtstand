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
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestProperty implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String propertyValueAttribute;
    private String propertyValue;
    private Boolean mutex;
//    private Boolean eval;
    private Boolean constant;
    @ManyToOne
    private FileRevision creator;

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
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestProperty)) {
            return false;
        }
        TestProperty other = (TestProperty) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.name == null && other.getName() != null) || (this.name != null && !this.name.equals(other.getName()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return TestProperty.class.getCanonicalName() + "[id=" + id + ",name=" + name + ",value=" + propertyValue + "]";
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlValue
    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @XmlAttribute(name = "value")
    public String getPropertyValueAttribute() {
        return propertyValueAttribute;
    }

    public void setPropertyValueAttribute(String propertyValueAttribute) {
        this.propertyValueAttribute = propertyValueAttribute;
    }

    @XmlAttribute
    public Boolean isMutex() {
        return mutex;
    }

    public void setMutex(Boolean mutex) {
        this.mutex = mutex;
    }

    @XmlAttribute
    public Boolean isConstant() {
        return constant;
    }

    public void setConstant(Boolean constant) {
        this.constant = constant;
    }

//    @XmlAttribute
//    public Boolean getEval() {
//        return eval;
//    }
//
//    public void setEval(Boolean eval) {
//        this.eval = eval;
//    }
    public Object getPropertyObject(GroovyClassLoader gcl, Binding binding) {
        if (getPropertyValueAttribute() != null) {
            return getPropertyValueAttribute();
        }
        if (propertyValue == null || propertyValue.length() == 0) {
            return propertyValue;
        }
        if (gcl != null) {
            if (binding != null) {
                return (new GroovyShell(gcl, binding)).evaluate(propertyValue);
            } else {
                return (new GroovyShell(gcl)).evaluate(propertyValue);
            }
        } else {
            if (binding != null) {
                return (new GroovyShell(binding)).evaluate(propertyValue);
            } else {
                return (new GroovyShell()).evaluate(propertyValue);
            }
        }
    }
}
