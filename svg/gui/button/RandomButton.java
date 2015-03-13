package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.RandomGenerationAction;

/**
 *
 * @author Ivan Guerrero
 */
public class RandomButton extends JButton {
    private SVGRepository repo;
    private String icon = "/images/randomIcon.png";
    public RandomButton() {
        super.setIcon(new ImageIcon(getClass().getResource(icon)));
    }
    
    public RandomButton(SVGRepository repo, IRefreshData dataContainer, boolean calledByUser) {
        super(new RandomGenerationAction(repo, dataContainer, calledByUser));
        this.repo = repo;
        super.setIcon(new ImageIcon(getClass().getResource(icon)));
    }

    /**
     * @param repo the repo to set
     */
    public void setRepo(SVGRepository repo) {
        this.repo = repo;
    }
}
