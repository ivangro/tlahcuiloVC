package svg.context;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import svg.core.SVGElement;

/**
 * Class to analyze a graph representing a drawing context
 * @author Ivan Guerrero
 */
public class ContextAnalyzer {
    private static ContextAnalyzer instance = new ContextAnalyzer();
    
    private ContextAnalyzer() {}
    
    public static ContextAnalyzer getInstance() {
        return instance;
    }
    
    /**
     * Obtains two graphs representing the inserted elements and the removed elements 
     * from the original graph to the modified graph
     * @param original
     * @param modified
     * @return The inserted and removed elements between the two graphs
     */
    public DifferenceGraph getDifferenceGraphs(Graph<SVGElement, SVGEdge> original, Graph<SVGElement, SVGEdge> modified) {
        Graph<SVGElement, SVGEdge>[] diff;
        diff = new SparseMultigraph[] {new SparseMultigraph(), new SparseMultigraph()};
        
        //Detect the differences of inserted elements
        //Detect the inserted edges
        for (SVGEdge edge : modified.getEdges()) {
            if (!original.containsEdge(edge)) {
                if (!diff[0].containsVertex(edge.getSource()))
                    diff[0].addVertex(edge.getSource());
                if (!diff[0].containsVertex(edge.getTarget()))
                    diff[0].addVertex(edge.getTarget());
                if (!diff[0].containsEdge(edge))
                    diff[0].addEdge(edge, edge.getSource(), edge.getTarget());
            }   
        }
        //Detect the inserted vertices
        for (SVGElement elem : modified.getVertices()) {
            if (!original.containsVertex(elem)) {
                diff[0].addVertex(elem);
            }
        }
        
        //Detect the differences of removed elements
        //Detect the removed edges
        for (SVGEdge edge : original.getEdges()) {
            if (!modified.containsEdge(edge)) {
                if (!diff[1].containsVertex(edge.getSource()))
                    diff[1].addVertex(edge.getSource());
                if (!diff[1].containsVertex(edge.getTarget()))
                    diff[1].addVertex(edge.getTarget());
                if (!diff[1].containsEdge(edge))
                    diff[1].addEdge(edge, edge.getSource(), edge.getTarget());
            }   
        }
        //Detect the removed vertices
        for (SVGElement elem : original.getVertices()) {
            if (!modified.containsVertex(elem)) {
                diff[1].addVertex(elem);
            }
        }
        
        DifferenceGraph result = new DifferenceGraph();
        result.setInsertion(diff[0]);
        result.setRemoval(diff[1]);
        
        return result;
    }
}