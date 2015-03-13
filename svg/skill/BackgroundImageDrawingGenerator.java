package svg.skill;

import java.util.List;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.elems.ElementFactory;

/**
 *
 * @author Ivan Guerrero
 */
public class BackgroundImageDrawingGenerator implements IDrawingGenerator {
    private SVGDocument doc;
    private String svgNS;
    
    public BackgroundImageDrawingGenerator() {}
    
    @Override
    public void draw(Element root, List<List<SVGElement>> elements, boolean displayAxis) {
        //Obtains the element to apply blur to the background
        //root.appendChild(generateBlurFilter());
        //root.appendChild(generateFilterWrapper());
        //Obtains the backgrond image element
        setBackgroundColor(root);
        
        SVGElement background = generateBackgroundElement();
        Element bgElement = background.getElement();
        bgElement.setAttribute("id", "bgImage");
        bgElement.setAttribute("style", "fill:none;fill-opacity:1;opacity:0.3");
        root.appendChild(bgElement);
        
        drawAxis(root, displayAxis);
        
        //Adds the rest of the elements
        for (SVGElement elem : elements.get(0)) {
            root.appendChild(elem.getElement());
        }
    }

    @Override
    public void setSVGDocument(SVGDocument doc) {
        this.doc = doc;
    }

    @Override
    public void setNameSpace(String svgNS) {
        this.svgNS = svgNS;
    }

    private SVGElement generateBackgroundElement() {
        //200 is the id for the background
        SVGElement background = ElementFactory.getInstance().createNewElement(SVGConfig.DEFAULT_ELEMENT_SIZE, 200);
        background.setCenter(background.getWidth()/2, background.getHeight()/2);
        background.setWidth(SVGConfig.CANVAS_WIDTH);
        background.setHeight(SVGConfig.CANVAS_HEIGHT);
        background.setID("bgElement");
        background.setElement(doc.createElementNS(svgNS, background.getSVGType()));
        
        return background;
    }

    private Element generateBlurFilter() {
        Element filter = doc.createElement("filter");
        filter.setAttributeNS(null, "id", "B");
        filter.setAttributeNS(null, "x", "0");
        filter.setAttributeNS(null, "y", "0");
        
        Element blur = doc.createElement("feGaussianBlur");
        blur.setAttributeNS(null, "in", "SourceGraphic");
        blur.setAttributeNS(null, "stdDeviation", "50");
        filter.appendChild(blur);
        return filter;
    }
    
    private Element generateFilterWrapper() {
        Element wrapper = doc.createElement("use");
        wrapper.setAttribute("filter", "url(#B)");
        wrapper.setAttribute("href", "url(#bgImage)");
        
        return wrapper;
    }

    public void drawAxis(Element root, boolean displayAxis) {
        if (displayAxis) {
            String[][] axisData = {{"x1", "y1", "x2", "y2", "stroke", "stroke-width"}, 
                                   {SVGConfig.CANVAS_WIDTH/2 + "", "0", SVGConfig.CANVAS_WIDTH/2 + "", SVGConfig.CANVAS_HEIGHT + "", "#aaaaaa", "1"},
                                   {"x1", "y1", "x2", "y2", "stroke", "stroke-width"}, 
                                   {"0", SVGConfig.CANVAS_HEIGHT/2 + "", SVGConfig.CANVAS_WIDTH + "", SVGConfig.CANVAS_HEIGHT/2 + "", "#aaaaaa", "1"}};
            Element[] axis = new Element[2];
            
            for (int i=0; i<axis.length; i++) {
                axis[i] = doc.createElementNS(svgNS, "line");
                for (int j=0; j<axisData[0].length; j++) {
                    axis[i].setAttributeNS(null, axisData[2*i][j], axisData[2*i+1][j]);
                }
                root.appendChild(axis[i]);
            }
        }
    }

    private void setBackgroundColor(Element root) throws DOMException {
        Element background = doc.createElementNS(svgNS, "rect");
        background.setAttributeNS(null, "x", "0");
        background.setAttributeNS(null, "y", "0");
        background.setAttributeNS(null, "width", SVGConfig.CANVAS_WIDTH + "");
        background.setAttributeNS(null, "height", SVGConfig.CANVAS_HEIGHT + "");
        background.setAttributeNS(null, "fill", SVGConfig.BACKGROUND_COLOR);
        root.appendChild(background);
    }
}
