package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.gui.SVGConfigGUI;

/**
 *
 * @author Ivan Guerrero
 */
public class ConfigurationAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent ae) {
        SVGConfigGUI config = new SVGConfigGUI(null, true);
        config.setVisible(true);
    }

}
