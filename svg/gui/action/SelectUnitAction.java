package svg.gui.action;

import ivangro.shapes.Shape;
import ivangro.shapes.gui.ShapeViewerGUI;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionCreateUnit;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 * Action called to select a previously created group.
 * @author Ivan Guerrero
 */
public class SelectUnitAction extends AbstractAction {

    private SVGRepository repo;
    private IRefreshData dataContainer;
    private boolean calledByUser;
    
    public SelectUnitAction(SVGRepository repo, IRefreshData dataContainer) {
        this(repo, dataContainer, false);
    }
    
    public SelectUnitAction(SVGRepository repo, IRefreshData dataContainer, boolean calledByUser) {
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.calledByUser = calledByUser;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        ShapeViewerGUI gui = new ShapeViewerGUI(null, true, repo.getShapeStore());
        gui.setVisible(true);
        Shape shape = gui.getSelectedShape();
        if (shape != null) {
            DActionCreateUnit action = new DActionCreateUnit();
            action.setCalledByUser(calledByUser);
            action.applyAction(shape, repo);
            dataContainer.refreshData();
        }
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