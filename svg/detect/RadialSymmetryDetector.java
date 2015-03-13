package svg.detect;

import java.util.ArrayList;
import java.util.List;
import svg.context.ElementRelationType;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.SVGElementComparer;

/**
 *
 * @author Ivan Guerrero
 */
public class RadialSymmetryDetector implements ISymmetryDetector {
    private int centerH, centerV;
    private List<SymmetryResult> results;
    private final int MAX_DISTANCE_CENTER = 20;
    private SVGRepository repository;
    
    public RadialSymmetryDetector() {
        centerV = SVGConfig.CANVAS_HEIGHT / 2;
        centerH = SVGConfig.CANVAS_WIDTH / 2;
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
        //Determines the mass on every half of the canvas to detect balance
        double[] mass = getMass(units);
        result.setFirstMass(mass[0]);
        result.setSecondMass(mass[1]);
        
        //Detect if every unit in the upper part has a correspondence in the lower part, the drawing is H-symmetric
        result.setMirrored(detectMirroredSymmetry(units, units, level));
        return result;
    }
    
    private boolean detectMirroredSymmetry(List<SVGElement> upper, List<SVGElement> lower, int level) {
        boolean res, mirroredElem;
        int newX, newY;
        double dist;
        res = !(upper.isEmpty() && !lower.isEmpty());
        
        for (int i=0; i<upper.size(); i++) {
            SVGElement up = upper.get(i);
            mirroredElem = false;
            newX = SVGConfig.CANVAS_WIDTH - up.getCenterX();
            newY = SVGConfig.CANVAS_HEIGHT - up.getCenterY();
            dist = Math.sqrt(Math.pow(up.getCenterX() - newX, 2) + Math.pow(up.getCenterY() - newY, 2));
            //If the element is in the center of the canvas, has radial symmetry with itself
            if (dist < 5) {
                repository.getContext().addRelation(up, up, ElementRelationType.RS, level);
            }
            
            for (int j=0; j<lower.size(); j++) {
                if (j != i) {
                    SVGElement down = lower.get(j);
                    dist = Math.sqrt(Math.pow(down.getCenterX() - newX, 2) + Math.pow(down.getCenterY() - newY, 2));
                    if (dist < MAX_DISTANCE_CENTER && SVGElementComparer.areEquivalent(up, down)) {
                        mirroredElem = true;
                        repository.getContext().addRelation(up, down, ElementRelationType.RS, level);
                        j = lower.size();
                    }
                }
            }
            res &= mirroredElem;
        }
        return res;
    }

    @Override
    public String getDescription() {
        String desc = "Radial Analysis:\n";
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
     * Obtains the sum of the mass of the elements with distance to the center
     * lower than the half of the canvas
     * @param units
     * @return 
     */
    private double[] getMass(List<SVGElement> units) {
        double mass[] = {0,0};
        for (SVGElement elem : units) {
            double dist = Math.sqrt(Math.pow(elem.getCenterX() - centerH, 2) + 
                                    Math.pow(elem.getCenterY() - centerV, 2));
            if (dist < centerH / 2)
                mass[0] += elem.getArea();
            else
                mass[1] += elem.getArea();
        }
        return mass;
    }
    
    /**
     * Obtains the results of the radial symmetry analysis to each layer
     * @return 
     */
    @Override
    public List<SymmetryResult> getResults() {
        return results;
    }
}