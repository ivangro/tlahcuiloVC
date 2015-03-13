package svg.actions;

import svg.core.*;
import svg.elems.GraphicElement;

/**
 * Class to create a new element inside a drawing.
 * The text stored for this action has the following format:<br>
 * ADD x y imageSize I-imageId endOfDesign?
 * @author Ivan Guerrero
 */
public class ActionCreateElement implements SVGAction {
    /** Text displayed inside the results window */
    private String actionText;
    /** Text stored in a story file to regenerate a drawing */
    private String actionStory;
    private boolean endOfDesignAction;
    private SVGRepository repository;
    
    @Override
    public void applyAction(SVGElement element) {
        if (element != null) {
            int lastLevel = repository.getCurrentLevel();
            if (lastLevel == 0) {
                //Looks for an element in exactly the same position, if exists, don't duplicate it
                SVGElement e = repository.getElementAt(element.getCenterX(), element.getCenterY());
                if (e == null || (e.getCenterX() != element.getCenterX() || e.getCenterY() != element.getCenterY())) {
                    //Important to set the ID first and then call the create element method, which increments the ID
                    element.setID("U0-" + repository.getLastCreationID());
                    element.setColor(SVGConfig.FORE_COLOR);
                    element.setElement(repository.createElement(element));
                    repository.getElements(0).add(element);
                    actionText = "Element " + element.getID() + " created at (" + element.getCenterX() + ", " + element.getCenterY() + ")";
                    actionStory = "ADD " + element.getCenterX() + " " + element.getCenterY();
                    if (element.getSize() != 0) {
                        actionStory += " " + element.getSize();
                        actionText += " S-" + element.getSize();
                    }
                    //Set the id of the image employed
                    if (SVGConfig.GRAPHIC_ELEMENT == GraphicElement.Image) {
                        actionStory += " I-" + element.getImageID();
                        actionText += " I-" + element.getImageID();
                    }
                }
            }
            else {
                repository.setCurrentLevel(0);
                for (SVGElement elem : element.getSimpleElements()) {
                    repository.applyAction((SVGAction)clone(), elem);
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

    /**
     * @param repository the repository to set
     */
    @Override
    public void setRepository(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public String toString() {
        return "Create element";
    }
    
    @Override
    public Object clone() {
        ActionCreateElement action = new ActionCreateElement();
        action.endOfDesignAction = endOfDesignAction;
        action.repository = repository;
        return action;
    }
}
