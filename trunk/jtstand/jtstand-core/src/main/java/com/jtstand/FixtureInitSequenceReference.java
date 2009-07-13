/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FixtureInitSequenceReference.java is part of JTStand.
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

import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

/**
 *
 * @author albert_kurucz
 */
@Entity
public class FixtureInitSequenceReference extends FileRevisionReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
//    private String useSequence;
    @OneToOne(mappedBy = "initSequence")
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

//    @XmlAttribute
//    public String getUseSequence() {
//        return useSequence;
//    }
//
//    public void setUseSequence(String use) {
//        this.useSequence = use;
//    }
//
    @XmlTransient
    public TestSequence getTestSequence() throws IOException, JAXBException, ParserConfigurationException, SAXException, SVNException, URISyntaxException {
//        if (getUseSequence() != null) {
//            return getTestFixture().getTestStation().getTestProject().getTestSequence(getUseSequence());
//        }
        return TestSequence.unmarshal(getNormal(getTestFixture().getCreator()));
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testFixture != null ? testFixture.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FixtureInitSequenceReference)) {
            return false;
        }
        FixtureInitSequenceReference other = (FixtureInitSequenceReference) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testFixture == null && other.getTestFixture() != null) || (this.testFixture != null && !this.testFixture.equals(other.getTestFixture()))) {
            return false;
        }
        return true;
    }
}
