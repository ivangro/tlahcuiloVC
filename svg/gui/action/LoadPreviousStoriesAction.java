package svg.gui.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import svg.context.LoadPreviousStories;
import svg.core.SVGConfig;
import svg.core.SVGRepository;
/**
 *
 * @author Ivan Guerrero
 */
public class LoadPreviousStoriesAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent ae) {
        int option = JOptionPane.showConfirmDialog(null, "This will delete the current atom's file", "Load Previous Stories", 
                                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            File atomsFile = new File(SVGConfig.AtomsFile);
            if (atomsFile.delete())
                System.out.println("File successfully deleted");
            
            LoadPreviousStories loader = LoadPreviousStories.getInstance();
            List<SVGRepository> previousStories = loader.getPreviousStories();
            for (SVGRepository repo : previousStories) {
                SaveContextAction saveAction = new SaveContextAction(repo);
                saveAction.actionPerformed(null);
            }
        }
    }

}
