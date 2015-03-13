package svg.actions.design;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionMoveElement;
import svg.core.SVGConfig;
import svg.core.SVGDAction;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGUnit;

/**
 * Aligns the available elements near a border with the closest element to the same border.
 * @author Ivan Guerrero
 */
public class DActionAlign extends SVGDAction {
    private SVGRepository repository;
    private static int DISTANCE_FACTOR = 2;
    /** [TBLR]ALIGN "U"level-element */
    private static Pattern alignPattern = Pattern.compile("\\s*([TBLR])ALIGN\\s+U(\\d+)-(\\d+).*");
    private Align alignment;
    /** Maximum distance from a group to the border to align it */
    private int MAX_BORDER_DIST = 50;

    public enum Align {TOP, BOTTOM, LEFT, RIGHT};
    
    public DActionAlign() {
        actionName = "ALIGN";
    }
    
    /**
     * Identifies the closest element to the selected border and applies the action to it
     * @param repository 
     */
    @Override
    public void applyAction(SVGRepository repository) {
        this.repository = repository;
        SVGElement closest = null;
        int dist, currentLevel;
        switch(alignment) {
            case TOP: dist = SVGConfig.CANVAS_HEIGHT; break;
            case LEFT: dist = SVGConfig.CANVAS_WIDTH; break;
            default: dist = 0;
        }
        
        //Identify the closest element to the selected border and apply the action to it
        currentLevel = repository.getCurrentLevel();
        repository.setCurrentLevel(1);
        actionLevel = 1;
        for (SVGElement elem : repository.getElements(repository.getCurrentLevel())) {
            SVGElement e = getClosestElementToBorder((SVGUnit)elem);
            switch (alignment) {
                case TOP:
                    if (e.getCenterY() <= dist) {
                        dist = e.getCenterY();
                        closest = elem;
                    }
                    break;
                case BOTTOM:
                    if (e.getCenterY() >= dist) {
                        dist = e.getCenterY();
                        closest = elem;
                    }
                    break;
                case LEFT:
                    if (e.getCenterX() <= dist) {
                        dist = e.getCenterX();
                        closest = elem;
                    }
                    break;
                case RIGHT:
                    if (e.getCenterX() >= dist) {
                        dist = e.getCenterX();
                        closest = elem;
                    }
                    break;
            }
        }
        
        applyAction(closest, repository);
        repository.setCurrentLevel(currentLevel);
    }

    @Override
    public void applyAction(SVGElement element, SVGRepository repository) {
        this.repository = repository;
        int offset;
        int level = repository.getCurrentLevel();
        actionLevel = level;
        List<SVGElement> elements = repository.getElements(level);        
        
        //If the element is close to the selected border, mark it for alignment
        SVGElement closestElement = (element instanceof SVGUnit) ? getClosestElementToBorder((SVGUnit)element) : element;
        for (SVGElement elem : elements) {
            SVGElement closest = (elem instanceof SVGUnit) ? getClosestElementToBorder((SVGUnit)elem) : elem;
            //Logger.getGlobal().log(Level.INFO, "Closest element in group {0}: {1}", new Object[]{elem, closest});
            if (isRelativelyClose(closestElement, closest) && isNearToBorder(closestElement)) {
                switch (alignment) {
                    case TOP:
                    case BOTTOM:
                        offset = closestElement.getCenterY() - closest.getCenterY();
                        moveElement(elem, elem.getCenterX(), elem.getCenterY() + offset);
                        break;
                    case LEFT:
                    case RIGHT:
                        offset = closestElement.getCenterX() - closest.getCenterX();
                        moveElement(elem, elem.getCenterX() + offset, elem.getCenterY());
                        break;
                }
            }
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
        m = alignPattern.matcher(actionStr);
        if (m.matches()) {
            char alignmentType = m.group(1).charAt(0);
            setAlignement(alignmentType);
            lastLevel = repository.getCurrentLevel();
            level = Integer.parseInt(m.group(2));
            noElement = Integer.parseInt(m.group(3));
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
        m = alignPattern.matcher(actionStr);
        if (m.matches()) {
            char alignmentType = m.group(1).charAt(0);
            setAlignement(alignmentType);
            level = Integer.parseInt(m.group(2));
            actionLevel = level;
            noElement = Integer.parseInt(m.group(3));
            if (repository.getElements(level).size() > noElement) {
                SVGElement elem = repository.getElements(level).get(noElement);
                super.element = elem;
                desc = actionName + " " + element.getID();
                return true;
            }
        }
        
        return false;
    }
    
    public void setAlignement(Align alignment) {
        this.alignment = alignment;
        actionName = alignment.name().charAt(0) + "ALIGN";
    }
    
    public void setAlignement(char alignmentType) {
        switch (alignmentType) {
            case 'T':
                setAlignement(Align.TOP);
                break;
            case 'B':
                setAlignement(Align.BOTTOM);
                break;
            case 'L':
                setAlignement(Align.LEFT);
                break;
            case 'R':
                setAlignement(Align.RIGHT);
                break;
        }
    }
    
    public Align getAlignment() {
        return alignment;
    }
    
    /**
     * Determines if the given element is close enough to the selected border.<br>
     * If the reference element is at a distance x to the border, 
     *  the element is close enough if its distance to the border is lower or equal than 2x.<br>
     * @param element The element to be analised
     * @param reference The user's selected element to be taken as distance reference
     * @return True if the element is close enough to the selected border according to the reference element
     */
    private boolean isRelativelyClose(SVGElement reference, SVGElement element) {
        int distReference = 0, dist = 0;
        
        switch (alignment) {
            case TOP:
                distReference = reference.getCenterY();
                dist = element.getCenterY();
                break;
            case BOTTOM:
                distReference = SVGConfig.CANVAS_HEIGHT - reference.getCenterY();
                dist = SVGConfig.CANVAS_HEIGHT - element.getCenterY();
                break;
            case LEFT:
                distReference = reference.getCenterX();
                dist = element.getCenterX();
                break;
            case RIGHT:
                distReference = SVGConfig.CANVAS_WIDTH - reference.getCenterX();
                dist = SVGConfig.CANVAS_WIDTH - element.getCenterX();
                break;
        }
        return dist <= distReference * DISTANCE_FACTOR;
    }
    
    private SVGElement getClosestElementToBorder(SVGUnit group) {
        SVGElement elem = null;
        int reference;
        
        switch (alignment) {
            case TOP:
                //Pick the element with the lowest y coordinate
                reference = group.getCenterY();
                for (SVGElement e : group.getElements()) {
                    if (e.getCenterY() <= reference){
                        reference = e.getCenterY();
                        elem = e;
                    }
                }
                break;
            case BOTTOM:
                //Pick the element with the highest y coordinate
                reference = group.getCenterY();
                for (SVGElement e : group.getElements()) {
                    if (e.getCenterY() >= reference){
                        reference = e.getCenterY();
                        elem = e;
                    }
                }
                break;
            case LEFT:
                //Pick the element with the lowest x coordinate
                reference = group.getCenterX();
                for (SVGElement e : group.getElements()) {
                    if (e.getCenterX() <= reference){
                        reference = e.getCenterX();
                        elem = e;
                    }
                }
                break;
            case RIGHT:
                //Pick the element with the highest x coordinate
                reference = group.getCenterX();
                for (SVGElement e : group.getElements()) {
                    if (e.getCenterX() >= reference){
                        reference = e.getCenterX();
                        elem = e;
                    }
                }
                break;
        }
        
        return elem;
    }
    
    private void moveElement(SVGElement elem, int newX, int newY) {
        elem.setUserAdded(false);
        ActionMoveElement action = new ActionMoveElement();
        action.setRepository(repository);
        action.setNewX(newX);
        action.setNewY(newY);
        repository.applyAction(action, elem, true);
    }
    
    private boolean isNearToBorder(SVGElement closestElement) {
        boolean ans = false;
        switch (alignment) {
            case TOP:
                return closestElement.getCenterY() < (MAX_BORDER_DIST + closestElement.getHeight() / 2);
            case BOTTOM:
                return closestElement.getCenterY() > (SVGConfig.CANVAS_HEIGHT - (MAX_BORDER_DIST + closestElement.getHeight() / 2));
            case LEFT:
                return closestElement.getCenterX() < (MAX_BORDER_DIST + closestElement.getWidth() / 2);
            case RIGHT:
                return closestElement.getCenterX() > (SVGConfig.CANVAS_WIDTH - (MAX_BORDER_DIST + closestElement.getWidth() / 2));
        }
        return ans;
    }
}