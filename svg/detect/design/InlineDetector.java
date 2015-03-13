package svg.detect.design;

import java.util.ArrayList;
import java.util.List;
import svg.context.ElementRelationType;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.detect.IDetector;
import svg.elems.SVGUnit;

/**
 * Class to identify elements or groups of elements located colinearly (horizontal or vertical)
 * @author Ivan Guerrero
 */
public class InlineDetector implements IDetector {
    private String desc;
    private List<InlineResult> results;
    private SVGRepository repository;
    
    public InlineDetector() {
        results = new ArrayList<>();
    }
    
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        this.repository = repository;
        results = new ArrayList<>();
        
        for (int i=1; i<elements.size(); i++) {
            InlineResult res = detectInlineElements(elements.get(i), i);
            results.add(res);
        }
    }

    private InlineResult detectInlineElements(List<SVGElement> elements, int level) {
        InlineResult result = new InlineResult();
        result.level = level;
        
        for (int i=0; i<elements.size(); i++) {
            SVGUnit unit1 = (SVGUnit)elements.get(i);
            for (int j=0; j<elements.size(); j++) {
                if (i != j) {
                    SVGUnit unit2 = (SVGUnit)elements.get(j);
                    //Add an element to a horizontal line
                    if (Math.abs(unit1.getCenterX() - unit2.getCenterX()) <= SVGConfig.INLINE_OFFSET) {
                        result.addVerticalRelation(unit1, unit2);
                    }
                    //Add an element to a vertical line
                    if (Math.abs(unit1.getCenterY() - unit2.getCenterY()) <= SVGConfig.INLINE_OFFSET) {
                        result.addHorizontalRelation(unit1, unit2);
                    }
                }
            }
        }
        
        for (SVGElementPair pair : result.getRelations()) {
            if (pair.isHorizontal) {
                repository.getContext().addRelation(pair.performer, pair.receiver, ElementRelationType.HL, level);
            } 
            else {
                repository.getContext().addRelation(pair.performer, pair.receiver, ElementRelationType.VL, level);
            }
        }
        
        return result;
    }
    
    @Override
    public String getDescription() {
        desc = "Inline results:\n";
        for (InlineResult result : results) {
            desc += "Level " + result.level + "\n";
            desc += result.getLineDescriptions();
        }
        
        return desc;
    }
}