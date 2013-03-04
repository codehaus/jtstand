/*
 * Copyright (c) 2013 Albert Kurucz. 
 *
 * This file, TestSequenceInstanceLabel.java is part of JTStand.
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
package com.jtstand.label;

import com.jtstand.TestSequenceInstance;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.swing.JPanel;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;

/**
 *
 * @author albert_kurucz
 */
public class TestSequenceInstanceLabel extends JPanel {

    public static final String PRINTER_NAME = "Smart Label Printer 450";
    public static final String PRINTER_MEDIA = "SLP-SRL Shipping(2.13x4.00)";
    String printerName = PRINTER_NAME;
    String printerMedia = PRINTER_MEDIA;

    public TestSequenceInstanceLabel() {
        initComponents();
        try {
            //Barcode barcode = BarcodeFactory.createCode128B(jLabelSerialNumber.getText());
            Barcode barcodeSerial = BarcodeFactory.createCode128B("1234567890");
            barcodeSerial.setBarWidth(1);
            jPanel1.add(barcodeSerial);
            Barcode barcodeErrorCode = BarcodeFactory.createCode128B("ERR01");
            barcodeErrorCode.setBarWidth(1);
            jPanel16.add(barcodeErrorCode);
        } catch (BarcodeException ex) {
            Logger.getLogger(TestSequenceInstanceLabel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TestSequenceInstanceLabel(String printerName, String printerMedia) {
        this();
        this.printerName = printerName;
        this.printerMedia = printerMedia;
    }

    public void setTestSequenceInstance(TestSequenceInstance tsi) {
        jLabelOperator.setText(tsi.getEmployeeNumber());
        jLabelTestStation.setText(tsi.getTestStation().getHostName());
        jLabelFixture.setText(tsi.getTestFixture().getFixtureName());
        jLabelPartNumber.setText(tsi.getPartNumber());
        jLabelPartRevision.setText(tsi.getPartRevision());
        jLabelTestType.setText(tsi.getTestTypeName());
        jLabelTestTime.setText(tsi.getStartedString());
        try {
            //Barcode barcode = BarcodeFactory.createCode128B(jLabelSerialNumber.getText());
            String sn = tsi.getSerialNumber();
            if (sn == null) {
                sn = "null";
            }
            Barcode barcodeSerial = BarcodeFactory.createCode128B(sn);
            barcodeSerial.setBarWidth(1);
            jPanel1.removeAll();
            jPanel1.add(barcodeSerial);
            jPanel1.validate();

            String fcode = "PASS";
            if (!tsi.isPassed()) {
                fcode = tsi.getFailureCode();
                if (fcode == null) {
                    fcode = "null";
                }
            }
            Barcode barcodeErrorCode = BarcodeFactory.createCode128B(fcode);
            barcodeErrorCode.setBarWidth(1);
            jPanel16.removeAll();
            jPanel16.add(barcodeErrorCode);
            jPanel16.validate();
        } catch (BarcodeException ex) {
            Logger.getLogger(TestSequenceInstanceLabel.class.getName()).log(Level.SEVERE, null, ex);
        }
//        setSize(getPreferredSize());
//        this.doLayout();
        validate();
    }

    public static void printPageFormat(PageFormat pf) {
        System.out.println("paper size:" + pf.getWidth() + "x" + pf.getHeight() + " orientation:" + pf.getOrientation());
        System.out.println("imageable position: X=" + pf.getImageableX() + " Y=" + pf.getImageableY());
        System.out.println("imageable size:" + pf.getImageableWidth() + "x" + pf.getImageableHeight());
    }

    public void print(PrinterJob printerJob) throws PrinterException {
        HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        Media[] res = (Media[]) printerJob.getPrintService().getSupportedAttributeValues(Media.class, null,
                null);
        for (int i = 0; i < res.length; i++) {
            System.out.println("res[" + i + "]=" + res[i]);
            System.out.println(res[i].toString());
            if (res[i].toString().equals(printerMedia)) {
                attributes.add(res[i]);
            }
        }
        printerJob.print(attributes);
        PageFormat pageFormat = printerJob.getPageFormat(attributes);
        System.out.println("[after opening]");
        printPageFormat(pageFormat);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        printerJob.setPrintable(new TestSequenceInstanceLabel.MyPrintable(this), pageFormat);
        try {
            printerJob.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    public void print(String printerName, String printerMedia) {
        this.printerName = printerName;
        this.printerMedia = printerMedia;
        print();
    }

    public void print() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        PrintService[] printService = PrinterJob.lookupPrintServices();
        for (int i = 0; i < printService.length; i++) {
            System.out.println(printService[i].getName());
            if (printService[i].getName().compareTo(printerName) == 0) {
                try {
                    printerJob.setPrintService(printService[i]);
                    setDoubleBuffered(false);
                    setSize(getPreferredSize());
                    addNotify();
                    validate();
                    print(printerJob);
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        TestSequenceInstanceLabel tsil = new TestSequenceInstanceLabel();
        tsil.print();

//        PrinterJob printerJob = PrinterJob.getPrinterJob();
//        PrintService[] printService = PrinterJob.lookupPrintServices();
//        for (int i = 0; i < printService.length; i++) {
//            System.out.println(printService[i].getName());
//
//            if (printService[i].getName().compareTo(PRINTER_NAME) == 0) {
//                try {
//                    printerJob.setPrintService(printService[i]);
//                    TestSequenceInstanceLabel tsil = new TestSequenceInstanceLabel();
//                    tsil.setDoubleBuffered(false);
//                    tsil.setSize(tsil.getPreferredSize());
//                    tsil.addNotify();
//                    tsil.validate();
//                    tsil.printTo(printerJob);
//                } catch (PrinterException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    public static class MyPrintable implements Printable {

        private JPanel panel;

        public MyPrintable(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat,
                int pageIndex) throws PrinterException {
            System.out.println(pageIndex);
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                g2d.scale(pageFormat.getImageableWidth() / panel.getPreferredSize().width, pageFormat.getImageableHeight() / panel.getPreferredSize().height);
                panel.paint(g2d);
                result = PAGE_EXISTS;
            }
            return result;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelOperator = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabelTestStation = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabelFixture = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabelPartNumber = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabelPartRevision = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabelTestType = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabelTestTime = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();

        jLabel9.setText("jLabel9");

        setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("Serial Number:");
        jPanel3.add(jLabel1);

        jPanel12.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jPanel7.add(jPanel1);

        jPanel12.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(0, 1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Operator:");
        jPanel4.add(jLabel3);

        jLabelOperator.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelOperator.setText("12345");
        jPanel4.add(jLabelOperator);

        jPanel2.add(jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Test Station:");
        jPanel5.add(jLabel5);

        jLabelTestStation.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelTestStation.setText("STATION1");
        jPanel5.add(jLabelTestStation);

        jPanel2.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("Fixture:");
        jPanel6.add(jLabel7);

        jLabelFixture.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelFixture.setText("FX1");
        jPanel6.add(jLabelFixture);

        jPanel2.add(jPanel6);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText("Part Number:");
        jPanel10.add(jLabel10);

        jLabelPartNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelPartNumber.setText("MYDEMO");
        jPanel10.add(jLabelPartNumber);

        jPanel2.add(jPanel10);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setText("Part Revision:");
        jPanel9.add(jLabel12);

        jLabelPartRevision.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelPartRevision.setText("001");
        jPanel9.add(jLabelPartRevision);

        jPanel2.add(jPanel9);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setText("Test Type:");
        jPanel8.add(jLabel14);

        jLabelTestType.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelTestType.setText("FULL");
        jPanel8.add(jLabelTestType);

        jPanel2.add(jPanel8);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.Y_AXIS));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel16.setText("Test Time:");
        jPanel11.add(jLabel16);

        jLabelTestTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelTestTime.setText("2013/03/01 14:38");
        jPanel11.add(jLabelTestTime);

        jPanel2.add(jPanel11);

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText("Error Code:");
        jPanel14.add(jLabel2);

        jPanel13.add(jPanel14, java.awt.BorderLayout.NORTH);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.Y_AXIS));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.Y_AXIS));
        jPanel15.add(jPanel16);

        jPanel13.add(jPanel15, java.awt.BorderLayout.CENTER);

        add(jPanel13, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFixture;
    private javax.swing.JLabel jLabelOperator;
    private javax.swing.JLabel jLabelPartNumber;
    private javax.swing.JLabel jLabelPartRevision;
    private javax.swing.JLabel jLabelTestStation;
    private javax.swing.JLabel jLabelTestTime;
    private javax.swing.JLabel jLabelTestType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    // End of variables declaration//GEN-END:variables
}
