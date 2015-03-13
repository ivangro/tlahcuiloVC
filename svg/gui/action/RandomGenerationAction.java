package svg.gui.action;

import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.AbstractAction;
import svg.actions.ActionCreateElement;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.ElementFactory;
import svg.gui.IRefreshData;

/**
 * Action that generates random points in a canvas
 * @author Ivan Guerrero
 */
public class RandomGenerationAction extends AbstractAction {
    private SVGRepository repo;
    private IRefreshData dataContainer;
    private boolean calledByUser;
    private Random random;
    
    public RandomGenerationAction() {
    }
    
    public RandomGenerationAction(SVGRepository repo, IRefreshData dataContainer, boolean calledByUser) {
        this.calledByUser = calledByUser;
        this.repo = repo;
        this.dataContainer = dataContainer;
        this.random = new Random();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        repo.applyAction(new ActionCreateElement(), getElement());
        dataContainer.refreshData();
    }
    
    public SVGElement getElement() {
        return getElement(true);
    }
    
    public SVGElement getElement(boolean useAllCanvas) {
        int x, y;
        int maxX, maxY;
        int size, imageID;
        if (useAllCanvas) {
            maxX = SVGConfig.CANVAS_WIDTH;
            maxY = SVGConfig.CANVAS_HEIGHT;
        }
        else {
            maxX = SVGConfig.CANVAS_WIDTH / 2;
            maxY = SVGConfig.CANVAS_HEIGHT / 2;
        }
        size = 1 + random.nextInt(3);
        imageID = random.nextInt(ElementFactory.getInstance().getSVGElementSpecs().size());
        SVGConfig.CURRENT_IMAGE_ID = imageID;
        SVGElement elem = ElementFactory.getInstance().createNewElement(size);
        x = elem.getWidth() +(int)(Math.round(Math.random() * (maxX - elem.getWidth()*2)));
        y = elem.getHeight() +(int)(Math.round(Math.random() * (maxY - elem.getHeight()*2)));
        //Logger.getLogger(RandomGenerationAction.class.getName()).log(Level.INFO, "Random at ({0}, {1})", new Object[]{x, y});
        elem.setCenter(x, y);
        elem.setUserAdded(calledByUser);
        return elem;
    }

}
