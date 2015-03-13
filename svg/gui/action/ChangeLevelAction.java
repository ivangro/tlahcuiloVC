package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.button.LevelComboBox;

/**
 *
 * @author Ivan Guerrero
 */
public class ChangeLevelAction extends AbstractAction {
    private SVGRepository repo;
    
    public ChangeLevelAction(SVGRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            LevelComboBox cbLevel = ((LevelComboBox)ae.getSource());
            if (cbLevel.getSelectedIndex() >= 0) {
                int level = Integer.parseInt(cbLevel.getSelectedItem().toString());
                repo.setCurrentLevel(level);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
