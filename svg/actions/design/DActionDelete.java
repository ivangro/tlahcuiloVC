package svg.actions.design;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionDeleteElement;
import svg.core.SVGDAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Action to delete a unit
 * @author Ivan Guerrero
 */
public class DActionDelete extends SVGDAction {
    /** DELETE "U"level-element*/
    private static Pattern deletePattern = Pattern.compile("\\s*DELETE\\s+U(\\d+)-(\\d+).*");
    
    public DActionDelete() {
        actionName = "DELETE";
    }
    
    @Override
    public void applyAction(SVGRepository repository) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void applyAction(SVGElement element, SVGRepository repository) {
        ActionDeleteElement action = new ActionDeleteElement();
        action.setRepository(repository);
        repository.applyAction(action, element, true);
        super.element = element;
        desc = actionName + " " + element.getID();
        actionLevel = repository.getCurrentLevel();
        
        if (repository.getCurrentLevel() > 0) {
            repository.addDesignAction(this);
            //Marks the last action as end of design action
            super.setDesignAction(repository);
        }
    }

    @Override
    public boolean parseAction(String actionStr, SVGRepository repository) {
        Matcher m;
        int noElement, level, lastLevel;
        m = deletePattern.matcher(actionStr);
        if (m.matches()) {
            lastLevel = repository.getCurrentLevel();
            level = Integer.parseInt(m.group(1));
            noElement = Integer.parseInt(m.group(2));
            if (repository.getElements(level).size() > noElement) {
                SVGElement elem = repository.getElements(level).get(noElement);
                repository.setCurrentLevel(level);
                applyAction(elem, repository);
                repository.setCurrentLevel(lastLevel);
            }
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean parseAction(String actionStr, SVGRepository repository, boolean execute) {
        if (execute)
            return parseAction(actionStr, repository);
        
        Matcher m;
        int noElement, level;
        m = deletePattern.matcher(actionStr);
        if (m.matches()) {
            level = Integer.parseInt(m.group(1));
            actionLevel = level;
            noElement = Integer.parseInt(m.group(2));
            if (repository.getElements(level).size() > noElement) {
                SVGElement elem = repository.getElements(level).get(noElement);
                super.element = elem;
                desc = actionName + " " + element.getID();
                if (repository.getCurrentLevel() > 0) {
                    repository.addDesignAction(this);
                }
                return true;
            }
            
        }
        
        return false;
    }
}