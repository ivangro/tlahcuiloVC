package svg.detect.design;

import java.util.*;
import java.util.regex.*;
import svg.core.SVGElement;
import svg.elems.*;

/**
 * Class to determine the type of flow inside a unit.
 * @author Ivan Guerrero
 */
public class FlowResult {
    private String desc;
    private static Pattern pAscending, pDesceding, pMixed, pNoFlow;
    
    static {
        pAscending = Pattern.compile("1?21?");
        pDesceding = Pattern.compile("1?01?");
        pMixed = Pattern.compile("(1?21?01?)|(1?01?21?)");
        pNoFlow = Pattern.compile("1");
    }
    
    public FlowResult() {
        desc = "";
    }
    
    public void analyzeFlow(SVGUnitPattern pattern, SVGUnit unit) {
        //Depending on the type of pattern, sort the elements inside the group
        SVGElement[] sorted = sortElements(pattern, unit.getElements());
        int[] increment = new int[sorted.length-1];
        List<Integer> flows = new ArrayList<>();
        //Compare the size of the sorted elements
        for (int i=1; i<sorted.length; i++) {
            increment[i-1] = sorted[i].getSize() - sorted[i-1].getSize();
            increment[i-1] = (increment[i-1] > 0) ? 1 : (increment[i-1] < 0) ? -1 : 0;
            if (flows.isEmpty() || flows.get(flows.size()-1) != increment[i-1])
                flows.add(increment[i-1]);
        }
        
        String flowStr = "";
        for (Integer i : flows) {
            flowStr += (i+1);
        }
        //If the elements are of the same size, there's no flow
        //If the elements are of the same size, the flows list has only one zero
        Matcher matcher = pNoFlow.matcher(flowStr);
        if (matcher.matches())
            unit.addFlow(SVGUnitFlow.UndefinedFlow);
        //If the size increments, the flow is ascending
            //If there's one ascending element, the flows has  0?10?
        matcher = pAscending.matcher(flowStr);
        if (matcher.matches())
            unit.addFlow(SVGUnitFlow.AscendingFlow);
        //If the size decrements, the flow is descending
            //If there's one ascending element, the flows has 0?-10?
        matcher = pDesceding.matcher(flowStr);
        if (matcher.matches())
            unit.addFlow(SVGUnitFlow.DescendingFlow);
        //If the size increments and decrements, the flow is mixed
            //If there's a mixture, then the flow has 0?10?-10? or 0?-10?10?
        matcher = pMixed.matcher(flowStr);
        if (matcher.matches())
            unit.addFlow(SVGUnitFlow.MixedFlow);
        //Otherwise, the flow is multiple
        if (unit.getFlows().isEmpty())
            unit.addFlow(SVGUnitFlow.MultipleFlow);
        
        desc += unit.getID() + " " + unit.getFlows();
    }

    private SVGElement[] sortElements(SVGUnitPattern pattern, Set<SVGElement> elements) {
        SVGElement[] sorted = elements.toArray(new SVGElement[0]);
        switch (pattern) {
            case HorizontalPattern:
                for (int i=0; i<sorted.length; i++) {
                    for (int j=i; j<sorted.length; j++) {
                        if (sorted[i].getCenterX() < sorted[j].getCenterX()) {
                            SVGElement tmp = sorted[i];
                            sorted[i] = sorted[j];
                            sorted[j] = tmp;
                        }
                    }
                }
                break;
            case VerticalPattern:
                for (int i=0; i<sorted.length; i++) {
                    for (int j=i; j<sorted.length; j++) {
                        if (sorted[i].getCenterY() < sorted[j].getCenterY()) {
                            SVGElement tmp = sorted[i];
                            sorted[i] = sorted[j];
                            sorted[j] = tmp;
                        }
                    }
                }
                break;
        }
        
        return sorted;
    }
    
    public String getDescription() {
        return desc;
    }
}