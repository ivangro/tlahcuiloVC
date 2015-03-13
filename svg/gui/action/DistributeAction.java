package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionDistribute;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
public class DistributeAction extends AbstractAction{
    private SVGRepository repo;
    private IRefreshData dataContainer;
    
    public DistributeAction(SVGRepository repo, IRefreshData dataContainer) {
        this.repo = repo;
        this.dataContainer = dataContainer;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        DActionDistribute action = new DActionDistribute();
        if (dataContainer.getSelectedElement() != null)
            action.applyAction(dataContainer.getSelectedElement(), repo);
        else
            action.applyAction(repo);
        
        dataContainer.refreshData();
    }
}