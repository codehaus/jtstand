/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AboutResources.java is part of JTStand.
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

package org.jfree.ui.about.resources;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ListResourceBundle;
import javax.swing.KeyStroke;

/**
 * A resource bundle that stores all the user interface items that might need localisation.
 *
 * @author David Gilbert
 */
public class AboutResources extends ListResourceBundle {

    /**
     * Default constructor.
     */
    public AboutResources() {
    }

    /**
     * Returns the array of strings in the resource bundle.
     *
     * @return the resources.
     */
    public Object[][] getContents() {
        return CONTENTS;
    }

    /** The resources to be localised. */
    private static final Object[][] CONTENTS = {

        {"about-frame.tab.about",             "About"},
        {"about-frame.tab.system",            "System"},
        {"about-frame.tab.contributors",      "Developers"},
        {"about-frame.tab.licence",           "Licence"},
        {"about-frame.tab.libraries",         "Libraries"},

        {"contributors-table.column.name",    "Name:"},
        {"contributors-table.column.contact", "Contact:"},

        {"libraries-table.column.name",       "Name:"},
        {"libraries-table.column.version",    "Version:"},
        {"libraries-table.column.licence",    "Licence:"},
        {"libraries-table.column.info",       "Other Information:"},

        {"system-frame.title",                "System Properties"},

        {"system-frame.button.close",         "Close"},
        {"system-frame.button.close.mnemonic", new Character('C')},

        {"system-frame.menu.file",                "File"},
        {"system-frame.menu.file.mnemonic",       new Character('F')},

        {"system-frame.menu.file.close",          "Close"},
        {"system-frame.menu.file.close.mnemonic", new Character('C')},

        {"system-frame.menu.edit",                "Edit"},
        {"system-frame.menu.edit.mnemonic",       new Character('E')},

        {"system-frame.menu.edit.copy",           "Copy"},
        {"system-frame.menu.edit.copy.mnemonic",  new Character('C')},

        {"system-properties-table.column.name",   "Property Name:"},
        {"system-properties-table.column.value",  "Value:"},

        {"system-properties-panel.popup-menu.copy", "Copy" },
        {"system-properties-panel.popup-menu.copy.accelerator",
                            KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK) },

    };

}
