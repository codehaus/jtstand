/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestProperty.java is part of JTStand.
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

import javax.script.ScriptException;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.hibernate.annotations.DiscriminatorOptions;

/**
 *
 * @author albert_kurucz
 */
@Entity
@DiscriminatorOptions(force = true)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@XmlType
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String propertyValueAttribute;
    private String interpreter;
    @Lob
    @Column(length = 2147483647)
    private String propertyValue;
    private Boolean mutex;
    private Boolean finalVariable;
//    private Boolean eager;
    @ManyToOne
    private FileRevision creator;
//    private String interpreter;

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

//    @XmlAttribute
//    public Boolean isEager() {
//        return eager;
//    }
//
//    public void setEager(Boolean eager) {
//        this.eager = eager;
//    }

    @XmlAttribute(name = "final")
    public Boolean isFinal() {
        return finalVariable;
    }

    public void setFinal(Boolean finalVariable) {
        this.finalVariable = finalVariable;
    }

    @XmlAttribute
    public String getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(String interpreter) {
        this.interpreter = interpreter;
    }

    public Object getPropertyObject(Bindings bindings) throws ScriptException {
        if (getPropertyValueAttribute() != null) {
            //System.out.println(name + ":" + getPropertyValueAttribute());
            return getPropertyValueAttribute();
        }
        if (propertyValue == null || propertyValue.length() == 0) {
            return null;
        }
//        if (classLoader != null) {
//            Thread.currentThread().setContextClassLoader(classLoader);
//        }
        ScriptEngine engine = TestProject.getScriptEngineManager().getEngineByName((getInterpreter() == null) ? "groovy" : getInterpreter());
        return engine.eval(getPropertyValue(), bindings);
    }
}
