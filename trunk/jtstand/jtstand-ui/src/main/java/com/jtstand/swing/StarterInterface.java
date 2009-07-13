/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, StarterInterface.java is part of JTStand.
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
package com.jtstand.swing;

import com.jtstand.TestFixture;
import com.jtstand.TestProject;
import com.jtstand.TestStation;
import com.jtstand.TestTypeReference;

/**
 *
 * @author albert_kurucz
 */
public interface StarterInterface {

    public String getEmployeeNumber();

    public TestProject getTestProject();

    public TestStation getTestStation();

    public TestFixture getTestFixture();

    public TestTypeReference getTestType();
}
