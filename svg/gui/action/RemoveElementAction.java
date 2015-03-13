package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionDelete;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
public class RemoveElementAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData data;
    
    public RemoveElementAction(SVGRepository repository, IRefreshData data) {
        this.repository = repository;
        this.data = data;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        SVGElement elem = data.getSelectedElement();
        if (elem != null) {
            elem.setUserAdded(false);
            DActionDelete deleteAction = new DActionDelete();
            deleteAction.applyAction(elem, repository);
            repository.setCurrentLevel(0);
            data.refreshData();
            data.setCurrentStatus("Elem " + elem.getID() + " deleted");
            data.setSelectedElement(null);
        }
    }

}
