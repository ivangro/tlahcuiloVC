package svg.skill;

import java.awt.Point;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Class to draw curves with the elements of a group
 * @author Ivan Guerrero
 */
public class CurveDrawingGenerator implements IDrawingGenerator{
    private SVGDocument doc;
    private String svgNS;
    private String[] colors = new String[] {"#0000ff", "#ff0000", "#00ff00", "#993333", "#339933", "#333399"};
    
    @Override
    public void draw(Element root, List<List<SVGElement>> elements, boolean displayAxis) {
        drawAxis(root, displayAxis);
        for (SVGElement elem : elements.get(0)) {
            root.appendChild(elem.getElement());
        }
        
        if (elements.size() > 1) {
            for (int i=elements.size()-1; i>0; i--) {
                for (SVGElement elem : elements.get(i)) {
                    SVGCurveElement curve = new SVGCurveElement();
                    curve.setElement(doc.createElementNS(svgNS, curve.getSVGType()));
                    curve.setColor(colors[i]);
                    for (SVGElement simple : elem.getSimpleElements()) {
                        curve.addPoint(new Point(simple.getCenterX(), simple.getCenterY()));
                    }
                    root.appendChild(curve.getElement());
                }
            }
        }
    }

    /**
     * @param doc the doc to set
     */
    @Override
    public void setSVGDocument(SVGDocument doc) {
        this.doc = doc;
    }

    @Override
    public void setNameSpace(String svgNS) {
        this.svgNS = svgNS;
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
}
