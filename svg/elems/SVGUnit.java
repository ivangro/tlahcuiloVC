package svg.elems;

import ivangro.utils.PolygonCalculator;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.*;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Class to represent a set of SVG elements
 * @author Ivan Guerrero
 */
public class SVGUnit extends AbstractUnit {
    private String color;
    
    public SVGUnit() {
        color = SVGConfig.FORE_COLOR;
    }
    
    @Override
    public boolean containsPoint(int x, int y) {
        boolean res = false;
        for (SVGElement elem : getElements()) {
            if (elem.containsPoint(x, y)) {
                res = true;
                break;
            }
        }
        return res;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SVGUnit unit = new SVGUnit();
        for (SVGElement elem : this.getElements()) {
            unit.addElement((SVGElement)elem.clone());
        }
        
        return unit;
    }
    
    /**
     * Given a unit, merges all its elements with the current unit
     * @param unit THe unit to be merged
     */
    public void mergeUnit(SVGUnit unit) {
        for (SVGElement elem : unit.getElements()) {
            elements.add(elem);
        }
        determineCenter();
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(Set<SVGElement> elements) {
        this.elements = elements;
        determineCenter();
    }
    
    /**
     * Obtains the area of all the elements inside the unit
     * If the unit has:<br>
     * <ul>
     * <li>1 element: The area is the area of the element</li>
     * <li>2 elements: The area is the area of the rectangle formed between them
     * (dist. between them * height of an element) + area of one element <br>
     * NOTE: Only one element is considered because half of the area of each element 
     * is considered inside the rectangle</li>
     * <li>3+ elements: The area of the convex skull of the points + 
     * area of every single element</li>
     * @return The area of all the elements inside the unit
     */
    public double getArea(boolean includeWhiteSpace) {
        return (includeWhiteSpace) ? getAreaWithSpace() : getElemenentsArea();
    }
    
    
    /**
     * Obtains the sum of the area of every single element in the unit without
     * considering the white space between them
     */
    @Override
    public double getArea() {
        return getElemenentsArea();
    }
    
    private double getAreaWithSpace() {
        double area;
        
        switch (elements.size()) {
            case 0:
                area = 0;
                break;
            case 1:
                area = elements.iterator().next().getArea();
                break;
            case 2:
                SVGElement[] elems = elements.toArray(new SVGElement[0]);
                area = Math.sqrt(Math.pow(elems[0].getCenterX() - elems[1].getCenterX(), 2) +
                                 Math.pow(elems[0].getCenterY() - elems[1].getCenterY(), 2));
                area *= elems[0].getHeight();
                area += elements.iterator().next().getArea();
                break;
            default:
                area = PolygonCalculator.calculateArea(this);
                area += elements.iterator().next().getArea() * elements.size();
        }

        return area;
    }
    
    private double getElemenentsArea() {
        double area = 0;
        for (SVGElement elem : elements) {
            area += elem.getArea();
        }
        return area;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean res = true;
        if (obj instanceof SVGUnit) {
            SVGUnit anotherUnit = (SVGUnit)obj;
            if (anotherUnit.getElements().size() != elements.size())
                res = false;
            else {
                for (SVGElement elem : elements) {
                    if (!anotherUnit.getElements().contains(elem)) {
                        res = false;
                        break;
                    }
                }
            }
        }
        else
            res = false;
        
        return res;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.elements);
        return hash;
    }
    
    @Override
    public String toString() {
        String text = getID() + ": " + getShape().name().substring(0,1) + " " +
                      "(" + getCenterX() + ", " + getCenterY() + ")";
        return text;
    }
    
    public String toStringWithElements() {
        String text = getID() + ": " + getShape().name().substring(0,1) + " " +
                      "(" + getCenterX() + ", " + getCenterY() + ")\t" + 
                      new DecimalFormat("0.00").format(getArea()) + "\n" + 
                      "Rhythms: " + getRhythms() + "\t" + 
                      "Dist: " + new DecimalFormat("0.00").format(getReferenceDistance()) + "\n" +
                      "Dist type: " + super.getDistanceType().name() + "\n" + 
                      "Dimensions: " + getDimensions() + "\n";
        for (SVGElement elem : elements) {
            text += "\t" + elem.toString() + "\n";
        }
        return text.trim();
    }
    
    @Override
    public void setColor(String color) {
        this.color = color;
        for (SVGElement elem : elements) {
            elem.setColor(color);
        }
    }
    
    @Override
    public String getColor() {
        return color;
    }
    
    @Override
    public void setUserAdded(boolean userAdded) {
        super.setUserAdded(userAdded);
        for (SVGElement elem : getSimpleElements()) {
            elem.setUserAdded(userAdded);
        }
    }
    
    public Dimension getDimensions() {
        int minX, minY, maxX = 0, maxY = 0, height = 0, width = 0;
        minX = Integer.MAX_VALUE;
        minY = Integer.MAX_VALUE;
        for (SVGElement elem : getSimpleElements()) {
            if (elem.getHeight() > height)
                height = elem.getHeight();
            if (elem.getWidth() > width)
                width = elem.getWidth();
            
            if (elem.getCenterX() < minX)
                minX = elem.getCenterX();
            if (elem.getCenterX() > maxX)
                maxX = elem.getCenterX();
            
            if (elem.getCenterY() < minY)
                minY = elem.getCenterY();
            if (elem.getCenterY() > maxY)
                maxY = elem.getCenterY();
        }
        return new Dimension(maxX - minX + width, maxY - minY + height);
    }
}