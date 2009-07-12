/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, TestStation.java is part of JTStand.
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
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jtstand;

import com.jtstand.query.GeneralQuery;
import groovy.lang.Binding;
import org.hibernate.ejb.HibernateEntityManagerFactory;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
@Entity
@XmlType(name = "testStationType", propOrder = {"hostName", "remark", "properties", "testTypes", "fixtures", "initSequence"})
@XmlAccessorType(value = XmlAccessType.PROPERTY)
public class TestStation extends AbstractVariables implements Serializable {

    public static final long serialVersionUID = 20081114L;
    private static final Logger LOGGER = Logger.getLogger(TestStation.class.getCanonicalName());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hostName;
    private String remark;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testStation")
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestStationProperty> properties = new ArrayList<TestStationProperty>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testStation")
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestTypeReference> testTypes = new ArrayList<TestTypeReference>();
    @ManyToOne
    private FileRevision creator;
    @ManyToOne
    private TestProject testProject;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "testStation")
    @OrderBy(TestProject.POSITION_ASC)
    private List<TestFixture> fixtures = new ArrayList<TestFixture>();
    @OneToOne(cascade = CascadeType.ALL)
    private StationInitSequenceReference initSequence;
    private int position;
    private static EntityManagerFactory entityManagerFactory;
//    private Integer rmiPort;
//
//    @XmlAttribute
//    public Integer getRmiPort() {
//        return rmiPort;
//    }
//
//    public void setRmiPort(Integer rmiPort) {
//        this.rmiPort = rmiPort;
//    }

    public EntityManager createEntityManager() {
        EntityManagerFactory theEmf = getEntityManagerFactory();
        if (theEmf == null) {
            return null;
        }
        return theEmf.createEntityManager();
    }

    public TestSequenceInstance getTestSequenceInstance(long id) {
        EntityManagerFactory theEmf = getEntityManagerFactory();
        if (theEmf == null) {
            return null;
        }
        EntityManager em = createEntityManager();
        if (em == null) {
            return null;
        }
        TestSequenceInstance seq = em.find(TestSequenceInstance.class, id);
        if (seq != null) {
            seq.em = em;
        }
        return seq;
    }

    @XmlTransient
    public EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null && getTestProject() != null && getTestProject().getPun() != null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(getTestProject().getPun(), getPeristencePropertiesFixedMap());
        }
        return entityManagerFactory;
    }

    @XmlTransient
    public Map<String, String> getPeristencePropertiesFixedMap() {
        Map<String, String> map = getPeristencePropertiesMap();
        if (getPeristencePropertiesMap().get("hibernate.connection.driver_class") == null) {
            map.put("hibernate.connection.driver_class", getDriver().driverClass);
        }
        if (getPeristencePropertiesMap().get("hibernate.dialect") == null) {
            map.put("hibernate.dialect", getDriver().dialect);
        }
        if (getPeristencePropertiesMap().get("hibernate.connection.url") == null) {
            map.put("hibernate.connection.url", getPersistenceProtocol() + getPersistencePath());
        }
        if (Driver.derby.equals(getDriver()) && System.getProperty("derby.stream.error.file") == null) {
            System.setProperty("derby.stream.error.file", getUserHome() + File.separator + ".jtstand" + File.separator + "derby.log");
        }
        return map;
    }

    @XmlTransient
    private Map<String, String> getPeristencePropertiesMap() {
        Map<String, String> ppm = getTestProject() != null ? getTestProject().getPeristencePropertiesMap() : new HashMap<String, String>();
        Object sppm = getPropertyObject("persistenceProperties");
        if (sppm != null) {
            if (sppm instanceof Map) {
                ppm.putAll((Map) sppm);
            } else {
                throw new IllegalArgumentException("Stations's persistenceProperties should be a Map, but it is: " + sppm.getClass().getCanonicalName());
            }
        }
        return ppm;
    }

    @XmlTransient
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @XmlElement(name = "initSequence")
    public StationInitSequenceReference getInitSequence() {
        return initSequence;
    }

    public void setInitSequence(StationInitSequenceReference initSequence) {
        this.initSequence = initSequence;
        if (initSequence != null) {
            initSequence.setCreator(getCreator());
        }
    }

    @XmlTransient
    public Long getId() {
        return id;
    }

    @XmlElement(name = "testType")
    public List<TestTypeReference> getTestTypes() {
        return testTypes;
    }

    public void setTestTypes(List<TestTypeReference> testTypes) {
        this.testTypes = testTypes;
        if (testTypes != null) {
            for (ListIterator<TestTypeReference> iterator = testTypes.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestTypeReference testTypeReference = iterator.next();
                testTypeReference.setTestStation(this);
                testTypeReference.setPosition(index);
            }
        }
    }

    @XmlAttribute
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @XmlElement
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElement(name = "property")
    public List<TestStationProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TestStationProperty> properties) {
        this.properties = properties;
        if (properties != null) {
            for (ListIterator<TestStationProperty> iterator = properties.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestStationProperty testStationProperty = iterator.next();
                testStationProperty.setTestStation(this);
                testStationProperty.setPosition(index);
            }
        }
    }

    @XmlElement(name = "fixture")
    public List<TestFixture> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<TestFixture> fixtures) {
        this.fixtures = fixtures;
        if (fixtures != null) {
            for (ListIterator<TestFixture> iterator = fixtures.listIterator(); iterator.hasNext();) {
                int index = iterator.nextIndex();
                TestFixture testFixture = iterator.next();
                testFixture.setTestStation(this);
                testFixture.setPosition(index);
            }
        }
    }

    public TestFixture getTestFixture(String name) {
        List<TestFixture> testFixtures = getFixtures();
        if (testFixtures != null) {
            for (TestFixture tf : testFixtures) {
                if (tf.getFixtureName().equals(name)) {
                    return tf;
                }
            }
        }
        return null;
    }

    @XmlTransient
    public FileRevision getCreator() {
        return creator;
    }

    public void setCreator(FileRevision creator) {
        this.creator = creator;
        if (getInitSequence() != null) {
            getInitSequence().setCreator(creator);
        }
        setProperties(getProperties());
        setFixtures(getFixtures());
        setTestTypes(getTestTypes());
    }

    @XmlTransient
    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
        setFixtures(getFixtures());
        if (testProject != null) {
            setCreator(testProject.getCreator());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (creator != null ? creator.hashCode() : 0);
        hash += (hostName != null ? hostName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TestStation)) {
            return false;
        }
        TestStation other = (TestStation) object;
        if ((this.creator == null && other.getCreator() != null) || (this.creator != null && !this.creator.equals(other.getCreator()))) {
            return false;
        }
        if ((this.hostName == null && other.getHostName() != null) || (this.hostName != null && !this.hostName.equals(other.getHostName()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + TestStation.class.getCanonicalName() + ":" + getCreator() + ":" + getHostName() + "]";
    }

    @Override
    public Binding getBinding() {
        if (binding == null) {
            binding = new Binding();
            binding.setVariable("station", this);
        }
        return binding;
    }

    @Override
    public Object getPropertyObject(String keyString, Binding binding) {
        if (binding != null) {
            binding.setVariable("station", this);
        }
        for (TestProperty tsp : getProperties()) {
            if (tsp.getName().equals(keyString)) {
                return tsp.getPropertyObject(getTestProject().getGroovyClassLoader(), binding);
            }
        }
        if (getTestProject() != null) {
            return getTestProject().getPropertyObject(keyString, binding);
        }
        return null;
    }

    @XmlTransient
    public List<String> getHostNames() {
        return queryStringList("select distinct ts.hostName from TestStation ts");
    }

    public List<String> getTestFixtureNames(String hostName) {
        return queryStringList((hostName != null && !hostName.equals("All")) ? "select distinct ts.fixtureName from TestFixture ts where ts.testStation.hostName = '" + hostName + "'" : "select distinct ts.fixtureName from TestFixture ts");
    }

    @XmlTransient
    public List<String> getPartNumbers() {
        return queryStringList("select distinct ts.partNumber from Product ts");
    }

    public List<String> getPartNumberRevs(String partNumber) {
        return queryStringList((partNumber != null && !partNumber.equals("All")) ? "select distinct ts.partRevision from Product ts where ts.partNumber = '" + partNumber + "'" : "select distinct ts.partRevision from Product ts");
    }

    public List<String> getTestTypes(String partNumber, String partRevision) {
        if (partNumber == null || partNumber.equals("All")) {
            if (partRevision == null || partRevision.equals("All")) {
                return queryStringList("select distinct ts.name from TestType ts");
            } else {
                return queryStringList("select distinct ts.name from TestType ts where ts.product.partRevision = '" + partRevision + "'");
            }
        } else {
            if (partRevision == null || partRevision.equals("All")) {
                return queryStringList("select distinct ts.name from TestType ts where ts.product.partNumber = '" + partNumber + "'");
            } else {
                return queryStringList("select distinct ts.name from TestType ts where ts.product.partNumber = '" + partNumber + "' and ts.product.partRevision = '" + partRevision + "'");
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<String> queryStringList(String qstr) {
        return (List<String>) (new GeneralQuery(getEntityManagerFactory(), qstr)).query();
    }

    @XmlTransient
    public String getPersistenceProtocol() {
        String pp = "jdbc:" + getDriver().toString() + ":";
        System.out.println("Persistence protocol: " + pp);
        return pp;
    }

    public static enum Driver {

        derby("org.apache.derby.jdbc.EmbeddedDriver", "org.hibernate.dialect.DerbyDialect"),
        postgresql("org.postgresql.Driver", "org.hibernate.dialect.PostgreSQLDialect"),
        mysql("com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQLDialect");
        public final String driverClass;
        public final String dialect;

        Driver(String driverClass, String dialect) {
            this.driverClass = driverClass;
            this.dialect = dialect;
        }
    }

    @XmlTransient
    public Driver getDriver() {
        String driverClass = getPeristencePropertiesMap().get("hibernate.connection.driver_class");
        if (driverClass == null) {
            return Driver.derby;
        }
        Driver[] drivers = Driver.values();
        for (int i = 0; i < drivers.length; i++) {
            if (drivers[i].driverClass.equals(driverClass)) {
                return drivers[i];
            }
        }
        throw new IllegalArgumentException("Unsupported driver: " + driverClass);
    }

    @XmlTransient
    public String getPersistencePath() {
        String url = getPeristencePropertiesMap().get("hibernate.connection.url");
        if (url == null) {
            Driver driver = getDriver();
            switch (driver) {
                case derby:
                    return getUserHome() + File.separator + ".jtstand" + File.separator + "jtfw"; //+";logDevice=" + getUserHome() + File.separator + ".jtstand";
                case postgresql:
                    return "//localhost/jtfw";
                case mysql:
                    return "//localhost:3306/jtfw";
                default:
                    throw new IllegalArgumentException("Database driver is not supported: " + driver);
            }
        }
        if (!url.startsWith("jdbc:")) {
            throw new IllegalArgumentException("url should start with 'jdbc:': " + url);
        }
        int start = url.indexOf(':', 5);
        if (start < 0) {
            throw new IllegalArgumentException("invalid url: " + url);
        }
        start++;
        int end = url.indexOf(';');
        String path = end > 0 ? url.substring(start, end) : url.substring(start);
        System.out.println("Persistence path: " + path);
        return path;
    }

    public boolean databaseReset(String username, String password) throws ClassNotFoundException, SQLException {
        createDB(username, password);
        return databaseUpdate();
    }

    private void createDB(String username, String password) throws ClassNotFoundException, SQLException {
        switch (getDriver()) {
            case derby:
                deleteDir(new File(getPersistencePath()));
                break;
            case postgresql:
                createDB_PSQL(username, password);
                break;
            case mysql:
                createDB_MYSQL(username, password);
                break;
            default:
                throw new IllegalArgumentException("Unsupported database driver");
        }
    }

    private static void createDB_MYSQL(String username, String password) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306";

        System.out.println("Loading driver:'" + TestStation.Driver.mysql.driverClass + "'...");
        Class.forName(TestStation.Driver.mysql.driverClass);

        System.out.println("Connecting...");
        Connection c = DriverManager.getConnection(url, username, password);
        Statement s = c.createStatement();
        try {
            System.out.println("Revoking user privileges...");
            s.execute("REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'dbuser'@'localhost'");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            System.out.println("Dropping user...");
            s.execute("DROP USER 'dbuser'@'localhost'");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            System.out.println("Dropping database...");
            s.execute("DROP DATABASE jtfw");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Creating database...");
        s.execute("CREATE DATABASE jtfw");
        System.out.println("Creating user...");
        s.execute("CREATE USER 'dbuser'@'localhost' IDENTIFIED BY 'password'");
        System.out.println("Grant...");
        s.execute("GRANT ALL ON jtfw.* TO 'dbuser'@'localhost'");

        System.out.println("Closing connection...");
        s.close();
        c.close();
        System.out.println("New database is created.");
    }

    private static void createDB_PSQL(String username, String password) throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://localhost/template1";

        System.out.println("Loading driver:'" + TestStation.Driver.postgresql.driverClass + "'...");
        Class.forName(TestStation.Driver.postgresql.driverClass);

        System.out.println("Connecting...");
        Connection c = DriverManager.getConnection(url, username, password);
        Statement s = c.createStatement();
        try {
            System.out.println("Dropping database...");
            s.execute("DROP DATABASE jtfw");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        try {
            System.out.println("Dropping user...");
            s.execute("DROP USER dbuser");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Creating user...");
        s.execute("CREATE USER dbuser WITH PASSWORD 'password'");
        System.out.println("Creating database...");
        s.execute("CREATE DATABASE jtfw OWNER dbuser TEMPLATE DEFAULT ENCODING 'UNICODE'");
        System.out.println("Closing connection...");
        s.close();
        c.close();
        System.out.println("New database is created.");
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    public boolean databaseValidate() {
        if (Driver.derby.equals(getDriver())) {
            if (!(new File(getPersistencePath())).isDirectory()) {
                System.out.println("Derby directory is missing: " + getPersistencePath());
                return false;
            }
        }
        return auto("validate");
    }

    public boolean databaseUpdate() {
        return auto("update");
    }

    private boolean auto(String operation) {
        long startTime = System.currentTimeMillis();
        System.out.println("Operation: " + operation + "...");
        HibernateEntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            Map<String, String> map = getPeristencePropertiesFixedMap();
            if (operation != null) {
                map.put("hibernate.hbm2ddl.auto", operation);
            }
            if (operation.equals("update")) {
                if (getDriver().equals(TestStation.Driver.derby)) {
                    String url = map.get("hibernate.connection.url");
                    if (!url.contains("create=true")) {
                        url += ";create=true";
                    }
//                    if (!url.contains("logDevice=")) {
//                        url += ";logDevice=" + getUserHome() + File.separator + ".jtstand";
//                    }
                    map.put("hibernate.connection.url", url);
                }
            }
            emf = (HibernateEntityManagerFactory) Persistence.createEntityManagerFactory(getTestProject().getPun(), map);
//            emf.getSessionFactory().getAllClassMetadata();
//            System.out.println(emf.getSessionFactory().getAllClassMetadata());
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.getTransaction().commit();
//            System.out.println("Closing entity manager");
            em.close();
//            System.out.println("Closing entity manager factory");
            emf.close();
            System.out.println("Database " + operation + " operation succeeded in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
        System.out.println("Database " + operation + " operation failed in " + Long.toString(System.currentTimeMillis() - startTime) + "ms");
        return false;
    }

    public static String getUserHome() {
        String userHome = System.getProperty("user.home");
//        System.out.println("User home: " + userHome);
        return userHome;
    }
    public static final String STR_SAVE_DIRECTORY = "jtstand.saveDirectory";

    @XmlTransient
    public File getSaveDirectory() {
        File saveDirectory;
        Object o = getPropertyObject(STR_SAVE_DIRECTORY);
        if (o != null) {
            if (File.class.isAssignableFrom(o.getClass())) {
                saveDirectory = (File) o;
            } else {
                saveDirectory = new File(o.toString());
            }
        } else {
            saveDirectory = new File(getUserHome() + File.separator + ".jtstand" + File.separator + "save");
        }
        if (saveDirectory.isFile()) {
            throw new IllegalArgumentException("Output directory is not a directory: a File is specified!");
        }
        if (!saveDirectory.isDirectory()) {
            if (saveDirectory.mkdirs()) {
                LOGGER.info("Output directory is created: " + saveDirectory.getPath());
            } else {
                throw new IllegalArgumentException("Output directory does not exist and cannot be created: " + saveDirectory.getPath());
            }
        }
        if (!saveDirectory.canWrite()) {
            throw new IllegalArgumentException("Output directory does not exist and cannot be written: " + saveDirectory.getPath());
        }
        return saveDirectory;
    }
}
