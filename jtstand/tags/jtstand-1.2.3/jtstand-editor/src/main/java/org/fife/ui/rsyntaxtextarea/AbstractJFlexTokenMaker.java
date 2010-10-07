/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, AbstractJFlexTokenMaker.java is part of JTStand.
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

import javax.swing.text.Segment;


/**
 * Base class for JFlex-generated token makers.  This class attempts to factor
 * out all common code from these classes.  Many methods <em>almost</em> could
 * be factored out into this class, but cannot because they reference JFlex
 * variables that we cannot access from this class.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public abstract class AbstractJFlexTokenMaker extends TokenMakerBase {

	protected Segment s;

	protected int start;		// Just for states.
	protected int offsetShift;	// As parser always starts at 0, but our line doesn't.


}