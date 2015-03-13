package svg.detect.design;

import java.util.*;
import svg.core.SVGElement;

/**
 * Class to store elements detected to be in a horizontal or vertical line
 * @author Ivan Guerrero
 */
public class InlineResult {
    int level;
    private List<Set<SVGElement>> horizontal, vertical;
    
    public InlineResult() {
        horizontal = new ArrayList<>();
        vertical = new ArrayList<>();
    }
    
    void addHorizontalRelation(SVGElement elem1, SVGElement elem2) {
        Set<SVGElement> line1 = determineLine(elem1, horizontal);
        Set<SVGElement> line2 = determineLine(elem2, horizontal);
        mergeLines(line1, line2, horizontal);
    }
    
    void addVerticalRelation(SVGElement elem1, SVGElement elem2) {
        Set<SVGElement> line1 = determineLine(elem1, vertical);
        Set<SVGElement> line2 = determineLine(elem2, vertical);
        mergeLines(line1, line2, vertical);
    }

    /**
     * If an existing line contains the given element, returns it, Otherwise, it creates a new line with the given element
     * @param elem
     * @return 
     */
    private Set<SVGElement> determineLine(SVGElement elem, List<Set<SVGElement>> list) {
        for (Set<SVGElement> line : list) {
            if (line.contains(elem))
                return line;
        }
        Set<SVGElement> line = new HashSet<>();
        line.add(elem);
        list.add(line);
        
        return line;
    }

    /**
     * If the given lines are different, it adds the elements from the second line to the first, 
     * and removes the second line from the lines' set
     * @param line1
     * @param line2 
     */
    private void mergeLines(Set<SVGElement> line1, Set<SVGElement> line2, List<Set<SVGElement>> list) {
        if (!line1.equals(line2)) {
            line1.addAll(line2);
            list.remove(line2);
        }
    }
    
    public List<Set<SVGElement>> getHorizontalLines() {
        return horizontal;
    }
    
    public List<Set<SVGElement>> getVerticalLines() {
        return vertical;
    }
    
    /**
     * Obtains a list with the relations found between each pair of elements
     * @return 
     */
    List<SVGElementPair> getRelations() {
        List<SVGElementPair> pairs = new ArrayList<>();
        
        //If two elements are on the same horizontal line, and there are no elements between them, add a relation
        for (Set<SVGElement> line : horizontal) {
            SVGElement[] array = line.toArray(new SVGElement[0]);
            SVGElementHComparable[] hArray = new SVGElementHComparable[array.length];
            for (int i=0; i<array.length; i++) {
                hArray[i] = new SVGElementHComparable(array[i]);
            }
            Arrays.sort(hArray);
            for (int i=1; i<hArray.length; i++) {
                pairs.add(new SVGElementPair(hArray[i].element, hArray[i-1].element, true));
            }
        }
        
        //If two elements are on the same vertical line, and there are no elements between them, add a relation
        for (Set<SVGElement> line : vertical) {
            SVGElement[] array = line.toArray(new SVGElement[0]);
            SVGElementVComparable[] vArray = new SVGElementVComparable[array.length];
            for (int i=0; i<array.length; i++) {
                vArray[i] = new SVGElementVComparable(array[i]);
            }
            Arrays.sort(vArray);
            for (int i=1; i<vArray.length; i++) {
                pairs.add(new SVGElementPair(vArray[i].element, vArray[i-1].element, false));
            }
        }
        
        return pairs;
    }
    
    public String getLineDescriptions() {
        String desc = "\tHorizontal\n";
        for (Set<SVGElement> line : horizontal) {
            desc += "\t\t" + line + "\n";
        }
        
        desc += "\tVertical\n";
        for (Set<SVGElement> line : vertical) {
            desc += "\t\t" + line + "\n";
        }
        
        return desc;
    }
}

class SVGElementPair {
    SVGElement performer, receiver;
    boolean isHorizontal;
    
    public SVGElementPair(SVGElement performer, SVGElement receiver, boolean isHorizontal) {
        this.performer = performer;
        this.receiver = receiver;
        this.isHorizontal = isHorizontal;
    }    
}

/**
 * Class to compare two elements found in one horizontal line
 * @author Ivan Guerrero
 */
class SVGElementHComparable implements Comparable {
    SVGElement element;
    
    SVGElementHComparable(SVGElement element) {
        this.element = element;
    }
    
    @Override
    public int compareTo(Object t) {
        if (t instanceof SVGElementHComparable) {
            SVGElementHComparable elem = (SVGElementHComparable)t;
            return (element.getCenterX() < elem.element.getCenterX()) ? -1 : 
                   (element.getCenterX() > elem.element.getCenterX()) ? 1 : 0;
        }
        else
            return -1;
    }    
}

/**
 * Class to compare two elements found in one vertical line
 * @author Ivan Guerrero
 */
class SVGElementVComparable implements Comparable {
    SVGElement element;
    
    SVGElementVComparable(SVGElement element) {
        this.element = element;
    }
    
    @Override
    public int compareTo(Object t) {
        if (t instanceof SVGElementVComparable) {
            SVGElementVComparable elem = (SVGElementVComparable)t;
            return (element.getCenterY() < elem.element.getCenterY()) ? -1 : 
                   (element.getCenterY() > elem.element.getCenterY()) ? 1 : 0;
        }
        else
            return -1;
    }    
}