/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package subgraph;

/**
 *
 * @author Ivan Guerrero
 */
public interface IEdge {

    /**
     * @return the label
     */
    ElementRelationType getLabel();

    /**
     * @return the source
     */
    INode getSource();

    /**
     * @return the target
     */
    INode getTarget();

    /**
     * @param label the label to set
     */
    void setLabel(ElementRelationType label);

    /**
     * @param source the source to set
     */
    void setSource(INode source);

    /**
     * @param target the target to set
     */
    void setTarget(INode target);
    
}
