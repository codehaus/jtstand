/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestProject.java is part of JTStand.
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

import java.util.Set;
import javax.script.ScriptException;
import org.tmatesoft.svn.core.SVNException;
import org.xml.sax.SAXException;
import javax.persistence.Entity;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import javax.xml.validation.Schema;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Query;
import javax.script.ScriptEngineManager;
import javax.script.Bindings;
import javax.script.SimpleBindings;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlRootElement(name = "testProject")
//@XmlType(name = "projectType", propOrder = {"remark", "classes", "libraryReferences", "properties", "authentication", "products", "testStations"})
@XmlType(name = "projectType", propOrder = {"remark", "properties", "testLimits", "authentication", "products", "testStations"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestProject extends AbstractProperties {

    public static final Class<?>[] CLASS_LOADER_CONSTRUCTOR = {ClassLoader.class};
    public static final String TEST_PROJECT = "testProject";
    //private static final Logger LOGGER = Logger.getLogger(TestProject.class.getCanonicalName());
    public static final String STR_PERSISTING_POLICY = "PERSISTING_POLICY";
    public static final OutputStream NULL_OUTPUT_STREAM = new OutputStream() {

        @Override
        public void write(int arg0) throws IOException {
        }
    };

    public static void close() {
        if (emf != null) {
            if (emf.isOpen()) {
                emf.close();
            }
            emf = null;
        }
    }
//    public TestProject() {
//    }
    public static final String schemaLocation = "http://www.jtstand.com/ http://www.jtstand.com/jtstand-1.0.xsd";
    private static JAXBContext jc;
    private static Marshaller m;
    private static Unmarshaller um;
    final private static Object JAXB_LOCK = new Object();
    private static EntityManagerFactory emf;
    private static Schema schema;

//    @XmlTransient
//    public EntityManagerFactory getEntityManagerFactory() {
//        if (emf == null && pun != null) {
//            emf = Persistence.createEntityManagerFactory(pun);
//        }
//        return emf;
//    }
    private static JAXBContext getJAXBContext()
            throws JAXBException {
        if (jc == null) {
            jc = JAXBContext.newInstance(TestProject.class);
        }
        return jc;
    }

    public static Marshaller getMarshaller()
            throws JAXBException {
        if (m == null) {
            m = getJAXBContext().createMarshaller();
            if (TestProject.getSchemaLocation() != null) {
                m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, TestProject.getSchemaLocation());
            }
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        return m;
    }

    public static Schema getSchema() {
        return schema;
    }

    public static void setSchema(Schema newschema) {
        schema = newschema;
    }

    public static Unmarshaller getUnmarshaller()
            throws JAXBException, SAXException {
        if (um == null) {
            um = getJAXBContext().createUnmarshaller();
            um.setSchema(getSchema());
        }
        return um;
    }

//    public static TestProject query(EntityManager em, TestProject tp) {
//        if (em.contains(tp)) {
//            return tp;
//        }
//        return query(em, tp.getCreator());
//    }
    @SuppressWarnings("unchecked")
    public static TestProject query(EntityManager em, FileRevision creator) throws JAXBException {
        if (em == null || creator == null) {
            return null;
        }
        FileRevision c = FileRevision.query(em, creator);
        if (c == null) {
            System.out.println("Creator was not found in database: " + creator.getSubversionUrl() + "@" + creator.getRevision());
            return null;
        }
        try {
            Query q = em.createQuery("select tp from TestProject tp where tp.creator = :creator");
            q.setParameter("creator", c);
            TestProject testProject = (TestProject) q.getSingleResult();
            TestProject.getMarshaller().marshal(testProject, TestProject.NULL_OUTPUT_STREAM);
            return testProject;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

//    public static TestProject query(FileRevision creator) {
////        Log.log("Query TestProject: " + creator);
//        TestProject testProject = null;
//        synchronized (cacheLock) {
//            testProject = cache.get(creator);
//        }
//        if (testProject != null) {
////            Log.log("Test Project is found in cache!");
//            return testProject;
//        }
//        TestProject qTestProject = (new TestProjectQuery(creator)).query();
//        if (qTestProject != null) {
//            synchronized (cacheLock) {
//                cache.put(creator, qTestProject);
//            }
//            return qTestProject;
//        }
//        return testProject;
//    }
    public static TestProject unmarshal(FileRevision fileRevision, boolean useCache)
            throws JAXBException, SAXException, SVNException {
        //System.out.println("unmarshalling: "+fileRevision);
        synchronized (JAXB_LOCK) {
            TestProject testProject = (TestProject) fileRevision.unmarshal(getUnmarshaller(), useCache);
            testProject.setCreator(fileRevision);
            return testProject;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private FileRevision creator;
    private String remark;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_PROJECT, fetch = FetchType.LAZY)
    @OrderBy("testProjectPropertyPosition ASC")
    private List<TestProjectProperty> properties = new ArrayList<TestProjectProperty>();
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_PROJECT)
//    @OrderBy("libraryReferencePosition ASC")
//    private List<LibraryReference> libraryReferences = new ArrayList<LibraryReference>();
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_PROJECT)
//    @OrderBy("testProjectClassPosition ASC")
//    private List<TestProjectClass> classes = new ArrayList<TestProjectClass>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_PROJECT, fetch = FetchType.LAZY)
    @OrderBy("testLimitPosition ASC")
    private List<TestProjectLimit> testLimits = new ArrayList<TestProjectLimit>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_PROJECT)
    @OrderBy("productPosition ASC")
    private List<Product> products = new ArrayList<Product>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = TEST_PROJECT)
    @OrderBy("testStationPosition ASC")
    private List<TestStation> testStations = new ArrayList<TestStation>();
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "testProject")
    private Authentication authentication;
    private String name;
    private String pun;
    private String defaultHostName;
//    private String scope;
//    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    private List<Library> libraries = new ArrayList<Library>();
//    private static GroovyClassLoader cl;
    private static ScriptEngineManager manager;
    private transient final Object propertiesLock = new Object();
    private transient final Object productsLock = new Object();
    private transient final Object testStationsLock = new Object();
    private transient final Object testLimitsLock = new Object();

    @XmlElement(name = "limit")
    public List<TestProjectLimit> getTestLimits() {
        synchronized (testLimitsLock) {
            if (testLimits == null) {
                System.err.println("testLimits is null!");
            }
            return testLimits;
        }
    }

    public void setTestLimits(List<TestProjectLimit> testLimits) {
        this.testLimits = testLimits;
        if (testLimits != null) {
            for (ListIterator<TestProjectLimit> iterator = testLimits.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestProjectLimit testLimit = iterator.next();
                testLimit.setTestProject(this);
                testLimit.setPosition(index);
            }
        }
    }

    @XmlTransient
    public Map<String, String> getPeristencePropertiesMap() throws ScriptException {
        Object pppm = this.getPropertyObject("persistenceProperties");
        if (pppm != null) {
            if (pppm instanceof Map) {
                return (Map) pppm;
            } else {
                throw new IllegalArgumentException("Project's persistenceProperties should be a Map, but it is: " + pppm.getClass().getCanonicalName());
            }
        }
        return new HashMap<String, String>();
    }

//    @XmlTransient
//    public List<Library> getLibraries() {
//        synchronized (librariesLock) {
//            return libraries;
//        }
//    }
    //@XmlTransient
    public static ScriptEngineManager getScriptEngineManager() {
        if (manager == null) {
            manager = new ScriptEngineManager();
        }
        return manager;
    }

//    @XmlTransient
//    public ClassLoader getClassLoader() {
//        synchronized (classesLock) {
//            if (cl == null) {
////                cl = Thread.currentThread().getContextClassLoader();
////                try {
////                    Class gclc = cl.loadClass("groovy.lang.GroovyClassLoader");
////                    Constructor c = gclc.getConstructor(CLASS_LOADER_CONSTRUCTOR);
////                    Object gcl = (ClassLoader) c.newInstance(cl);
////
////                } catch (Exception ex) {
////                }
//
//                cl = new GroovyClassLoader(Thread.currentThread().getContextClassLoader()) {
//
//                    Map<String, String> sources = new Hashtable<String, String>();
//
//                    @Override
//                    public Class<?> parseClass(String source) {
//                        Class c = super.parseClass(source);
//                        sources.put(c.getCanonicalName(), source);
//                        return c;
//                    }
//
//                    @Override
//                    public Class<?> findClass(String name) throws ClassNotFoundException {
//                        for (TestProjectClass tpc : getClasses()) {
//                            if (tpc.getName().equals(name)) {
//                                try {
//                                    return parseClass(tpc.getFileContent());
//                                } catch (Exception ex) {
//                                    throw new ClassNotFoundException(ex.getMessage());
//                                }
//                            }
//                        }
//                        for (Library lib : libraries) {
//                            for (LibraryClass tpc : lib.getClasses()) {
//                                if (tpc.getName().equals(name)) {
//                                    try {
//                                        return parseClass(tpc.getFileContent());
//                                    } catch (Exception ex) {
//                                        throw new ClassNotFoundException(ex.getMessage());
//                                    }
//                                }
//                            }
//                        }
//                        return super.findClass(name);
//                    }
//
//                    public String getSource(String name) {
//                        return sources.get(name);
//                    }
//                };
//                if (getClasses().size() > 0) {
//                    System.out.println("Compiling individual classes...");
//                    for (TestProjectClass tpc : getClasses()) {
//                        String content = null;
//                        try {
////                            System.out.println("Loading class file content '" + tpc.getName() + "'");
//                            content = tpc.getFileContent();
////                            System.out.println("Successfully loaded class file content: '" + content + "'");
//                        } catch (URISyntaxException ex) {
//                            System.err.println("URISyntaxException while loading class '" + tpc.getName() + "'");
//                            System.err.println(ex.getMessage());
//                            ex.printStackTrace();
//                            System.exit(-1);
//                        } catch (SVNException ex) {
//                            System.err.println("SVNException while loading class '" + tpc.getName() + "'");
//                            System.err.println(ex.getMessage());
//                            ex.printStackTrace();
//                            System.exit(-1);
//                        } catch (IOException ex) {
//                            System.err.println("IOException while loading class '" + tpc.getName() + "'");
//                            System.err.println(ex.getMessage());
//                            ex.printStackTrace();
//                            System.exit(-1);
//                        }
//                        try {
////                            System.out.println("Compiling class '" + tpc.getName() + "'");
//                            Class c = cl.parseClass(content);
//                            if (!c.getCanonicalName().equals(tpc.getName())) {
//                                throw new IllegalStateException("Class name mismatch! Specified name: '" + tpc.getName() + "' in class text: '" + c.getCanonicalName() + "'");
//                            }
//                            System.out.println("Successfully compiled class: '" + c.getCanonicalName() + "'");
//                        } catch (Exception ex) {
//                            System.err.println("Parsing: '" + content + "'");
//                            System.err.println(ex.getMessage());
//                            ex.printStackTrace();
//                            System.exit(-1);
//                        }
//                    }
//                }
//                if (getLibraryReferences().size() > 0) {
//                    System.out.println("Loading libraries...");
//                    try {
//                        for (LibraryReference libref : getLibraryReferences()) {
//                            libraries.add(libref.getLibrary());
//                        }
//                    } catch (Exception ex) {
//                        System.out.println("Exception while loading libraries: " + ex.getMessage());
//                        ex.printStackTrace();
//                        System.exit(-1);
//                    }
//                    System.out.println("Compiling libraries...");
//                    for (Library lib : libraries) {
//                        for (LibraryClass tpc : lib.getClasses()) {
//                            String content = null;
//                            try {
//                                content = tpc.getFileContent();
//                                LOGGER.fine("successfully loaded class file content:\n" + content);
//                            } catch (URISyntaxException ex) {
//                                System.err.println("URISyntaxException while loading class '" + tpc.getName() + "'");
//                                System.err.println(ex.getMessage());
//                                ex.printStackTrace();
//                                System.exit(-1);
//                            } catch (SVNException ex) {
//                                System.err.println("SVNException while loading class '" + tpc.getName() + "'");
//                                System.err.println(ex.getMessage());
//                                ex.printStackTrace();
//                                System.exit(-1);
//                            } catch (IOException ex) {
//                                System.err.println("IOException while loading class '" + tpc.getName() + "'");
//                                System.err.println(ex.getMessage());
//                                ex.printStackTrace();
//                                System.exit(-1);
//                            }
//                            try {
//                                Class c = cl.parseClass(content);
//                                if (!c.getCanonicalName().equals(tpc.getName())) {
//                                    throw new IllegalStateException("Class name mismatch! Specified name: '" + tpc.getName() + "' in class text: '" + c.getCanonicalName() + "'");
//                                }
//                                LOGGER.fine("successfully parsed class: " + c.getCanonicalName());
//                            } catch (CompilationFailedException ex) {
//                                System.err.println("Parsing:\n" + content);
//                                System.err.println(ex.getMessage());
//                                ex.printStackTrace();
//                                System.exit(-1);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return cl;
//    }
//    public TestSequence getTestSequence(String use) {
//        for (TestSequence seq : getSequences()) {
//            if (use.equals(seq.getName())) {
//                return seq;
//            }
//        }
//        throw new IllegalArgumentException("Sequence with name '" + use + "' does not exist");
//    }
//    public static EntityManager getEntityManager() {
//        synchronized (emLock) {
//            if (em == null) {
//                em = getEntityManagerFactory().createEntityManager();
//            }
//
//            if (!Thread.currentThread().equals(lockerThread)) {
//                if (lockerThread != null) {
//                    Log.log("Waiting for " + lockerThread + "...");
//                    long waitingStarted = System.currentTimeMillis();
//                    try {
//                        lockerThread.join();
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(TestProject.class.getName()).log(Level.SEVERE, null, ex);
//                        Log.log(" is interrupted. It ");
//                    }
//                    Log.log("Waiting for " + lockerThread + " took " + Long.toString(System.currentTimeMillis() - waitingStarted) + "ms");
//                }
//                lockerThread = Thread.currentThread();
//            }
//            return em;
//        }
//    }
    @XmlAttribute(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getPun() {
        return pun;
    }

    public void setPun(String pun) {
        this.pun = pun;
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        //Log.log("Setting creator on testProject...");
        this.creator = creator;
        if (authentication != null) {
            authentication.setTestProject(this);
        }
        setProperties(getProperties());
        setTestLimits(getTestLimits());
        setTestStations(getTestStations());
        setProducts(getProducts());
//        setClasses(getClasses());
//        setLibraryReferences(getLibraryReferences());
        //Log.log("Setting creator on testProject OK");
    }

    public static String getSchemaLocation() {
        return schemaLocation;
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElement
    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    @XmlElement(name = "property")
    public List<TestProjectProperty> getProperties() {
        synchronized (propertiesLock) {
            return properties;
        }
    }

    public void setProperties(List<TestProjectProperty> properties) {
        this.properties = properties;
        if (properties != null) {
            for (ListIterator<TestProjectProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestProjectProperty testProjectProperty = iterator.next();
                testProjectProperty.setTestProject(this);
                testProjectProperty.setPosition(index);
            }
        }
    }

    public TestType getTestType(TestTypeReference testTypeReference) {
        return getProduct(testTypeReference).getTestType(testTypeReference.getName());
    }

    public Product getProduct(TestTypeReference testType) {
        if (testType == null) {
            return null;
        }
        String pn = testType.getPartNumber();
        String pr = testType.getPartRevision();
        if (getProducts() != null) {
            for (Product p : getProducts()) {
                if (p.getPartNumber().equals(pn) && p.getPartRevision().equals(pr)) {
                    return p;
                }
            }
        }
        throw new IllegalArgumentException("Product does not exist in this project: " + testType.getPartNumber() + "@" + testType.getPartRevision());
    }

    public TestType getTestType(TestType testType) {
        if (testType != null) {
            Product p = getProduct(testType.getProduct());
            if (p != null) {
                return p.getTestType(testType.getName());
            }
        }
        return null;
    }

    public Product getProduct(Product product) {
        if (product != null) {
            if (getProducts() != null) {
                for (Product p : getProducts()) {
                    if (p.getPartNumber().equals(product.getPartNumber()) && p.getPartRevision().equals(product.getPartRevision())) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    @XmlElement(name = "product")
    public List<Product> getProducts() {
        synchronized (productsLock) {
            return products;
        }
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        if (products != null) {
            for (ListIterator<Product> iterator = products.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                Product product = iterator.next();
                product.setTestProject(this);
                product.setPosition(index);
            }
        }
    }

    @XmlElement(name = "testStation")
    public List<TestStation> getTestStations() {
        synchronized (testStationsLock) {
            return testStations;
        }
    }

    public void setTestStations(List<TestStation> testStations) {
        this.testStations = testStations;
        if (testStations != null) {
            for (ListIterator<TestStation> iterator = testStations.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestStation testStation = iterator.next();
                testStation.setTestProject(this);
                testStation.setPosition(index);
            }
        }
    }

    public TestStation getTestStation(String host) {
        List<TestStation> stations = getTestStations();
        if (stations != null) {
            for (TestStation ts : stations) {
                if (ts.getHostName().equals(host)) {
                    return ts;
                }
            }
        }
        throw new IllegalArgumentException("Station configuration cannot be found for host: " + host);
    }

//    @XmlElement(name = "library")
//    public List<LibraryReference> getLibraryReferences() {
//        synchronized (libraryReferencesLock) {
//            return libraryReferences;
//        }
//    }
//
//    public void setLibraryReferences(List<LibraryReference> libraryReferences) {
//        this.libraryReferences = libraryReferences;
//        if (libraryReferences != null) {
//            for (ListIterator<LibraryReference> iterator = libraryReferences.listIterator(); iterator.hasNext();) {
//                int index = iterator.nextIndex();
//                LibraryReference libraryReference = iterator.next();
//                libraryReference.setTestProject(this);
//                libraryReference.setPosition(index);
//            }
//        }
//    }
//    @XmlElement(name = "class")
//    public List<TestProjectClass> getClasses() {
//        synchronized (classesLock) {
//            return classes;
//        }
//    }
//
//    public void setClasses(List<TestProjectClass> classes) {
//        this.classes = classes;
//        if (classes != null) {
//            for (ListIterator<TestProjectClass> iterator = classes.listIterator(); iterator.hasNext();) {
//                int index = iterator.nextIndex();
//                TestProjectClass testProjectClass = iterator.next();
//                testProjectClass.setTestProject(this);
//                testProjectClass.setPosition(index);
//            }
//        }
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestProject)) {
            return false;
        }
        TestProject other = (TestProject) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return TestProject.class.getCanonicalName() + "[id=" + id + "]";
    }

    @XmlAttribute
    public String getDefaultHostName() {
        return defaultHostName;
    }

    public void setDefaultHostName(String defaultHostName) {
        this.defaultHostName = defaultHostName;
    }

    public TestStation getTestStationOrDefault(String hostName) throws UnknownHostException {
        String h = hostName == null ? InetAddress.getLocalHost().getHostName().toUpperCase() : hostName;
        for (TestStation ts : getTestStations()) {
            if (ts.getHostName().equalsIgnoreCase(h)) {
                return ts;
            }
        }
        if (defaultHostName != null) {
            for (TestStation ts : getTestStations()) {
                if (ts.getHostName().equalsIgnoreCase(defaultHostName)) {
                    return ts;
                }
            }
            throw new IllegalArgumentException("wrong default host name: " + defaultHostName);
        }
        throw new IllegalArgumentException("Station configuration cannot be found for host: " + hostName);
    }
    protected transient Bindings bindings;

    @Override
    public Bindings getBindings() {
        if (bindings == null) {
            bindings = new SimpleBindings();
            bindings.put("project", this);
        }
        return bindings;
    }

    @Override
    public Object getPropertyObjectUsingBindings(String keyString, Bindings bindings) throws ScriptException {
//        if (bindings != null) {
//            bindings.put("project", this);
//        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return tsp.getPropertyObject(bindings);
            }
        }
        try {
            String prop = System.getProperty(keyString);
            if (prop != null) {
                return prop;
            }
        } catch (IllegalArgumentException ex1) {
            ex1.printStackTrace();
        } catch (SecurityException ex2) {
            ex2.printStackTrace();
        }
        try {
            return System.getenv(keyString);
        } catch (IllegalArgumentException ex1) {
            ex1.printStackTrace();
        } catch (SecurityException ex2) {
            ex2.printStackTrace();
        }
        return null;
    }

    public boolean isSerialNumberOK(String sn, String partNumber, String partRevision, String testTypeName, List<FixtureTestTypeReference> testTypeReferences) {
        for (TestTypeReference ttr : testTypeReferences) {
            TestType tt = getTestType(ttr);
            if (tt != null) {
                if ((partNumber == null || partNumber.equals(tt.getProduct().getPartNumber()))
                        && (partRevision == null || partRevision.equals(tt.getProduct().getPartRevision()))
                        && (testTypeName == null || testTypeName.equals(tt.getName()))) {
                    if (tt.isSerialNumberOK(sn)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isSerialNumberOK(String sn, String partNumber, String partRevision, String testTypeName) {
        for (Product product : getProducts()) {
            if ((partNumber == null || product.getPartNumber().equals(partNumber))
                    && (partRevision == null || product.getPartRevision().equals(partRevision))) {
                for (TestType tt : product.getTestTypes()) {
                    if (testTypeName == null || tt.getName().equals(testTypeName)) {
                        if (tt.isSerialNumberOK(sn)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean containsProperty(String key) {
        if ("project".equals(key)) {
            return true;
        }
        if (bindings != null && bindings.containsKey(key)) {
            return true;
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(key)) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public Set<String> getPropertyNames() {
//        Set<String> propertyNames = new HashSet<String>();
//        for (TestProperty tp : getProperties()) {
//            propertyNames.add(tp.getName());
//        }
//        return propertyNames;
//    }
}
