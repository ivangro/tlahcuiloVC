package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.AtomComparerGUI;

/**
 * Action to open the atoms' catalog GUI
 * @author Ivan Guerrero
 */
public class ViewAtomsCatalogAction extends AbstractAction {
    private SVGRepository repository;
    
    public ViewAtomsCatalogAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        AtomComparerGUI gui = new AtomComparerGUI(null, false, repository);
        gui.setVisible(true);
    }

}
