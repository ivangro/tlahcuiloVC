package svg.gui.action;

import ivangro.shapes.gui.ShapeViewerGUI;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;

/**
 *
 * @author Ivan Guerrero
 */
public class DisplayShapesAction extends AbstractAction {
    private SVGRepository repository;
    
    public DisplayShapesAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        ShapeViewerGUI gui = new ShapeViewerGUI(null, true, repository.getShapeStore());
        gui.setVisible(true);
    }

}
