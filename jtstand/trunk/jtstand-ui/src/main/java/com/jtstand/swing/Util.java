/*
 * Copyright (c) 2009 Albert Kurucz. 
 *
 * This file, Util.java is part of JTStand.
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

import com.jtstand.TestStepInstance;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.jboss.logging.Logger;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author albert_kurucz
 */
public class Util {

    private static final Logger log = Logger.getLogger(TestStepInstances.class.getName());
    public static final Class<?>[] emptyContructor = {};

    public static void scrollSelectedRowToVisible(final JXTable jTable) {

        if (jTable.getSelectedRows().length == 1) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int[] rows = jTable.getSelectedRows();
                    if (rows.length == 1) {
                        jTable.scrollRowToVisible(rows[0]);
                    }
                }
            });

        }
    }

    public static void setDividerLocation(JSplitPane jSplitPane, JXTable jTable) {
        int pref = getPref(jSplitPane);//jSplitPane.getTopComponent().getPreferredSize().height + jSplitPane.getInsets().top;
//        System.out.println("TopComponent preferred: " + pref + "   Current divider location: " + jSplitPane.getDividerLocation());
        if (jSplitPane.getDividerLocation() != pref) {
            jSplitPane.setDividerLocation(pref);
            scrollSelectedRowToVisible(jTable);
        }
    }

    public static void setVisibleRowCount(final JXTable jTable, int rows, final JSplitPane jSplitPane) {
        if (rows < 0) {
            return;
        }
//        System.out.println(jTable.getName() + " setVisibleRowCount:" + rows);
        jTable.setVisibleRowCount(rows);
//        Dimension d = new Dimension(table.getPreferredScrollableViewportSize().width, getHeight(table, rows));
//        System.out.println("setting table preferred scrollable viewport size:" + d);
//        table.setPreferredScrollableViewportSize(d);

//        jTable.revalidate();
//        jSplitPane.revalidate();
        //jTable.getPreferredScrollableViewportSize(); //instead of revalidating, try to calculate

//        Dimension psvs = jTable.getPreferredScrollableViewportSize();
//        System.out.println("table preferred scrollable viewport size:" + jTable.getPreferredScrollableViewportSize());

//        if (jSplitPane != null) {
//            Component c = jSplitPane.getTopComponent();
//            if (c != null) {
//                c.invalidate();
//                c.validate();
//            }
//        }

        //        System.out.println("table preferred scrollable viewport size:" + jTable.getPreferredScrollableViewportSize());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setDividerLocation(jSplitPane, jTable);
            }
        });

    }

//    public static int setVisibleRowCount(JXTable jTable, int rows, JSplitPane jSplitPane) {
//        System.out.println("setVisibleRowCount:" + rows);
//        jTable.setVisibleRowCount(rows);
//        int pref = getPref(jSplitPane);//jSplitPane.getTopComponent().getPreferredSize().height + jSplitPane.getInsets().top;
//        if (jSplitPane.getDividerLocation() != pref) {
//            jSplitPane.setDividerLocation(pref);
//        }
//        int hh = pref - rows * jTable.getRowHeight();
//        System.out.println("hh:" + hh);
//        return hh;
//    }
    public static int getPref(JSplitPane jSplitPane) {
        if (jSplitPane == null) {
            return 0;
        }
        Component c = jSplitPane.getTopComponent();
        if (c == null || c.getPreferredSize() == null) {
            if (jSplitPane.getSize() != null) {
                return jSplitPane.getSize().height;
            } else {
                return 0;
            }
        }
        if (JScrollPane.class.isAssignableFrom(c.getClass())) {
            JScrollBar jsb = ((JScrollPane) c).getHorizontalScrollBar();
            if (jsb.isShowing()) {
                return jsb.getSize().height + c.getPreferredSize().height + jSplitPane.getInsets().top;
            }
        }
        return c.getPreferredSize().height + jSplitPane.getInsets().top;
    }

    public static int dividerChanged(JXTable jTable, JSplitPane jSplitPane) {
        if (jTable == null) {
            return 0;
        }
        if (jSplitPane.getBottomComponent() == null || !jSplitPane.getBottomComponent().isVisible()) {
            return jTable.getVisibleRowCount();
        }
        if (jTable.getRowCount() == 0) {
            return 0;
        }
        int rc = jTable.getRowCount();
        int pref = Util.getPref(jSplitPane);
        int vrc = jTable.getVisibleRowCount();
        int hh = pref - vrc * jTable.getRowHeight();
        int current = jSplitPane.getDividerLocation();
//        System.out.println("hh:" + hh + " current:" + current);
//        int rc = (current - jScrollPaneTop.getInsets().top - hh + jTable.getRowHeight() / 3) / jTable.getRowHeight();
        int cvrc = Math.min(rc, (current - hh + jTable.getRowHeight() / 3) / jTable.getRowHeight());
        if (cvrc == 0) {
            cvrc = 1;
        }
//        System.out.println("current: " + current + " row count: " + rc + " current visible row count: " + vrc + " computed visible row count: " + cvrc + " preferred: " + pref);
        if (cvrc != vrc) {
            Util.setVisibleRowCount(jTable, cvrc, jSplitPane);
        } else {
            Util.setDividerLocation(jSplitPane, jTable);
        }
        return cvrc;
    }

    public static List<String> getPathList(String path) {
        StringTokenizer tk = new StringTokenizer(path, ".");
        List<String> retval = new ArrayList<String>();
        while (tk.hasMoreTokens()) {
            retval.add(tk.nextToken());
        }
        return retval;
    }

    public static boolean isElement(int[] rows, int row) {
        if (rows == null || rows.length == 0) {
            return false;
        }
        for (int i = 0; i < rows.length; i++) {
            if (row == rows[i]) {
                return true;
            }
        }
        return false;
    }

    public static String getElapsedString(Long ms) {
        boolean running;
        if (ms < 0) {
            ms = -ms;
            running = true;
        } else {
            running = false;
        }
        if (ms < 1000L) {
            return Long.toString(ms) + "ms";
        }
        long sec = ms / 1000L;
        ms %= 1000L;
        if (sec < 60L) {
            String out = Long.toString(sec);
            if (ms > 0L && running) {
                out += "." + TestStepInstance.FORMATTER_3.format(ms);
            }
            return out + "s";
        }
        long min = sec / 60L;
        sec %= 60L;
        long hr = min / 60L;
        min %= 60L;
        if (hr < 24) {
            /* less than a day hh:mm:ss */
            return TestStepInstance.FORMATTER_2.format(hr) + ":" + TestStepInstance.FORMATTER_2.format(min) + ":" + TestStepInstance.FORMATTER_2.format(sec);
        }
        /* more than a day */
        long day = hr / 24L;
        hr %= 24L;
        if (day < 100) {
            return Long.toString(day) + "day " + Long.toString(hr) + "hour";
        }
        return Long.toString(day) + "day";
    }

    public static void centerOnParent(JDialog dialog) {
        centerOn(dialog, dialog.getParent());
    }

    public static void centerOn(JDialog dialog, Component other) {
        int w = dialog.getWidth();
        int h = dialog.getHeight();
        Rectangle bounds = other.getBounds();
        int bw = bounds.width;
        int bh = bounds.height;
        dialog.setLocation(bounds.x + (bw - w) / 2, bounds.y + (bh - h) / 2);
    }

    public static void centerOnScreen(JDialog jdialog) {
        int w = jdialog.getWidth();
        int h = jdialog.getHeight();
//        System.out.println("size: " + w + "," + h);

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        int bw = bounds.width;
        int bh = bounds.height;
//        System.out.println("max: " + bw + "," + bh);
//        jdialog.setLocationByPlatform(false);
        jdialog.setLocation((bw - w) / 2, (bh - h) / 2);
    }

    public static void maxIt(JFrame jFrame) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle r = env.getMaximumWindowBounds();
        Dimension d = r.getSize();
        jFrame.setSize(d);
        jFrame.setPreferredSize(d);
    }

    public static void maxItWidth(JFrame jFrame) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle r = env.getMaximumWindowBounds();
        Dimension d = r.getSize();
        d.setSize(d.width, Math.min(d.height, jFrame.getPreferredSize().height));
        jFrame.setSize(d);
        jFrame.setPreferredSize(d);
    }

    /**
     * Returns the row index of the last visible row.
     */
    public static int getFirstVisibleRowIndex(JTable table) {
        ComponentOrientation or = table.getComponentOrientation();
        Rectangle r = table.getVisibleRect();
        if (!or.isLeftToRight()) {
            r.translate((int) r.getWidth() - 1, 0);
        }
        return table.rowAtPoint(r.getLocation());
    }

    /**
     * Returns the row index of the last visible row.
     */
    public static int getLastVisibleRowIndex(JTable table) {
        ComponentOrientation or = table.getComponentOrientation();
        Rectangle r = table.getVisibleRect();
        r.translate(0, (int) r.getHeight() - 1);
        if (or.isLeftToRight()) {
            r.translate((int) r.getWidth() - 1, 0);
            // The next if makes sure that we don't return -1 simply because
            // there is white space at the bottom of the table (ie, the display
            // area is larger than the table)
        }
        if (table.rowAtPoint(r.getLocation()) == -1) {
            if (getFirstVisibleRowIndex(table) == -1) {
                return -1;
            } else {
                return table.getModel().getRowCount() - 1;
            }
        }
        return table.rowAtPoint(r.getLocation());
    }

    public static void scrollToCenter(JTable table, int rowIndex, int vColIndex) {
        //System.out.println("Scrolling to center...");
        if (!(table.getParent() instanceof JViewport)) {
            log.trace("Parent is not a JViewport, but: " + table.getParent());
            return;
        }
        JViewport viewport = (JViewport) table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);


        // The location of the view relative to the table
        Rectangle viewRect = viewport.getViewRect();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0).
        rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

        // Calculate location of rect if it were at the center of view
        int centerX = (viewRect.width - rect.width) / 2;
        int centerY = (viewRect.height - rect.height) / 2;

        // Fake the location of the cell so that scrollRectToVisible
        // will move the cell to the center
        if (rect.x < centerX) {
            centerX = -centerX;
        }
        if (rect.y < centerY) {
            centerY = -centerY;
        }
        rect.translate(centerX, centerY);

        // Scroll the area into view.
        viewport.scrollRectToVisible(rect);
    }

    public static int packColumns(JTable table, int margin) {
        int width = 0;
        for (int c = 0; c < table.getColumnCount(); c++) {
            width += packColumn(table, c, margin, false);
        }
        return width;
    }

    public static int packColumnsWidthFixedFirst(JTable table, int margin) {
        int width = 0;
        for (int c = 0; c < table.getColumnCount(); c++) {
            if (c == 0) {
                width += packColumn(table, c, margin, true);
            } else {
                width += packColumn(table, c, margin, false);
            }
        }
        return width;
    }

    public static int packColumn(JTable table, int vColIndex, int margin, boolean fixed) {
        //TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        if (vColIndex < table.getColumnCount()) {
            TableColumn col = colModel.getColumn(vColIndex);
            if (col != null) {
                if (fixed) {
                    col.setResizable(false);
                    int width = getPreferredColumnWidth(table, vColIndex, margin);
                    if (width != col.getPreferredWidth()) {
                        //System.out.println("Setting first column width to: " + width);
                        col.setMinWidth(width);
                        col.setMaxWidth(width);
                        col.setPreferredWidth(width);
                    }
                    return width;
                } else {
                    int width = getPreferredColumnWidth(table, vColIndex, margin);
                    col.setPreferredWidth(width);
                    return width;
                }
            }
        }
        return 0;
    }
//    public static int packColumn(JTable table, int vColIndex, int margin, boolean max) {
//        //TableModel model = table.getModel();
//        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
//        if (vColIndex < table.getColumnCount()) {
//            TableColumn col = colModel.getColumn(vColIndex);
//            if (col != null) {
//                if (max) {
//                    col.setResizable(false);
//                    int width = margin < 0 ? getPreferredColumnWidth(table, vColIndex, margin) : margin;
//                    if (width != col.getPreferredWidth()) {
//                        System.out.println("Setting first column width to: " + width);
//                        col.setMinWidth(width);
//                        col.setMaxWidth(width);
//                        col.setPreferredWidth(width);
//                    }
//                    return width;
//                } else {
//                    int width = getPreferredColumnWidth(table, vColIndex, margin);
//                    col.setPreferredWidth(width);
//                    return width;
//                }
//            }
//        }
//        return 0;
//    }

    public static int getPreferredColumnWidth(JTable table, int vColIndex, int margin) {
        int width = 0;
        if (vColIndex >= table.getColumnCount()) {
            return 0;
        }
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        if (renderer != null) {
            Component c = renderer.getTableCellRendererComponent(table, col.getHeaderValue(), false, false, 0, 0);
            if (c != null) {
                Dimension d = c.getPreferredSize();
                if (d != null) {
                    width = d.width;
                }
            }
        }

        // Get maximum width of column data
        for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            if (renderer != null) {
                Component c = renderer.getTableCellRendererComponent(table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
                if (c != null) {
                    Dimension d = c.getPreferredSize();
                    if (d != null) {
                        width = Math.max(width, d.width);
                    }
                }
            }
        }

        // Add margin
        width += 2 * margin;
        return width;
    }

    public static Dimension getMaximumWindowDimension() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        //System.out.println("bounds:" + bounds.width + " " + bounds.height);
        return new Dimension(bounds.width, bounds.height);
    }

    public static String getBytes(long bytes) {
        if (bytes <= 9999) {
            return Long.toString(bytes) + " B";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " KB";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " MB";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " GB";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " TB";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " PB";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " EB";
        }
        bytes /= 1024;
        if (bytes <= 9999) {
            return Long.toString(bytes) + " ZB";
        }
        bytes /= 1024;
        return Long.toString(bytes) + " YB";
    }
}
