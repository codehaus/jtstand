/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, ListAdaptor.java is part of JTStand.
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
package org.jdesktop.swingx.autocomplete;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

/**
 * An implementation of the AbstractAutoCompleteAdaptor that is suitable for a
 * JList in conjunction with a JTextComponent.
 * 
 * @author Thomas Bierhance
 */
public class ListAdaptor extends AbstractAutoCompleteAdaptor implements ListSelectionListener {
    
    /** the list containing the items */
    JList list;
    /** the text component that is used for automatic completion*/
    JTextComponent textComponent;
    /** the converter used to transform items to strings */
    ObjectToStringConverter stringConverter;
    
    /**
     * Creates a new JListAdaptor for the given list and text component.
     * @param list the list that contains the items that are used for automatic
     * completion
     * @param textComponent the text component that will be used automatic
     * completion
     */
    public ListAdaptor(JList list, JTextComponent textComponent) {
        this(list, textComponent, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
    }
    
    /**
     * Creates a new JListAdaptor for the given list and text component.
     * @param list the list that contains the items that are used for automatic
     * completion
     * @param textComponent the text component that will be used automatic
     * completion
     * @param stringConverter the converter used to transform items to strings
     */
    public ListAdaptor(JList list, JTextComponent textComponent, ObjectToStringConverter stringConverter) {
        this.list = list;
        this.textComponent = textComponent;
        this.stringConverter = stringConverter;
        // when a new item is selected set and mark the text
        list.addListSelectionListener(this);
    }
    
    /**
     * Implementation side effect - do not invoke.
     * @param listSelectionEvent -
     */
    // ListSelectionListener (listening to list)
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        // set the text to the currently selected item
        getTextComponent().setText(stringConverter.getPreferredStringForItem(list.getSelectedValue()));
        // mark the entire text
        markEntireText();
    }
    
    public Object getSelectedItem() {
        return list.getSelectedValue();
    }
    
    public int getItemCount() {
        return list.getModel().getSize();
    }
    
    public Object getItem(int index) {
        return list.getModel().getElementAt(index);
    }
    
    public void setSelectedItem(Object item) {
        list.setSelectedValue(item, true);
    }
    
    public JTextComponent getTextComponent() {
        return textComponent;
    }
}
