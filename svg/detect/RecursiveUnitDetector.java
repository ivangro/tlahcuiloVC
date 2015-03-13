package svg.detect;

import java.util.ArrayList;
import java.util.List;
import svg.context.CanvasContext;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.ElementFactory;
import svg.elems.SVGUnit;



/**
 * Class to generate units of elements according to the following considerations:
 * DRk is the reference distance inside a unit and is equal to the minimum distance between any two elements inside the group
 * The distance between any two elements inside a group is lower than MAX_UNIT_DISTANCE
 * The distance between any two elements inside a group is lower than DRk + DISTANCE_OFFSET
 * 
 * @author Ivan Guerrero
 */
public class RecursiveUnitDetector implements IDetector {
    private List<List<SVGElement>> elements;
    private SVGRepository repository;
    
    public RecursiveUnitDetector() {
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
        
        /*
        if (lastLevel != null && lastLevel.size() > 1) {
            newLevel = generateUniqueGroup(lastLevel, level);
            elements.add(newLevel);
            createContext(newLevel, level);
        }*/
    }
    
    /**
     * Algorithm for group formation:<br>
     * while (there are elements without a group)<br>
     *      Find the closest pair of elements<br>
     *      reference distance = distance between the pair of elements<br>
     *      if (reference distance <= MAX_UNIT_DISTANCE)<br>
     *          Create a group with both elements<br>
     *          Set the reference distance of the group equal to the distance between the two elements<br>
     *          while (exists another element close enough to an element in the group)<br>
     *              find the closest element to any other element in the group<br>
     *              distance = minimum distance between the element found and any other element in the group<br>
     *              if (distance <= DISTANCE_OFFSET)<br>
     *                  add the element to the group<br>
     * 
     * @param elems The elements available in the current level
     * @param level The level being analyzed
     * @return A list with the elements grouped
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
            MinDistElement minElem = getMinDist(elems, q, dc);
            //If there are no linked elements, remove the last element on the list
            if (minElem.elem == null)
                minElem.elem = q.remove(q.size()-1);
            else
                q.remove(minElem.elem);
            
            SVGUnit unit = new SVGUnit();
            unit.setID("U" + level + "-" + units.size());
            double drUnit = -1;
            
            unit.addElement(minElem.elem);
            MinDistElement minDist;
            boolean elementAdded;
            
            do {
                elementAdded = false;
                 minDist = getMinDist(elems, q, unit, dc);
                 if (minDist.elem != null) {
                    if (minDist.dist <= SVGConfig.MAX_UNIT_DISTANCE * ElementFactory.getInstance().getUnitFactor(minDist.elem)) {
                        if (drUnit < 0) {
                            drUnit = minDist.dist;
                            unit.setReferenceDistance(drUnit);
                        }
                        if (Math.abs(minDist.dist-drUnit) <= SVGConfig.DISTANCE_OFFSET) {
                            q.remove(minDist.elem);
                            unit.addElement(minDist.elem);
                            elementAdded = true;
                        }
                    }
                 }
            } while (elementAdded && minDist.elem != null);
            units.add(unit);
        }
        return units;
    }
    
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
     * Obtains the nearest element to the unit which is also inside the q list
     * @param q The list of available elements
     * @param unit The reference unit
     * @param dc Array with all the available distances
     * @return 
     */
    private MinDistElement getMinDist(List<SVGElement> elements, List<SVGElement> q, SVGUnit unit, double[][] dc) {
        MinDistElement minElem = new MinDistElement();
        minElem.dist = Double.MAX_VALUE;
        
        for (SVGElement elem : q) {
            for (SVGElement unitElem : unit.getElements()) {
                double dist = dc[elements.indexOf(elem)][elements.indexOf(unitElem)];
                if (dist < minElem.dist) {
                    minElem.dist = dist;
                    minElem.elem = elem;
                }
            }
        }
        
        return minElem;
    }

    /**
     * Obtains the element with the nearest distance inside the q list
     * @param q List of elements
     * @param dc Distances between the elements
     * @return The element with the nearest neighbor inside the q list
     */
    private MinDistElement getMinDist(List<SVGElement> elements, List<SVGElement> q, double[][] dc) {
        MinDistElement minElem = new MinDistElement();
        minElem.dist = Double.MAX_EXPONENT;
        
        for (SVGElement elem : q) {
            for (SVGElement elem2 : q) {
                if (!elem.equals(elem2)) {
                    double dist = dc[elements.indexOf(elem)][elements.indexOf(elem2)];
                    if (dist < minElem.dist) {
                        minElem.dist = dist;
                        minElem.elem = elem;
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
    
    private List<SVGElement> generateUniqueGroup(List<SVGElement> elems, int level) {
        List<SVGElement> units = new ArrayList<>();
        double[][] dc = calculateDistances(elems);
        
        //Insert every element in the Q list
        List<SVGElement> q = new ArrayList<>();
        for (SVGElement elem : elems) {
            q.add(elem);
        }
        
        SVGUnit unit = new SVGUnit();
        unit.setID("U" + level + "-0");
        
        boolean elementAdded;
        MinDistElement minDist = getMinDist(elems, q, dc);
        q.remove(minDist.elem);
        unit.addPattern(minDist.dist);
        unit.addElement(minDist.elem);
        
        do {
            elementAdded = false;
            minDist = getMinDist(elems, q, unit, dc);
            if (minDist.elem != null) {
                q.remove(minDist.elem);
                unit.addElement(minDist.elem);
                unit.addPattern(minDist.dist);
                elementAdded = true;
             }
        } while (elementAdded && minDist.elem != null);
        units.add(unit);

        return units;
    }
}

class MinDistElement {
    public double dist;
    public SVGElement elem;
}