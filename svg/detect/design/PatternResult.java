package svg.detect.design;

import java.util.*;
import svg.core.SVGElement;
import svg.elems.*;

/**
 * Class to store the pattern analysis results
 * @author Ivan Guerrero
 */
public class PatternResult {
    private static final int PATTERN_OFFSET = 20;
    private Map<SVGUnitPattern, List<SVGElement>> results;
    private FlowResult flowResult;
    private int level;
    
    public PatternResult() {
        results = new HashMap<>();
        flowResult = new FlowResult();
    }
    
    public SVGUnitPattern detectPattern(SVGElement element) {
        SVGUnitPattern pattern;
        int cx, cy;
        SVGUnit unit = (SVGUnit)element;
        Iterator<SVGElement> iterator = unit.getElements().iterator();
        SVGElement first = iterator.next();
        cx = first.getCenterX();
        cy = first.getCenterY();
        boolean isHorizontal, isVertical;
        isHorizontal = true;
        isVertical = true;
        
        while (iterator.hasNext()) {
            SVGElement elem = iterator.next();
            if (Math.abs(elem.getCenterX() - cx ) > PATTERN_OFFSET)
                isVertical = false;
            if (Math.abs(elem.getCenterY() - cy ) > PATTERN_OFFSET)
                isHorizontal = false;
        }
        if (isVertical)
            pattern = SVGUnitPattern.VerticalPattern;
        else if (isHorizontal)
            pattern = SVGUnitPattern.HorizontalPattern;
        else if (detectDiagonalPattern(element))
            pattern = SVGUnitPattern.DiagonalPattern;
        else
            pattern = SVGUnitPattern.NonLinearPattern;
        
        addPatternToResult(pattern, element);
        
        return pattern;
    }

    private void addPatternToResult(SVGUnitPattern pattern, SVGElement element) {
        flowResult.analyzeFlow(pattern, (SVGUnit)element);
        
        if (results.containsKey(pattern)) {
            List<SVGElement> list = results.get(pattern);
            list.add(element);
        }
        else {
            List<SVGElement> list = new ArrayList<>();
            list.add(element);
            results.put(pattern, list);
        }
    }
    
    public Map<SVGUnitPattern, List<SVGElement>> getResults() {
        return results;
    }
    
    public String getDescription() {
        String desc = "Patterns in level " + getLevel() + "\n";
        for (SVGUnitPattern pattern : results.keySet()) {
            List<SVGElement> elems = results.get(pattern);
            desc += "\t" + pattern.name() + " " + describeElements(elems) + "\n";
        }
        
        return desc;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    private String describeElements(List<SVGElement> list) {
        String desc = "[";
        for (SVGElement elem : list) {
            desc += elem.getID() + " ";
        }
        desc = desc.trim();
        desc += "]";
        
        return desc;
    }
    
    public FlowResult getFlowResult() {
        return flowResult;
    }

    private boolean detectDiagonalPattern(SVGElement element) {
        return element.getShape() == ElementShape.NEGATIVE_TREND ||
               element.getShape() == ElementShape.POSITIVE_TREND;
    }
}