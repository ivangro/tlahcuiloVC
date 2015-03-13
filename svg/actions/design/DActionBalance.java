package svg.actions.design;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import svg.actions.ActionMoveElement;
import svg.core.*;
import svg.elems.SVGElementSelector;
import svg.elems.SVGUnit;

/**
 * Class to perform a balance action in a drawing or selected element(s)
 * @author Ivan Guerrero
 */
public class DActionBalance extends SVGDAction {
    private int cx, cy;
    private boolean horizontalBalance;
    /** HBalance "U"level-element */
    private static final String hBalanceAction = "\\s*HBALANCE\\s+U(\\d+)-(\\d+).*";
    /** VBalance "U"level-element */
    private static final String vBalanceAction = "\\s*VBALANCE\\s+U(\\d+)-(\\d+).*";
    private static Pattern hBalancePattern = Pattern.compile(hBalanceAction);
    private static Pattern vBalancePattern = Pattern.compile(vBalanceAction);
    
    public DActionBalance() {
        cx = SVGConfig.CANVAS_WIDTH / 2;
        cy = SVGConfig.CANVAS_HEIGHT / 2;
    }
    
    @Override
    public void applyAction(SVGRepository repository) {
        SVGElementSelector selector = repository.getElementSelector();
        SVGElement[] elements = new SVGElement[0];
        if (selector != null)
            elements = selector.getSelectedElements().toArray(new SVGElement[0]);
        
        switch (elements.length) {
            case 0:
                break;
            case 1:
                applyAction(elements[0], repository);
                break;
            case 2:
                applyAction(elements[0], elements[1]);
                break;
        }
    }

    /**
     * Creates a new group to balance the given element if there are no other groups in a position that can be employed.
     * @param element The element to be balanced
     * @param repository 
     */
    @Override
    public void applyAction(SVGElement element, SVGRepository repository) {
        super.element = element;
        actionName = (horizontalBalance) ? "HBALANCE" : "VBALANCE";
        desc = actionName + " " + element.getID();
        actionLevel = repository.getCurrentLevel();
        repository.addDesignAction(this);
        
        //Look for an element that can balance the given element
        SVGElement newElem;
        newElem = findNearbyElement(element, repository);
        if (newElem != null) {
            applyAction(element, newElem);
        }
        else {
            int attemps = 10;
            do {
                //Create a new group and determine its location in the canvas to balance the given element
                DActionCreateUnit creation = new DActionCreateUnit();
                creation.setBanDesignAction(true);
                creation.applyAction(repository);
                newElem = creation.getElement();
                applyAction(element, newElem);
                attemps--;
            } while (!isInBounds(newElem) && attemps > 0);
            repository.setCurrentLevel(actionLevel);
        }
        
        //Move the previously created group to the appropriate location
        ActionMoveElement move = new ActionMoveElement();
        move.setNewX(newElem.getCenterX());
        move.setNewY(newElem.getCenterY());
        repository.applyAction(move, newElem, true);
        
        //Mark the last action performed as design action
        List<SVGAction> actions = repository.getActions();
        int numberOfActions = newElem.getSimpleElements().size();
        actions.get(actions.size() - numberOfActions - 1).setEndOfDesignAction();
        actions.get(actions.size() - 1).setEndOfDesignAction();
        
        if (newElem instanceof SVGUnit)
            Logger.getGlobal().log(Level.INFO, "Elem {0} balanced with {1}", new Object[] {element, ((SVGUnit)newElem).toStringWithElements()});
        else
            Logger.getGlobal().log(Level.INFO, "Elem {0} balanced with {1}", new Object[] {element, newElem});
    }
    
    private double calculateMassCentre(SVGElement element) {
        double massCentre;
        double dist;
        dist = Math.sqrt(Math.pow(element.getCenterX() - cx, 2) + Math.pow(element.getCenterY() - cy, 2));
        massCentre = element.getArea() * dist;
        return massCentre;
    }
    
    /**
     * Looks an appropriate position for the second element to balance the first element
     * @param element The element to be balanced
     * @param element2 The element to be moved
     */
    public void applyAction(SVGElement element, SVGElement element2) {
        double massCentre, massCentre2, diff;
        double dist;
        
        massCentre = calculateMassCentre(element);
        massCentre2 = calculateMassCentre(element2);
        diff = Math.abs(massCentre - massCentre2);
        
        //If the difference between the mass centres is greater than a threshold, move the second element
        if (diff > SVGConfig.MAX_BALANCE_OFFSET * element.getArea()) {
            dist = massCentre / element2.getArea();
            int dx, dy;
            Random random = new Random();
            dx = random.nextInt((int)dist);
            dy = (int)Math.round(Math.sqrt(Math.pow(dist,2) - Math.pow(dx, 2)));
            int quadrant = determineQuadrant(element);
            
            if (isHorizontalBalance()) {
                dx *= (random.nextBoolean()) ? 1 : -1;
                switch (quadrant) {
                    case 1:
                    case 2:
                        element2.setCenter(cx+dx, cy+dy); 
                        break;
                    case 3:
                    case 4:
                        element2.setCenter(cx+dx, cy-dy); 
                        break;
                }
            }
            else {
                dy *= (random.nextBoolean()) ? 1 : -1;
                switch (quadrant) {
                    case 1:
                    case 3:
                        element2.setCenter(cx+dx, cy+dy); 
                        break;
                    case 2:
                    case 4:
                        element2.setCenter(cx-dx, cy+dy); 
                        break;
                }
            }
        }
    }

    /**
     * Obtains the queadrant in which the element is located
     * @param element
     * @return 
     * -------------
     * |     |     |
     * |  1  |  2  |
     * |     |     |
     * -------------
     * |     |     |
     * |  3  |  4  |
     * |     |     |
     * -------------
     */
    private int determineQuadrant(SVGElement element) {
        int quadrant;
        if (element.getCenterX() > cx)
            quadrant = (element.getCenterY() > cy) ? 4 : 2;
        else
            quadrant = (element.getCenterY() > cy) ? 3 : 1;
        return quadrant;
    }

    /**
     * @return the horizontalBalance
     */
    public boolean isHorizontalBalance() {
        return horizontalBalance;
    }

    /**
     * @param horizontalBalance the horizontalBalance to set
     */
    public void setHorizontalBalance(boolean horizontalBalance) {
        this.horizontalBalance = horizontalBalance;
    }

    /**
     * Parses the given action string and applies it to the given repository
     * @param actionStr Action to be applied
     * @param repository Repository where the action will be applied
     */
    @Override
    public boolean parseAction(String actionStr, SVGRepository repository) {
        Matcher m;
        int noElement, level, lastLevel;
        lastLevel = repository.getCurrentLevel();
        actionName = (horizontalBalance) ? "HBALANCE" : "VBALANCE";
        
        m = hBalancePattern.matcher(actionStr);
        if (m.matches()) {
            setHorizontalBalance(true);
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
        m = vBalancePattern.matcher(actionStr);
        if (m.matches()) {
            setHorizontalBalance(false);
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

        m = hBalancePattern.matcher(actionStr);
        if (m.matches()) {
            setHorizontalBalance(true);
            level = Integer.parseInt(m.group(1));
            noElement = Integer.parseInt(m.group(2));
            actionLevel = level;
            if (repository.getElements(level).size() > noElement) {
                SVGElement elem = repository.getElements(level).get(noElement);
                actionName = (horizontalBalance) ? "HBALANCE" : "VBALANCE";
                desc = actionName + " " + elem.getID();
                super.element = elem;
                repository.addDesignAction(this);
                return true;
            }
            
        }
        m = vBalancePattern.matcher(actionStr);
        if (m.matches()) {
            setHorizontalBalance(false);
            level = Integer.parseInt(m.group(1));
            noElement = Integer.parseInt(m.group(2));
            actionLevel = level;
            if (repository.getElements(level).size() > noElement) {
                SVGElement elem = repository.getElements(level).get(noElement);
                actionName = (horizontalBalance) ? "HBALANCE" : "VBALANCE";
                desc = actionName + " " + elem.getID();
                super.element = elem;
                repository.addDesignAction(this);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Determines if the given element is within the canvas limits
     * @param elem
     * @return TRUE if the coordinates of the element are inside the canvas
     */
    private boolean isInBounds(SVGElement elem) {
        return (elem.getCenterX() > 0 && elem.getCenterX() < SVGConfig.CANVAS_WIDTH &&
                elem.getCenterY() > 0 && elem.getCenterY() < SVGConfig.CANVAS_HEIGHT);
    }

    /**
     * Looks for a nearby element to balance a given element.
     * @param elem The element to be balanced.
     * @param repository The repository with all the data related to the canvas.
     * @return An existing element that can balance the given element.
     */
    private SVGElement findNearbyElement(SVGElement elem, SVGRepository repository) {
        SVGElement neighbor = null;
        
        //Calculate the attributes of the given element
        double massCentre = calculateMassCentre(elem);
        int quadrant = determineQuadrant(elem);
        
        //Obtain all the elements available in the current level
        List<SVGElement> elements = repository.getElements(repository.getCurrentLevel());
        for (SVGElement e : elements) {
            if (e.equals(elem))
                continue;
            double massCentre2 = calculateMassCentre(e);
            double massFactor = Math.max(elem.getArea(), e.getArea());
            massFactor *= SVGConfig.MAX_BALANCE_OFFSET * 5;
            //Obtain the element with a similar mass centre
            if (Math.abs(massCentre - massCentre2) < massFactor) {
                //Determine the quadrant of the element and the type of balance requested
                int quad = determineQuadrant(e);
                if (horizontalBalance) {
                    if (((quadrant == 1 || quadrant == 2) && (quad == 3 ||quad == 4)) ||
                        ((quadrant == 3 || quadrant == 4) && (quad == 1 || quad == 2))) {
                        neighbor = e;
                        break;
                    }
                }
                else {
                    if (((quadrant == 1 || quadrant == 3) && (quad == 2 ||quad == 4)) ||
                        ((quadrant == 2 || quadrant == 4) && (quad == 1 || quad == 3))) {
                        neighbor = e;
                        break;
                    }
                }
            }
        }
        return neighbor;
    }
}