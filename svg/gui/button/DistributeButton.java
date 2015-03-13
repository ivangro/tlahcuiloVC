package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.DistributeAction;

/**
 *
 * @author Ivan Guerrero
 */
public class DistributeButton extends JButton {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    
    public DistributeButton() {
        this(null, null);
    }
    
    public DistributeButton(SVGRepository repo, IRefreshData dataContainer) {
        super(new DistributeAction(repo, dataContainer));
        this.repo = repo;
        this.dataContainer = dataContainer;
        super.setIcon(new ImageIcon(getClass().getResource("/images/distributeIcon.png")));
    }
}
