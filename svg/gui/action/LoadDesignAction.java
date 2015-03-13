package svg.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.story.FileManager;

/**
 * Action to load a design from a file
 * @author Ivan Guerrero
 */
public class LoadDesignAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData dataContainer;
    private boolean execute;
    
    public LoadDesignAction(SVGRepository repository, IRefreshData dataContainer) {
        this.repository = repository;
        this.dataContainer = dataContainer;
        this.execute = true;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("Design Files", "des"));
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                //repository.generateSVGDocument();
                File file = chooser.getSelectedFile();
                FileManager.loadDesign(file, repository, execute);
                dataContainer.refreshData();
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error while saving file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }            
        }
    }

    public void setExecute(boolean execute) {
        this.execute = execute;
    }
}
