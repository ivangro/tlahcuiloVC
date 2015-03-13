package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.apache.batik.swing.JSVGCanvas;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.DuplicateUnitAction;

/**
 * Button to perform the unit copy action
 * @author Ivan Guerrero
 */
public class DuplicateUnitButton extends JButton {
    public DuplicateUnitButton() {
        this(null, null, null);
    }
    
    public DuplicateUnitButton(SVGRepository repository, JSVGCanvas canvas, IRefreshData container) {
        super(new DuplicateUnitAction(repository, canvas, container));
        super.setIcon(new ImageIcon(getClass().getResource("/images/copyIcon.png")));
    }
}
