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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author albert_kurucz
 */
public class AboutDialog extends javax.swing.JDialog {

    public static final long serialVersionUID = 20080507L;

    /** Creates new form AboutDialog */
    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        Package p = getClass().getPackage();
//        String info = p.getImplementationTitle() + " " + p.getImplementationVersion() + " by " + p.getImplementationVendor();
        //jTextArea2.setText(getPackagesInfo());
        if (Desktop.isDesktopSupported()) {
            addBrowseDocumentationButton();
        }
        if (getClass().getPackage() != null && getClass().getPackage().getImplementationVersion() != null) {
            jTextAreaBuild.setText(getClass().getPackage().getImplementationVersion());
        }
        pack();
        Util.centerOnParent(this);
        setVisible(true);
    }

    public String getPackagesInfo() {
        String retval = "";
        TreeSet<String> infoSet = new TreeSet<String>();
        for (Package p : Package.getPackages()) {
            String info = getPackageInfo(p);
            if (info != null) {
                infoSet.add(info);
            }
        }
        for (String info : infoSet) {
            if (retval.length() > 0) {
                retval += "\n";
            }
            retval += info;
        }
        return retval;
    }

    private String getPackageInfo(Package p) {
        if (p.getImplementationTitle() == null) {
            return null;
        }
        String info = p.getImplementationTitle();
        if (p.getImplementationVersion() != null) {
            info += " " + p.getImplementationVersion();
        }
        if (p.getImplementationVendor() != null) {
            info += " by " + p.getImplementationVendor();
        }
        return info;
    }
    javax.swing.JButton jButtonBrowseDocumentation;

    private void addBrowseDocumentationButton() {
        jButtonBrowseDocumentation = new javax.swing.JButton();
        jButtonBrowseDocumentation.setText("Browse Documentation");
        jButtonBrowseDocumentation.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseDocumentationActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonBrowseDocumentation);

    }

    private void browseDocumentationActionPerformed(java.awt.event.ActionEvent evt) {
        openHelp();
        dispose();
    }

    static void openHelp() {
        if (!openFile("doc" + File.separator + "index.html") && !openFile(".." + File.separator + "jtstand" + File.separator + "doc" + File.separator + "index.html")) {
            try {
                Desktop.getDesktop().browse(URI.create("http://www.jtstand.com/"));
            } catch (IOException ex) {
                Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static boolean openFile(String path) {
        File file = new File(path);
        if (!file.exists() || !file.canRead()) {
            return false;
        } else {
            URI uri = file.toURI().normalize();
            File normalFile = new File(uri);
            System.out.println("Opening file: '" + normalFile.getPath() + "'...");
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                try {
                    Runtime.getRuntime().exec("cmd.exe /c " + normalFile.getPath());
                } catch (IOException ex) {
                    Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    Desktop.getDesktop().browse(uri);
                } catch (IOException ex) {
                    Logger.getLogger(AboutDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jTextAreaBuild = new javax.swing.JTextArea();
        jPanelButtons = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/jtbean.png")));
        jLabel1.setText(" JTStand");
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel4.add(jLabel1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 10);
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("About"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Verdana", 0, 13)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("JTStand is a free scripting environment for data collection,\nan open source software, written in Java.\nIt helps you to develop and execute Sequences and to analyze the Results.\n\nProject home page: http://www.jtstand.com/");
        jPanel1.add(jTextArea1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Version"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextAreaBuild.setColumns(20);
        jTextAreaBuild.setEditable(false);
        jTextAreaBuild.setFont(new java.awt.Font("Verdana", 0, 13));
        jTextAreaBuild.setRows(1);
        jTextAreaBuild.setText("preliminary");
        jPanel2.add(jTextAreaBuild, java.awt.BorderLayout.PAGE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel2, gridBagConstraints);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelButtons.add(jButtonClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanelButtons, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        dispose();
}//GEN-LAST:event_jButtonCloseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        openHelp();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaBuild;
    // End of variables declaration//GEN-END:variables
}
