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

import org.jfree.chart.ChartColor;

import java.awt.*;

/**
 *
 * @author albert_kurucz
 */
public class ChartCategories {

    private static final Color[] COLORS = new Color[]{
        ChartColor.BLUE,
        ChartColor.GREEN,
        ChartColor.RED,
        ChartColor.DARK_YELLOW,
        ChartColor.DARK_MAGENTA,
        ChartColor.DARK_RED,
        ChartColor.DARK_BLUE,
        ChartColor.DARK_GREEN,
        ChartColor.DARK_CYAN,
        ChartColor.LIGHT_RED,
        ChartColor.LIGHT_BLUE,
        ChartColor.LIGHT_GREEN,
        ChartColor.LIGHT_YELLOW,
        ChartColor.LIGHT_MAGENTA,
        ChartColor.LIGHT_CYAN,
        ChartColor.VERY_DARK_RED,
        ChartColor.VERY_DARK_BLUE,
        ChartColor.VERY_DARK_GREEN,
        ChartColor.VERY_DARK_YELLOW,
        ChartColor.VERY_DARK_MAGENTA,
        ChartColor.VERY_DARK_CYAN,
        ChartColor.VERY_LIGHT_RED,
        ChartColor.VERY_LIGHT_BLUE,
        ChartColor.VERY_LIGHT_GREEN,
        ChartColor.VERY_LIGHT_YELLOW,
        ChartColor.VERY_LIGHT_MAGENTA,
        ChartColor.VERY_LIGHT_CYAN
    };

    public static Color getColor(int i) {
        return COLORS[i % COLORS.length];
    }
}
