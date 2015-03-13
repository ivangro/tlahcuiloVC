package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.ManageBackgroundAction;

/**
 * Class to display the button to display the background config panel
 * @author Ivan Guerrero
 */
public class ManageBackgroundButton extends JToggleButton {
    public ManageBackgroundButton() {
        setIcon(new ImageIcon(getClass().getResource("/images/backgroundIcon.png")));
    }
    
    public ManageBackgroundButton(SVGRepository repo, IRefreshData dataContainer) {
        super(new ManageBackgroundAction(repo, dataContainer));
        setIcon(new ImageIcon(getClass().getResource("/images/backgroundIcon.png")));
    }
}
