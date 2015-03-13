package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
public class DeleteDrawingAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData data;
    
    public DeleteDrawingAction(SVGRepository repository, IRefreshData data) {
        this.repository = repository;
        this.data = data;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the current drawing?", 
                                                   "Delete drawing", 
                                                   JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            data.deleteDrawing();
        }
    }

}
