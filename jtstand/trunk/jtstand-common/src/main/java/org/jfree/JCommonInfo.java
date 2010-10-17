/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JCommonInfo.java is part of JTStand.
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

package org.jfree;

import java.util.Arrays;
import java.util.ResourceBundle;

import org.jfree.base.BaseBoot;
import org.jfree.base.Library;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.Licences;
import org.jfree.ui.about.ProjectInfo;
import org.jfree.util.ResourceBundleWrapper;

/**
 * Information about the JCommon project.  One instance of this class is
 * assigned to JCommon.INFO.
 *
 * @author David Gilbert
 */
public class JCommonInfo extends ProjectInfo {

    /** The singleton instance of the project info object. */
    private static JCommonInfo singleton;

    /**
     * Returns the single instance of this class.
     *
     * @return The single instance of information about the JCommon library.
     */
    public static synchronized JCommonInfo getInstance() {
        if (singleton == null) {
            singleton = new JCommonInfo();
        }
        return singleton;
    }

    /**
     * Creates a new instance.
     */
    private JCommonInfo() {

        // get a locale-specific resource bundle...
        final String baseResourceClass = "org.jfree.resources.JCommonResources";
        final ResourceBundle resources = ResourceBundleWrapper.getBundle(
                baseResourceClass);

        setName(resources.getString("project.name"));
        setVersion(resources.getString("project.version"));
        setInfo(resources.getString("project.info"));
        setCopyright(resources.getString("project.copyright"));

        setLicenceName("LGPL");
        setLicenceText(Licences.getInstance().getLGPL());

        setContributors(Arrays.asList(
            new Contributor[] {
                new Contributor("Anthony Boulestreau", "-"),
                new Contributor("Jeremy Bowman", "-"),
                new Contributor("J. David Eisenberg", "-"),
                new Contributor("Paul English", "-"),
                new Contributor("David Gilbert",
                        "david.gilbert@object-refinery.com"),
                new Contributor("Hans-Jurgen Greiner", "-"),
                new Contributor("Arik Levin", "-"),
                new Contributor("Achilleus Mantzios", "-"),
                new Contributor("Thomas Meier", "-"),
                new Contributor("Aaron Metzger", "-"),
                new Contributor("Thomas Morgner", "-"),
                new Contributor("Krzysztof Paz", "-"),
                new Contributor("Nabuo Tamemasa", "-"),
                new Contributor("Mark Watson", "-"),
                new Contributor("Matthew Wright", "-"),
                new Contributor("Hari", "-"),
                new Contributor("Sam (oldman)", "-")
            }
        ));

        addOptionalLibrary(new Library("JUnit", "3.8", "IBM Public Licence",
                "http://www.junit.org/"));

        setBootClass(BaseBoot.class.getName());
    }
}
