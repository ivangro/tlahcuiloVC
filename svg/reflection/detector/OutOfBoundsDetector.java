package svg.reflection.detector;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.actions.*;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Detects when an element is out of the drawing bounds and moves it back inside the drawing
 * @author Ivan Guerrero
 */
public class OutOfBoundsDetector implements IReflectionDetector{

    @Override
    public boolean execute(SVGRepository repository) {
        boolean res = false;
        int lastLevel = repository.getCurrentLevel();
        List<SVGElement> forRemoval = new ArrayList<>();
        
        for (SVGElement elem : repository.getElements(0)) {
            boolean remove = false;
            if ((elem.getCenterX() > (SVGConfig.CANVAS_WIDTH - elem.getWidth()/2)) ||
                 elem.getCenterX() < elem.getWidth()/2)
                remove = true;
            if ((elem.getCenterY() > (SVGConfig.CANVAS_HEIGHT - elem.getHeight()/2)) ||
                 elem.getCenterY() < elem.getHeight()/2)
                remove = true;
            
            if (remove)
                forRemoval.add(elem);
        }
        
        for(SVGElement elem : forRemoval) {
            ActionDeleteElement del = new ActionDeleteElement();
            elem.setUserAdded(false);
            repository.applyAction(del, elem);
            Logger.getGlobal().log(Level.INFO, "OOB removed {0}", elem);
        }
        
        repository.setCurrentLevel(lastLevel);
        repository.runDetectors();
        return res;
    }
}