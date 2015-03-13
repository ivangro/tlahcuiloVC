package svg.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import svg.core.SVGConfig;
import svg.elems.*;
import svg.gui.button.AddElementButton;
import svg.gui.button.CanvasListener;

/**
 * Class to display the available elements for a drawing
 * @author Ivan Guerrero
 */
public class ElementCreationToolBar extends JToolBar {
    private ICanvasListener mainFrame;
    private final int BUTTON_HEIGHT = 40, BUTTON_WIDTH = 50;
    private List<AddElementButton> buttons;
    
    public ElementCreationToolBar() {
        
    }
            
    public ElementCreationToolBar(ICanvasListener mainFrame) {
        this.mainFrame = mainFrame;
        loadPredefinedElements();
        loadSVGElementSpecs();
    }
    
    private void bttnManageActionPerformed(ActionEvent ae) {
        SVGElementSpecsGUI gui = new SVGElementSpecsGUI(null, true);
        gui.setVisible(true);
        loadSVGElementSpecs();
    }

    private void loadSVGElementSpecs() {
        super.removeAll();
        ElementFactory factory = ElementFactory.getInstance();
        buttons = new ArrayList<>();
        
        for (final SVGElementSpecs specs : factory.getSVGElementSpecs()) {
            final AddElementButton bttn = new AddElementButton(specs);
            bttn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    SVGConfig.GRAPHIC_ELEMENT = GraphicElement.Image;
                    SVGConfig.CURRENT_IMAGE_ID = specs.getID();
                    mainFrame.adminListener(bttn);
                }
            });
            try {
                //Resets the icon size to have the same size of the button
                Image img = ImageIO.read(new File(specs.getIconPath()));
                Image scaledInstance = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledInstance);
            
                bttn.setIcon(icon);
                bttn.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                bttn.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                bttn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                buttons.add(bttn);
                super.add(bttn);
            } catch (Exception ex) {}
        }
        
        JButton bttnManage = new JButton(" + ");
        bttnManage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                bttnManageActionPerformed(ae);
            }
        });
        super.add(bttnManage);
        
        JSlider slSize = new JSlider(JSlider.VERTICAL, 1, 3, 2);
        slSize.setInverted(true);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(new Integer(1), new JLabel("Small"));
        labels.put(new Integer(2), new JLabel("Medium"));
        labels.put(new Integer(3), new JLabel("Big"));
        slSize.setLabelTable(labels);
        slSize.setPaintLabels(true);
        slSize.setPreferredSize(new Dimension(50, 100));
        slSize.setMaximumSize(new Dimension(50, 100));
        slSize.setMinimumSize(new Dimension(50, 100));
        slSize.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent ce) {
                int value = ((JSlider)ce.getSource()).getValue();
                for(AddElementButton bttn : buttons) {
                    bttn.setSize(value);
                }
            }
        });
        super.add(slSize);
        super.validate();
    }
    
    public void loadPredefinedElements() {
        ElementLoader loader = new ElementLoader();
        File basicsDir = new File(SVGConfig.BASIC_ELEMS_PATH);
        loader.loadElements(basicsDir);
        
//        File[] files = basicsDir.listFiles(new FilenameFilter() {
//            @Override
//            public boolean accept(File file, String string) {
//                return string.endsWith(".png") && !string.startsWith("background");
//            }
//        });
//        
//        int i=0;
//        ElementFactory factory = ElementFactory.getInstance();
//        
//        for (File file : files) {
//            try {
//                BufferedImage img = ImageIO.read(file);
//                SVGElementSpecs specs = new SVGElementSpecs();
//                specs.setAvailableSizes(3);
//                specs.setID(i);
//                i++;
//                specs.setIconPath(file.getAbsolutePath());
//                specs.setImageAlpha(100);
//                specs.setImageHeight(img.getHeight());
//                specs.setImagePath(file.getAbsolutePath());
//                specs.setImageWidth(img.getWidth());
//                if (!file.getName().startsWith("focalPoint"))
//                    factory.addSVGElementSpecs(specs);
//                else {
//                    specs.setID(100);
//                    i--;
//                    factory.setFocalPointSpecs(specs);
//                }
//            } catch (IOException ex) {
//                Logger.getGlobal().log(Level.SEVERE, null, ex);
//            }
//        }
//        Logger.getGlobal().log(Level.INFO, "Image specs added: {0}", factory.getSVGElementSpecs().size());
    }

    /**
     * Releases the rest of the buttons that add an element to the canvas
     * @param button 
     */
    public void releaseAddElementButtons(CanvasListener button) {
        for (int i=0; i<buttons.size(); i++) {
            AddElementButton bttn = buttons.get(i);
            if (!bttn.equals(button) && bttn.isSelected()) {
                bttn.setSelected(false);
                mainFrame.adminListener(bttn);
            }
        }
    }
}