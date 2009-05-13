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

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author albert_kurucz
 */
public class JXDateTimePicker extends JPanel implements MouseWheelListener, ChangeListener {

    public static final long serialVersionUID = 20081114L;
    JSpinner spinner;
    JXDatePicker picker;

    public JXDateTimePicker(Long time) {
        Date date = (time == null) ? null : new Date(time);
        picker = new JXDatePicker(date);
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        add(picker);
        spinner = new JSpinner();
        spinner.setModel(new javax.swing.SpinnerDateModel((date != null) ? date : new Date(), null, null, Calendar.DAY_OF_MONTH));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", picker.getLocale());
        format.setTimeZone(picker.getTimeZone());
        ((DefaultEditor) spinner.getEditor()).getTextField().setFormatterFactory(
                new DefaultFormatterFactory(new DateFormatter(format)));
        spinner.setVisible(date != null);
        add(spinner);
        picker.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PickerActionPerformed(evt);
            }
        });
        spinner.addChangeListener(this);
        spinner.addMouseWheelListener(this);
        picker.addMouseWheelListener(this);
//        picker.addKeyListener(
//                new KeyAdapter() {
//
//                    @Override
//                    public void keyTyped(KeyEvent e) {
//                        int keyChar = e.getKeyChar();
//
//                        switch (keyChar) {
//                            case KeyEvent.VK_CLEAR://.VK_ESCAPE:
//                                setDate(null);
//                        }
//                    }
//                });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(spinner.getPreferredSize().width + picker.getPreferredSize().width, Math.max(picker.getPreferredSize().height, spinner.getPreferredSize().height));
    }

    private void PickerActionPerformed(ActionEvent evt) {
        Date date = picker.getDate();
        //System.out.println("Setting spinner to date: " + date);
        setSpinnerDate(date);
        picker.revalidate();
    }

    private void setSpinnerDate(Date date) {
        spinner.setVisible(date != null);
        spinner.setValue(date != null ? date : new Date());
        if (date != null) {
            spinner.requestFocus();
        }
    }

    public void setDate(Date date) {
        //System.out.println("setting date to " + date);
        picker.setDate(date);
        picker.revalidate();
        setSpinnerDate(date);
    }

    public Date getDate() {
        return (spinner.isVisible()) ? (Date) spinner.getValue() : null;
    }

    public Long getTime() {
        Date date = getDate();
        return (date != null) ? date.getTime() : null;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        spinner.setVisible(true);
        spinner.setValue(new Date(((Date) spinner.getValue()).getTime() - 1000 * 60 * 60 * e.getWheelRotation()));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (spinner.equals(e.getSource())) {
            if (spinner.isVisible()) {
                long time = ((Date) spinner.getValue()).getTime();
                int offset = picker.getTimeZone().getOffset(time);
                Date date = new Date(time - ((time + offset) % (1000 * 60 * 60 * 24)));

                if (!date.equals(picker.getDate())) {
                    picker.setDate(date);
                    picker.revalidate();
                }
            }
        }
    }
}