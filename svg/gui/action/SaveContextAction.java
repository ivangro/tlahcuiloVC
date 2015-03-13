package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import svg.context.SavePreviousStory;
import svg.core.SVGRepository;
import svg.engagement.*;

/**
 * Class to store the context of an image into a file<br>
 * IMPORTANT: For the atom generation process, declare the desired class to employ here.
 * @author Ivan Guerrero
 */
public class SaveContextAction extends AbstractAction{
    private SVGRepository repository;
    
    public SaveContextAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        IAtomGenerator generator = new DesignAtomGenerator();
        generator.detectAtoms(repository, true);
        SavePreviousStory.getInstance().saveDrawing(repository);
        JOptionPane.showMessageDialog(null, "Context successfully stored", "Context stored", JOptionPane.INFORMATION_MESSAGE);
    }
}