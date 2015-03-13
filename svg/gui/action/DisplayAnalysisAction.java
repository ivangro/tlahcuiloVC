package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.DrawingAnalysisGUI;

/**
 * Class to display the gui employed to view the different analysis performed to the current canvas
 * @author Ivan Guerrero
 */
public class DisplayAnalysisAction extends AbstractAction {
    private SVGRepository repository;
    
    public DisplayAnalysisAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        DrawingAnalysisGUI gui = new DrawingAnalysisGUI(null, false, repository);
        gui.setVisible(true);
    }
}
