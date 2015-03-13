package svg.context;

import java.text.DecimalFormat;
import svg.core.SVGElement;
import svg.elems.AbstractUnit;

/**
 * 
 * @author Ivan Guerrero
 */
public class SimpleElement extends AbstractUnit {
    private double area;
    private String originalID;
    private int noElements;
    
    public SimpleElement() {
        this("");
    }
    
    public SimpleElement(String id) {
        super.setID(id);
    }
    
    @Override
    public double getArea() {
        return area;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SimpleElement elem = new SimpleElement();
        elem.setID(super.getID());
        elem.setArea(area);
        elem.setCenter(getCenterX(), getCenterY());
        elem.setNoElements(noElements);
        for (double pattern : getRhythmList()) {
            elem.addPattern(pattern);
        }
        
        return elem;
    }

    /**
     * @param area the area to set
     */
    public void setArea(double area) {
        this.area = area;
    }
    
    @Override
    public String toString() {
        return super.getID();
    }
    
    @Override
    public String toStringWithValues() {
        DecimalFormat format = new DecimalFormat("0.00");
        return "ID: " + getID() + 
               "\tArea: " + format.format(getArea()) + 
               "\tElements: " + getNoElements() + 
               "\tShape: " + getShape().name() + 
               "\tDistance: " + getDistanceType().name() + 
               "\tShape ID: " + getShapeID();
    }

    /**
     * @return the originalID
     */
    public String getOriginalID() {
        return originalID;
    }

    /**
     * @param originalID the originalID to set
     */
    public void setOriginalID(String originalID) {
        this.originalID = originalID;
    }

    @Override
    public boolean containsPoint(int x, int y) {
        for (SVGElement elem : getElements()) {
            if (elem.containsPoint(x, y))
                return true;
        }
        return false;
    }

    /**
     * @return the noElements
     */
    public int getNoElements() {
        return noElements;
    }

    /**
     * @param noElements the noElements to set
     */
    public void setNoElements(int noElements) {
        this.noElements = noElements;
    }
 }