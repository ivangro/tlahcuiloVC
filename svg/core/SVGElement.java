package svg.core;

import java.io.Serializable;
import java.util.List;
import org.w3c.dom.Element;
import svg.elems.ElementDistanceType;
import svg.elems.ElementShape;

/**
 * Basic elements for a SVG drawing element
 * @author Ivan Guerrero
 */
public abstract class SVGElement implements Cloneable, Serializable, Comparable {
    private int x, y, height, width, size, shapeID;
    private String SVGType, color, ID;
    private transient Element element;
    private boolean userAdded;
    private ElementShape shape;
    private ElementDistanceType distanceType;
    private int imageID;
    
    public abstract double getArea();
    
    public abstract boolean containsPoint(int x, int y);
    
    public abstract int getCenterX();
    
    public abstract int getCenterY();
    
    public abstract void setCenter(int x, int y);
    
    public abstract List<SVGElement> getSimpleElements();
    
    public abstract int getRhythms();
    
    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    /**
     * @return the ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID the ID to set
     */
    public void setID(String ID) {
        this.ID = ID;
    }
    
    /**
     * @return the imageID
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * @param imageID the imageID to set
     */
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    /**
     * @return the SVGType
     */
    public String getSVGType() {
        return SVGType;
    }

    /**
     * @param SVGType the SVGType to set
     */
    public void setSVGType(String SVGType) {
        this.SVGType = SVGType;
    }

    /**
     * @return the SVG element that can be employed to draw it in a canvas
     */
    public Element getElement() {
        element.setAttributeNS(null, "x", getX()+"");
        element.setAttributeNS(null, "y", getY()+"");
        element.setAttributeNS(null, "fill", getColor()+"");
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Determines if the element has been added by the user, in such case, it cannot be removed
     * @return 
     */
    public boolean isUserAdded() {
        return userAdded;
    }
    
    /**
     * Establishes if the element has been added by the user
     * @param userAdded 
     */
    public void setUserAdded(boolean userAdded) {
        this.userAdded = userAdded;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof SVGElement) {
            SVGElement elem = (SVGElement)obj;
            res = elem.getID().equals(this.ID);
        }
        return res;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + this.ID.hashCode();
        return hash;
    }
    
    /**
     * @return the shape
     */
    public ElementShape getShape() {
        return shape;
    }
    
    public void setShape(ElementShape shape) {
        this.shape = shape;
    }

    /**
     * @return the distanceType
     */
    public ElementDistanceType getDistanceType() {
        return distanceType;
    }

    /**
     * @param distanceType the distanceType to set
     */
    public void setDistanceType(ElementDistanceType distanceType) {
        this.distanceType = distanceType;
    }

    /**
     * Compares the position of the element. If the shape is vertical, the first coordinate is Y, otherwise is X.
     * @param obj
     * @return 
     */
    @Override
    public int compareTo(Object obj) {
        SVGElement elem = (SVGElement)obj;    
        switch (SVGConfig.ELEMENT_SORT_TYPE) {
            case VERTICAL:
                return (y < elem.y) ? -1 : (y == elem.y) ? 
                                                ((x < elem.x) ? -1 : 
                                                ((x > elem.x) ? 1 : 0)) : 1;
            default:
            if (x < elem.x) {
                return -1;
            }
            else if (x == elem.x) {
                if (y < elem.y) {
                    return -1;
                }
                else if (y > elem.y) {
                    return 1;
                }
                else
                    return 0;
            }
            else
                return 1;
        }
    }
    
    /**
     * @return the shapeID
     */
    public int getShapeID() {
        return shapeID;
    }

    /**
     * @param shapeID the shapeID to set
     */
    public void setShapeID(int shapeID) {
        this.shapeID = shapeID;
    }

    public String toStringWithValues() {
        return toString();
    }
}