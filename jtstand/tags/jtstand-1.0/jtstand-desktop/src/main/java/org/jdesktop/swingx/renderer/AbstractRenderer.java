/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, AbstractRenderer.java is part of JTStand.
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
 * along with JTStand.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jdesktop.swingx.renderer;

import java.awt.Color;
import java.io.Serializable;

import org.jdesktop.swingx.rollover.RolloverRenderer;

/**
 * Convience common ancestor for SwingX renderers.
 * 
 * @author Jeanette Winzenburg
 */
public abstract class AbstractRenderer 
    implements  RolloverRenderer, StringValue, Serializable {

    protected ComponentProvider componentController;

    public AbstractRenderer(ComponentProvider provider) {
        if (provider ==  null) {
            provider = createDefaultComponentProvider();
        }
        this.componentController = provider;
    }
    
    /**
     * Returns the ComponentProvider used by this renderer.
     * 
     * @return the ComponentProvider used by this renderer
     */
    public ComponentProvider getComponentProvider() {
        return componentController;
    }

    /**
     * The default ComponentProvider to use if no special.
     * 
     * @return the default <code>ComponentProvider</code>
     */
    protected abstract ComponentProvider createDefaultComponentProvider();
    
// --------------- implement StringValue    
    
    /**
     * {@inheritDoc}
     */
    public String getString(Object value) {
        return componentController.getString(value);
    }

 // ------------ implement RolloverRenderer   
    
    /**
     * {@inheritDoc}
     */
    public void doClick() {
        if (isEnabled()) {
            ((RolloverRenderer) componentController).doClick();
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isEnabled() {
        return (componentController instanceof RolloverRenderer)
                && ((RolloverRenderer) componentController).isEnabled();
    }


//-------------------- legacy: configure arbitrary visuals    
    /**
     * @param background
     */
    public void setBackground(Color background) {
        componentController.getDefaultVisuals().setBackground(background);
    
    }

    /**
     * @param foreground
     */
    public void setForeground(Color foreground) {
        componentController.getDefaultVisuals().setForeground(foreground);
    }


}
