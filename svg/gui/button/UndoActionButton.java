package svg.gui.button;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.gui.action.UndoAction;

/**
 * Button to undo the last action performed
 * @author Ivan Guerrero
 */
public class UndoActionButton extends JButton {
    
    public UndoActionButton() {
        this(null, null);
    }
    
    public UndoActionButton(SVGRepository repository, IRefreshData data) {
        super(new UndoAction(repository, data));
        super.setIcon(new ImageIcon(getClass().getResource("/images/undoIcon.png")));
    }
}
