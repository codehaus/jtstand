/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Main.java is part of JTStand.
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
package com.jtstand.swing;

import com.jtstand.FileRevision;
import com.jtstand.TestProject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author albert_kurucz
 *
 */
public class Main {

    private String projectLocation = null;
    private int revision = 0;
    private String version = null;
    private String station = null;
    private String title = null;
    private Options options;

    public static URI resolve(String uristr) {
        File curdirrel = new File(".");
        File curdir = new File(curdirrel.getAbsolutePath());
        URI cururi = curdir.toURI().normalize();
        System.out.println("URI of the current directory: " + cururi.toString());
        return resolve(uristr, cururi);
    }

    public static URI resolve(String uristr, URI baseuri) {
        if (uristr.equals(".") || uristr.equals("this")) {
            return baseuri;
        }
        URI uri = null;
        try {
            uri = new URI(uristr);
        } catch (URISyntaxException ex) {
            return null;
        }
        if (uri.isAbsolute()) {
            return uri;
        }
        return baseuri.resolve(uri);
    }

    private void startProject() {
        if (projectLocation == null) {
            /* if project location is not defined, default will kick in */
            /* if none of the default location is a file, then exit */
            projectLocation = "./config/project.xml";
            if (!new File(projectLocation).isFile()) {
                projectLocation = "./project.xml";
            }
            if (!new File(projectLocation).isFile()) {
                projectLocation = "./src/main/resources/config/project.xml";
            }
            if (!new File(projectLocation).isFile()) {
                projectLocation = "../jtstand-demo/src/main/resources/config/project.xml";
            }
            if (!new File(projectLocation).isFile()) {
                System.err.println("Project not specified and it could not be found in default locations!");
                System.exit(1);
            } else {
                System.out.println("Project file specified:" + projectLocation);
            }
            System.out.println("Resolving: " + projectLocation);
            URI projectURI = resolve(projectLocation);
            System.out.println("Project file is resolved to URI: " + projectURI);
            projectLocation = projectURI.getPath();
            System.out.println("Project file path: " + projectLocation);
            if (projectLocation.charAt(2) == ':') {
                projectLocation = projectLocation.substring(1);
                System.out.println("Project file path is corrected to: " + projectLocation);
            }
        }
        System.out.println("Project file requested revision: " + revision);
        try {
            new MainFrame(TestProject.unmarshal(FileRevision.createFromUrlOrFile(projectLocation, (long) revision), true).getTestStationOrDefault(station), title, version);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.FATAL, "IOException", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.FATAL, "JAXBException", ex);
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Project file is invalid!\nPress OK to exit.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.FATAL, "Exception", ex);
        }
    }

    public static void printVersion() {
//        System.out.println("Specification Title: " + Main.class.getPackage().getSpecificationTitle());
//        System.out.println("Specification Vendor: " + Main.class.getPackage().getSpecificationVendor());
//        System.out.println("Specification Version: " + Main.class.getPackage().getSpecificationVersion());
//        System.out.println();
//        System.out.println("Implementation Title: " + Main.class.getPackage().getImplementationTitle());
//        System.out.println("Implementation Vendor: " + Main.class.getPackage().getImplementationVendor());
//        System.out.println("Implementation Version: " + Main.class.getPackage().getImplementationVersion());
        System.out.println("Version: " + Main.class.getPackage().getImplementationVersion());
    }

    public Main() {
//        printVersion();
        options = new Options();
        options.addOption("help", false, "print this message");
        options.addOption("version", false, "print the version information and exit");
        options.addOption("s", true, "station host name");
        options.addOption("t", true, "title text");
        options.addOption("r", true, "revision number");
        options.addOption("v", true, "version");
        options.addOption("x", true, "schema file location");
    }

    public Main(String[] args) {
        this();
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("jtstand", options);
                System.exit(0);
            } else if (cmd.hasOption("version")) {
                printVersion();
                System.exit(0);
            } else {
                if (cmd.getArgs() != null && cmd.getArgs().length > 0) {
                    if (cmd.getArgs().length == 1) {
                        projectLocation = cmd.getArgs()[0];
                    } else {
                        System.err.println("Only one argument is expected; the project location!");
                        System.err.println("Received arguments:");
                        for (int i = 0; i < cmd.getArgs().length; i++) {
                            System.err.println(cmd.getArgs()[i]);
                        }
                    }
                }
                if (cmd.hasOption("s")) {
                    station = cmd.getOptionValue("s");
                }
                if (cmd.hasOption("r")) {
                    revision = Integer.parseInt(cmd.getOptionValue("r"));
                }
                if (cmd.hasOption("v")) {
                    version = cmd.getOptionValue("v");
                }
                if (cmd.hasOption("t")) {
                    title = cmd.getOptionValue("t");
                }
                if (cmd.hasOption("x")) {
                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    String schemaLocation = cmd.getOptionValue("x");
                    File schemaFile = new File(schemaLocation);
                    if (schemaFile.isFile()) {
                        try {
                            TestProject.setSchema(schemaFactory.newSchema(schemaFile));
                        } catch (SAXException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.FATAL, null, ex);
                            javax.swing.JOptionPane.showMessageDialog(
                                    null,
                                    "Schema file is invalid!\nPress OK to exit.",
                                    "Error",
                                    javax.swing.JOptionPane.ERROR_MESSAGE);
                            System.exit(-1);
                        }
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                                null,
                                "Schema file cannot be opened!\nPress OK to continue.",
                                "Warning",
                                javax.swing.JOptionPane.WARNING_MESSAGE);
                    }
                }
                startProject();
            }
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        setupLibrary();
        new Main(args);
    }

    private static void setupLibrary() {
        DAVRepositoryFactory.setup(); //For using over http:// and https://
        //SVNRepositoryFactoryImpl.setup(); //For using over svn:// and svn+xxx://
        //FSRepositoryFactory.setup(); //For using over file:///
    }
}
