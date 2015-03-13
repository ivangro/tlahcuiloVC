package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.*;
import svg.core.SVGRepository;

/**
 *
 * @author Ivan Guerrero
 */
public class DisplayContextAction extends AbstractAction {
    private SVGRepository repository;
    
    public DisplayContextAction(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JDialog contextDialog = new JDialog();
        JTabbedPane pane = new JTabbedPane();
        contextDialog.add(pane);
        //contextDialog.setLayout(new GridLayout(1, repository.getLevels()-1, 10, 10));
        
        for (int i=1; i<repository.getLevels(); i++) {
            pane.addTab("Level " + i, repository.getContext().getVisualizationElement(i));
            //contextDialog.getContentPane().add(repository.getContext().getVisualizationElement(i));
        }
        contextDialog.pack();
        contextDialog.setTitle("Relations in context");
        contextDialog.setVisible(true);
    }
}
