package svg.detect.design;

import java.util.*;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.IDetector;
import svg.elems.SVGUnit;

/**
 * Class to count the number of rhythms detected on each level of a drawing
 * @author Ivan Guerrero
 */
public class RhythmDetector implements IDetector{
    private List<RhythmResult> results;
    private SVGRepository repository;
    
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        this.repository = repository;
        results = new ArrayList<>();
        
        for (int i=1; i<elements.size(); i++) {
            RhythmResult res = detectRhythm(elements.get(i), i);
            results.add(res);
        }
    }

    private RhythmResult detectRhythm(List<SVGElement> elements, int level) {
        RhythmResult result = new RhythmResult();
        result.level = level;
        
        for (SVGElement elem : elements) {
            SVGUnit unit = (SVGUnit)elem;
            result.addPattern(unit.getReferenceDistance());
            result.addPatterns(unit.getRhythmList());
        }
        
        return result;
    }
    
    @Override
    public String getDescription() {
        String desc = "Rhythms: \n";
        int level = 1;
        if (results != null) {
            for (RhythmResult result : results) {
                desc += result + "\n";
                level++;
            }
        }        
        return desc;
    }
    
    public List<RhythmResult> getResults() {
        return results;
    }

}
