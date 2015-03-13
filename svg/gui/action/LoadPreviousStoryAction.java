package svg.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;

/**
 *
 * @author Ivan Guerrero
 */
public class LoadPreviousStoryAction extends AbstractAction{
    private IRefreshData container;
    private SVGRepository repository;
    
    public LoadPreviousStoryAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        LoadStoryAction storyLoader = new LoadStoryAction(repository, container);
        storyLoader.setKeepDesignActions(true);
        storyLoader.actionPerformed(ae);
        LoadDesignAction designLoader = new LoadDesignAction(repository, container);
        designLoader.setExecute(false);
        designLoader.actionPerformed(ae);
        //repository.clearData(true);
    }

}
