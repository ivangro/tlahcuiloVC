package svg.elems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.w3c.dom.Element;
import svg.core.SVGElement;

/**
 * Represents an image in a canvas
 * @author Ivan Guerrero
 */
public class ElementImage extends SVGElement {
    private final int DEFAULT_SIZE = 2;
    private int originalHeight, originalWidth;
    private double opacity;
    private String path;
    
    public ElementImage(int height, int width, String path, int imageID) {
        setSVGType("image");
        originalHeight = height;
        originalWidth = width;
        opacity = 1;
        this.path = path;
        setImageID(imageID);
        setID("");
        setShape(ElementShape.SINGLE_ELEMENT);
        setDistanceType(ElementDistanceType.SINGLE);
        setSize(DEFAULT_SIZE);
    }
    
    @Override
    public Element getElement() {
        Element elem = super.getElement();
        elem.setAttributeNS("http://www.w3.org/1999/xlink", "xlink:href", path);
        elem.setAttributeNS(null, "height", getHeight() + "");
        elem.setAttributeNS(null, "width", getWidth() + "");
        elem.setAttributeNS(null, "style", "opacity:" + getOpacity());
        return elem;
    }
    
    @Override
    public double getArea() {
        return getWidth() * getHeight();
    }

    @Override
    public boolean containsPoint(int x, int y) {
        //TODO: Validate if is requested a special instruction for y<0
        boolean res;
        if (getX() >= 0)
            res = ((x >= getX() && x <= getX()+getWidth()) && (y >= getY() && y <= getY()+getHeight()));
        else {
            int tmpX = (getX() + getWidth()) * -1;
            res = ((x >= tmpX && x <= tmpX+getWidth()) && (y >= getY()+getHeight() && y <= getY()));
        }
        
        return res;
    }

    @Override
    public int getCenterX() {
        int cx;
        cx =(getX() >= 0) ? (getX() + getWidth()/2) : (-getX() - getWidth()/2);
        return cx;
    }

    @Override
    public int getCenterY() {
        int cy;
        cy =(getY() >= 0) ? (getY() + getHeight()/2) : (-getY() - getHeight()/2);
        //cy = getY() + getHeight()/2;
        return cy;
    }

    @Override
    public void setCenter(int x, int y) {
        setX(x - getWidth()/2);
        setY(y - getHeight()/2);
    }

    @Override
    public List<SVGElement> getSimpleElements() {
        List<SVGElement> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    @Override
    public int getRhythms() {
        return 1;
    }
    
    /**
     * Sets the size of the font depending on the given size<br>
     * Available sizes are: 1, 2, 3<br>
     * If another number is passed, a random size is picked.
     */
    @Override
    public final void setSize(int size) {
        double factor = 0;
        super.setSize(size);
        switch (size) {
            case 1:
            case 2:
            case 3:
                factor = getSizeFactor(size);
                break;
            default:
                setSize(1 + new Random().nextInt(2));
        }
        setWidth((int)Math.round(originalWidth * factor));
        setHeight((int)Math.round(originalHeight * factor));
    }
    
    /**
     * Obtains the factor employed to switch the font size depending on the size of the element
     * @param size
     * @return Factor to convert between sizes
     */
    public static double getSizeFactor(int size) {
        double factor = 1;
        
        switch(size) {
            case 1:
                factor = 2.0 / 3;
                break;
            case 2:
                factor = 1;
                break;
            case 3:
                factor = 1.5;
                break;
        }
        
        return factor;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ElementImage clone = new ElementImage(originalHeight, originalWidth, path, getImageID());
        clone.setID(getID());
        clone.setSize(getSize());
        clone.setCenter(getCenterX(), getCenterY());
        clone.setShapeID(getShapeID());
        clone.setOpacity(opacity);
        return clone;
    }
    
    @Override
    public String toString() {
        return "I" + getImageID() + "-" + getID() + ": " + getShape().name().substring(0,1) + " (" + getCenterX() + ", " + getCenterY() + ")";
    }

    /**
     * @return the opacity
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * @param opacity the opacity to set
     */
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }
}