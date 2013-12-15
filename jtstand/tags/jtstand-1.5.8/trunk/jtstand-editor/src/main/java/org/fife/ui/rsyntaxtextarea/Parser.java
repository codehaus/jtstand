/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, Parser.java is part of JTStand.
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

import java.io.Reader;
import java.util.Iterator;


/**
 * An interface for a parser.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public interface Parser {


	/**
	 * Parses input from the specified <code>Reader</code>.
	 *
	 * @param r The input stream from which to parse.
	 * @see #getNoticeIterator()
	 */
	public void parse(Reader r);


	/**
	 * Returns an iterator over the <code>ParserNotice</code>s received
	 * from this parser during the call to {@link #parse}.
	 *
	 * @return An iterator over the <code>ParserNotice</code>s.
	 * @see ParserNotice
	 * @see #parse(Reader r)
	 */
	public Iterator getNoticeIterator();


}