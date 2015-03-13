package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.actions.design.DActionAlign;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.AlignAction;

/**
 *
 * @author Ivan Guerrero
 */
public class AlignButton extends JButton {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    
    public AlignButton() {
        super.setIcon(new ImageIcon(getClass().getResource("/images/bottomAlignIcon.png")));
    }
    
    public AlignButton(SVGRepository repo, IRefreshData dataContainer, DActionAlign.Align alignment) {
        super(new AlignAction(repo, dataContainer, alignment));
        this.repo = repo;
        this.dataContainer = dataContainer;
        switch(alignment) {
            case TOP: super.setIcon(new ImageIcon(getClass().getResource("/images/topAlignIcon.png"))); break;
            case BOTTOM: super.setIcon(new ImageIcon(getClass().getResource("/images/bottomAlignIcon.png"))); break;
            case LEFT: super.setIcon(new ImageIcon(getClass().getResource("/images/leftAlignIcon.png"))); break;
            case RIGHT: super.setIcon(new ImageIcon(getClass().getResource("/images/rightAlignIcon.png"))); break;
        }
        
    }
}
