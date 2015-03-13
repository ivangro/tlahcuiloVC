package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionAlign;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
public class AlignAction extends AbstractAction {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    private DActionAlign.Align alignment;
    
    public AlignAction(SVGRepository repo, IRefreshData dataContainer, DActionAlign.Align alignment) {
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.alignment = alignment;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        DActionAlign action = new DActionAlign();
        action.setAlignement(getAlignment());
        if (dataContainer.getSelectedElement() != null)
            action.applyAction(dataContainer.getSelectedElement(), repo);
        else
            action.applyAction(repo);
        
        dataContainer.refreshData();
    }

    /**
     * @return the alignment
     */
    public DActionAlign.Align getAlignment() {
        return alignment;
    }

    /**
     * @param alignment the alignment to set
     */
    public void setAlignment(DActionAlign.Align alignment) {
        this.alignment = alignment;
    }

}
