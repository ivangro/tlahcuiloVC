package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.SymmetryAction;

/**
 *
 * @author Ivan Guerrero
 */
public class SymmetryButton extends JButton {
    private SVGRepository repo;
    
    public SymmetryButton() {
        super.setIcon(new ImageIcon(getClass().getResource("/images/hCopyIcon.png")));
    }
    
    public SymmetryButton(SVGRepository repo, IRefreshData dataContainer, boolean horizontalCopy) {
        super(new SymmetryAction(repo, dataContainer, horizontalCopy));
        this.repo = repo;
        if (horizontalCopy)
            super.setIcon(new ImageIcon(getClass().getResource("/images/hCopyIcon.png")));
        else
            super.setIcon(new ImageIcon(getClass().getResource("/images/vCopyIcon.png")));
    }
    
    public void setRepository(SVGRepository repo) {
        this.repo = repo;
    }
}
