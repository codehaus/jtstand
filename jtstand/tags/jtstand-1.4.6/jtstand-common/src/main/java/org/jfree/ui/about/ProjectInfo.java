/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, ProjectInfo.java is part of JTStand.
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

package org.jfree.ui.about;

import java.awt.Image;
import java.util.Iterator;
import java.util.List;

import org.jfree.base.BootableProjectInfo;
import org.jfree.base.Library;

/**
 * A class for recording the basic information about a free or open source software project.
 *
 * @author David Gilbert
 */
public class ProjectInfo extends BootableProjectInfo {

    /** An optional project logo. */
    private Image logo;

    /** The licence text. */
    private String licenceText;

    /** A list of contributors. */
    private List contributors;

    /**
     * Constructs an empty project info object.
     */
    public ProjectInfo() {
        // nothing required
    }

    /**
     * Constructs a project info object.
     *
     * @param name  the name of the project.
     * @param version  the version.
     * @param info  other info (usually a URL).
     * @param logo  the project logo.
     * @param copyright  a copyright statement.
     * @param licenceName  the name of the licence that applies to the project.
     * @param licenceText  the text of the licence that applies to the project.
     */
    public ProjectInfo(final String name,
                       final String version,
                       final String info,
                       final Image logo,
                       final String copyright,
                       final String licenceName,
                       final String licenceText) {

        super(name, version, info, copyright, licenceName);
        this.logo = logo;
        this.licenceText = licenceText;

    }

    /**
     * Returns the logo.
     *
     * @return the project logo.
     */
    public Image getLogo() {
        return this.logo;
    }

    /**
     * Sets the project logo.
     *
     * @param logo  the project logo.
     */
    public void setLogo(final Image logo) {
        this.logo = logo;
    }

    /**
     * Returns the licence text.
     *
     * @return the licence text.
     */
    public String getLicenceText() {
        return this.licenceText;
    }

    /**
     * Sets the project licence text.
     *
     * @param licenceText  the licence text.
     */
    public void setLicenceText(final String licenceText) {
        this.licenceText = licenceText;
    }

    /**
     * Returns the list of contributors for the project.
     *
     * @return the list of contributors.
     */
    public List getContributors() {
        return this.contributors;
    }

    /**
     * Sets the list of contributors.
     *
     * @param contributors  the list of contributors.
     */
    public void setContributors(final List contributors) {
        this.contributors = contributors;
    }

    /**
     * Returns a string describing the project.
     *
     * @return a string describing the project.
     */
    public String toString() {

        final StringBuffer result = new StringBuffer();
        result.append(getName());
        result.append(" version ");
        result.append(getVersion());
        result.append(".\n");
        result.append(getCopyright());
        result.append(".\n");
        result.append("\n");
        result.append("For terms of use, see the licence below.\n");
        result.append("\n");
        result.append("FURTHER INFORMATION:");
        result.append(getInfo());
        result.append("\n");
        result.append("CONTRIBUTORS:");
        if (this.contributors != null) {
            final Iterator iterator = this.contributors.iterator();
            while (iterator.hasNext()) {
                final Contributor contributor = (Contributor) iterator.next();
                result.append(contributor.getName());
                result.append(" (");
                result.append(contributor.getEmail());
                result.append(").");
            }
        }
        else {
            result.append("None");
        }

        result.append("\n");
        result.append("OTHER LIBRARIES USED BY ");
        result.append(getName());
        result.append(":");
        final Library[] libraries = getLibraries();
        if (libraries.length != 0) {
            for (int i = 0; i < libraries.length; i++) {
                final Library lib = libraries[i];
                result.append(lib.getName());
                result.append(" ");
                result.append(lib.getVersion());
                result.append(" (");
                result.append(lib.getInfo());
                result.append(").");
            }
        }
        else {
            result.append("None");
        }
        result.append("\n");
        result.append(getName());
        result.append(" LICENCE TERMS:");
        result.append("\n");
        result.append(getLicenceText());

        return result.toString();

    }

}
