package svg.detect.design;

import java.util.*;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.IDetector;
import svg.elems.ElementShape;
import svg.elems.SVGUnit;
import svg.elems.SVGUnitPattern;

/**
 * Class to look for patterns inside each created unit
 * @author Ivan Guerrero
 */
public class PatternDetector implements IDetector {
    private String desc;
    private SVGRepository repository;
    private List<PatternResult> results;

    public PatternDetector() {
        results = new ArrayList<>();
    }
    
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        this.repository = repository;
        results = new ArrayList<>();
        
        //The elements in level 0 are single elements, thus no pattern can emerge by analyzing them
        //Each element in level 1 consist of single elements, the only pattern is the unit shape
        for (int i=1; i<elements.size(); i++) {
            PatternResult res = detectPatterns(elements.get(i), i);
            results.add(res);
        }
    }

    @Override
    public String getDescription() {
        desc = "Patterns detected\n";
        for (PatternResult result : results) {
            desc += result.getDescription();
        }
        desc += "\nFlows detected\n";
        for (PatternResult result : results) {
            desc += result.getFlowResult().getDescription();
        }
        return desc;
    }

    private PatternResult detectPatterns(List<SVGElement> elements, int level) {
        PatternResult result = new PatternResult();
        result.setLevel(level);
        
        for (SVGElement elem : elements) {
            SVGUnit unit = (SVGUnit)elem;
            ElementShape shape = ElementShape.UNDEFINED;
            for (SVGElement e : unit.getElements()) {
                //Obtain the shape of every element inside the unit
                //If the shape is equal on every group, a pattern is detected
                if (shape == ElementShape.UNDEFINED) {
                    shape = e.getShape();
                }
                else if (shape != e.getShape()) {
                    shape = ElementShape.UNDEFINED;
                    break;
                }
            }
            if (shape != ElementShape.UNDEFINED) {
                SVGUnitPattern pattern = result.detectPattern(elem);
                unit.addPattern(pattern);
            }
        }
        
        return result;
    }
}