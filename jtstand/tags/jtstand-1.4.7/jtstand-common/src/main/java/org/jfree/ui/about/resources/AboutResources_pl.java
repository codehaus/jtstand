/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AboutResources_pl.java is part of JTStand.
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
 * @author KP
 */
public class AboutResources_pl extends ListResourceBundle {

    /**
     * Default constructor.
     */
    public AboutResources_pl() {
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

        {"about-frame.tab.about",             "Informacja o"},
        {"about-frame.tab.system",            "System"},
        {"about-frame.tab.contributors",      "Tw\u00f3rcy"},
        {"about-frame.tab.licence",           "Licencja"},
        {"about-frame.tab.libraries",         "Biblioteki"},

        {"contributors-table.column.name",    "Nazwa:"},
        {"contributors-table.column.contact", "Kontakt:"},

        {"libraries-table.column.name",       "Nazwa:"},
        {"libraries-table.column.version",    "Wersja:"},
        {"libraries-table.column.licence",    "Licencja:"},
        {"libraries-table.column.info",       "Inne informacje:"},

        {"system-frame.title",                "W?a\u015bciwo\u015bci systemowe"},

        {"system-frame.button.close",         "Zamknij"},
        {"system-frame.button.close.mnemonic", new Character('Z')},

        {"system-frame.menu.file",                "Plik"},
        {"system-frame.menu.file.mnemonic",       new Character('P')},

        {"system-frame.menu.file.close",          "Zamknij"},
        {"system-frame.menu.file.close.mnemonic", new Character('K')},

        {"system-frame.menu.edit",                "Edycja"},
        {"system-frame.menu.edit.mnemonic",       new Character('E')},

        {"system-frame.menu.edit.copy",           "Kopiuj"},
        {"system-frame.menu.edit.copy.mnemonic",  new Character('C')},

        {"system-properties-table.column.name",   "Nazwa w?a\u015bciwo\u015bci:"},
        {"system-properties-table.column.value",  "Warto\u015b\u0107:"},

        {"system-properties-panel.popup-menu.copy", "Kopiuj" },
        {"system-properties-panel.popup-menu.copy.accelerator",
                            KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK) },

    };

}
