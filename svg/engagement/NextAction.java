package svg.engagement;

import java.io.Serializable;
import java.util.Objects;
import svg.context.ElementRelationType;
import svg.core.SVGElement;

/**
 *
 * @author Ivan Guerrero
 */
public class NextAction implements Serializable, Cloneable{
    private SVGElement source, target;
    private ElementRelationType relationType;
    private boolean insertion, removal;
    
    public NextAction() {
        
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

    /**
     * @return the relationType
     */
    public ElementRelationType getRelationType() {
        return relationType;
    }

    /**
     * @param relationType the relationType to set
     */
    public void setRelationType(ElementRelationType relationType) {
        this.relationType = relationType;
    }

    /**
     * @return the insertion
     */
    public boolean isInsertion() {
        return insertion;
    }

    /**
     * @param insertion the insertion to set
     */
    public void setInsertion(boolean insertion) {
        this.insertion = insertion;
    }
 
    /**
     * @return the removal
     */
    public boolean isRemoval() {
        return removal;
    }

    /**
     * @param removal the removal to set
     */
    public void setRemoval(boolean removal) {
        this.removal = removal;
    }
       
    @Override
    public String toString() {
        return ((insertion) ? "+" : "-") + relationType.name() + " " + 
                ((source != null) ? source.getID() + " " : " ") + 
                ((target != null) ? target.getID() : "");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.source);
        hash = 37 * hash + Objects.hashCode(this.target);
        hash = 37 * hash + (this.relationType != null ? this.relationType.hashCode() : 0);
        hash = 37 * hash + (this.insertion ? 1 : 0);
        hash = 37 * hash + (this.removal ? 1 : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NextAction) {
            NextAction action = (NextAction)obj;
            return (action.insertion == insertion && action.removal == removal &&
                    action.relationType == relationType && ((
                        action.source.equals(source) && 
                        action.target.equals(target)) ||
                        action.source.equals(target) && 
                        action.target.equals(source)));
        }
        return false;
    }
    
    @Override
    public Object clone() {
        NextAction action = new NextAction();
        action.insertion = insertion;
        action.removal = removal;
        action.relationType = relationType;
        action.source = source;
        action.target = target;
        return action;
    }
}