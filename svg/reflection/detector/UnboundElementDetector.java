package svg.reflection.detector;

import edu.uci.ics.jung.graph.Graph;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.actions.ActionDeleteElement;
import svg.context.SVGEdge;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.AbstractUnit;

/**
 * Detects elements with no relation with any element in the drawing
 * @author Ivan Guerrero
 */
public class UnboundElementDetector implements IReflectionDetector {
    private Map<Integer, List<SVGElement>> unboundElements;
    private SVGRepository repository;
    private int totalUnboundElements;
    private boolean enableRemoval;
    
    public UnboundElementDetector() {
        enableRemoval = true;
    }
    
    @Override
    public boolean execute(SVGRepository repository) {
        this.repository = repository;
        boolean res = false;
        unboundElements = new HashMap<>();
        totalUnboundElements = 0;
        
        //detects unbound elements in the higher levels of the drawing
        //then looks for unbound elements inside them, if exists one, this is detected as an unbound element
        List<Graph<SVGElement, SVGEdge>> contexts = repository.getContext().getContexts();
        for (int i=1; i<contexts.size(); i++) {
            Graph<SVGElement, SVGEdge> context = contexts.get(i);
            for (SVGElement elem : context.getVertices()) {
                int bindings = countBindings(elem, contexts, i);
                if (bindings == 0) {
                    addUnboundElement(elem, i);
                }
            }
        }
        if (unboundElements.size() > 0) {
            res = true;
            if (isEnableRemoval())
                removeUnboundElements();
            totalUnboundElements = unboundElements.size();
        }
        
        repository.runDetectors();
        
        return res;
    }

    /**
     * Counts the number of bindings of the given element
     * @param element The element
     * @param contexts The set of available contexts at different levels
     * @param level The level of the element
     * @return The sum of the bindings of the elements in its level and its constituent elements on lower levels
     */
    private int countBindings(SVGElement element, List<Graph<SVGElement, SVGEdge>> contexts, int level) {
        int bindings = 0;
        if (element != null && contexts != null) {
            if (level == 1) {
                if (contexts.get(level) != null) {
                    try {
                        bindings = contexts.get(level).inDegree(element);
                    } catch (NullPointerException npe) {
                        //System.err.println("Null at " + level + " " + element);
                    }
                }
            }
            else {
                AbstractUnit unit = (AbstractUnit)element;
                for (SVGElement elem : unit.getElements()) {
                    bindings += countBindings(elem, contexts, level-1);
                }
            }
        }
        
        return bindings;
    }

    private void removeUnboundElements() {
        int lastLevel = repository.getCurrentLevel();
        for (Integer level : unboundElements.keySet()) {
            for (SVGElement elem : unboundElements.get(level)) {
                repository.setCurrentLevel(level);
                ActionDeleteElement delete = new ActionDeleteElement();
                delete.setRepository(repository);
                repository.applyAction(delete, elem, true);
                //delete.applyAction(elem);
                Logger.getGlobal().log(Level.INFO, "Unbound element {0} removed", elem);
            }
        }
        
        repository.setCurrentLevel(lastLevel);
    }

    private void addUnboundElement(SVGElement elem, int level) {
        if (unboundElements.containsKey(level))
            unboundElements.get(level).add(elem);
        else {
            List<SVGElement> list = new ArrayList<>();
            list.add(elem);
            unboundElements.put(level, list);
        }
    }

    /**
     * @return the totalUnboundElements
     */
    public int getTotalUnboundElements() {
        return totalUnboundElements;
    }

    /**
     * @return the enableRemoval
     */
    public boolean isEnableRemoval() {
        return enableRemoval;
    }

    /**
     * @param enableRemoval the enableRemoval to set
     */
    public void setEnableRemoval(boolean enableRemoval) {
        this.enableRemoval = enableRemoval;
    }
}