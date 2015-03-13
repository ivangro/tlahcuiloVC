package svg.gui.button;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import svg.core.SVGRepository;
import svg.gui.action.ChangeLevelAction;

/**
 *
 * @author Ivan Guerrero
 */
public class LevelComboBox extends JComboBox<Integer> {
    private SVGRepository repository;
    
    public LevelComboBox() {}
    
    public LevelComboBox(SVGRepository repository) {
        super.addActionListener(new ChangeLevelAction(repository));
        this.repository = repository;
        setLevels();
    }
    
    public void setSVGRepository(SVGRepository repository) {
        this.repository = repository;
    }
    
    public final void setLevels() {
        int lastLevel = repository.getCurrentLevel();
        ((DefaultComboBoxModel)this.getModel()).removeAllElements();
        for (int i=0; i<repository.getLevels(); i++) {
            ((DefaultComboBoxModel)this.getModel()).addElement(i);
        }
        repository.setCurrentLevel(lastLevel);
        this.getModel().setSelectedItem(lastLevel);
    }
}
