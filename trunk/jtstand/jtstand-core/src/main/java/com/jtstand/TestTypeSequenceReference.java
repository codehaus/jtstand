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
public class TestTypeSequenceReference extends FileRevisionReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @OneToOne(mappedBy = "testSequence")
    private TestType testType;

    @XmlTransient
    public TestSequence getTestSequence() throws IOException, JAXBException, ParserConfigurationException, SAXException, SVNException, URISyntaxException {
        return TestSequence.unmarshal(getNormal(getTestType().getCreator()));
    }

    @XmlTransient
    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
        if (testType != null) {
            setCreator(testType.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testType != null ? testType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestTypeSequenceReference)) {
            return false;
        }
        TestTypeSequenceReference other = (TestTypeSequenceReference) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testType == null && other.getTestType() != null) || (this.testType != null && !this.testType.equals(other.getTestType()))) {
            return false;
        }
        return true;
    }
}
