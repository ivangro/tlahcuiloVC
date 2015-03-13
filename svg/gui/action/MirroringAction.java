package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionHMirroring;
import svg.actions.design.DActionRadialMirroring;
import svg.actions.design.DActionVMirroring;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
public class MirroringAction extends AbstractAction {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    private MirroringType type;
    public enum MirroringType {Horizontal, Vertical, Radial};
    
    public MirroringAction(SVGRepository repo, IRefreshData dataContainer, MirroringType type) {
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.type = type;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        switch(type) {
            case Horizontal:
                DActionHMirroring hAction = new DActionHMirroring();
                if (dataContainer.getSelectedElement() != null)
                    hAction.applyAction(dataContainer.getSelectedElement(), repo);
                else
                    hAction.applyAction(repo);
                break;
            case Vertical:
                DActionVMirroring vAction = new DActionVMirroring();
                if (dataContainer.getSelectedElement() != null)
                    vAction.applyAction(dataContainer.getSelectedElement(), repo);
                else
                    vAction.applyAction(repo);
                break;
            case Radial:
                DActionRadialMirroring rAction = new DActionRadialMirroring();
                if (dataContainer.getSelectedElement() != null)
                    rAction.applyAction(dataContainer.getSelectedElement(), repo);
                else
                    rAction.applyAction(repo);
                break;
        }
        dataContainer.refreshData();
    }
}
