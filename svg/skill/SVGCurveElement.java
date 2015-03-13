package svg.skill;

import java.awt.Point;
import java.util.*;
import org.w3c.dom.Element;
import svg.core.SVGConfig;

/**
 * Class to draw a SVG quadratic bezier curve 
 * @author Ivan Guerrero
 */
public class SVGCurveElement {
    private List<Point> points;
    private transient Element element;
    private String SVGType, color, ID;
    
    public SVGCurveElement() {
        points = new ArrayList<>();
        SVGType = "path";
        ID = "";
        color = SVGConfig.FORE_COLOR;
    }
    
    public void setID(String ID) {
        this.ID = ID;
    }
    
    public String getID() {
        return ID;
    }
    
    public void addPoint(Point p) {
        points.add(p);
    }
    
    /**
     * @param element the element to set
     */
    public void setElement(Element element) {
        this.element = element;
    }
    
    /**
     * @return the SVG element that can be employed to draw it in a canvas
     */
    public Element getElement() {
        String path = "M";
        Point p, last;
        if (points.size() > 0) {
            p = points.get(0);
            path += p.x + "," + p.y + " ";
            last = p;
            for (int i=1; i<points.size(); i++) {
                int midx, midy;
                p = points.get(i);
                midx = (last.x + p.x) / 2;
                midy = (last.y + p.y) / 2;
                path += "S" + midx + "," + midy + " " + p.x + "," + p.y + " ";
                last = p;
            }
        }
        
        element.setAttributeNS(null, "d", path + "z");
        element.setAttributeNS(null, "fill", "none");
        element.setAttributeNS(null, "stroke", getColor()+"");
        element.setAttributeNS(null, "stroke-width", "2");
        return element;
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
    
    public String getSVGType() {
        return SVGType;
    }
}
