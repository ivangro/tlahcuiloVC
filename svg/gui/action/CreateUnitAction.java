package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionCreateUnit;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 * Action to create a new unit into a drawing
 * @author Ivan Guerrero
 */
public class CreateUnitAction extends AbstractAction {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    private boolean calledByUser;
    
    public CreateUnitAction(SVGRepository repo, IRefreshData dataContainer) {
        this(repo, dataContainer, false);
    }
    
    public CreateUnitAction(SVGRepository repo, IRefreshData dataContainer, boolean calledByUser) {
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.calledByUser = calledByUser;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        DActionCreateUnit action = new DActionCreateUnit();
        action.setCalledByUser(calledByUser);
        action.applyAction(repo);
        if (dataContainer != null)
            dataContainer.refreshData();
        else
            repo.runDetectors();
    }

    /**
     * @return the calledByUser
     */
    public boolean isCalledByUser() {
        return calledByUser;
    }

    /**
     * @param calledByUser the calledByUser to set
     */
    public void setCalledByUser(boolean calledByUser) {
        this.calledByUser = calledByUser;
    }

}
