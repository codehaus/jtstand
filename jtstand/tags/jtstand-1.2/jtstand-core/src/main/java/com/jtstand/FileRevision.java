/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, FileRevision.java is part of JTStand.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 *
 * @author albert_kurucz
 */
@Entity
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"subversionurl", "revision"}))
@XmlType(propOrder = {"revision", "subversionUrl"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class FileRevision implements Serializable {

    public static final long serialVersionUID = 20081114L;
//    public static FileRevision getFileRevision(String subversionUrl, Long revision) {
//        Enumeration<FileRevision> fileRevisions = cache.keys();
//        while (fileRevisions.hasMoreElements()) {
//            FileRevision fileRevision = fileRevisions.nextElement();
//            if (fileRevision.getSubversionUrl().equals(subversionUrl) && fileRevision.getRevision().equals(revision)) {
//                return fileRevision;
//            }
//        }
//        return new FileRevision(subversionUrl, revision);
//    }
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

    FileRevision resolve(String subversionUrl, Long revision) {
        Enumeration<FileRevision> keys = cache.keys();
        while (keys.hasMoreElements()) {
            FileRevision fr = keys.nextElement();
            if (fr.subversionUrl.equals(subversionUrl) && fr.revision.equals(revision)) {
                return new FileRevision(subversionUrl, revision, fr.file);
            }
        }
        if (file != null) {
            try {
                File parentFile = file.getParentFile();
                System.out.println("Path of Filer's parent: '" + parentFile.getAbsolutePath());

                URI filerURI = URI.create(subversionUrl);
                System.out.println("filerURI:" + filerURI);

                URI parentURI = filerURI.resolve(".");
                System.out.println("parentURI:" + parentURI);

                URI uri = new URI(subversionUrl);
                System.out.println("uri:" + uri);

                URI relative = parentURI.relativize(uri);
                System.out.println("Relative path:" + relative);

                File f = new File(parentFile.getPath() + File.separator + relative.toString());
                System.out.println("Resolved file path:" + f.getPath());

                return new FileRevision(subversionUrl, revision, f);
                //TBD
            } catch (Exception ex) {
                System.out.println("Exception while checking current revision of local file: " + ex);
            }
        }
        return new FileRevision(subversionUrl, revision);
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
            subversionUrl = subversionUrlorFilePath;
            Enumeration<FileRevision> keys = cache.keys();
            while (keys.hasMoreElements()) {
                FileRevision fr = keys.nextElement();
                if (fr.subversionUrl.equals(subversionUrl) && fr.revision.equals(revision)) {
                    file = fr.file;
                    break;
                }
            }
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
    private final static transient ConcurrentHashMap<FileRevision, Object> cache = new java.util.concurrent.ConcurrentHashMap<FileRevision, Object>();
    private final static transient Object CACHE_LOCK = new Object();

    public Object unmarshal(Unmarshaller un, boolean useCache)
            throws SVNException, JAXBException {
        if (useCache) {
            synchronized (CACHE_LOCK) {
                Object object = cache.get(this);
                if (object != null) {
                    return object;
                }
                object = un.unmarshal(getInputStream());
                cache.put(this, object);
                return object;
            }
        } else {
            return un.unmarshal(getInputStream());
        }
    }

    public String getText(String charsetName) throws SVNException, IOException {
//        System.out.println("Reading file...");
        InputStream in = getInputStream();
        StringBuilder out = new StringBuilder();
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
        String retval = getSubversionUrl() + "@" + getRevision();
        if (file != null) {
            retval += "@" + file.getPath();
        }
        return retval;
    }
}
