package svg.detect;

import java.util.List;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Determines if two elemets overlap or are tangent each other
 * @author Ivan Guerrero
 */
public class CollisionDetector implements IDetector {
    private String descCollision = "", descTangent = "";
    private double COLLISION_OFFSET = 2;
    private int totalCollisions;
    
    public CollisionDetector() {
    }

    @Override
    public void detect(List<List<SVGElement>> elementsList, SVGRepository repository) {
        List<SVGElement> elements = elementsList.get(0);
        descCollision = "";
        descTangent = "";
        totalCollisions = 0;
        
        for (int i=0; i<elements.size(); i++) {
            SVGElement elem = elements.get(i);
            SVGElement elemj = hasCollision(elements, elem);
            if (elemj != null) {
                descCollision += "\t(" + elem.getID() + ", " + elemj.getID() + ") ";
                totalCollisions++;
            }
            else {
                elemj = hasTangent(elements, elem);
                if (elemj != null)
                descTangent += "\t(" + elem.getID() + ", " + elemj.getID() + ") ";
            }
        }
    }
    
    public SVGElement hasCollision(List<SVGElement> elements, SVGElement elem) {
        for (int j=0; j<elements.size(); j++) {
            SVGElement elemj = elements.get(j);
            if (!elemj.equals(elem)) {
                double dist = Math.sqrt(Math.pow(elem.getCenterX() - elemj.getCenterX(), 2) + 
                                        Math.pow(elem.getCenterY() - elemj.getCenterY(), 2));
                if (dist < elem.getWidth())
                    return elemj;
            }
        }
        return null;
    }
    
    public SVGElement hasTangent(List<SVGElement> elements, SVGElement elem) {
        for (int j=0; j<elements.size(); j++) {
            SVGElement elemj = elements.get(j);
            if (!elemj.equals(elem)) {
                double dist = Math.sqrt(Math.pow(elem.getCenterX() - elemj.getCenterX(), 2) + 
                                        Math.pow(elem.getCenterY() - elemj.getCenterY(), 2));
                if (Math.abs(dist - elem.getWidth()) <= COLLISION_OFFSET)
                    return elemj;
            }
        }
        return null;
    }

    @Override
    public String getDescription() {
        String desc = "Collisions: \n" + descCollision + "\nAdjacent: \n" + descTangent;
        return desc;
    }

    /**
     * @return the totalCollisions
     */
    public int getTotalCollisions() {
        return totalCollisions;
    }
}