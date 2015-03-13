package svg.detect;

import ivangro.utils.SortedList;
import java.util.*;
import svg.context.ElementRelationType;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.utils.SVGUnitComparator;
import svg.elems.SVGElementComparer;

/**
 * Class to determine if a drawing is horizontally symmetric
 * @author Ivan Guerrero
 */
public class HSymmetryDetector implements ISymmetryDetector {
    private int centerH;
    private List<SymmetryResult> results;
    private SVGRepository repository;
    
    public HSymmetryDetector() {
        centerH = SVGConfig.CANVAS_HEIGHT / 2;
    }
    
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        this.repository = repository;
        results = new ArrayList<>();
        for (int i=1; i<elements.size(); i++) {
            //Store the result for each level in an object    
            results.add(detectSymmetry(elements.get(i), i));
        }
    }
    
    /**
     * Determines if the given drawing is symmetric respect to the horizontal axis
     * @param repository 
     */
    private SymmetryResult detectSymmetry(List<SVGElement> units, int level) {
        SymmetryResult result = new SymmetryResult();
        List<SVGElement> upper, lower, middle;
        upper = new SortedList<>(new SVGUnitComparator(true));
        lower = new SortedList<>(new SVGUnitComparator(true));
        middle = new SortedList<>(new SVGUnitComparator(true));
        
        //Split the units in upper and lower units
        for (SVGElement unit : units) {
            if (unit.getCenterY() > centerH + SVGConfig.MAX_SYMMETRY_OFFSET)
                lower.add(unit);
            else if (unit.getCenterY() < centerH - SVGConfig.MAX_SYMMETRY_OFFSET)
                upper.add(unit);
            else
                middle.add(unit);
        }
        
        //Determines the mass on every half of the canvas to detect balance
        result.setFirstMass(getMass(upper));
        result.setSecondMass(getMass(lower));
        new BalanceDetector(repository).detectBalance(upper, lower, BalanceDetector.BalanceType.Horizontal, level);
        
        //Detect if every unit in the upper part has a correspondence in the lower part, the drawing is H-symmetric
        result.setSymmetric(detectCopiedSymmetry(upper, lower, level));
        result.setMirrored(detectMirroredSymmetry(upper, lower, level));
        result.setElemsInTheMiddle(middle.size());
        
        for (SVGElement elem : middle) {
            repository.getContext().addRelation(elem, elem, ElementRelationType.HMS, level);
            //repository.getContext().addRelation(elem, elem, ElementRelationType.HTS, level);
            repository.getContext().addRelation(elem, elem, ElementRelationType.HB, level);
        }
        
        return result;
    }
    
    private double getMass(List<SVGElement> units) {
        double mass = 0;
        double distCenter;
        for (SVGElement unit : units) {
            distCenter = Math.sqrt(Math.pow(unit.getCenterX() - SVGConfig.CANVAS_WIDTH/2, 2) + 
                         Math.pow(unit.getCenterY() - SVGConfig.CANVAS_HEIGHT/2, 2));
            mass += unit.getArea() * distCenter;
        }
        return mass;
    }
    
    private boolean detectMirroredSymmetry(List<SVGElement> upper, List<SVGElement> lower, int level) {
        boolean res;
        res = !(upper.isEmpty() && !lower.isEmpty());
        
        for (int i=0; i<upper.size(); i++) {
            SVGElement up = upper.get(i);
            boolean hasMirroredElem = false;
            for (int j=0; j<lower.size(); j++) {
                SVGElement down = lower.get(j);
                //X must be equal and distY to center must be equal
                if ((Math.abs(up.getCenterX() - down.getCenterX()) < SVGConfig.MAX_SYMMETRY_OFFSET) && 
                   (Math.abs(SVGConfig.CANVAS_HEIGHT - up.getCenterY() - down.getCenterY()) < SVGConfig.MAX_SYMMETRY_OFFSET) &&
                   SVGElementComparer.areMirrored(up, down)) {
                        hasMirroredElem = true;
                        repository.getContext().addRelation(up, down, ElementRelationType.HMS, level);
                        break;
                }
            }
            if (!hasMirroredElem)
                res = false;
        }
        return res;
    }
    
    private boolean detectCopiedSymmetry(List<SVGElement> upper, List<SVGElement> lower, int level) {
        boolean res = true;
        
        for (int i=0; i<upper.size(); i++) {
            SVGElement up = upper.get(i);
            boolean foundEquivalent = false;
            for (int j=0; !foundEquivalent && j<lower.size(); j++) {
                SVGElement down = lower.get(j);
                //X must be equal to an element on the other half of the drawing
                //Y distance to the upper limit of the half of the drawing most be the same
                if ((Math.abs(up.getCenterX() - down.getCenterX()) <= SVGConfig.MAX_SYMMETRY_OFFSET) && 
                    (Math.abs(up.getCenterY() + centerH - down.getCenterY()) <= SVGConfig.MAX_SYMMETRY_OFFSET) &&
                    SVGElementComparer.areEquivalent(up, down)) {
                   foundEquivalent = true;
                   repository.getContext().addRelation(up, down, ElementRelationType.HTS, level);
                }
            }
            res &= foundEquivalent;
        }

        return res;
    }
    
    @Override
    public String getDescription() {
        String desc = "Horizontal Analysis:\n";
        int level = 1;
        if (results != null) {
            for (SymmetryResult result : results) {
                desc += "Level " + level + "\n" + result.getDescription() + "\n";
                level++;
            }
        }
        
        return desc;
    }
    
    /**
     * Obtains the results of the horizontal symmetry analysis to each layer
     * @return 
     */
    @Override
    public List<SymmetryResult> getResults() {
        return results;
    }
}