package svg.elems;

import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Class to make comparissons between SVG elements
 * @author Ivan Guerrero
 */
public class SVGElementComparer {
    private static final double AREA_OFFSET = 5;
    private static final double DIST_OFFSET = 2;

    /**
     * Determines if the given unit is equivalent in area and in the alignment.<br>
     * Two elements are equivalent if have the same center and the same size
     * @param elem
     * @param elem2
     * @return 
     */
    public static boolean areEquivalent(SVGElement elem, SVGElement elem2) {
        boolean res;// = (elem.getCenterX() == elem2.getCenterX() && elem.getCenterY() == elem2.getCenterY());
        double area = elem.getArea();
        double area2 = elem2.getArea();
        
        res = Math.abs(area2 - area) <= AREA_OFFSET && elem.getShape() == elem2.getShape();
        
        return res;
    }
    
    /**
     * Two elements are mirrored if their distances to the center are the same,
     * share a common coordinate (x or y) and have mirrored shapes
     * @param elem
     * @param elem2
     * @return 
     */
    public static boolean areMirrored(SVGElement elem, SVGElement elem2) {
        boolean res;
        double area = elem.getArea();
        double area2 = elem2.getArea();
        
        res = Math.abs(area2 - area) <= AREA_OFFSET;
        
        if (res && (elem2.getCenterX() == elem.getCenterX() || elem.getCenterY() == elem2.getCenterY())) {
            double dist, dist2;
            dist = Math.sqrt(Math.pow(elem.getCenterX() - SVGConfig.CANVAS_WIDTH/2, 2) + 
                             Math.pow(elem.getCenterY() - SVGConfig.CANVAS_HEIGHT/2, 2));
            dist2 = Math.sqrt(Math.pow(elem2.getCenterX() - SVGConfig.CANVAS_WIDTH/2, 2) + 
                             Math.pow(elem2.getCenterY() - SVGConfig.CANVAS_HEIGHT/2, 2));
            res = (Math.abs(dist - dist2) <= DIST_OFFSET);
        }
        else 
            res = false;
        
        if (res) {
            switch(elem.getShape()) {
                case HORIZONTAL:
                    res = elem2.getShape() == ElementShape.HORIZONTAL;
                    break;
                case VERTICAL:
                    res = elem2.getShape() == ElementShape.VERTICAL;
                    break;
                case NEGATIVE_TREND:
                    res = elem2.getShape() == ElementShape.POSITIVE_TREND;
                    break;
                case POSITIVE_TREND:
                    res = elem2.getShape() == ElementShape.NEGATIVE_TREND;
                    break;
                case SINGLE_ELEMENT:
                    res = elem2.getShape() == ElementShape.SINGLE_ELEMENT;
                    break;
                case MULTIPLE:
                    res = elem2.getShape() == ElementShape.MULTIPLE;
                    break;
                default:
                    res = false;
            }
        }
        
        return res;
    }

    public static boolean areSimilar(SVGElement elem, SVGElement elem2) {
        return elem.getClass().equals(elem2.getClass());
    }
    
}
