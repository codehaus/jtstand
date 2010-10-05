/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, URLObjectDescription.java is part of JTStand.
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

package org.jfree.xml.factory.objects;

import java.net.URL;

import org.jfree.io.IOUtils;
import org.jfree.util.Log;
import org.jfree.xml.Parser;

/**
 * An object-description for a <code>URL</code> object.
 *
 * @author Thomas Morgner
 */
public class URLObjectDescription extends AbstractObjectDescription {

    /**
     * Creates a new object description.
     */
    public URLObjectDescription() {
        super(URL.class);
        setParameterDefinition("value", String.class);
    }

    /**
     * Creates an object based on this description.
     *
     * @return The object.
     */
    public Object createObject() {
        final String o = (String) getParameter("value");
        final String baseURL = getConfig().getConfigProperty(Parser.CONTENTBASE_KEY);
        try {
            try {
                final URL bURL = new URL(baseURL);
                return new URL(bURL, o);
            }
            catch (Exception e) {
                Log.warn("BaseURL is invalid: ", e);
            }
            return new URL(o);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the parameters of this description object to match the supplied object.
     *
     * @param o  the object (should be an instance of <code>URL</code>).
     *
     * @throws ObjectFactoryException if the object is not an instance of <code>URL</code>.
     */
    public void setParameterFromObject(final Object o) throws ObjectFactoryException {
        if (!(o instanceof URL)) {
            throw new ObjectFactoryException("Is no instance of java.net.URL");
        }

        final URL comp = (URL) o;
        final String baseURL = getConfig().getConfigProperty(Parser.CONTENTBASE_KEY);
        try {
            final URL bURL = new URL(baseURL);
            setParameter("value", IOUtils.getInstance().createRelativeURL(comp, bURL));
        }
        catch (Exception e) {
            Log.warn("BaseURL is invalid: ", e);
        }
        setParameter("value", comp.toExternalForm());
    }

}
