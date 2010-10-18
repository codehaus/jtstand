/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AboutResources_es.java is part of JTStand.
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
 * @author Hans-Jurgen Greiner.
 */
public class AboutResources_es extends ListResourceBundle {

    /**
     * Default constructor.
     */
    public AboutResources_es() {
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

        {"about-frame.tab.about",             "Acerca"},
        {"about-frame.tab.system",            "Sistema"},
        {"about-frame.tab.contributors",      "Desarrolladores"},
        {"about-frame.tab.licence",           "Licencia"},
        {"about-frame.tab.libraries",         "Bibliotecas"},

        {"contributors-table.column.name",    "Nombre:"},
        {"contributors-table.column.contact", "Contacto:"},

        {"libraries-table.column.name",       "Nombre::"},
        {"libraries-table.column.version",    "Versi\u00f5n:"},
        {"libraries-table.column.licence",    "Licencia:"},
        {"libraries-table.column.info",       "Otra Informaci?n:"},

        {"system-frame.title",                "propiedades del Sistema"},

        {"system-frame.button.close",         "Cerrar"},

        {"system-frame.menu.file",                "Archivo"},
        {"system-frame.menu.file.mnemonic",       new Character('F')},

        {"system-frame.menu.file.close",          "Cerrar"},
        {"system-frame.menu.file.close.mnemonic", new Character('C')},

        {"system-frame.menu.edit",                "Edici?n"},
        {"system-frame.menu.edit.mnemonic",       new Character('E')},

        {"system-frame.menu.edit.copy",           "Copiar"},
        {"system-frame.menu.edit.copy.mnemonic",  new Character('C')},

        {"system-properties-table.column.name",   "Nombre de Propiedad:"},
        {"system-properties-table.column.value",  "Valor:"},

        {"system-properties-panel.popup-menu.copy", "Copiar" },
        {"system-properties-panel.popup-menu.copy.accelerator",
                            KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK) },

    };

}

