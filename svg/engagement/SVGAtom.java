package svg.engagement;

import edu.uci.ics.jung.graph.*;
import java.io.Serializable;
import java.util.*;
import svg.context.SVGEdge;
import svg.core.SVGElement;

/**
 * Class representing an atom to be employed during the engagement phase
 * @author Ivan Guerrero
 */
public class SVGAtom implements Serializable {
    private int level;
    /** Identifies an atom. 'C' <cellNumber> '-A' <atomLevel> '-' <atomNumber> */
    private String ID;
    /** Identifies the context from where the atom was originated. 'Ctx-' <contextNumber> */
    private String contextID;
    private List<SVGElement> elements;
    private List<SVGEdge> edges;
    private List<INextAction> nextActions;
    /** Determines if the atom represents the context where the design action was applied */
    private boolean actionLevel;
    
    public SVGAtom() {
        level = 0;
        elements = new ArrayList<>();
        edges = new ArrayList<>();
        nextActions = new ArrayList<>();
    }
    
    public SVGAtom(Graph<SVGElement, SVGEdge> graph, int level) {
        elements = new ArrayList<>();
        edges = new ArrayList<>();
        nextActions = new ArrayList<>();
        for (SVGElement elem : graph.getVertices()) {
            elements.add(elem);
        }
        for (SVGEdge edge : graph.getEdges()) {
            edges.add(edge);
        }
        this.level = level;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the graph
     */
    public Graph<SVGElement, SVGEdge> getGraph() {
        Graph<SVGElement, SVGEdge> graph = new SparseMultigraph<>();
        for (SVGElement elem : elements) {
            graph.addVertex(elem);
        }
        for (SVGEdge edge : edges) {
            graph.addEdge(edge, edge.getSource(), edge.getTarget());
        }
        
        return graph;
    }

    
    @Override
    public String toString() {
        String desc = getID() + ": ";
        desc += "(" + elements.size() + ", " + edges.size() + ")";
        return desc;
    }

    /**
     * @return the elements
     */
    public Collection<SVGElement> getElements() {
        return elements;
    }

    /**
     * @param elements the elements to set
     */
    public void setElements(List<SVGElement> elements) {
        this.elements = elements;
    }

    /**
     * @return the edges
     */
    public Collection<SVGEdge> getEdges() {
        return edges;
    }

    /**
     * @param edges the edges to set
     */
    public void setEdges(List<SVGEdge> edges) {
        this.edges = edges;
    }
    
    public void addNextAction(INextAction action) {
        nextActions.add(action);
    }
    
    public List<INextAction> getNextActions() {
        return nextActions;
    }
    
    public void setID(String ID) {
        this.ID = ID;
    }
    
    public String getID() {
        return ID;
    }
    
    public void setContextID(String contextID) {
        this.contextID = contextID;
    }
    
    public String getContextID() {
        return contextID;
    }
    
    public boolean isActionLevel() {
        return actionLevel;
    }
    
    /**
     * Establishes that this atom stores the context of the level where the design action took place
     * @param actionLevel 
     */
    public void setActionLevel(boolean actionLevel) {
        this.actionLevel = actionLevel;
    }
}