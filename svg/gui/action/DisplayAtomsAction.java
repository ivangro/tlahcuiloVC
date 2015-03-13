package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.gui.AtomViewer;

/**
 * Opens a window displaying the available atoms in the system
 * @author Ivan Guerrero
 */
public class DisplayAtomsAction extends AbstractAction{

    @Override
    public void actionPerformed(ActionEvent ae) {
        AtomViewer viewer = new AtomViewer(null, true);
        viewer.setVisible(true);
    }

}
