package svg.skill;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;
import svg.core.SVGElement;

/**
 * Interface to determine the methods requested to generate a drawing from a set of elements
 * @author Ivan Guerrero
 */
public interface IDrawingGenerator {
    void setSVGDocument(SVGDocument doc);
    void setNameSpace(String svgNS);
    void draw(Element root, List<List<SVGElement>> elements, boolean displayAxis);
}
