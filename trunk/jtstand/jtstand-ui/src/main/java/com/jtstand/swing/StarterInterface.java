/*
 * Copyright 2009 Albert Kurucz
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
