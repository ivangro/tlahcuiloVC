package svg.context;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import java.io.Serializable;
import java.util.*;
import svg.core.SVGElement;
import svg.engagement.GraphUtils;

/**
 * Class to represent the distinct elements between two graphs
 * @author Ivan Guerrero
 */
public class DifferenceGraph implements Serializable {
    private Graph<SVGElement, SVGEdge> insertion, removal, context;
    private Map<SVGElement, SVGElement> mapping;
    private int level;
    
    public DifferenceGraph() {
        insertion = new SparseMultigraph<>();
        removal = new SparseMultigraph<>();
        context = new SparseMultigraph<>();
        mapping = new HashMap<>();
    }

    /**
     * @return the insertion
     */
    public Graph<SVGElement, SVGEdge> getInsertion() {
        return insertion;
    }

    /**
     * @param insertion the insertion to set
     */
    public void setInsertion(Graph<SVGElement, SVGEdge> insertion) {
        this.insertion = GraphUtils.simplifyElements(insertion, mapping);
    }

    /**
     * @return the removal
     */
    public Graph<SVGElement, SVGEdge> getRemoval() {
        return removal;
    }

    /**
     * @param removal the removal to set
     */
    public void setRemoval(Graph<SVGElement, SVGEdge> removal) {
        this.removal = GraphUtils.simplifyElements(removal, mapping);
    }

    public void setContext(Graph<SVGElement, SVGEdge> contextGraph) {
        this.context = GraphUtils.simplifyElements(contextGraph, mapping);
    }

    /**
     * @return the context
     */
    public Graph<SVGElement, SVGEdge> getContext() {
        return context;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }    
}