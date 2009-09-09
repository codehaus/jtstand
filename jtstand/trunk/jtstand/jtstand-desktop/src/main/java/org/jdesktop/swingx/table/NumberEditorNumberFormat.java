/*
 * Copyright (c) 2009 Albert Kurucz.
 *
 * This file, NumberEditorNumberFormat.java is part of JTStand.
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
package org.jdesktop.swingx.table;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * A specialised Format for the NumberEditor that returns a null for empty
 * strings.
 * 
 * @author Noel Grandin
 */
class NumberEditorNumberFormat extends Format {
    private final NumberFormat childFormat;

    public NumberEditorNumberFormat(NumberFormat childFormat) {
        if (childFormat == null) {
            childFormat = NumberFormat.getInstance();
        }
        this.childFormat = childFormat;
    }

    @Override
    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        if (obj == null)
            return new AttributedString("").getIterator();
        return childFormat.formatToCharacterIterator(obj);
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo,
            FieldPosition pos) {
        if (obj == null)
            return new StringBuffer("");
        return childFormat.format(obj, toAppendTo, pos);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        if (source == null) {
            pos.setIndex(1); // otherwise Format thinks parse failed
            return null;
        }
        if (source.trim().equals("")) {
            pos.setIndex(1); // otherwise Format thinks parse failed
            return null;
        }
        Object val = childFormat.parseObject(source, pos);
        /*
         * The default behaviour of Format objects is to keep parsing as long as
         * they encounter valid data. By for table editing we don't want
         * trailing bad data to be considered a "valid value". So set the index
         * to 0 so that the parse(Object) method knows that we had an error.
         */
        if (pos.getIndex() != source.length()) {
            pos.setErrorIndex(pos.getIndex());
            pos.setIndex(0);
        }
        return val;
    }
}