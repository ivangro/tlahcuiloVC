package svg.gui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import svg.elems.*;

/**
 * Class to manage the SVGelement specs
 * @author Ivan Guerrero
 */
public class SVGElementSpecsGUI extends javax.swing.JDialog {

    /**
     * Creates new form SVGElementSpecsGUI
     */
    public SVGElementSpecsGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadSpecs();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtImagePath = new javax.swing.JTextField();
        txtWidth = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        txtSizes = new javax.swing.JTextField();
        spAlpha = new javax.swing.JSpinner();
        bttnSelectImage = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lbElementSpecs = new javax.swing.JList(new DefaultListModel<SVGElementSpecs>());
        jLabel6 = new javax.swing.JLabel();
        bttnDelete = new javax.swing.JButton();
        bttnAdd = new javax.swing.JButton();
        bttnSave = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtImageIcon = new javax.swing.JTextField();
        bttnSelectIcon = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Image path:");

        jLabel2.setText("Width:");

        jLabel3.setText("Height:");

        jLabel4.setText("Available sizes:");

        jLabel5.setText("Alpha:");

        txtImagePath.setMaximumSize(new java.awt.Dimension(200, 20));
        txtImagePath.setMinimumSize(new java.awt.Dimension(200, 20));
        txtImagePath.setPreferredSize(new java.awt.Dimension(200, 20));

        txtWidth.setMaximumSize(new java.awt.Dimension(6, 60));
        txtWidth.setMinimumSize(new java.awt.Dimension(6, 60));

        txtHeight.setMaximumSize(new java.awt.Dimension(6, 60));
        txtHeight.setMinimumSize(new java.awt.Dimension(6, 60));
        txtHeight.setPreferredSize(new java.awt.Dimension(60, 20));

        txtSizes.setText("3");
        txtSizes.setMaximumSize(new java.awt.Dimension(6, 60));
        txtSizes.setMinimumSize(new java.awt.Dimension(6, 60));
        txtSizes.setPreferredSize(new java.awt.Dimension(60, 20));

        spAlpha.setModel(new javax.swing.SpinnerNumberModel(100, 0, 100, 1));

        bttnSelectImage.setText("Select");
        bttnSelectImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSelectImageActionPerformed(evt);
            }
        });

        lbElementSpecs.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lbElementSpecsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lbElementSpecs);

        jLabel6.setText("Element Specifications:");

        bttnDelete.setText("Delete");
        bttnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnDeleteActionPerformed(evt);
            }
        });

        bttnAdd.setText("Add");
        bttnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnAddActionPerformed(evt);
            }
        });

        bttnSave.setText("Save");
        bttnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSaveActionPerformed(evt);
            }
        });

        jLabel7.setText("Image icon:");

        txtImageIcon.setMaximumSize(new java.awt.Dimension(200, 20));
        txtImageIcon.setMinimumSize(new java.awt.Dimension(200, 20));
        txtImageIcon.setPreferredSize(new java.awt.Dimension(200, 20));

        bttnSelectIcon.setText("Select");
        bttnSelectIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSelectIconActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bttnDelete)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bttnSave, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtWidth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtSizes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtHeight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(bttnAdd)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7))
                                .addGap(42, 42, 42)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtImageIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtImagePath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bttnSelectImage)
                                    .addComponent(bttnSelectIcon))))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtImagePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bttnSelectImage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtImageIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bttnSelectIcon))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtSizes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(spAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bttnAdd))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bttnDelete)
                    .addComponent(bttnSave))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSelectImageActionPerformed
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg","gif","png"));
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                txtImagePath.setText(file.getCanonicalPath().toString());
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error while loading image file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_bttnSelectImageActionPerformed

    private void bttnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnAddActionPerformed
        SVGElementSpecs specs = lbElementSpecs.getSelectedValue();
        if (specs != null) {
            readSpecs(specs);
        }
        else {
            specs = new SVGElementSpecs();
            readSpecs(specs);
            DefaultListModel<SVGElementSpecs> model = (DefaultListModel<SVGElementSpecs>)lbElementSpecs.getModel();
            specs.setID(model.getSize());
            model.addElement(specs);
            writeSpecs(new SVGElementSpecs());
        }
    }//GEN-LAST:event_bttnAddActionPerformed

    private void bttnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnDeleteActionPerformed
        SVGElementSpecs specs = lbElementSpecs.getSelectedValue();
        if (specs != null) {
            DefaultListModel<SVGElementSpecs> model = (DefaultListModel<SVGElementSpecs>)lbElementSpecs.getModel();
            model.removeElement(specs);
        }
    }//GEN-LAST:event_bttnDeleteActionPerformed

    private void lbElementSpecsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lbElementSpecsValueChanged
        SVGElementSpecs specs = lbElementSpecs.getSelectedValue();
        if (specs != null) {
            writeSpecs(specs);
            bttnAdd.setText("Update");
        }
        else {
            writeSpecs(new SVGElementSpecs());
            bttnAdd.setText("Add");
        }
    }//GEN-LAST:event_lbElementSpecsValueChanged

    private void bttnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSaveActionPerformed
        ElementFactory factory = ElementFactory.getInstance();
        factory.getSVGElementSpecs().clear();
        DefaultListModel<SVGElementSpecs> model = (DefaultListModel<SVGElementSpecs>)lbElementSpecs.getModel();
        for (int i=0; i<model.size(); i++) {
            SVGElementSpecs specs = model.elementAt(i);
            factory.addSVGElementSpecs(specs);
        }
        JOptionPane.showMessageDialog(this, "Image specs saved", "Image specs saved", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bttnSaveActionPerformed

    private void bttnSelectIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSelectIconActionPerformed
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg","gif","png"));
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                txtImageIcon.setText(file.getCanonicalPath().toString());
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error while loading image file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_bttnSelectIconActionPerformed


    private void readSpecs(SVGElementSpecs specs) {
        if (specs != null) {
            specs.setImagePath("file:///" + txtImagePath.getText().replaceAll("\\\\", "/"));
            specs.setIconPath(txtImageIcon.getText().replaceAll("\\\\", "/"));
            specs.setImageAlpha((int)spAlpha.getValue());
            specs.setImageHeight(Integer.parseInt(txtHeight.getText()));
            specs.setImageWidth(Integer.parseInt(txtWidth.getText()));
            specs.setAvailableSizes(Integer.parseInt(txtSizes.getText()));
        }
    }
    
    private void writeSpecs(SVGElementSpecs specs) {
        String imagePath = specs.getImagePath();
        if (!imagePath.startsWith("file:///"))
            imagePath = "file:///" + imagePath;
        txtImagePath.setText(imagePath);
        imagePath = specs.getIconPath();
        txtImageIcon.setText(imagePath);
        txtHeight.setText(specs.getImageHeight() + "");
        txtWidth.setText(specs.getImageWidth() + "");
        txtSizes.setText(specs.getAvailableSizes().size() + "");
        spAlpha.setValue(specs.getImageAlpha());
    }
    
    private void loadSpecs() {
        ElementFactory factory = ElementFactory.getInstance();
        DefaultListModel<SVGElementSpecs> model = (DefaultListModel<SVGElementSpecs>)lbElementSpecs.getModel();
        model.clear();
        for (SVGElementSpecs specs : factory.getSVGElementSpecs()) {
            model.addElement(specs);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SVGElementSpecsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SVGElementSpecsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SVGElementSpecsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SVGElementSpecsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SVGElementSpecsGUI dialog = new SVGElementSpecsGUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bttnAdd;
    private javax.swing.JButton bttnDelete;
    private javax.swing.JButton bttnSave;
    private javax.swing.JButton bttnSelectIcon;
    private javax.swing.JButton bttnSelectImage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<SVGElementSpecs> lbElementSpecs;
    private javax.swing.JSpinner spAlpha;
    private javax.swing.JTextField txtHeight;
    private javax.swing.JTextField txtImageIcon;
    private javax.swing.JTextField txtImagePath;
    private javax.swing.JTextField txtSizes;
    private javax.swing.JTextField txtWidth;
    // End of variables declaration//GEN-END:variables
}