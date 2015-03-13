package svg.engagement;

import java.util.*;
import svg.actions.design.DActionCreateUnit;
import svg.context.CanvasContext;
import svg.core.*;
import svg.story.StoryLoader;

/**
 * Class to generate atoms based on the design actions applied to the context of a drawing<br>
 * Procedure:<br>
 * 1. Obtain the contexts for each design action<br>
 * 2. Obtain the following action associated to design action applied<br>
 * 3. Create an atom with the initial context and the design actions<br>
 * 4. Add the actions atom to the repository
 * @author Ivan Guerrero
 */
public class DesignAtomGenerator implements IAtomGenerator {

    @Override
    public void detectAtoms(SVGRepository repository, boolean updateParams) {
        SVGRepository newRepository = SVGRepository.getNewInstance();
        newRepository.generateSVGDocument();
        StoryLoader loader = new StoryLoader();
        List<CanvasContext> contexts = new ArrayList<>();
                
        //Obtains all the parcial contexts
        for (SVGAction action : repository.getActions()) {
            String actionStr = action.getActionForStory();
            loader.parseAction(actionStr, newRepository);
            if (action.isEndOfDesignAction()) {
                newRepository.runDetectors();
                CanvasContext context = (CanvasContext)newRepository.getContext().clone();
                contexts.add(context);
            }
        }
        
        List<SVGDAction> designActions = repository.getDesignActions();
        NextDesignAction action;
        int contextCount = SVGAtomStore.getInstance().getContextCount();
        for (int i=0; i<contexts.size()-1; i++) {
            CanvasContext context = contexts.get(i);
            SVGDAction designAction = designActions.get(i+1);
            contextCount++;
            for (int j=1; j<context.getLevels(); j++) {
                Map<SVGElement, SVGElement> mapping = new HashMap<>();
                SVGAtom atom = new SVGAtom(GraphUtils.simplifyElements(context.getContextGraph(j), mapping), j);
                //If the action level is the same of the atom 
                //or the design action was applied to the whole drawing and the atom is for the last level
                if ((atom.getLevel() == designAction.getActionLevel()) ||
                    (designAction.getActionLevel() == 0 && (j+1) == context.getLevels()))
                    atom.setActionLevel(true);
                else
                    atom.setActionLevel(false);
                action = new NextDesignAction();
                SVGElement element = getKeyElement(mapping, designAction.getElement());
                
                if (element != null && !(designAction instanceof DActionCreateUnit))
                    action.setAction(designAction, element);
                else
                    action.setAction(designAction);
                
                atom.addNextAction(action);
                atom.setContextID("Ctx-" + contextCount);
                SVGAtomStore.getInstance().addAtom(atom);
            }
        }
        SVGAtomStore.getInstance().saveAtoms(SVGConfig.AtomsFile);
    }

    private SVGElement getKeyElement(Map<SVGElement, SVGElement> mapping, SVGElement element) {
        if (element != null) {
            for (SVGElement elem : mapping.keySet()) {
                if (elem.getID().equals(element.getID()))
                    return mapping.get(elem);
            }
        }
        return null;
    }
}