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
 *
 * @author Ivan Guerrero
 */
public class LoadStoryAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData dataContainer;
    private boolean keepDesignActions;
    
    public LoadStoryAction(SVGRepository repository, IRefreshData dataContainer) {
        this.repository = repository;
        this.dataContainer = dataContainer;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("Story Files", "str"));
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                repository.generateSVGDocument(keepDesignActions);
                File file = chooser.getSelectedFile();
                FileManager.loadStory(file, repository, keepDesignActions);
                dataContainer.refreshData();
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error while saving file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }            
        }
    }

    /**
     * @return the keepDesignActions
     */
    public boolean isKeepDesignActions() {
        return keepDesignActions;
    }

    /**
     * @param keepDesignActions the keepDesignActions to set
     */
    public void setKeepDesignActions(boolean keepDesignActions) {
        this.keepDesignActions = keepDesignActions;
    }

}
