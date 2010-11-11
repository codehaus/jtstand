/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, AnnualDateRule.java is part of JTStand.
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

package org.jfree.date;

/**
 * The base class for all 'annual' date rules: that is, rules for generating
 * one date for any given year.
 * <P>
 * One example is Easter Sunday (which can be calculated using published algorithms).
 *
 * @author David Gilbert
 */
public abstract class AnnualDateRule implements Cloneable {

    /**
     * Default constructor.
     */
    protected AnnualDateRule() {
    }

    /**
     * Returns the date for this rule, given the year.
     *
     * @param year  the year (1900 &lt;= year &lt;= 9999).
     *
     * @return the date for this rule, given the year.
     */
    public abstract SerialDate getDate(int year);

    /**
     * Returns a clone of the rule.
     * <P>
     * You should refer to the documentation of the clone() method in each
     * subclass for exact details.
     *
     * @return a clone of the rule.
     *
     * @throws CloneNotSupportedException if the rule is not clonable.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
