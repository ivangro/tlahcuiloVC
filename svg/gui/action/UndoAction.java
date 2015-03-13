package svg.gui.action;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import svg.core.SVGAction;
import svg.core.SVGDAction;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 * Class to undo the last action performed by the user.
 * @author Ivan Guerrero
 */
public class UndoAction extends AbstractAction {
    private IRefreshData data;
    private SVGRepository repo;
    
    public UndoAction(SVGRepository repository, IRefreshData data) {
        this.repo = repository;
        this.data = data;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        List<SVGAction> actions = repo.getActions();
        SVGAction action = actions.get(actions.size()-1);
        if (action.isEndOfDesignAction()) {
            List<SVGDAction> designActions = repo.getDesignActions();
            designActions.remove(designActions.size()-1);
        }
        actions.remove(action);
        data.refreshData();
    }
}
