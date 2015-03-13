package ivangro.utils;

import algorithms.FastConvexHull;
import java.awt.Point;
import java.util.*;
import svg.core.SVGElement;
import svg.elems.SVGUnit;

/**
 * Class to determine the area or the convex skull of a polygon
 * @author Ivan Guerrero
 */
public class PolygonCalculator {
    public static double calculateArea(SVGUnit unit) {
        double area = 0;
        int n;
        List<Point> hull = calculateConvexSkull(unit);
        n = hull.size();
        for (int i=0; i<n; i++) {
            Point p = hull.get(i);
            Point p2 = hull.get((i+1) % n);
            area += p.x * p2.y - p.y * p2.x;
        }
        area /= 2;
        return area;
    }
    
    public static List<Point> calculateConvexSkull(SVGUnit unit) {
        FastConvexHull convexSkull = new FastConvexHull();
        ArrayList<Point> points = new ArrayList<>();
        for (SVGElement elem : unit.getElements()) {
            points.add(new Point(elem.getCenterX(), elem.getCenterY()));
        }
        
        ArrayList<Point> hull = convexSkull.execute(points);
        
        return hull;
    }
}
