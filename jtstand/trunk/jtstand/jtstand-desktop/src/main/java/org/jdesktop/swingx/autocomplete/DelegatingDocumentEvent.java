/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, DelegatingDocumentEvent.java is part of JTStand.
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

/**
 * 
 */
package org.jdesktop.swingx.autocomplete;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * @author Karl George Schaefer
 *
 */
final class DelegatingDocumentEvent implements DocumentEvent {
    private final Document resourcedDocument;
    private final DocumentEvent sourceEvent;
    
    public DelegatingDocumentEvent(Document resourcedDocument, DocumentEvent sourceEvent) {
        this.resourcedDocument = resourcedDocument;
        this.sourceEvent = sourceEvent;
    }
    
    /**
     * {@inheritDoc}
     */
    public ElementChange getChange(Element elem) {
        return sourceEvent.getChange(elem);
    }

    /**
     * {@inheritDoc}
     */
    public Document getDocument() {
        return resourcedDocument;
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return sourceEvent.getLength();
    }

    /**
     * {@inheritDoc}
     */
    public int getOffset() {
        return sourceEvent.getOffset();
    }

    /**
     * {@inheritDoc}
     */
    public EventType getType() {
        return sourceEvent.getType();
    }

}
