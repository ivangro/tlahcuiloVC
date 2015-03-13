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
import svg.gui.SVGDrawingGUI;
import svg.story.FileManager;

/**
 * Action to save a story in a file
 * @author Ivan Guerrero
 */
public class SaveStoryAction extends AbstractAction {
    private SVGRepository repository;
    
    public SaveStoryAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("Story Files", "str"));
        chooser.setSelectedFile(new File("drawing.str"));
        
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                FileManager.saveStory(file, repository);
                JOptionPane.showMessageDialog(null, "The file has been saved", "File saved", 
                                              JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getGlobal().log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error while saving file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }            
        }
    }
}
