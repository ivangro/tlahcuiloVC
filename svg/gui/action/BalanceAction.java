package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.actions.design.DActionBalance;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 * Class to apply the balance action to the selected elements on the canvas
 * @author Ivan Guerrero
 */
public class BalanceAction extends AbstractAction {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    private boolean horizontalBalance;
    
    /**
     * Constructs a new balance action
     * @param repo The repository to obtain all the data concerning to the visual composition.
     * @param dataContainer The GUI employed.
     * @param horizontalBalance The type of balance (If TRUE, the balance is horizontal, otherwise is vertical).
     */
    public BalanceAction(SVGRepository repo, IRefreshData dataContainer, boolean horizontalBalance) {
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.horizontalBalance = horizontalBalance;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        DActionBalance action = new DActionBalance();
        action.setHorizontalBalance(horizontalBalance);
        action.applyAction(repo);
        dataContainer.refreshData();
    }

}
