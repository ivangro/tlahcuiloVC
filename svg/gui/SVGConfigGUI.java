package svg.gui;

import config.Configuration;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import static svg.core.SVGConfig.*;
import svg.elems.GraphicElement;

/**
 *
 * @author Ivan Guerrero
 */
public class SVGConfigGUI extends javax.swing.JDialog {

    /**
     * Creates new form SVGConfigGUI
     */
    public SVGConfigGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadConfig();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblWidth = new javax.swing.JLabel();
        lblHeight = new javax.swing.JLabel();
        lblUnitDist = new javax.swing.JLabel();
        lblDistOffset = new javax.swing.JLabel();
        lblSymmetry = new javax.swing.JLabel();
        txtWidth = new javax.swing.JTextField();
        txtHeight = new javax.swing.JTextField();
        txtUnitDist = new javax.swing.JTextField();
        txtDistOffset = new javax.swing.JTextField();
        txtSymmetry = new javax.swing.JTextField();
        bttnSave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMinArea = new javax.swing.JTextField();
        txtMaxArea = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMinRhythms = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMaxRhythms = new javax.swing.JTextField();
        lblER = new javax.swing.JLabel();
        lblMaxCycles = new javax.swing.JLabel();
        txtMaxCycles = new javax.swing.JTextField();
        lblSteps = new javax.swing.JLabel();
        txtEngagementSteps = new javax.swing.JTextField();
        lblEngagementSteps = new javax.swing.JLabel();
        lblReflectionSteps = new javax.swing.JLabel();
        txtReflectionSteps = new javax.swing.JTextField();
        simpleElementPanel = new javax.swing.JTabbedPane();
        textPanel = new javax.swing.JPanel();
        lblFont = new javax.swing.JLabel();
        lblFontSize = new javax.swing.JLabel();
        lblWidthFactor = new javax.swing.JLabel();
        fcFont = new ivangro.utils.FontChooserComboBox();
        txtFontSize = new javax.swing.JTextField();
        txtFontWidthFactor = new javax.swing.JTextField();
        lblHeightFactor = new javax.swing.JLabel();
        txtFontHeightFactor = new javax.swing.JTextField();
        imagePanel = new javax.swing.JPanel();
        lblImagePath = new javax.swing.JLabel();
        lblImageHeight = new javax.swing.JLabel();
        lblImageWidth = new javax.swing.JLabel();
        txtImagePath = new javax.swing.JTextField();
        txtImageHeight = new javax.swing.JTextField();
        txtImageWidth = new javax.swing.JTextField();
        bttnSelectFile = new javax.swing.JButton();
        lblSimpleElement = new javax.swing.JLabel();
        cbSimpleElement = new JComboBox(GraphicElement.values());
        lblBGColor = new javax.swing.JLabel();
        txtBGColor = new javax.swing.JLabel();
        bttnSelectBGColor = new javax.swing.JButton();
        lblForeColor = new javax.swing.JLabel();
        txtForeColor = new javax.swing.JLabel();
        bttnSelectForeColor = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("SVG Drawing Config");
        setLocationByPlatform(true);

        lblWidth.setText("Canvas Width: ");

        lblHeight.setText("Canvas Height:");

        lblUnitDist.setText("Max. Unit Distance:");

        lblDistOffset.setText("Distance Offset:");

        lblSymmetry.setText("Max. Symmetry Offset:");

        bttnSave.setText("Save");
        bttnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSaveActionPerformed(evt);
            }
        });

        jLabel1.setText("Employed area:");

        jLabel2.setText("Max:");

        jLabel3.setText("Min:");

        jLabel4.setText("Detected Rhythms:");

        jLabel5.setText("Min:");

        jLabel6.setText("Max:");

        lblER.setText("E-R:");

        lblMaxCycles.setText("Max cycles:");

        lblSteps.setText("Max Steps");

        lblEngagementSteps.setText("Engagement:");

        lblReflectionSteps.setText("Reflection:");

        lblFont.setText("Default Font:");

        lblFontSize.setText("Default Font size:");

        lblWidthFactor.setText("Font Width Factor:");

        lblHeightFactor.setText("Font Height Factor:");

        javax.swing.GroupLayout textPanelLayout = new javax.swing.GroupLayout(textPanel);
        textPanel.setLayout(textPanelLayout);
        textPanelLayout.setHorizontalGroup(
            textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblWidthFactor)
                    .addComponent(lblFontSize)
                    .addComponent(lblFont))
                .addGap(18, 18, 18)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fcFont, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(textPanelLayout.createSequentialGroup()
                        .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtFontSize, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(txtFontWidthFactor, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHeightFactor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFontHeightFactor, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        textPanelLayout.setVerticalGroup(
            textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFont)
                    .addComponent(fcFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFontSize)
                    .addComponent(txtFontSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWidthFactor)
                    .addComponent(txtFontWidthFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHeightFactor)
                    .addComponent(txtFontHeightFactor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        simpleElementPanel.addTab("Text", textPanel);

        lblImagePath.setText("Image path:");

        lblImageHeight.setText("Image height:");

        lblImageWidth.setText("Image width:");

        bttnSelectFile.setText("Select file");
        bttnSelectFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSelectFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(imagePanelLayout.createSequentialGroup()
                        .addComponent(lblImagePath)
                        .addGap(18, 18, 18)
                        .addComponent(txtImagePath, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bttnSelectFile))
                    .addGroup(imagePanelLayout.createSequentialGroup()
                        .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblImageHeight)
                            .addComponent(lblImageWidth))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtImageHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(txtImageWidth))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(imagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblImagePath)
                    .addComponent(txtImagePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bttnSelectFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblImageHeight)
                    .addComponent(txtImageHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblImageWidth)
                    .addComponent(txtImageWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        simpleElementPanel.addTab("Image", imagePanel);

        lblSimpleElement.setText("Simple element type:");

        cbSimpleElement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSimpleElementActionPerformed(evt);
            }
        });

        lblBGColor.setText("Background color:");

        txtBGColor.setText("White");

        bttnSelectBGColor.setText("Select");
        bttnSelectBGColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSelectBGColorActionPerformed(evt);
            }
        });

        lblForeColor.setText("Fore color:");

        txtForeColor.setText("Black");

        bttnSelectForeColor.setText("Select");
        bttnSelectForeColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnSelectForeColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(simpleElementPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSymmetry)
                                    .addComponent(lblDistOffset, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblUnitDist, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(lblER))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtSymmetry, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDistOffset, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUnitDist, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(lblMaxCycles)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtMaxCycles))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtMinRhythms))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtMinArea, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel6)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtMaxRhythms, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtMaxArea, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(46, 46, 46)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(bttnSave)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(lblReflectionSteps)
                                                        .addComponent(lblEngagementSteps)
                                                        .addComponent(lblSteps))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtEngagementSteps)
                                                        .addComponent(txtReflectionSteps, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                            .addGap(2, 2, 2)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSimpleElement)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbSimpleElement, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblWidth)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblHeight)
                            .addComponent(lblBGColor)
                            .addComponent(lblForeColor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtBGColor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bttnSelectBGColor))
                            .addComponent(txtHeight)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtForeColor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bttnSelectForeColor)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWidth)
                    .addComponent(txtWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHeight)
                    .addComponent(txtHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSimpleElement)
                    .addComponent(cbSimpleElement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBGColor)
                    .addComponent(txtBGColor)
                    .addComponent(bttnSelectBGColor))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblForeColor)
                    .addComponent(txtForeColor)
                    .addComponent(bttnSelectForeColor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(simpleElementPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUnitDist)
                    .addComponent(txtUnitDist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDistOffset)
                    .addComponent(txtDistOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSymmetry)
                    .addComponent(txtSymmetry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMinArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaxArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txtMinRhythms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtMaxRhythms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblER)
                    .addComponent(lblMaxCycles)
                    .addComponent(txtMaxCycles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSteps))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEngagementSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEngagementSteps))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReflectionSteps)
                    .addComponent(txtReflectionSteps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(bttnSave)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bttnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSaveActionPerformed
        //Canvas values
        CANVAS_HEIGHT = Integer.parseInt(txtHeight.getText());
        CANVAS_WIDTH = Integer.parseInt(txtWidth.getText());
        BACKGROUND_COLOR = txtBGColor.getText();
        FORE_COLOR = txtForeColor.getText();
        
        GRAPHIC_ELEMENT = (GraphicElement)cbSimpleElement.getSelectedItem();
        //Element text
        defaultFontType = fcFont.getSelectedFontName();
        defaultFontSize = Integer.parseInt(txtFontSize.getText());
        FONT_HEIGHT_FACTOR = Double.parseDouble(txtFontHeightFactor.getText());
        FONT_WIDTH_FACTOR = Double.parseDouble(txtFontWidthFactor.getText());
        //Element image
        imagePath = txtImagePath.getText();
        if (!imagePath.startsWith("file:///"))
            imagePath = "file:///" + imagePath;
        IMAGE_HEIGHT = Integer.parseInt(txtImageHeight.getText());
        IMAGE_WIDTH = Integer.parseInt(txtImageWidth.getText());
        
        MAX_UNIT_DISTANCE = Integer.parseInt(txtUnitDist.getText());
        DISTANCE_OFFSET = Double.parseDouble(txtDistOffset.getText());
        MAX_SYMMETRY_OFFSET = Integer.parseInt(txtSymmetry.getText());
        MIN_EMPLOYED_AREA = Double.parseDouble(txtMinArea.getText());
        MAX_EMPLOYED_AREA = Double.parseDouble(txtMaxArea.getText());
        MIN_NUMBER_OF_RHYTHMS = Integer.parseInt(txtMinRhythms.getText());
        MAX_NUMBER_OF_RHYTHMS = Integer.parseInt(txtMaxRhythms.getText());
        //ER parameters
        ENGAGEMENT_STEPS = Integer.parseInt(txtEngagementSteps.getText());
        REFLECTION_STEPS = Integer.parseInt(txtReflectionSteps.getText());
        MAX_CYCLES = Integer.parseInt(txtMaxCycles.getText());
        
        Configuration.getInstance().saveConfig();
        JOptionPane.showMessageDialog(this, "Config has been saved", "Config saved", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bttnSaveActionPerformed

    private void loadConfig() {
        Configuration.getInstance().loadConfig();
        
        txtHeight.setText(CANVAS_HEIGHT + "");
        txtWidth.setText(CANVAS_WIDTH + "");
        txtForeColor.setText(FORE_COLOR);
        txtBGColor.setText(BACKGROUND_COLOR);
        
        cbSimpleElement.setSelectedItem(GRAPHIC_ELEMENT);
        //Text element
        fcFont.setSelectedItem(defaultFontType + "");
        txtFontSize.setText(defaultFontSize + "");
        txtFontWidthFactor.setText(FONT_WIDTH_FACTOR + "");
        txtFontHeightFactor.setText(FONT_HEIGHT_FACTOR + "");
        //Image element
        txtImageHeight.setText(IMAGE_HEIGHT + "");
        txtImageWidth.setText(IMAGE_WIDTH + "");
        txtImagePath.setText(imagePath);
        txtUnitDist.setText(MAX_UNIT_DISTANCE + "");
        txtDistOffset.setText(DISTANCE_OFFSET + "");
        txtSymmetry.setText(MAX_SYMMETRY_OFFSET + "");
        txtMinArea.setText(MIN_EMPLOYED_AREA + "");
        txtMaxArea.setText(MAX_EMPLOYED_AREA + "");
        txtMinRhythms.setText(MIN_NUMBER_OF_RHYTHMS + "");
        txtMaxRhythms.setText(MAX_NUMBER_OF_RHYTHMS + "");
        txtEngagementSteps.setText(ENGAGEMENT_STEPS + "");
        txtReflectionSteps.setText(REFLECTION_STEPS + "");
        txtMaxCycles.setText(MAX_CYCLES + "");
    }
    
    private void bttnSelectFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSelectFileActionPerformed
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg, gif, png"));
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                txtImagePath.setText(file.getCanonicalPath().toString());
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error while loading image file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_bttnSelectFileActionPerformed

    private void cbSimpleElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSimpleElementActionPerformed
        switch ((GraphicElement)cbSimpleElement.getSelectedItem()) {
            case Image:
                simpleElementPanel.setSelectedIndex(1);
                break;
            case Text:
                simpleElementPanel.setSelectedIndex(0);
                break;
        }
    }//GEN-LAST:event_cbSimpleElementActionPerformed

    private void bttnSelectBGColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSelectBGColorActionPerformed
        Color color = JColorChooser.showDialog(this, "Choose background color", getBackground());
        if (color != null) {
            String red = Integer.toHexString(color.getRed());
            red += (red.length() == 1) ? "0" : "";
            String green = Integer.toHexString(color.getGreen());
            green += (green.length() == 1) ? "0" : "";
            String blue = Integer.toHexString(color.getBlue());
            blue += (blue.length() == 1) ? "0" : "";
            txtBGColor.setText("#" + red + green + blue);
        }
    }//GEN-LAST:event_bttnSelectBGColorActionPerformed

    private void bttnSelectForeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bttnSelectForeColorActionPerformed
        Color color = JColorChooser.showDialog(this, "Choose fore color", getBackground());
        if (color != null) {
            String red = Integer.toHexString(color.getRed());
            red += (red.length() == 1) ? "0" : "";
            String green = Integer.toHexString(color.getGreen());
            green += (green.length() == 1) ? "0" : "";
            String blue = Integer.toHexString(color.getBlue());
            blue += (blue.length() == 1) ? "0" : "";
            txtForeColor.setText("#" + red + green + blue);
        }
    }//GEN-LAST:event_bttnSelectForeColorActionPerformed

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
            java.util.logging.Logger.getLogger(SVGConfigGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SVGConfigGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SVGConfigGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SVGConfigGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SVGConfigGUI dialog = new SVGConfigGUI(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton bttnSave;
    private javax.swing.JButton bttnSelectBGColor;
    private javax.swing.JButton bttnSelectFile;
    private javax.swing.JButton bttnSelectForeColor;
    private javax.swing.JComboBox<GraphicElement> cbSimpleElement;
    private ivangro.utils.FontChooserComboBox fcFont;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblBGColor;
    private javax.swing.JLabel lblDistOffset;
    private javax.swing.JLabel lblER;
    private javax.swing.JLabel lblEngagementSteps;
    private javax.swing.JLabel lblFont;
    private javax.swing.JLabel lblFontSize;
    private javax.swing.JLabel lblForeColor;
    private javax.swing.JLabel lblHeight;
    private javax.swing.JLabel lblHeightFactor;
    private javax.swing.JLabel lblImageHeight;
    private javax.swing.JLabel lblImagePath;
    private javax.swing.JLabel lblImageWidth;
    private javax.swing.JLabel lblMaxCycles;
    private javax.swing.JLabel lblReflectionSteps;
    private javax.swing.JLabel lblSimpleElement;
    private javax.swing.JLabel lblSteps;
    private javax.swing.JLabel lblSymmetry;
    private javax.swing.JLabel lblUnitDist;
    private javax.swing.JLabel lblWidth;
    private javax.swing.JLabel lblWidthFactor;
    private javax.swing.JTabbedPane simpleElementPanel;
    private javax.swing.JPanel textPanel;
    private javax.swing.JLabel txtBGColor;
    private javax.swing.JTextField txtDistOffset;
    private javax.swing.JTextField txtEngagementSteps;
    private javax.swing.JTextField txtFontHeightFactor;
    private javax.swing.JTextField txtFontSize;
    private javax.swing.JTextField txtFontWidthFactor;
    private javax.swing.JLabel txtForeColor;
    private javax.swing.JTextField txtHeight;
    private javax.swing.JTextField txtImageHeight;
    private javax.swing.JTextField txtImagePath;
    private javax.swing.JTextField txtImageWidth;
    private javax.swing.JTextField txtMaxArea;
    private javax.swing.JTextField txtMaxCycles;
    private javax.swing.JTextField txtMaxRhythms;
    private javax.swing.JTextField txtMinArea;
    private javax.swing.JTextField txtMinRhythms;
    private javax.swing.JTextField txtReflectionSteps;
    private javax.swing.JTextField txtSymmetry;
    private javax.swing.JTextField txtUnitDist;
    private javax.swing.JTextField txtWidth;
    // End of variables declaration//GEN-END:variables
}