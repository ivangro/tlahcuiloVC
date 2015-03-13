package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.reflection.detector.FocalPointDetector;

/**
 * Action to be called for the insertion of a focal point in a drawing
 * @author Ivan Guerrero
 */
public class FocalPointAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData data;
    
    public FocalPointAction(SVGRepository repository, IRefreshData data) {
        this.repository = repository;
        this.data = data;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        FocalPointDetector focalPoint = new FocalPointDetector();
        focalPoint.execute(repository);
        data.refreshData();
    }

}
