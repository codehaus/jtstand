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
public class StepReference extends FileRevisionReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @OneToOne(mappedBy = "stepReference")
    private TestStep testStep;

    @XmlTransient
    public TestStep getTestStep() {
        return testStep;
    }

    public void setTestStep(TestStep testStep) {
        this.testStep = testStep;
        if (testStep != null) {
            setCreator(testStep.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash += (testStep != null ? testStep.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StepReference)) {
            return false;
        }
        StepReference other = (StepReference) object;
        if (!super.equals(other)) {
            return false;
        }
        if ((this.testStep == null && other.getTestStep() != null) || (this.testStep != null && !this.testStep.equals(other.getTestStep()))) {
            return false;
        }
        return true;
    }

    public TestStep getTestStep(TestStepInstance step) throws URISyntaxException, IOException, JAXBException, ParserConfigurationException, SAXException, SVNException {
        FileRevision creator = testStep.getStepReference().getNormal(testStep.getCreator());
        if (step.getParent() != null) {
            step.getParent().checkLoop(creator);
        }
        TestStep calledStep = step.getTestSequenceInstance().getTestStep(creator);
        if (calledStep == null) {
            calledStep = TestStep.unmarshal(creator);
            step.getTestSequenceInstance().putTestStep(creator, calledStep);
        }
        return calledStep;
    }
}
