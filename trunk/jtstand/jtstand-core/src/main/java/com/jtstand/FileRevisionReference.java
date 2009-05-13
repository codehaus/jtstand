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

import org.hibernate.annotations.ForceDiscriminator;
import org.tmatesoft.svn.core.SVNException;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType(propOrder = {"revision", "subversionUrl"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
@ForceDiscriminator
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
public class FileRevisionReference implements Serializable {

    public static final long serialVersionUID = 20081114L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subversionUrl;
    private Long revision;
    @ManyToOne
    private FileRevision creator;
    private String charsetName;
    @Lob
    @Column(length=2147483647)
    private String text;

    @XmlTransient
    public String getFileContent() throws URISyntaxException, SVNException, IOException {
        if (getText() == null || getText().length() == 0) {
//            System.out.println("Getting content from file...");
            setText(getNormal(getCreator()).getText(getCharsetName()));
        }
        return getText();
    }

    @XmlValue
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @XmlAttribute
    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
    }

//    public FileRevisionReference() {
//    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    protected FileRevision getNormal(FileRevision creator) throws URISyntaxException {
        Long rev = (revision != null) ? revision : creator.getRevision();
        if (rev < 1) {
            throw new IllegalArgumentException("Cannot normalize revision number: " + revision);
        }
        URI creatorURI = URI.create(creator.getSubversionUrl());
        URI specified = URI.create(getSubversionUrl());
//        System.out.println("specified: " + specified + " relative:" + !specified.isAbsolute());
        URI newURI = specified.isAbsolute() ? specified : creatorURI.resolve(specified);
        if (!specified.isAbsolute() && creator.getFile() != null) {
            return new FileRevision(newURI.toString(), rev, new File(creator.getFile().toURI().resolve(specified)));
        } else {
            return new FileRevision(newURI.toString(), rev, null);
        }
    }

    @XmlAttribute
    public String getSubversionUrl() {
        return subversionUrl;
    }

    public void setSubversionUrl(String subversionUrl) {
        this.subversionUrl = subversionUrl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (subversionUrl != null ? subversionUrl.hashCode() : 0);
        hash += (revision != null ? revision.hashCode() : 0);
        hash += (text != null ? text.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FileRevisionReference)) {
            return false;
        }
        FileRevisionReference other = (FileRevisionReference) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.subversionUrl == null && other.getSubversionUrl() != null) || (this.subversionUrl != null && !this.subversionUrl.equals(other.getSubversionUrl()))) {
            return false;
        }
        if ((this.revision == null && other.getRevision() != null) || (this.revision != null && !this.revision.equals(other.getRevision()))) {
            return false;
        }
        if ((this.text == null && other.getText() != null) || (this.text != null && !this.text.equals(other.getText()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.jtstand.FileRevision[id=" + id + ", url=" + subversionUrl + ", rev=" + revision + "]";
    }

    @XmlAttribute
    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }
}
