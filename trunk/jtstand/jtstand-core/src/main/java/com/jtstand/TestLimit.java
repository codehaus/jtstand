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

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 * @author albert_kurucz
 */
@Entity
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
    @ManyToOne
    private FileRevision creator;
    @ManyToOne
    private TestSequence testSequence;
    @ManyToOne
    private TestStep testStep;
    private int position;

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @XmlTransient
    public TestStep getTestStep() {
        return testStep;
    }

    public void setTestStep(TestStep testStep) {
        this.testStep = testStep;
        if (testStep != null) {
            setCreator(testStep.getRootTestStep().getCreator());
        }
    }

    @XmlTransient
    public TestSequence getTestSequence() {
        return testSequence;
    }

    public void setTestSequence(TestSequence testSequence) {
        this.testSequence = testSequence;
        if (testSequence != null) {
            setCreator(testSequence.getCreator());
        }
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
        hash += (testSequence != null ? testSequence.hashCode() : 0);
        hash += (testStep != null ? testStep.hashCode() : 0);
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
        if ((this.testSequence == null && other.getTestSequence() != null) || (this.testSequence != null && !this.testSequence.equals(other.getTestSequence()))) {
            return false;
        }
        if ((this.testStep == null && other.getTestStep() != null) || (this.testStep != null && !this.testStep.equals(other.getTestStep()))) {
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

//    @XmlAttribute(required=true)
    @XmlAttribute
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
        if (measurementUnit != null) {
            return getLslString() + measurementUnit;
        } else {
            return getLslString();
        }
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
        return lowerSpecifiedLimit != null ||
                upperSpeficiedLimit != null ||
                nominal != null;
    }
}
