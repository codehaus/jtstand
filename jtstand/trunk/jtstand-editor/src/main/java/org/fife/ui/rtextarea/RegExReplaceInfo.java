/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, RegExReplaceInfo.java is part of JTStand.
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
package org.fife.ui.rtextarea;


/**
 * Information on how to implement a regular expression "replace" operation.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RegExReplaceInfo {

	private String matchedText;
	private int startIndex;
	private int endIndex;
	private String replacement;


	/**
	 * Constructor.
	 *
	 * @param matchedText The text that matched the regular expression.
	 * @param start The start index of the matched text in the
	 *        <code>CharSequence</code> searched.
	 * @param end The end index of the matched text in the
	 *        <code>CharSequence</code> searched.
	 * @param replacement The text to replace the matched text with.  This
	 *        string has any matched groups and character escapes replaced.
	 */
	public RegExReplaceInfo(String matchedText, int start, int end,
						String replacement) {
		this.matchedText = matchedText;
		this.startIndex = start;
		this.endIndex = end;
		this.replacement = replacement;
	}

	/**
	 * Returns the end index of the matched text.
	 *
	 * @return The end index of the matched text in the document searched.
	 * @see #getMatchedText()
	 * @see #getEndIndex()
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * Returns the text that matched the regular expression.
	 *
	 * @return The matched text.
	 */
	public String getMatchedText() {
		return matchedText;
	}

	/**
	 * Returns the string to replaced the matched text with.
	 * 
	 * @return The string to replace the matched text with.
	 */
	public String getReplacement() {
		return replacement;
	}

	/**
	 * Returns the start index of the matched text.
	 *
	 * @return The start index of the matched text in the document searched.
	 * @see #getMatchedText()
	 * @see #getEndIndex()
	 */
	public int getStartIndex() {
		return startIndex;
	}

}