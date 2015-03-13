package svg.engagement;

import java.util.Properties;
import svg.core.SVGDAction;
import svg.core.SVGElement;

/**
 * Class to represent a desired next action to apply given the context of an atom
 * @author Ivan Guerrero
 */
public class NextDesignAction implements INextAction {
    transient private SVGDAction action;
    private String actionName, element;
    private Properties properties;
    
    public NextDesignAction() {
        properties = new Properties();
    }
    
    public void setAction(SVGDAction action) {
        this.action = action;
        actionName = action.getActionName();
        properties = action.getProperties();
        element = "ALL";
    }
    
    public void setAction(SVGDAction action, SVGElement mappedElement) {
        this.action = action;
        actionName = action.getActionName();
        properties = action.getProperties();
        element = mappedElement.getID(); //mapping.get(action.getElement()).getID();
    }
    
    public SVGDAction getAction() {
        return action;
    }
    
    @Override
    public String toString() {
        return getActionName() + " " + getElement();
    }

    /**
     * @return the actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * @return the element
     */
    public String getElement() {
        return element;
    }
    
    public Properties getProperties() {
        return properties;
    }
}