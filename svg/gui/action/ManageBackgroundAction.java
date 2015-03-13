package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JToggleButton;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.skill.BackgroundImageDrawingGenerator;
import svg.skill.SimpleDrawingGenerator;

/**
 *
 * @author Ivan Guerrero
 */
public class ManageBackgroundAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData dataContainer;
    
    public ManageBackgroundAction(SVGRepository repository, IRefreshData dataContainer) {
        this.repository = repository;
        this.dataContainer = dataContainer;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JToggleButton bttn = (JToggleButton)ae.getSource();
        if (bttn.isSelected())
            repository.setDrawingGenerator(new BackgroundImageDrawingGenerator());
        else
            repository.setDrawingGenerator(new SimpleDrawingGenerator());
        dataContainer.refreshData();
    }

}
