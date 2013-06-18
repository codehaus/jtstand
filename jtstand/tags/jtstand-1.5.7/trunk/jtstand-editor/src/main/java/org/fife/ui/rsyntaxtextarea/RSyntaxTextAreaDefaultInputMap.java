/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, RSyntaxTextAreaDefaultInputMap.java is part of JTStand.
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
package org.fife.ui.rsyntaxtextarea;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import org.fife.ui.rtextarea.RTADefaultInputMap;


/**
 * The default input map for an <code>RSyntaxTextArea</code>.
 * Currently, the new key bindings include:
 * <ul>
 *   <li>Shift+Tab indents the current line or currently selected lines
 *       to the left.
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaDefaultInputMap extends RTADefaultInputMap {

	/**
	 * Constructs the default input map for an <code>RSyntaxTextArea</code>.
	 */
	public RSyntaxTextAreaDefaultInputMap() {

		//int ctrl = InputEvent.CTRL_MASK;
		int shift = InputEvent.SHIFT_MASK;
		//int alt = InputEvent.ALT_MASK;

		put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,   shift),	RSyntaxTextAreaEditorKit.rstaDecreaseIndentAction);
		put(KeyStroke.getKeyStroke('}'),						RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction);

		// FIXME:  The keystroke associated with this action should be dynamic and
		// configurable and synchronized with the "trigger" defined in RSyntaxTextArea's
		// CodeTemplateManager.
		// NOTE:  no modifiers => mapped to keyTyped.  If we had "0" as a second
		// second parameter, we'd get the template action (keyPressed) AND the
		// default space action (keyTyped).
		//put(KeyStroke.getKeyStroke(' '),			RSyntaxTextAreaEditorKit.rstaPossiblyInsertTemplateAction);
		put(CodeTemplateManager.TEMPLATE_KEYSTROKE,	RSyntaxTextAreaEditorKit.rstaPossiblyInsertTemplateAction);

	}


}