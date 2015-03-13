package svg.engagement;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.actions.design.*;
import svg.context.SimpleElement;
import svg.core.SVGDAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.reflection.Guideline;

/**
 * Class to perform the actions found during the engagement process.<br>
 * This class employs the NextActionSet class
 * @author Ivan Guerrero
 */
public class EngagementActionPerformer implements IEngagementActionPerformer {
    private SVGRepository repository;
    private Map<SVGElement, SVGElement> verticesMap;
    
    public EngagementActionPerformer(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void applyActions(INextAction actions, Map<SVGElement, SVGElement> verticesMap) {
        Logger.getGlobal().log(Level.INFO, "Applying actions: {0}", actions.toString());
        this.verticesMap = (verticesMap == null) ? new HashMap<SVGElement, SVGElement>() : verticesMap;        
        
        if (actions instanceof NextDesignAction)
            applyActions((NextDesignAction)actions, verticesMap);
        else if (actions instanceof NextActionSet) {
            //TODO: Validate if this is still being employed 20-03-14
            System.out.println("USING NEXT ACTION SET: " + actions);
            applyActions((NextActionSet)actions, verticesMap);
        }
    }
    
    /**
     * Applies the given action to the current drawing
     * @param action The action to be applied
     * @param verticesMap The mapping of the elements.<br>
     * This is employed to determine which element in the canvas corresponds to which element in the atom from which the action came from.
     */
    private void applyActions(NextDesignAction action, Map<SVGElement, SVGElement> verticesMap) {
        SVGDAction designAction = null;
        switch (action.getActionName()) {
            case "HBALANCE":
                designAction = new DActionBalance();
                ((DActionBalance)designAction).setHorizontalBalance(true);
                break;
            case "VBALANCE":
                designAction = new DActionBalance();
                break;
            case "UNIT":
                designAction = new DActionCreateUnit();
                break;
            case "DELETE":
                designAction = new DActionDelete();
                break;
            case "HMIRROR":
                designAction = new DActionHMirroring();
                break;
            case "VMIRROR":
                designAction = new DActionVMirroring();
                break;
            case "HSYMMETRY":
                designAction = new DActionHSymmetry();
                break;
            case "VSYMMETRY":
                designAction = new DActionVSymmetry();
                break;
            case "RSYMMETRY":
                designAction = new DActionRadialMirroring();
                break;
//            case "ADD_FOCAL_POINT":
//                designAction = new DActionFocalPoint();
//                break;
            case "COPY":
                designAction = new DActionCopyUnit();
                break;
            case "DISTRIBUTE":
                designAction = new DActionDistribute();
                break;
            case "BALIGN":
            case "TALIGN":
            case "LALIGN":
            case "RALIGN":
                designAction = new DActionAlign();
                ((DActionAlign)designAction).setAlignement(action.getActionName().charAt(0));
                break;
            default:
                Logger.getGlobal().log(Level.INFO, "Action {0} not applied", action);
        }
        SVGElement element = new SimpleElement(action.getElement());
        designAction.setProperties(action.getProperties());
        if (verticesMap.containsKey(element)) {
            designAction.applyAction(verticesMap.get(element), repository);
        }
        else {
            designAction.applyAction(repository);
        }
        //Mark the last action as the end of a design action
        repository.getActions().get(repository.getActions().size()-1).setEndOfDesignAction();
    }
    
    @Deprecated
    private void applyActions(NextActionSet actions, Map<SVGElement, SVGElement> verticesMap) {
        for (NextAction action : actions.getActions()) {
            boolean res = false;
            
            if (action.isInsertion()) {
                res = performAddAction(action);
            }
            else if (action.isRemoval()){
                res = performRemoveAction(action);
            }
            
            if (!res)
                Logger.getGlobal().log(Level.INFO, "Action {0} not applied", action);
        }
        //Mark the last action as the end of a design action
        repository.getActions().get(repository.getActions().size()-1).setEndOfDesignAction();
    }

    /**
     * Obtains the last unit created at the level 1 with center at the given coordinates
     * @return 
     */
    @Deprecated
    private SVGElement selectUnit(SVGElement elem) {
        int lastLevel = repository.getCurrentLevel();
        repository.setCurrentLevel(1);
        SVGElement element = repository.getElementAt(elem.getCenterX(), elem.getCenterY());
        repository.setCurrentLevel(lastLevel);
        return element;
    }

    @Deprecated
    private SVGElement simplifyElement(SVGElement newElement, SVGElement elem) {
        SimpleElement simple = new SimpleElement();
        simple.setID(elem.getID());
        simple.setArea(newElement.getArea());
        simple.setCenter(newElement.getCenterX(), newElement.getCenterY());
        simple.setNoElements(newElement.getSimpleElements().size());
        simple.setOriginalID(newElement.getID());
        return simple;
    }

    @Deprecated
    private SVGElement getElement(SVGElement... ids) {
        SVGElement elem = null;
        for (SVGElement id : ids) {
            if (verticesMap.containsKey(id)) {
                elem = verticesMap.get(id);
                break;
            }
        }
        return elem;
    }

    @Deprecated
    private boolean applyActionToElement(SVGDAction action, SVGElement elem) {
        if (elem != null) {
            int lastLevel = repository.getCurrentLevel();
            repository.setCurrentLevel(1);
            action.applyAction(elem, repository);
            repository.setCurrentLevel(lastLevel);
            return true;
        }
        return false;
    }

    @Deprecated
    private boolean performAddAction(NextAction nextAction) {
        SVGDAction action;
        SVGElement elem;
        boolean res = false;
        SVGElement newElement;
        switch (nextAction.getRelationType()) {
            case HTS:
                action = new DActionHSymmetry();
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "HTS to {0}", elem);
                break;
            case VTS:
                action = new DActionVSymmetry();
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "VTS to {0}", elem);
                break;
            case RS:
                action = new DActionRadialMirroring();
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "RS to {0}", elem);
                break;
            case HMS:
                action = new DActionHMirroring();
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "HMS to {0}", elem);
                break;
            case VMS:
                action = new DActionVMirroring();
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "VMS to {0}", elem);
                break;
            case HB:
                action = new DActionBalance();
                ((DActionBalance)action).setHorizontalBalance(true);
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "HB to {0}", elem);
                break;
            case VB:
                action = new DActionBalance();
                elem = getElement(nextAction.getSource(), nextAction.getTarget());
                res = applyActionToElement(action, elem);
                if (res)
                    Logger.getGlobal().log(Level.INFO, "VB to {0}", elem);
                break;
            case NEW_ELEM:
                if (!repository.getGuidelines().hasGuideline(Guideline.stopGeneration)) {
                    action = new DActionCreateUnit();
                    SimpleElement simple = (SimpleElement)nextAction.getSource();
                    applyActionToElement(action, simple);
                    newElement = selectUnit(((DActionCreateUnit)action).getElement());
                    if (newElement != null) {
                        SVGElement simpleElem = simplifyElement(newElement, nextAction.getSource());
                        verticesMap.put(simpleElem, newElement);
                        res = true;
                        Logger.getGlobal().log(Level.INFO, "Elem added {0} from {1}", 
                                               new Object[]{newElement, simple.toStringWithValues()});
                    }
                }
                else {
                    Logger.getGlobal().log(Level.INFO, "Generation stopped");
                }
                break;
            case VL: //TODO: Implement
            case HL: //TODO: Implement
            default:
                Logger.getGlobal().log(Level.INFO,"{0} not supported yet", nextAction);
        }
        return res;
    }

    @Deprecated
    private boolean performRemoveAction(NextAction nextAction) {
        SVGElement elem;
        boolean res = false;
        switch (nextAction.getRelationType()) {
            case NEW_ELEM:
                if (!repository.getGuidelines().hasGuideline(Guideline.stopRemoval)) {
                    elem = getElement(nextAction.getSource());
                    if (applyActionToElement(new DActionDelete(), elem)) {
                        res = true;
                        Logger.getGlobal().log(Level.INFO, "Elem removed {0}", elem.getID());
                    }
                }
                else {
                    Logger.getGlobal().log(Level.INFO, "Removal stopped");
                }
                break;
            default:
                Logger.getGlobal().log(Level.INFO,"{0} not supported yet", nextAction);
        }
        return res;
    }
}