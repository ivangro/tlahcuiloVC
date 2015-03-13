package svg.detect.utils;

import java.util.Comparator;
import svg.core.SVGElement;

/**
 * Compares the distance between the centers of SVGUnits
 * @author Ivan Guerrero
 */
public class SVGUnitComparator implements Comparator {
    private boolean compareX;
    
    /**
     * If compareX = TRUE the units are ordered by its X coordinate
     * @param compareX 
     */
    public SVGUnitComparator(boolean compareX) {
        this.compareX = compareX;
    }
    
    public int compare(SVGElement unit1, SVGElement unit2) {
        int res;
        if (compareX) {
            res = unit1.getCenterX() - unit2.getCenterX();
        }
        else {
            res = unit1.getCenterY() - unit2.getCenterY();
        }
        
        return res;
    }

    @Override
    public int compare(Object t, Object t1) {
        if (t instanceof SVGElement)
            return compare((SVGElement)t, (SVGElement)t1);
        else
            return -1;
    }

}