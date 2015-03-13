package svg.actions;

import svg.core.SVGAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGUnit;

/**
 * Moves an element.
 * The text stored for this action has the following format:<br>
 * MOVE elementID x y endOfDesign?
 * @author Ivan Guerrero
 */
public class ActionMoveElement implements SVGAction {
    private String actionText, actionStory;
    private int newX, newY;
    private boolean endOfDesignAction;
    private SVGRepository repository;
    
    public ActionMoveElement() {
    }
    
    @Override
    public void applyAction(SVGElement element) {
        //If the element was added by the user it cannot be moved
        if (!element.isUserAdded()) {
            int lastLevel = repository.getCurrentLevel();
            if (lastLevel != 0) {
                repository.setCurrentLevel(0);
                int offsetX, offsetY;
                offsetX = newX - element.getCenterX();
                offsetY = newY - element.getCenterY();
                for (SVGElement elem : element.getSimpleElements()) {
                    ActionMoveElement newAction = (ActionMoveElement)clone();
                    newAction.setNewX(elem.getCenterX() + offsetX);
                    newAction.setNewY(elem.getCenterY() + offsetY);
                    repository.applyAction(newAction, elem);
                }
                if (element instanceof SVGUnit)
                    ((SVGUnit)element).determineCenter();
                repository.setCurrentLevel(lastLevel);
            }
            else {
                element.setCenter(newX, newY);
                actionText = "Element " + element.getID() + " moved to (" + element.getCenterX() + ", " + element.getCenterY() + ")";
                actionStory = "MOVE " + element.getID() + " " + element.getCenterX() + " " + element.getCenterY();
            }
        }
    }

    @Override
    public String getActionText() {
        return (endOfDesignAction) ? "*" + actionText : actionText;
    }

    /**
     * @return the newX
     */
    public int getNewX() {
        return newX;
    }

    /**
     * @param newX the newX to set
     */
    public void setNewX(int newX) {
        this.newX = newX;
    }

    /**
     * @return the newY
     */
    public int getNewY() {
        return newY;
    }

    /**
     * @param newY the newY to set
     */
    public void setNewY(int newY) {
        this.newY = newY;
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
        ActionMoveElement action = new ActionMoveElement();
        action.newX = newX;
        action.newY = newY;
        action.endOfDesignAction = endOfDesignAction;
        action.repository = repository;
        return action;
    }
}
