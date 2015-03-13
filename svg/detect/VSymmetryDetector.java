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
 * Class to determine if a drawing is vertically symmetric
 * @author Ivan Guerrero
 */
public class VSymmetryDetector implements ISymmetryDetector {
    private int centerV;
    private List<SymmetryResult> results;
    private SVGRepository repository;
    
    public VSymmetryDetector() {
        centerV = SVGConfig.CANVAS_WIDTH / 2;
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
     * Determines if the given drawing is symmetric respect to the vertical axis
     * @param repository 
     */
    public SymmetryResult detectSymmetry(List<SVGElement> units, int level) {
        SymmetryResult result = new SymmetryResult();
        List<SVGElement> left, right, middle;
        left = new SortedList<>(new SVGUnitComparator(false));
        right = new SortedList<>(new SVGUnitComparator(false));
        middle = new SortedList<>(new SVGUnitComparator(false));
        
        //Split the units in upper and lower units
        for (SVGElement unit : units) {
            if (unit.getCenterX() > centerV + SVGConfig.MAX_SYMMETRY_OFFSET)
                right.add(unit);
            else if (unit.getCenterX() < centerV - SVGConfig.MAX_SYMMETRY_OFFSET)
                left.add(unit);
            else
                middle.add(unit);
        }
        
        result.setFirstMass(getMass(left));
        result.setSecondMass(getMass(right));
        new BalanceDetector(repository).detectBalance(left, right, BalanceDetector.BalanceType.Vertical, level);
        
        //Detect if every unit in the left part has a correspondence in the right part, the drawing is V-symmetric
        result.setSymmetric(detectCopiedSymmetry(left, right, level));
        result.setMirrored(detectMirroredSymmetry(left, right, level));
        result.setElemsInTheMiddle(middle.size());
        
        for (SVGElement elem : middle) {
            repository.getContext().addRelation(elem, elem, ElementRelationType.VMS, level);
            //repository.getContext().addRelation(elem, elem, ElementRelationType.VTS, level);
            repository.getContext().addRelation(elem, elem, ElementRelationType.VB, level);
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
    
    private boolean detectMirroredSymmetry(List<SVGElement> left, List<SVGElement> right, int level) {
        boolean res;
        res = !(left.isEmpty() && !right.isEmpty());
        
        for (int i=0; i<left.size(); i++) {
            SVGElement l = left.get(i);
            boolean hasMirroredElem = false;
            for (int j=0; j<right.size(); j++) {
                SVGElement r = right.get(j);
                //Y must be equal and distX to center must be equal
                if ((Math.abs(l.getCenterY() - r.getCenterY()) < SVGConfig.MAX_SYMMETRY_OFFSET) && 
                   //(Math.abs(SVGConfig.CANVAS_WIDTH - l.getCenterX() - r.getCenterX()) < SVGConfig.MAX_SYMMETRY_OFFSET) &&
                   SVGElementComparer.areMirrored(l, r)) {
                        hasMirroredElem = true;
                        repository.getContext().addRelation(l, r, ElementRelationType.VMS, level);
                        break;
                }
            }
            if (!hasMirroredElem)
                res = false;
        }
        return res;
    }
    
    private boolean detectCopiedSymmetry(List<SVGElement> left, List<SVGElement> right, int level) {
        boolean res = true;
        
        for (int i=0; i<left.size(); i++) {
            SVGElement l = left.get(i);
            boolean foundEquivalent = false;
            for (int j=0; !foundEquivalent && j<right.size(); j++) {
                SVGElement r = right.get(j);
                //X must be equal to any element on the other half of the drawing
                if ((Math.abs(l.getCenterY() - r.getCenterY()) <= SVGConfig.MAX_SYMMETRY_OFFSET) && 
                    (Math.abs(l.getCenterX() + centerV - r.getCenterX()) <= SVGConfig.MAX_SYMMETRY_OFFSET) &&
                    SVGElementComparer.areEquivalent(l, r)) {
                   foundEquivalent = true;
                   repository.getContext().addRelation(l, r, ElementRelationType.VTS, level);
                }
            }
            res &= foundEquivalent;
        }

        return res;
    }
    
    @Override
    public String getDescription() {
        String desc = "Vertical Analysis:\n";
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
     * Obtains the results of the vertical symmetry analysis to each layer
     * @return 
     */
    @Override
    public List<SymmetryResult> getResults() {
        return results;
    }
}