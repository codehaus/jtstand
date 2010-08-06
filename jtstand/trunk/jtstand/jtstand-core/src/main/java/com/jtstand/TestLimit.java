/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestLimit.java is part of JTStand.
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

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.hibernate.annotations.ForceDiscriminator;

/**
 *
 * @author albert_kurucz
 */
@Entity
@ForceDiscriminator
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@XmlType(propOrder = {"measurementUnit", "upperSpeficiedLimit", "lowerSpecifiedLimit", "nominal", "comp", "name", "remark"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestLimit implements Serializable {

    public static final long serialVersionUID = 20081114L;

    public static enum Comp {

        EQ, NE, GT, GE, LT, LE, GTLT, GELE, GELT, GTLE, BOOL
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String remark;
    @Basic(fetch = FetchType.EAGER)
    private Comp comp;
    private Double lowerSpecifiedLimit;
    private Double upperSpeficiedLimit;
    private Double nominal;
    private Double targetCPL;
    private Double targetCPU;
    private String measurementUnit;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testLimit", fetch = FetchType.LAZY)
//    private List<TestStepNamePath> testStepNamePaths = new ArrayList<TestStepNamePath>();
    @ManyToOne
    private FileRevision creator;
    private int testLimitPosition;

//    @XmlTransient
//    public List<TestStepNamePath> getTestStepNamePaths() {
//        return testStepNamePaths;
//    }
//
//    public void setTestStepNamePaths(List<TestStepNamePath> testStepNamePaths) {
//        this.testStepNamePaths = testStepNamePaths;
//    }
    @XmlTransient
    public int getPosition() {
        return testLimitPosition;
    }

    public void setPosition(int position) {
        this.testLimitPosition = position;
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
        if (!(object instanceof TestLimit)) {
            return false;
        }
        TestLimit other = (TestLimit) object;
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
        return TestLimit.class.getCanonicalName() + "[id=" + id + "]";
    }

    @XmlAttribute(name = "comp", required = true)
    public Comp getComp() {
        return comp;
    }

    public void setComp(Comp comp) {
        this.comp = comp;
    }

    @XmlAttribute(name = "lsl")
    public Double getLowerSpecifiedLimit() {
        return lowerSpecifiedLimit;
    }

    public void setLowerSpecifiedLimit(Double lsl) {
        this.lowerSpecifiedLimit = lsl;
    }

    @XmlAttribute(name = "usl")
    public Double getUpperSpeficiedLimit() {
        return upperSpeficiedLimit;
    }

    public void setUpperSpeficiedLimit(Double usl) {
        this.upperSpeficiedLimit = usl;
    }

    @XmlAttribute(name = "targetCPL")
    public Double getTargetCPL() {
        return targetCPL;
    }

    public void setTargetCPL(Double targetCPL) {
        this.targetCPL = targetCPL;
    }

    @XmlAttribute(name = "targetCPU")
    public Double getTargetCPU() {
        return targetCPU;
    }

    public void setTargetCPU(Double targetCPU) {
        this.targetCPU = targetCPU;
    }

    @XmlAttribute(name = "unit")
    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlAttribute
    public Double getNominal() {
        return nominal;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    @XmlTransient
    public String getLslStringWithUnit() {
        if (!isNumericKind()) {
            return "";
        }
        if (measurementUnit != null) {
            return getLslString() + measurementUnit;
        }
        return getLslString();
    }

    @XmlTransient
    public String getLslString() {
        if (nominal != null) {
            switch (comp) {
                case EQ:
                    return "=" + nominal;
                case NE:
                    return "!=" + nominal;
                case BOOL:
                    return "=true";
            }
        }
        if (comp == null || lowerSpecifiedLimit == null) {
            return "";
        }
        switch (comp) {
            case GT:
            case GTLT:
            case GTLE:
                return ">" + lowerSpecifiedLimit;
            case GE:
            case GELE:
            case GELT:
                return ">=" + lowerSpecifiedLimit;
            default:
                return "";
        }
    }

    @XmlTransient
    public String getUslStringWithUnit() {
        if (!isNumericKind()) {
            return "";
        }
        if (measurementUnit != null) {
            return getUslString() + measurementUnit;
        } else {
            return getUslString();
        }
    }

    @XmlTransient
    public String getUslString() {
        if (comp == null || upperSpeficiedLimit == null) {
            return "";
        }
        switch (comp) {
            case LT:
            case GTLT:
            case GELT:
                return "<" + upperSpeficiedLimit;
            case GELE:
            case GTLE:
            case LE:
                return "<=" + upperSpeficiedLimit;
            default:
                return "";
        }
    }

    @XmlTransient
    public boolean isNumericKind() {
        return lowerSpecifiedLimit != null
                || upperSpeficiedLimit != null
                || nominal != null;
    }
}
