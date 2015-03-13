package svg.actions;

import svg.core.SVGAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGUnit;

/**
 * Modifies the attributes of an element.
 * The text stored for this action has the following format:<br>
 * UPDATE elementID size imageID
 * @author Ivan Guerrero
 */
public class ActionModifyElement implements SVGAction {
    private String actionText, actionStory;
    private int newSize, newImageID;
    private boolean endOfDesignAction;
    private SVGRepository repository;
    
    public ActionModifyElement() {
    }
    
    @Override
    public void applyAction(SVGElement element) {
        //If the element was added by the user it cannot be moved
        if (!element.isUserAdded()) {
            int lastLevel = repository.getCurrentLevel();
            if (lastLevel != 0) {
                repository.setCurrentLevel(0);
                for (SVGElement elem : element.getSimpleElements()) {
                    ActionModifyElement newAction = (ActionModifyElement)clone();
                    newAction.setNewImageID(newImageID);
                    newAction.setNewSize(newSize);
                    repository.applyAction(newAction, elem);
                }
                if (element instanceof SVGUnit)
                    ((SVGUnit)element).determineCenter();
                repository.setCurrentLevel(lastLevel);
            }
            else {
                element.setSize(getNewSize());
                element.setImageID(getNewImageID());
                actionText = "Element " + element.getID() + " updated to size:" + element.getSize() + ", imageID: " + element.getImageID();
                actionStory = "UPDATE " + element.getID() + " S-" + element.getSize() + " I-" + element.getImageID();
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
        ActionModifyElement action = new ActionModifyElement();
        action.newImageID = newImageID;
        action.newSize = newSize;
        action.endOfDesignAction = endOfDesignAction;
        action.repository = repository;
        return action;
    }

    /**
     * @return the newSize
     */
    public int getNewSize() {
        return newSize;
    }

    /**
     * @param newSize the newSize to set
     */
    public void setNewSize(int newSize) {
        this.newSize = newSize;
    }

    /**
     * @return the newImageID
     */
    public int getNewImageID() {
        return newImageID;
    }

    /**
     * @param newImageID the newImageID to set
     */
    public void setNewImageID(int newImageID) {
        this.newImageID = newImageID;
    }
}