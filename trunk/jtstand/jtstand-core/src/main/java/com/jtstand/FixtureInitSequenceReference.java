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
