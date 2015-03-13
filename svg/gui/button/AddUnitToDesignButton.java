package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.AddUnitToDesignAction;

/**
 *
 * @author Ivan Guerrero
 */
public class AddUnitToDesignButton extends JButton {
    public AddUnitToDesignButton() {
        this(null, null);
    }
    
    public AddUnitToDesignButton(SVGRepository repository, IRefreshData container) {
        super(new AddUnitToDesignAction(repository, container));
        super.setIcon(new ImageIcon(getClass().getResource("/images/addUnitToDesignIcon.png")));
    }
}
