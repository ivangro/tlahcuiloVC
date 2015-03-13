package svg.engagement;

import java.io.Serializable;
import java.util.*;
import svg.context.ElementRelationType;

/**
 * Class to represent the next action that took place in a given context
 * @author Ivan Guerrero
 */
@Deprecated
public class NextActionSet implements INextAction, Serializable {
    private List<NextAction> actions;
    
    public NextActionSet() {
        actions = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return Arrays.toString(actions.toArray(new NextAction[0]));
    }
    
    public List<NextAction> getActions() {
        return actions;
    }

    /**
     * Adds an action to the list of next actions
     * If a RS, HMS or VMS action is added, the balance actions -VB, HB- are removed
     * @param action 
     */
    public void addAction(NextAction action) {
        ElementRelationType[] equivalentTypes;
        switch(action.getRelationType()) {
            case HB:
                equivalentTypes = new ElementRelationType[]{ElementRelationType.HMS, ElementRelationType.RS};
                if (!findEquivalentAction(equivalentTypes, action))
                    actions.add(action);
                break;
            case VB:
                equivalentTypes = new ElementRelationType[]{ElementRelationType.VMS, ElementRelationType.RS};
                if (!findEquivalentAction(equivalentTypes, action))
                    actions.add(action);
                break;
            case RS:
                equivalentTypes = new ElementRelationType[]{ElementRelationType.HB, ElementRelationType.VB};
                if (findEquivalentAction(equivalentTypes, action)) {
                    NextAction removeAction = (NextAction)action.clone();
                    removeAction.setRelationType(ElementRelationType.HB);
                    actions.remove(removeAction);
                    removeAction.setRelationType(ElementRelationType.VB);
                    actions.remove(removeAction);
                }
                    actions.add(action);
                break;
            case HMS:
                equivalentTypes = new ElementRelationType[]{ElementRelationType.HB};
                if (findEquivalentAction(equivalentTypes, action)) {
                    NextAction removeAction = (NextAction)action.clone();
                    removeAction.setRelationType(ElementRelationType.HB);
                    actions.remove(removeAction);
                }
                    actions.add(action);
                break;
            case VMS:
                equivalentTypes = new ElementRelationType[]{ElementRelationType.VB};
                if (findEquivalentAction(equivalentTypes, action)) {
                    NextAction removeAction = (NextAction)action.clone();
                    removeAction.setRelationType(ElementRelationType.VB);
                    actions.remove(removeAction);
                }
                    actions.add(action);
                break;
            default:
                actions.add(action);
        }
    }

    private boolean findEquivalentAction(ElementRelationType[] equivalentTypes, NextAction action) {
        boolean ban = false;
        
        for (ElementRelationType type : equivalentTypes) {
            NextAction symAction = (NextAction)action.clone();
            symAction.setRelationType(type);
            if (actions.contains(symAction)) {
                ban = true;
                break;
            }
        }
        
        return ban;
    }
}