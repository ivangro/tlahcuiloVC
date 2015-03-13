package svg.core;

import java.util.List;
import java.util.Properties;

/**
 * Interface to be implemented by every action of design
 * @author Ivan Guerrero
 */
public abstract class SVGDAction implements Cloneable {
    protected int actionLevel;
    protected String desc = "", actionName;
    protected SVGElement element;
    /** Employed to store additional parameters to the design action */
    protected Properties properties;
    
    public SVGDAction() {
        properties = new Properties();
    }
    
    /**
     * The method must implement the following tasks:<br>
     * 1. Determine the description of the action (this will be employed when the design is stored in a file)<br>
     * 2. Add the action to the bundle of design actions inside the repository<br>
     * 3. Mark the last single action performed as end of design action (this will be employed when the simple actions are stored in a file)<br>
     * @param repository 
     */
    public abstract void applyAction(SVGRepository repository);
    /**
     * The method must implement the following tasks:<br>
     * 1. Determine the description of the action (this will be employed when the design is stored in a file)<br>
     * 2. Add the action to the bundle of design actions inside the repository<br>
     * 3. Mark the last single action performed as end of design action (this will be employed when the simple actions are stored in a file)<br>
     * 4. Set the element to which the action has been applied<br>
     * @param repository 
     */
    public abstract void applyAction(SVGElement element, SVGRepository repository);
    public abstract boolean parseAction(String actionStr, SVGRepository repository);
    public abstract boolean parseAction(String actionStr, SVGRepository repository, boolean execute);
        
    public String getActionText() {
        return desc;
    }
    
    public SVGElement getElement() {
        return element;
    }
    
    public String getActionName() {
        return actionName;
    }
    
    /**
     * Marks the last action as end of design action
     * @param repository 
     */
    public void setDesignAction(SVGRepository repository) { 
        List<SVGAction> actions = repository.getActions();
        if (actions.size() > 0)
            actions.get(actions.size() - 1).setEndOfDesignAction();
    }
    
    @Override
    public String toString() {
        return desc;
    }

    public Properties getProperties() {
        return properties;
    }
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    
    public int getActionLevel() {
        return actionLevel;
    }
    
    /**
     * General method to execute a parsed action
     * @param level The level of the selected element
     * @param noElement The number of element selected
     * @param appliesToAll If the action applies to all the elements or not
     * @param execute If the action is going to be executed or only added to the actions' list
     * @param repository The current repository with all the data of the drawing
     */
    protected void executeAction(int level, int noElement, boolean appliesToAll, boolean execute, SVGRepository repository) {
        if (execute) {
            int lastLevel = repository.getCurrentLevel();
            if (appliesToAll) {
                applyAction(repository);
            }
            else {
                SVGElement elem = repository.getElements(level).get(noElement);
                repository.setCurrentLevel(level);
                applyAction(elem, repository);
            }
            repository.setCurrentLevel(lastLevel);
        }
        else {
            if (appliesToAll) {
                desc = actionName + " " + "ALL";
            }
            else {
                SVGElement elem = repository.getElements(level).get(noElement);
                element = elem;
                desc = actionName + " " + elem.getID();
            }
            repository.addDesignAction(this);
        }
    }
    
    /**
     * Method to execute a parsed action for the given element
     * @param level Level of the element to which the action will be applied
     * @param noElement Number of the element to which the action will be applied
     * @param execute If the action is going to be executed or only added to the actions' list
     * @param repository The current data repository of the drawing
     */
    protected void executeAction(int level, int noElement, boolean execute, SVGRepository repository) {
        executeAction(level, noElement, false, execute, repository);
    }
    
    /**
     * Method to add a parsed action for all the elements
     * @param execute If the action is going to be executed or only added to the actions' list
     * @param repository The current data repository of the drawing
     */
    protected void executeAction(boolean execute, SVGRepository repository) {
        executeAction(-1, -1, true, execute, repository);
    }
}
