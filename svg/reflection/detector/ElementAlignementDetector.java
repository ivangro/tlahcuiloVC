package svg.reflection.detector;

import svg.actions.design.DActionAlign;
import svg.core.SVGRepository;

/**
 * Class to determine if an alignement of elements with one of the canvas' borders is requested.
 * @author Ivan Guerrero
 */
public class ElementAlignementDetector implements IReflectionDetector {

    /**
     * Verifies if there are several elements near to one of the canvas' borders, and if they are not aligned, the action is performed.
     * @param repository The data repository of the current canvas.
     * @return False if no additional executions of the method are requested during the current reflection phase.
     */
    @Override
    public boolean execute(SVGRepository repository) {
        boolean res = false;
        DActionAlign action = new DActionAlign();
        
        for (DActionAlign.Align align : DActionAlign.Align.values()) {
            action.setAlignement(align);
            action.applyAction(repository);
        }
        
        return res;
    }

}
