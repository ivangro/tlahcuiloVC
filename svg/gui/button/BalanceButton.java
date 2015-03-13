package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.BalanceAction;

/**
 * Button to apply balance to a drawing
 * @author Ivan Guerrero
 */
public class BalanceButton extends JButton {
    private SVGRepository repo;
    
    public BalanceButton() {
        this(null, null, true);
    }
    
    public BalanceButton(SVGRepository repo, IRefreshData dataContainer, boolean horizontalBalance) {
        super(new BalanceAction(repo, dataContainer, horizontalBalance));
        this.repo = repo;
        if (horizontalBalance)
            super.setIcon(new ImageIcon(getClass().getResource("/images/hBalanceIcon.png")));
        else
            super.setIcon(new ImageIcon(getClass().getResource("/images/vBalanceIcon.png")));
    }
    
    public void setRepository(SVGRepository repo) {
        this.repo = repo;
    }
}
