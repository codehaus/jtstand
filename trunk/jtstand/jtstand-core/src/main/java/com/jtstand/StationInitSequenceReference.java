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
public class StationInitSequenceReference extends FileRevisionReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @OneToOne(mappedBy = "initSequence")
    private TestStation testStation;

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

    @XmlTransient
    public TestSequence getTestSequence() throws IOException, JAXBException, ParserConfigurationException, SAXException, SVNException, URISyntaxException {
        return TestSequence.unmarshal(getNormal(getTestStation().getCreator()));
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testStation != null ? testStation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StationInitSequenceReference)) {
            return false;
        }
        StationInitSequenceReference other = (StationInitSequenceReference) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testStation == null && other.getTestStation() != null) || (this.testStation != null && !this.testStation.equals(other.getTestStation()))) {
            return false;
        }
        return true;
    }
}
