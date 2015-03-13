package svg.elems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.w3c.dom.Element;
import svg.core.*;

/**
 * Represents a textual element to represent in a canvas
 * @author Ivan Guerrero
 */
public class ElementText extends SVGElement {
    private String fontFamily, text;
    private int fontSize;
    
    public ElementText(String text) {
        this.text = text;
        setColor(SVGConfig.FORE_COLOR);
        setSize(SVGConfig.DEFAULT_ELEMENT_SIZE);
        fontFamily = SVGConfig.defaultFontType;
        super.setSVGType("text");
        super.setID("");
        super.setShape(ElementShape.SINGLE_ELEMENT);
        super.setDistanceType(ElementDistanceType.SINGLE);
    }
    
    @Override
    public double getArea() {
        return getHeight() * getWidth();
    }
    
    @Override
    public Element getElement() {
        Element elem = super.getElement();
        String style;
        style = "font-family: " + getFontFamily() + "; " + 
                "font-size: " + getFontSize() + "; ";
        elem.setTextContent(getText());
        elem.setAttributeNS(null, "style", style);
        if (getX() < 0)
            elem.setAttributeNS(null, "transform", "scale(-1,1)");
        else
            elem.setAttributeNS(null, "transform", "");
        return elem;
    }
    
    @Override
    public boolean containsPoint(int x, int y) {
        boolean res;
        if (getX() >= 0)
            res = ((x >= getX() && x <= getX()+getWidth()) && (y >= getY()-getHeight() && y <= getY()));
        else {
            int tmpX = (getX() + getWidth()) * -1;
            res = ((x >= tmpX && x <= tmpX+getWidth()) && (y >= getY()-getHeight() && y <= getY()));
        }
        
        return res;
    }
    
    @Override
    public void setCenter(int x, int y) {
        setX(x - getWidth()/2);
        setY(y + getHeight()/2);
    }

    /**
     * @return the fontType
     */
    public String getFontFamily() {
        return fontFamily;
    }

    /**
     * @param fontType the fontType to set
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
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
        cy = getY() - getHeight()/2;
        return cy;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ElementText cloned = new ElementText(text);
        cloned.setSize(getSize());
        cloned.setCenter(getCenterX(), getCenterY());
        cloned.setID(getID());
        return cloned;
    }
    
    @Override
    public String toString() {
        return "E" + getID() + " (" + getCenterX() + ", " + getCenterY() + ")";
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
        super.setSize(size);
        switch (size) {
            case 1:
            case 2:
            case 3:
                fontSize = (int)(SVGConfig.defaultFontSize * getSizeFactor(size));
                break;
            default:
                setSize(1 + new Random().nextInt(2));
        }
        setWidth((int)Math.round(SVGConfig.FONT_WIDTH_FACTOR * fontSize));
        setHeight((int)Math.round(SVGConfig.FONT_HEIGHT_FACTOR * fontSize));
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
}