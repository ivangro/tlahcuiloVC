package svg.gui.action;

import java.awt.HeadlessException;
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
import svg.story.FileManager;

/**
 *
 * @author Ivan Guerrero
 */
public class SaveImageAction extends AbstractAction {
    private SVGRepository repository;
    
    public SaveImageAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("SVG Images", "svg"));
        chooser.setSelectedFile(new File("drawing.svg"));
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            saveFile(file, true);
        }
    }

    public void saveFile(File file, boolean guiMessages) throws HeadlessException {
        try {
            FileManager.saveFile(file, repository);
            if (guiMessages)
                JOptionPane.showMessageDialog(null, "The file has been saved", "File saved",            
                                          JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.SEVERE, null, ex);
            if (guiMessages)
                JOptionPane.showMessageDialog(null, "Error while saving file", ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

}
