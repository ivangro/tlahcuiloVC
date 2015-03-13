package svg.detect;

import java.util.ArrayList;
import java.util.List;
import svg.context.CanvasContext;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGUnit;




/**
 * Class to generate units of elements according to the following considerations:<br>
 * Invariant: Each element is grouped with its closest element if the distance between them is lower than MAX_DIST_UNIT<br>
 * Algorithm:<br>
 * 1. Add each element to the queue Q<br>
 * 2. While Q is not empty <br>
 *     2.1 Find ei, ej which are the elements with the min distance between them
 *     2.2 if dist < MAX_DIST_UNIT <br>
 *         Gi is the group to which ei belongs to<br>
 *         Gj is the group to which ej belongs to<br>
 *         if Gi && Gj == null<br>
 *             Create a new group Gk and add ei and ej to it<br>
 *         else if Gi == null<br>
 *             Add ei to Gj<br>
 *         else if Gj == null<br>
 *             Add ej to Gi<br>
 *         else<br>
 *             Merge Gi with Gj<br>
 *     remove ei and ej from Q<br>
 * @author Ivan Guerrero
 */
public class IncrementalUnitDetector implements IDetector {
    private List<List<SVGElement>> elements;
    private SVGRepository repository;
    
    public IncrementalUnitDetector() {
    }
        
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        this.repository = repository;
        this.elements = elements;
        //Every time a unit detection starts, remove the previous elements in context and in elements' list
        for (int i=elements.size()-1; i > 0; i--) {
            elements.remove(i);
        }
        repository.getContext().clearContexts();
        
        List<SVGElement> newLevel, lastLevel = null;
        int level = 1;
        int groupDifference;
        
            do {
                newLevel = detectGroups(elements.get(level-1), level);
                groupDifference = (lastLevel == null) ? newLevel.size() : lastLevel.size() - newLevel.size();
                if (groupDifference > 0) {
                    elements.add(newLevel);
                    createContext(newLevel, level);
                    lastLevel = newLevel;
                    level++;
                }
            }while (groupDifference > 1);
        }
    
    /**
     * Groups the given elements according to the proximity between each element
     * @param elems The elements to be grouped
     * @param level The level at which the elements belong
     * @return A list of groups of elements containing all the given elements
     */
    protected List<SVGElement> detectGroups(List<SVGElement> elems, int level) {
        List<SVGElement> units = new ArrayList<>();
        double[][] dc = calculateDistances(elems);
        
        //Insert every element in the Q list
        List<SVGElement> q = new ArrayList<>();
        for (SVGElement elem : elems) {
            q.add(elem);
        }
            
        while (!q.isEmpty()) {
            //Obtain the last element in the list
            MinDistPair minElem = getMinDist(elems, q, dc);
            if (minElem.distance <= SVGConfig.MAX_UNIT_DISTANCE) {
                //Obtains the group of each element
                SVGUnit group1 = obtainGroup(minElem.elem1, units);
                SVGUnit group2 = obtainGroup(minElem.elem2, units);
                if (group1 == null && group2 == null) {
                    SVGUnit unit = new SVGUnit();
                    unit.setID("U" + level + "-" + units.size());
                    unit.addElement(minElem.elem1);
                    unit.addElement(minElem.elem2);
                    units.add(unit);
                }
                else if (group1 == null)
                    group2.addElement(minElem.elem1);
                else if (group2 == null)
                    group1.addElement(minElem.elem2);
                else {
                    group1.mergeUnit(group2);
                }
            }
            else {
                //Adds every element in a different group
                if (minElem.elem1 != null) {
                    SVGUnit unit1 = new SVGUnit();
                    unit1.setID("U" + level + "-" + units.size());
                    unit1.addElement(minElem.elem1);
                    units.add(unit1);
                }
                if (minElem.elem2 != null) {
                    SVGUnit unit2 = new SVGUnit();
                    unit2.setID("U" + level + "-" + units.size());
                    unit2.addElement(minElem.elem2);
                    units.add(unit2);
                }
            }
            q.remove(minElem.elem1);
            q.remove(minElem.elem2);
        }
        return units;
    }
    
    /**
     * Calculates the distances between every pair of elements
     * @param elements List of elements
     * @return Distances between each element in the list
     */
    private double[][] calculateDistances(List<SVGElement> elements) {
        double[][] dc;
        MinDistElement dr = new MinDistElement();
        dr.dist = SVGConfig.MAX_UNIT_DISTANCE;
        dc = new double[elements.size()][elements.size()];
        
        //Calculate dc for every pair of elements
        for (int i=0; i<elements.size(); i++) {
            int xi, yi;
            xi = elements.get(i).getCenterX();
            yi = elements.get(i).getCenterY();
            for (int j=i+1; j<elements.size(); j++) {
                int xj, yj;
                xj = elements.get(j).getCenterX();
                yj = elements.get(j).getCenterY();
                dc[i][j] = Math.sqrt(Math.pow(xi-xj,2) + Math.pow(yi-yj,2));
                dc[j][i] = dc[i][j];
                //Calculate dr = min(dc)
                if (dc[i][j] < dr.dist) {
                    dr.dist = dc[i][j];
                    dr.elem = elements.get(i);
                }
            }
        }
        
        return dc;
    }
    
    @Override
    public String getDescription() {
        String unitText = "Detected units: \n";
        if (elements != null && elements.size() > 0) {
            List<SVGElement> list = elements.get(elements.size() - 1);
            for (SVGElement elem : list) {
                unitText += elem.toString() + "\n";
            }
        }
        else
            unitText = "";
        
        return unitText;
    }

    /**
     * Obtains the element with the nearest distance inside the q list to any available element
     * @param elements List of all the available elements
     * @param q List of the elements outside any group
     * @param dc Distances between the elements
     * @return The elements with the smaller distance between them inside the q list
     */
    private MinDistPair getMinDist(List<SVGElement> elements, List<SVGElement> q, double[][] dc) {
        MinDistPair minElem = new MinDistPair();
        minElem.distance = Double.MAX_EXPONENT;
        
        for (SVGElement elem : q) {
            for (SVGElement elem2 : elements) {
                if (!elem.equals(elem2)) {
                    double dist = dc[elements.indexOf(elem)][elements.indexOf(elem2)];
                    if (dist < minElem.distance) {
                        minElem.distance = dist;
                        minElem.elem1 = elem;
                        minElem.elem2 = elem2;
                    }
                }
            }
        }
        
        return minElem;
    }
 
    /**
     * 
     * @param elements
     * @param level 
     */
    private void createContext(List<SVGElement> elements, int level) {
        CanvasContext context = repository.getContext();
        for (SVGElement elem : elements) {
            context.addElement(elem, level);
        }
    }

    /**
     * Obtains the group to which the element belongs to
     * @param elem The element
     * @param units The list of available groups
     * @return The group to which the given element belongs
     */
    private SVGUnit obtainGroup(SVGElement elem, List<SVGElement> units) {
        SVGUnit group = null;
        for (SVGElement unit : units) {
            if (((SVGUnit)unit).containsElement(elem))
                return (SVGUnit)unit;
        }
        
        return group;
    }
}
class MinDistPair {
    public SVGElement elem1, elem2;
    public double distance;
}