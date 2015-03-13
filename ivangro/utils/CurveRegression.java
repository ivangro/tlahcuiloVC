package ivangro.utils;

import java.util.*;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.elems.AbstractUnit;
import svg.elems.ElementShape;

/**
 * Class to determine the coefficients of a polynomial which includes the given points.
 * @author Ivan Guerrero
 */
public class CurveRegression {
    private static CurveRegression instance = new CurveRegression();
    /** Determines the maximum error. Above this value, the curve is detected as non-linear */
    private double MAX_LINEAR_ERROR = 5;
    private double MAX_DISTANCE_TO_LINE = 10;
    
    private CurveRegression() {}
    
    public static CurveRegression getInstance() {
        return instance;
    }
    
    /**
     * Obtains the coefficients of the line to which all the given points are closer to
     * If the x coordinates of the points are all similar (with standard deviation < MIN_DEVIATION)
     *  the points are considered to form a vertical line. <br>
     * Important: A vertical line has coeffs = Double.MAX_VALUE
     * @param unit 
     * @return 
     */
    public List<Double> getCoeffs(AbstractUnit unit) {
        ArrayList<Pair> points = new ArrayList<>();
        double[] xList = new double[unit.getElements().size()];
        int i=0;
        
        for (SVGElement elem : unit.getElements()) {
            points.add(new Pair(elem.getCenterX(), elem.getCenterY()));
            xList[i] = elem.getCenterX();
            i++;
        }
        
        double deviation = Statistics.standardDeviation(xList);
        List<Double> coeffs = new ArrayList<>();
        
        //If the deviation in lower than the minimum, the unit is considered as a vertical line
        if (unit.getElements().size() < 2)
            unit.setShape(ElementShape.SINGLE_ELEMENT);
        else if (deviation < SVGConfig.VERTICAL_SHAPE_DEVIATION) {
            coeffs.add(Double.MAX_VALUE);
            coeffs.add(Double.MAX_VALUE);
            unit.setShape(ElementShape.VERTICAL);
        }
        else {
            coeffs = MatrixFunctions.compute_coefficients(points, 1);
            double standardError = MatrixFunctions.std_error(points, (ArrayList<Double>)coeffs);
            //System.out.println("Group: " + unit + " (" + unit.getSimpleElements().size() + ") Error " + standardError + " Coeffs: " + coeffs);
            if (standardError < MAX_LINEAR_ERROR * unit.getSimpleElements().size() && 
               (getMaxDistanceToLine(coeffs, unit.getSimpleElements()) < MAX_DISTANCE_TO_LINE)) {
                if (coeffs.get(1) < -1.0/3)
                    unit.setShape(ElementShape.NEGATIVE_TREND);
                else if (coeffs.get(1) > 1.0/3)
                    unit.setShape(ElementShape.POSITIVE_TREND);
                else
                    unit.setShape(ElementShape.HORIZONTAL);
            }
            else {
                unit.setShape(ElementShape.MULTIPLE);
            }
        }
        return coeffs;
    }

    /**
     * The distance from a point to a line is calculated by the formula Ax0+By0+C / sqrt(A^2+B^2)
     * @param coeffs The coefficients of a line segment in the form (b, m). Where a line is represented as y=mx+b
     * @param elements The points from the group
     * @return The maximum distance from any point to the line represented by the coefficients.
     */
    private double getMaxDistanceToLine(List<Double> coeffs, List<SVGElement> elements) {
        double maxDist = 0;
        double HORIZONTAL_OFFSET = 0.5;
        double A, B, C;
        A = coeffs.get(1);
        B = -1;
        C = coeffs.get(0);
        
        if (Math.abs(C) > HORIZONTAL_OFFSET && Math.abs(A) > HORIZONTAL_OFFSET) {
            for (SVGElement point : elements) {
                double dist = A * point.getCenterX() + 
                              B * point.getCenterY() + C;
                dist /= Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
                if (Math.abs(dist) > maxDist)
                    maxDist = Math.abs(dist);
            }
        }
        else {
            //If it's an horizontal line
            for (SVGElement point : elements) {
                double dist = point.getCenterY() - C;
                if (Math.abs(dist) > maxDist)
                    maxDist = dist;
            }
        }
        return maxDist;
    }
}
