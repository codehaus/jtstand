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

import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;

import javax.persistence.*;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"subversionurl", "revision"}))
@XmlType(propOrder = {"revision", "subversionUrl"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class FileRevision implements Serializable {

    public static final long serialVersionUID = 20081114L;
    private String subversionUrl;
    private Long revision;
    private transient File file;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public FileRevision() {
    }

    public static FileRevision query(EntityManager em, FileRevision creator) {
        if (em == null) {
            return null;
        }
        if (creator == null) {
            throw new IllegalArgumentException("creator parameter cannot be null");
        }
        if (creator.getId() != null && em.contains(creator)) {
            return creator;
        }
        try {
            Query q1 = em.createQuery("from FileRevision frev where frev.subversionUrl = :subversionUrl and frev.revision = :revision");
            q1.setParameter("subversionUrl", creator.getSubversionUrl());
            q1.setParameter("revision", creator.getRevision());
            FileRevision fr = (FileRevision) q1.getSingleResult();
            fr.setFile(creator.getFile());
            return fr;
        } catch (Exception ex) {
            return null;
        }
    }

//    public static FileRevision query(final FileRevision creator) {
//        return (new FileRevisionQuery(creator)).query();
//    }
    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlAttribute
    public String getSubversionUrl() {
        return subversionUrl;
    }

    public void setSubversionUrl(String subversionUrl) {
        this.subversionUrl = subversionUrl;
    }

    @XmlAttribute
    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

//    public FileRevision() {
//    }
    public FileRevision(String subversionUrlorFilePath, Long revision) {
        this.revision = revision;
        File checkfile = new File(subversionUrlorFilePath);
        if (checkfile.isFile()) {
            SVNClientManager cm = SVNClientManager.newInstance();
            try {
                SVNStatus svns = cm.getStatusClient().doStatus(checkfile, false);
                long currentRevision = svns.getCommittedRevision().getNumber();
                if (revision == 0) {
                    this.revision = currentRevision;
                }
                System.out.println("URL of the file: " + svns.getURL());
                System.out.println("Current revision of the file: " + Long.toString(currentRevision));
                file = checkfile;
                this.subversionUrl = svns.getURL().toString();
            } catch (SVNException ex) {
                Logger.getLogger(FileRevision.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
                System.exit(1);
            }
        } else {
            if (revision == 0) {
                this.revision = -1L;
            }
            this.subversionUrl = subversionUrlorFilePath;
        }
    }

    public FileRevision(String subversionUrl, Long revision, File file) {
        this.subversionUrl = subversionUrl;
        this.revision = revision;
        this.file = file;
    }

    @XmlTransient
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Object unmarshal(Unmarshaller un)
            throws SVNException, JAXBException {
        return un.unmarshal(getInputStream());
    }

//    public String getText() throws SVNException, IOException {
//        InputStream in = getInputStream();
//        StringBuffer out = new StringBuffer();
//        byte[] b = new byte[4096];
//        for (int n; (n = in.read(b)) != -1;) {
//            out.append(new String(b, 0, n));
//        }
//        return out.toString();
//    }
    public String getText(String charsetName) throws SVNException, IOException {
//        System.out.println("Reading file...");
        InputStream in = getInputStream();
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1;) {
            if (charsetName == null) {
                out.append(new String(b, 0, n));
            } else {
                out.append(new String(b, 0, n, charsetName));
            }
        }
        return out.toString();
    }

    private InputStream getInputStream() throws SVNException {
        if (file != null && file.isFile()) {
            System.out.println("Path: '" + file.getAbsolutePath() + "' revision: '" + revision + "'");
            SVNClientManager cm = SVNClientManager.newInstance();
            try {
                SVNStatus svns = cm.getStatusClient().doStatus(file, false);
                if (SVNStatusType.STATUS_NORMAL.equals(svns.getContentsStatus())) {
                    long committedRevision = svns.getCommittedRevision().getNumber();
                    if (svns.getURL().toDecodedString().equals(subversionUrl)) {
                        if (committedRevision == revision) {
                            return new FileInputStream(file);
                        } else {
                            System.out.println("Committed revision of the file is " + Long.toString(committedRevision) + " instead of " + revision + "!");
                        }
                    } else {
                        System.out.println("Local copy URL: '" + svns.getURL().toDecodedString() + "' does not match!");
                    }
                } else {
                    System.out.println("Status is '" + svns.getContentsStatus() + "' instead of '" + SVNStatusType.STATUS_NORMAL + "'!");
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Exception while reading local file: " + ex);
            } catch (Exception ex) {
                System.out.println("Exception while checking current revision of local file: " + ex);
            }
        }
        System.out.println("URL: '" + subversionUrl + "' revision: '" + revision + "'");
        DAVRepositoryFactory.setup();
        int pos = subversionUrl.lastIndexOf('/');
        String url = subversionUrl.substring(0, pos);
//        System.out.println("URL: " + url);
        String filePath = subversionUrl.substring(pos + 1);
//        System.out.println("Path: " + filePath);
        SVNRepository repository = DAVRepositoryFactory.create(SVNURL.parseURIDecoded(url));
        SVNNodeKind nodeKind = repository.checkPath(filePath, -1);
        if (nodeKind.equals(SVNNodeKind.NONE)) {
            throw new IllegalArgumentException("Specified file with URL: '" + subversionUrl + "' is not versioned");
        } else if (nodeKind.equals(SVNNodeKind.DIR)) {
            throw new IllegalArgumentException("Specified file with URL: '" + subversionUrl + "' is a directory");
        }

        //Map fileProperties = new HashMap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SVNProperties props = new SVNProperties();
        repository.getFile(filePath, revision, props, baos);
        Long rev = Long.parseLong(props.getStringValue(SVNProperty.COMMITTED_REVISION));
        if (!rev.equals(revision)) {
            System.out.println("Changing revision from: '" + revision + "' to: '" + rev + "'...");
            revision = rev;
        }
        repository.closeSession();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subversionUrl != null ? subversionUrl.hashCode() : 0);
        hash += (revision != null ? revision.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FileRevision)) {
            return false;
        }
        FileRevision other = (FileRevision) object;
        if ((this.subversionUrl == null && other.getSubversionUrl() != null) || (this.subversionUrl != null && !this.subversionUrl.equals(other.getSubversionUrl()))) {
            return false;
        }
        if ((this.revision == null && other.getRevision() != null) || (this.revision != null && !this.revision.equals(other.getRevision()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getSubversionUrl() + "@" + getRevision();
    }
}
