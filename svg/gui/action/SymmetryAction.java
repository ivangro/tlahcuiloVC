package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionHSymmetry;
import svg.actions.design.DActionVSymmetry;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 * Class to convert a drawing in H-Symmetric
 * @author Ivan Guerrero
 */
public class SymmetryAction extends AbstractAction{
    private SVGRepository repo;
    private IRefreshData dataContainer;
    private boolean horizontalCopy;
    
    public SymmetryAction(SVGRepository repo, IRefreshData dataContainer, boolean horizontalCopy) {
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.horizontalCopy = horizontalCopy;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (horizontalCopy) {
            DActionHSymmetry action = new DActionHSymmetry();
            if (dataContainer.getSelectedElement() != null)
                action.applyAction(dataContainer.getSelectedElement(), repo);
            else
                action.applyAction(repo);
        }
        else {
            DActionVSymmetry action = new DActionVSymmetry();
            if (dataContainer.getSelectedElement() != null)
                action.applyAction(dataContainer.getSelectedElement(), repo);
            else
                action.applyAction(repo);
        }
        dataContainer.refreshData();
    }

}
