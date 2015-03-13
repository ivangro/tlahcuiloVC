package svg.elems;

import java.util.*;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Class to select the given elements on the canvas
 * @author Ivan Guerrero
 */
public class SVGElementSelector {
    private Set<SVGElement> simpleElements, elements;
    private String svgNS;
    private SVGDocument doc;
    private Element root;
    
    public SVGElementSelector(String svgNS, SVGDocument doc, Element root) {
        simpleElements = new HashSet<>();
        elements = new HashSet<>();
        this.svgNS = svgNS;
        this.doc = doc;
        this.root = root;
    }
    
    public void addSelection(SVGElement element) {
        if (element instanceof SVGUnit) {
            SVGUnit unit = (SVGUnit)element;
            simpleElements.addAll(unit.getSimpleElements());
        }
        else {
            simpleElements.add(element);
        }
        elements.add(element);
    }
    
    public void clearSelection() {
        simpleElements.clear();
        elements.clear();
    }
    
    /**
     * Obtains a set with all the simple selected elements
     * @return 
     */
    public Set<SVGElement> getSimpleSelectedElements() {
        return simpleElements;
    }
    
    /**
     * Obtains a list with all the selected elements
     * @return 
     */
    public Set<SVGElement> getSelectedElements() {
        return elements;
    }
        
    public void drawSelectionBoxes() {
        for (SVGElement element : simpleElements) {
            Element box = getBox(element.getCenterX() - element.getWidth()/2, 
                                 element.getCenterY() - element.getHeight()/2, 
                                 element.getWidth(), element.getHeight(), SVGConfig.SELECTION_COLOR);
            root.appendChild(box);
        }
    }
    
    private Element getBox(int x, int y, int boxWidth, int boxHeight, String boxColor) {
        Element element = doc.createElementNS(svgNS, "rect");
        element.setAttributeNS(null, "x", x+"");
        element.setAttributeNS(null, "y", y+"");
        element.setAttributeNS(null, "width", boxWidth + "");
        element.setAttributeNS(null, "height", boxHeight + "");
        element.setAttributeNS(null, "style", "stroke:" + boxColor + ";fill:none");
        
        return element;
    }
}