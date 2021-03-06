package svg.gui;

import svg.core.SVGRepository;

/**
 * Class to display the different analysis applied to the canvas
 * @author Ivan Guerrero
 */
public class DrawingAnalysisGUI extends javax.swing.JDialog {
    private SVGRepository repository;
    
    public DrawingAnalysisGUI(java.awt.Frame parent, boolean modal, SVGRepository repository) {
        super(parent, modal);
        this.repository = repository;
        initComponents();
        loadAnalysis();
    }
    /**
     * Creates new form DrawingAnalysisGUI
     */
    public DrawingAnalysisGUI(java.awt.Frame parent, boolean modal) {
        this(parent, modal, null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpResults = new javax.swing.JTabbedPane();
        panelActions = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtActions = new javax.swing.JEditorPane();
        panelDesign = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDesign = new javax.swing.JEditorPane();
        panelResults = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtResults = new javax.swing.JEditorPane();
        bttnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Drawing analysis");

        jScrollPane1.setViewportView(txtActions);

        javax.swing.GroupLayout panelActionsLayout = new javax.swing.GroupLayout(panelActions);
        panelActions.setLayout(panelActionsLayout);
        panelActionsLayout.setHorizontalGroup(
            panelActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelActionsLayout.setVerticalGroup(
            panelActionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelActionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpResults.addTab("Actions", panelActions);

        jScrollPane2.setViewportView(txtDesign);

        javax.swing.GroupLayout panelDesignLayout = new javax.swing.GroupLayout(panelDesign);
        panelDesign.setLayout(panelDesignLayout);
        panelDesignLayout.setHorizontalGroup(
            panelDesignLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDesignLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelDesignLayout.setVerticalGroup(
            panelDesignLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDesignLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpResults.addTab("Design", panelDesign);

        jScrollPane3.setViewportView(txtResults);

        javax.swing.GroupLayout panelResultsLayout = new javax.swing.GroupLayout(panelResults);
        panelResults.setLayout(panelResultsLayout);
        panelResultsLayout.setHorizontalGroup(
            panelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelResultsLayout.setVerticalGroup(
            panelResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpResults.addTab("Results", panelResults);

        bttnRefresh.setText("Refresh");
        bttnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tpResults, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bttnRefresh)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tpResults)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bttnRefresh)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnRefreshActionPerformed
        loadAnalysis();
    }//GEN-LAST:event_bttnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(DrawingAnalysisGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DrawingAnalysisGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DrawingAnalysisGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DrawingAnalysisGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DrawingAnalysisGUI dialog = new DrawingAnalysisGUI(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton bttnRefresh;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel panelActions;
    private javax.swing.JPanel panelDesign;
    private javax.swing.JPanel panelResults;
    private javax.swing.JTabbedPane tpResults;
    private javax.swing.JEditorPane txtActions;
    private javax.swing.JEditorPane txtDesign;
    private javax.swing.JEditorPane txtResults;
    // End of variables declaration//GEN-END:variables

    private void loadAnalysis() {
        txtActions.setText(repository.getActionDescriptions());
        txtDesign.setText(repository.getDesignActionDescriptions());
        txtResults.setText(repository.getDetectorAnalysis());
        txtActions.setCaretPosition(0);
        txtDesign.setCaretPosition(0);
        txtResults.setCaretPosition(0);
    }
}
