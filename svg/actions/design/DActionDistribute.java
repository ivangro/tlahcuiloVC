package svg.actions.design;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionMoveElement;
import svg.core.*;
import svg.elems.SVGUnit;

/**
 * Equally distributes the elements.<br>
 * It ensures that every element within a group is at the same distance according to its nearest element.
 * @author Ivan Guerrero
 */
public class DActionDistribute extends SVGDAction {
    /** DISTRIBUTE "U"level-element */
    private static Pattern distributePattern = Pattern.compile("\\s*DISTRIBUTE\\s+U(\\d+)-(\\d+).*");
    
    public DActionDistribute() {
        actionName = "DISTRIBUTE";
    }

    @Override
    public void applyAction(SVGRepository repository) {
        int level = repository.getCurrentLevel();
        repository.setCurrentLevel(1);
        actionLevel = repository.getCurrentLevel();
        
        List<SVGElement> elements = repository.getElements(1);
        for (SVGElement elem : elements) {
            //If the element is not a shape, distribute its elements
            if (elem.getShapeID() < 0)
                applyAction(elem, repository);
        }
        repository.setCurrentLevel(level);
    }

    @Override
    public void applyAction(SVGElement element, SVGRepository repository) {
        //If the element has a linear shape, continue
        switch (element.getShape()) {
            case VERTICAL:
            case HORIZONTAL:
            case NEGATIVE_TREND:
            case POSITIVE_TREND:
                //Obtain the elements in the previous level conforming the given element
                SVGUnit unit = (SVGUnit)element;
                Set<SVGElement> elements = unit.getElements();
                //Order the elements from left to right and from the top to the bottom
                List<SVGElement> list = new ArrayList<>();
                list.addAll(elements);
                SVGConfig.ELEMENT_SORT_TYPE = element.getShape();
                Collections.sort(list);
                //Equally distribute the elements along the line
                //Obtain the mean x and y distance between the elements
                int meanX = 0, meanY = 0;
                //Locate the elements at the mean distance
                SVGElement prev = list.get(0);
                for (int i=1; i<list.size(); i++) {
                    SVGElement elem = list.get(i);
                    meanX += elem.getCenterX() - prev.getCenterX();
                    meanY += elem.getCenterY() - prev.getCenterY();
                    prev = elem;
                }
                meanX /= (list.size()-1);
                meanY /= (list.size()-1);
                
                prev = list.get(0);
                for (int i=1; i<list.size(); i++) {
                    SVGElement elem = list.get(i);
                    elem.setUserAdded(false);
                    ActionMoveElement action = new ActionMoveElement();
                    action.setRepository(repository);
                    action.setNewX(prev.getCenterX() + meanX * i);
                    action.setNewY(prev.getCenterY() + meanY * i);
                    repository.applyAction(action, elem, true);
                }
                break;
        }
            
        super.element = element;
        desc = actionName + " " + element.getID();
        //repository.addDesignAction(this);
        //Marks the last action as end of design action
        //super.setDesignAction(repository);
    }

    @Override
    public boolean parseAction(String actionStr, SVGRepository repository) {
        Matcher m;
        int noElement, level, lastLevel;
        m = distributePattern.matcher(actionStr);
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
        m = distributePattern.matcher(actionStr);
        if (m.matches()) {
            level = Integer.parseInt(m.group(1));
            noElement = Integer.parseInt(m.group(2));
            if (repository.getElements(level).size() > noElement) {
                SVGElement elem = repository.getElements(level).get(noElement);
                super.element = elem;
                desc = actionName + " " + element.getID();
            }
            return true;
        }
        
        return false;
    }

}
