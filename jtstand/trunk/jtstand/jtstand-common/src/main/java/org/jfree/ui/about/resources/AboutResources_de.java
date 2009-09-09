/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AboutResources_de.java is part of JTStand.
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
 * @author Thomas Meier
 */
public class AboutResources_de extends ListResourceBundle {

    /**
     * Default constructor.
     */
    public AboutResources_de() {
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

        {"about-frame.tab.about",             "\u00dcber"},
        {"about-frame.tab.system",            "System"},
        {"about-frame.tab.contributors",      "Entwickler"},
        {"about-frame.tab.licence",           "Lizenz"},
        {"about-frame.tab.libraries",         "Bibliotheken"},

        {"contributors-table.column.name",    "Name:"},
        {"contributors-table.column.contact", "Kontakt:"},

        {"libraries-table.column.name",       "Name:"},
        {"libraries-table.column.version",    "Version:"},
        {"libraries-table.column.licence",    "Lizenz:"},
        {"libraries-table.column.info",       "Zus. Information:"},

        {"system-frame.title",                "Systemeigenschaften"},

        {"system-frame.button.close",         "Schlie\u00dfen"},
        {"system-frame.button.close.mnemonic", new Character('C')},

        {"system-frame.menu.file",                "Datei" },
        {"system-frame.menu.file.mnemonic",       new Character('D')},

        {"system-frame.menu.file.close",          "Beenden" },
        {"system-frame.menu.file.close.mnemonic", new Character('B')},

        {"system-frame.menu.edit",                "Bearbeiten"},
        {"system-frame.menu.edit.mnemonic",       new Character('B')},

        {"system-frame.menu.edit.copy",           "Kopieren"},
        {"system-frame.menu.edit.copy.mnemonic",  new Character('K')},

        {"system-properties-table.column.name",   "Eigenschaft:"},
        {"system-properties-table.column.value",  "Wert:"},

        {"system-properties-panel.popup-menu.copy", "Kopieren"},
        {"system-properties-panel.popup-menu.copy.accelerator",
            KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK)}

    };

}
