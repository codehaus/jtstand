/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AdminProject.java
 *
 * Created on Dec 5, 2009, 2:15:18 PM
 */

package com.jtstand.swing;

/**
 *
 * @author albert_kurucz
 */
public class AdminProject extends javax.swing.JPanel {

    /** Creates new form AdminProject */
    public AdminProject() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPaneProjectProperties = new javax.swing.JScrollPane();
        jListProjectProperties = new javax.swing.JList();
        jScrollPaneProducts = new javax.swing.JScrollPane();
        jListProducts = new javax.swing.JList();
        jScrollPaneStations = new javax.swing.JScrollPane();
        jListStations = new javax.swing.JList();

        setLayout(new java.awt.BorderLayout());

        jListProjectProperties.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneProjectProperties.setViewportView(jListProjectProperties);

        jTabbedPane1.addTab("Properties", jScrollPaneProjectProperties);

        jListProducts.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneProducts.setViewportView(jListProducts);

        jTabbedPane1.addTab("Products", jScrollPaneProducts);

        jListStations.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPaneStations.setViewportView(jListStations);

        jTabbedPane1.addTab("Stations", jScrollPaneStations);

        jSplitPane1.setLeftComponent(jTabbedPane1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jListProducts;
    private javax.swing.JList jListProjectProperties;
    private javax.swing.JList jListStations;
    private javax.swing.JScrollPane jScrollPaneProducts;
    private javax.swing.JScrollPane jScrollPaneProjectProperties;
    private javax.swing.JScrollPane jScrollPaneStations;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

}
