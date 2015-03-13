package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.AtomPickerGUI;
import svg.gui.IRefreshData;

/**
 * Action to display the GUI to compare the composition's contexts with the available atoms.
 * @author Ivan Guerrero
 */
public class CompareContextsAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData container;
    
    public CompareContextsAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        AtomPickerGUI gui = new AtomPickerGUI(null, false, repository, container);
        gui.setVisible(true);
    }

}
