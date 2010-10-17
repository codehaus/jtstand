/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jtstand;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author albert_kurucz
 */
@Entity
public class FixtureTestTypeReference extends TestTypeReference {

    @ManyToOne
    private TestFixture testFixture;

    @XmlTransient
    public TestFixture getTestFixture() {
        return testFixture;
    }

    public void setTestFixture(TestFixture testFixture) {
        this.testFixture = testFixture;
        if (testFixture != null) {
            this.setCreator(testFixture.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testFixture != null ? testFixture.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStepProperty)) {
            return false;
        }
        FixtureTestTypeReference other = (FixtureTestTypeReference) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testFixture == null && other.getTestFixture() != null) || (this.testFixture != null && !this.testFixture.equals(other.getTestFixture()))) {
            return false;
        }
        return true;
    }
}
