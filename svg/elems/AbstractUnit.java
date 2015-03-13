package svg.elems;

import ivangro.utils.CurveRegression;
import java.util.*;
import svg.actions.design.UnitFactory;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Abstract class to be implemented by all the classes that represent groups of elements
 * @author Ivan Guerrero
 */
public abstract class AbstractUnit extends SVGElement {
    private int cx, cy;
    /** The set of elements detected inside the unit */
    protected Set<SVGElement> elements;
    /** A rhythm is a distance between two elements inside the unit */
    protected List<Double> rhythms;
    /** A curve is represented by a collection of coefficients of the polynomial representing the shape */
    private List<Double> curve;
    /** Represents the distance between two elements inside the unit */
    protected double referenceDistance;
    /** A pattern is a sequence of elements with the same shape inside the unit */
    protected Map<SVGUnitPattern, Integer> patterns;
    /** A flow is detected inside a pattern with multiple element sizes */
    protected Map<SVGUnitFlow, Integer> flows;

    public AbstractUnit() {
        elements = new HashSet<>();
        rhythms = new ArrayList<>();
        patterns = new HashMap<>();
        flows = new HashMap<>();
        cx = 0;
        cy = 0;
        super.setDistanceType(ElementDistanceType.SINGLE);
    }

    /**
     * Adds the given element to the current unit
     * @param element The new element of the unit
     */
    public void addElement(SVGElement element) {
        if (getElements().isEmpty())
            setImageID(element.getImageID());
        setSize(element.getSize());
        getElements().add(element);
        determineCenter();
        
        if (element instanceof SVGUnit) {
            SVGUnit unit = (SVGUnit) element;
            for (Double p : unit.rhythms) {
                addPattern(p);
            }
        }
    }
    
    /**
     * Method to calculate the geometrical centre of the group depending on the position of its conforming elements 
     */
    public void determineCenter() {
        cx = 0;
        cy = 0;
        int elems = elements.size();
        if (elems > 0) {
            for (SVGElement elem : elements) {
                if (elem != null) {
                    cx += elem.getCenterX();
                    cy += elem.getCenterY();
                }
                else
                    elems--;
            }
            cx = cx / elems;
            cy = cy / elems;
            calculateCurve();
        }
    }
    
    /**
     * Calculates the coefficients of the nearset line to the points of the unit
     */
    public void calculateCurve() {
        curve = CurveRegression.getInstance().getCoeffs(this);
    }
    
    /**
     * Obtains the coefficients of the nearset line to the points of the unit
     */
    public List<Double> getCurve() {
        return curve;
    }
    
    private boolean containsPattern(double pattern) {
        boolean res = false;
        for (Double p : rhythms) {
            if (Math.abs(p - pattern) < SVGConfig.MIN_PATTERN_DISTANCE) {
                res = true;
                break;
            }
        }
        return res;
    }

    public void addPattern(double pattern) {
        if (!containsPattern(pattern) && pattern > SVGConfig.MIN_PATTERN_DISTANCE) {
            rhythms.add(pattern);
        }
    }

    /**
     * Determines if the unit contains the given element
     * @param elem The element to be looked for inside the unit
     * @return TRUE if the unit contains the element
     */
    public boolean containsElement(SVGElement elem) {
        return elements.contains(elem);
    }

    @Override
    public int getCenterX() {
        return cx;
    }

    @Override
    public int getCenterY() {
        return cy;
    }

    /**
     * @return the elements
     */
    public Set<SVGElement> getElements() {
        return elements;
    }

    public List<Double> getRhythmList() {
        return rhythms;
    }

    public double getReferenceDistance() {
        return referenceDistance;
    }

    @Override
    public int getRhythms() {
        return rhythms.size();
    }

    @Override
    public List<SVGElement> getSimpleElements() {
        List<SVGElement> list = new ArrayList<>();
        for (SVGElement elem : elements) {
            List<SVGElement> singleList = elem.getSimpleElements();
            list.addAll(singleList);
        }
        return list;
    }
    
    /**
     * Method to set the center of the group. When this method is called, all the elements inside
     * the group are moved to preserve the center of the group in the given position.<br>
     * IMPORTANT: The actions performed to the elements are not stored
     * @param x The new x coordinate
     * @param y The new y coordinate
     */
    @Override
    public void setCenter(int x, int y) {
        int offsetX;
        int offsetY;
        offsetX = x - getCenterX();
        offsetY = y - getCenterY();
        for (SVGElement elem : elements) {
            elem.setCenter(elem.getCenterX() + offsetX, elem.getCenterY() + offsetY);
        }
        determineCenter();
    }

    public void setReferenceDistance(double referenceDistance) {
        this.referenceDistance = referenceDistance;
        addPattern(referenceDistance);
        super.setDistanceType(UnitFactory.getInstance().getDistanceType(referenceDistance, getSize()));
    }
    
    public void addPattern(SVGUnitPattern pattern) {
        if (patterns.containsKey(pattern))
            patterns.put(pattern, patterns.get(pattern) + 1);
        else
            patterns.put(pattern, 1);
    }
    
    public void addFlow(SVGUnitFlow flow) {
        if (flows.containsKey(flow))
            flows.put(flow, flows.get(flow) + 1);
        else
            flows.put(flow, 1);
    }
    
    public Map<SVGUnitPattern, Integer> getPatterns() {
        return patterns;
    }
    
    public Map<SVGUnitFlow, Integer> getFlows() {
        return flows;
    }
    
    @Override
    public void setSize(int size) {
        if (elements.isEmpty())
            super.setSize(size);
        else {
            super.setSize((getSize() != size) ? 4 : size);
        }
    }
}