package svg.context;

import java.io.Serializable;
import java.util.Objects;
import svg.core.SVGElement;

/**
 * Class to represent a relation between two SVGElements in a drawing
 * @author Ivan Guerrero
 */
public class SVGEdge implements Serializable {
    private ElementRelationType type;
    private SVGElement source, target;
    
    public SVGEdge() {}
    
    public SVGEdge(SVGElement source, SVGElement target, ElementRelationType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    /**
     * @return the type
     */
    public ElementRelationType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ElementRelationType type) {
        this.type = type;
    }

    /**
     * @return the source
     */
    public SVGElement getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(SVGElement source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    public SVGElement getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(SVGElement target) {
        this.target = target;
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof SVGEdge) {
            SVGEdge edge = (SVGEdge)obj;
            res = ((source.equals(edge.source) && target.equals(edge.target) && (type == edge.type)) ||
                    (source.equals(edge.target) && target.equals(edge.source) && (type == edge.type)));
        }
        return res;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 13 * hash + Objects.hashCode(this.source);
        hash = 13 * hash + Objects.hashCode(this.target);
        return hash;
    }
    
    @Override
    public String toString() {
        return type.name() + ": " + source.getID() + "->" + target.getID();
    }
}
