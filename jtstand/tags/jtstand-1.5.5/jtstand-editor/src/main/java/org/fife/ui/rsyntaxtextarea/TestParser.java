/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, TestParser.java is part of JTStand.
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
 * TODO: Delete this class.
 *
 * This is a test parser that highlights all "while" and "if" strings as errors.
 * Actually, not all of them, but enough for testing purposes.
 */
class TestParser implements Parser {

	private java.util.ArrayList noticeList = new java.util.ArrayList(1);


	/**
	 * Parses input from the specified <code>Reader</code>.
	 *
	 * @param r The input stream from which to parse.
	 * @see #getNoticeIterator()
	 */
	private static final int BUF_LENGTH = 1024;
	public void parse(Reader r) {
		noticeList.clear();
		char[] buf = new char[BUF_LENGTH];
		int readerPos = 0;
		boolean moreToGo = true;
		try {
			do {
				moreToGo = r.read(buf)==BUF_LENGTH;
				int pos = 0;
				while (pos<BUF_LENGTH-4) {
					if (buf[pos]=='w') {
						if (buf[pos+1]=='h') {
							if (buf[pos+2]=='i') {
								if (buf[pos+3]=='l') {
									if (buf[pos+4]=='e') {
										noticeList.add(new ParserNotice("Test 'while' notice", readerPos+pos,5));
										pos += 5;
									}
									else pos += 4;
								}
								else pos += 3;
							}
							else pos += 2;
						}
						else pos += 1;
					}
					else if (buf[pos]=='i') {
						if (buf[pos+1]=='f') {
							noticeList.add(new ParserNotice("Test 'if' notice", readerPos+pos,2));
							pos += 2;
						}
						else pos += 1;
					}
					else pos += 1;
				}
				readerPos += BUF_LENGTH;
			} while (moreToGo);
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		}
	}


	/**
	 * Returns an iterator over the <code>ParserNotice</code>s received
	 * from this parser during the call to {@link #parse}.
	 *
	 * @return An iterator over the <code>ParserNotice</code>s.
	 * @see ParserNotice
	 * @see #parse(Reader r)
	 */
	public Iterator getNoticeIterator() {
		return noticeList.iterator();
	}


}