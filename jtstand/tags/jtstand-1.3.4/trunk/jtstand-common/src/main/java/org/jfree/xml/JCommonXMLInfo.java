/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, JCommonXMLInfo.java is part of JTStand.
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
package org.jfree.xml;

import java.util.Arrays;

import org.jfree.JCommonInfo;
import org.jfree.ui.about.Contributor;
import org.jfree.ui.about.ProjectInfo;

/**
 * The info implementation for the XML classes of JCommon.
 *
 * @author Thomas Morgner
 */
public class JCommonXMLInfo extends ProjectInfo {
    /**
     * The info singleton.
     */
    private static JCommonXMLInfo singleton;

    /**
     * Returns the single instance of this class.
     *
     * @return The single instance of information about the JCommon library.
     */
    public static synchronized JCommonXMLInfo getInstance() {
        if (singleton == null) {
            singleton = new JCommonXMLInfo();
        }
        return singleton;
    }


    /**
     * Constructs an empty project info object.
     */
    private JCommonXMLInfo() {
        final JCommonInfo info = JCommonInfo.getInstance();

        setName("JCommon-XML");
        setVersion(info.getVersion());
        setInfo(info.getInfo());
        setCopyright(info.getCopyright());

        setLicenceName(info.getLicenceName());
        setLicenceText(info.getLicenceText());

        setContributors(Arrays.asList(new Contributor[]{
            new Contributor("David Gilbert", "david.gilbert@object-refinery.com"),
            new Contributor("Thomas Morgner", "taqua@users.sourceforge.net"),
            new Contributor("Peter Becker", "-"),
        }));

        addLibrary(info);
    }
}
