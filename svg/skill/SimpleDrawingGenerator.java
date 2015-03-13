package svg.skill;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Class to create a drawing from the given elements
 * @author Ivan Guerrero
 */
public class SimpleDrawingGenerator implements IDrawingGenerator {
    private SVGDocument doc;
    private String svgNS;
    
    public SimpleDrawingGenerator() {}
    
    @Override
    public void draw(Element root, List<List<SVGElement>> elements, boolean displayAxis) {
        drawAxis(root, displayAxis);
        
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
    
    public void drawAxis(Element root, boolean displayAxis) {
        Element background = doc.createElementNS(svgNS, "rect");
        background.setAttributeNS(null, "x", "0");
        background.setAttributeNS(null, "y", "0");
        background.setAttributeNS(null, "width", SVGConfig.CANVAS_WIDTH + "");
        background.setAttributeNS(null, "height", SVGConfig.CANVAS_HEIGHT + "");
        background.setAttributeNS(null, "fill", SVGConfig.BACKGROUND_COLOR);
        root.appendChild(background);
        
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
}
