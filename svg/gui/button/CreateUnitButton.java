package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.CreateUnitAction;

/**
 *
 * @author Ivan Guerrero
 */
public class CreateUnitButton extends JButton {
    private SVGRepository repo;
    private String icon = "/images/createUnitIcon.png";
    
    public CreateUnitButton() {
        super.setIcon(new ImageIcon(getClass().getResource(icon)));
    }
    
    public CreateUnitButton(SVGRepository repo, IRefreshData dataContainer, boolean calledByUser) {
        super(new CreateUnitAction(repo, dataContainer, calledByUser));
        this.repo = repo;
        super.setIcon(new ImageIcon(getClass().getResource(icon)));
    }
}
