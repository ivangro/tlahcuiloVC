package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;

/**
 * Marks / Unmarks the last action performed as end of design's action
 * @author Ivan Guerrero
 */
public class EndOfDesignStepAction extends AbstractAction{
    private SVGRepository repository;
    
    public EndOfDesignStepAction(SVGRepository repository) {
        this.repository = repository;
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        repository.getActions().get(repository.getActions().size()-1).setEndOfDesignAction();
    }

}
