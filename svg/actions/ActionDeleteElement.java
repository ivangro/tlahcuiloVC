package svg.actions;

import svg.core.SVGAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Action to delete an element from the current drawing.<br>
 * The text stored for this action has the following format:<br>
 * DELETE elementID endOfDesign?<br>
 * This action should be employed with the applyAction method of the SVGRepository class.
 * @author Ivan Guerrero
 */
public class ActionDeleteElement implements SVGAction{
    private String actionText, actionStory;
    private boolean endOfDesignAction;
    private SVGRepository repository;
    
    @Override
    public void applyAction(SVGElement element) {
        //If the element was added by the user it cannot be removed
        if (!element.isUserAdded()) {
            int lastLevel = repository.getCurrentLevel();
            if (lastLevel == 0) {
                repository.getElements(0).remove(element);
                actionText = "Element " + element.getID() + " has been removed";
                actionStory = "DELETE " + element.getID();
                //repository.getActions().add(this);
            }
            else {
                repository.setCurrentLevel(0);
                for (SVGElement elem : element.getSimpleElements()) {
                    ActionDeleteElement newAction = (ActionDeleteElement)clone();
                    repository.applyAction(newAction, elem);
                }
                repository.setCurrentLevel(lastLevel);
            }
        }
    }

    @Override
    public String getActionText() {
        return (endOfDesignAction) ? "*" + actionText : actionText;
    }

    @Override
    public String getActionForStory() {
        return (endOfDesignAction) ? actionStory + " %" : actionStory;
    }

    @Override
    public void setEndOfDesignAction() {
        endOfDesignAction = !endOfDesignAction;
    }
    
    @Override
    public boolean isEndOfDesignAction() {
        return endOfDesignAction;
    }
    
    @Override
    public void setRepository(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Object clone() {
        ActionDeleteElement action = new ActionDeleteElement();
        action.endOfDesignAction = endOfDesignAction;
        action.repository = repository;
        return action;
    }
}
