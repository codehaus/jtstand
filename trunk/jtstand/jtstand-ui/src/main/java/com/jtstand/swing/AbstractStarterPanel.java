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

import javax.swing.*;

/**
 *
 * @author albert_kurucz
 */
public abstract class AbstractStarterPanel extends JPanel {

    abstract javax.swing.JButton jButtonStart();

    abstract javax.swing.JButton jButtonDebug();

    abstract javax.swing.JButton jButtonCancel();

    abstract javax.swing.JComboBox jComboBoxPartNumber();

    abstract javax.swing.JComboBox jComboBoxPartRev();

    abstract javax.swing.JComboBox jComboBoxTestType();

    abstract javax.swing.JTextField jTextFieldSN();
}
