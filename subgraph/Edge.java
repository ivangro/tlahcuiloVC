package subgraph;

/**
 *
 * @author Ivan Guerrero
 */
public class Edge implements IEdge {
    private INode source, target;
    private ElementRelationType label;
    
    public Edge() {}
    
    public Edge(INode source, INode target, ElementRelationType label) {
        this.source = source;
        this.target = target;
        this.label = label;
    }
    
    public Edge(String source, String target, int labelID) {
        this.source = new Node(source);
        this.target = new Node(target);
        this.label = ElementRelationType.values()[labelID];
    }

    /**
     * @return the source
     */
    @Override
    public INode getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    @Override
    public void setSource(INode source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    @Override
    public INode getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    @Override
    public void setTarget(INode target) {
        this.target = target;
    }

    /**
     * @return the label
     */
    @Override
    public ElementRelationType getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    @Override
    public void setLabel(ElementRelationType label) {
        this.label = label;
    }
    
    @Override
    public String toString() {
        return source.toString() + "-" + label.name() + "-" + target.toString();
    }
}
