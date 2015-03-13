package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.ActionMoveElement;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
@Deprecated
public class MoveElementAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData data;
    
    public MoveElementAction(SVGRepository repository, IRefreshData data) {
        this.repository = repository;
        this.data = data;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        SVGElement elem = data.getSelectedElement();
        if (elem != null) {
            elem.setUserAdded(false);
            ActionMoveElement action = new ActionMoveElement();
        }
    }

}
