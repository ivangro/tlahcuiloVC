package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.*;

/**
 *
 * @author Ivan Guerrero
 */
public class SelectUnitButton extends JButton {
    private SVGRepository repo;
    private String icon = "/images/selectUnitIcon.png";
    
    public SelectUnitButton() {
        super.setIcon(new ImageIcon(getClass().getResource(icon)));
    }
    
    public SelectUnitButton(SVGRepository repo, IRefreshData dataContainer, boolean calledByUser) {
        super(new SelectUnitAction(repo, dataContainer, calledByUser));
        this.repo = repo;
        super.setIcon(new ImageIcon(getClass().getResource(icon)));
    }
}
