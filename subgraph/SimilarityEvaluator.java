package subgraph;

import edu.uci.ics.jung.graph.Graph;
import java.util.*;

/**
 *
 * @author Ivan Guerrero
 */
public class SimilarityEvaluator {
    /** Allows the correct label comparison */
    private boolean isInverted;
    
    public Solution evaluate(Graph<INode, IEdge> graph1, Graph<INode, IEdge> graph2) {
        if (graph1.getEdgeCount() > graph2.getEdgeCount()) {
            isInverted = false;
            return evaluateGraphs(graph2, graph1);
        }
        else {
            isInverted = true;
            Solution solution = evaluateGraphs(graph1, graph2);
            if (solution != null)
                solution.invertMapping();
            return solution;
        }
    }
    
    /**
     * Compares the percentage of elements in the graph1 included in the graph2
     * @param graph1 The graph with the largest number of edges
     * @param graph2 The graph with the smallest number of edges
     * @return 
     */
    public Solution evaluateGraphs(Graph<INode, IEdge> graph1, Graph<INode, IEdge> graph2) {
        Map<Integer,List<Solution>> solutionBundle = new HashMap<>();
        List<Solution> solutions = new ArrayList<>();
        Solution solution = new Solution();
        solution.setRemainingGraph(graph2);
        solution.setNoUnmatchedEdges(graph1.getEdgeCount());
        solution.setNoEdges(graph2.getEdgeCount());
        solution.setTotalEdges(graph1.getEdgeCount() + graph2.getEdgeCount());
        solution.setTotalVertices(graph1.getVertexCount() + graph2.getVertexCount());
        addIsolatedVertices(graph1, graph2, solution);
        solutions.add(solution);
        int level = 0;
        solutionBundle.put(level, solutions);
        
        for (IEdge ei : graph1.getEdges()) {
            solutions = solutionBundle.get(level);
            List<Solution> newSolutions = new ArrayList<>();
            
            for (Solution s : solutions) {
                Collection<IEdge> sei = getEdgesWithLabel(ei, s.getRemainingGraph());
                for (IEdge ej : sei) {
                    INode mapSource = s.getMapping(ei.getSource());
                    INode mapTarget = s.getMapping(ei.getTarget());
                    boolean unmappedSource = isUnmapped(s, ej.getSource());
                    boolean unmappedTarget = isUnmapped(s, ej.getTarget());
                    if (((mapSource == null && unmappedSource) || (mapSource != null && mapSource.equals(ej.getSource()))) &&
                        ((mapTarget == null && unmappedTarget) || (mapTarget != null && mapTarget.equals(ej.getTarget())))) {
                        Solution newSolution = (Solution)s.clone();
                        newSolution.setNoEdges(graph2.getEdgeCount());
                        if (newSolution.getMapping(ei.getSource()) == null)
                            newSolution.addMapping(ei.getSource(), ej.getSource());
                        if (newSolution.getMapping(ei.getTarget()) == null)
                            newSolution.addMapping(ei.getTarget(), ej.getTarget());
                        newSolution.removeEdge(ej);
                        newSolutions.add(newSolution);
                    }
                }
            }
            //TODO: Prevents from adding an empty level when no mappings were found, and preserves the last working set of solutions
            if (!newSolutions.isEmpty()) {
                level++;
                solutionBundle.put(level, newSolutions);
            }
        }
        
        Solution maxSimilarity = calculateMaxSimilarity(solutionBundle);
        return maxSimilarity;
    }
    
    /**
     * Verifies that the given node is unmapped inside the solution, so it can be mapped
     * @param solution
     * @param source
     * @return True if the node is unmapped inside the solution
     */
    private boolean isUnmapped(Solution solution, INode node) {
        for  (INode mapped : solution.getMappingValues()) {
            if (mapped.equals(node))
                return false;
        }
        return true;
    }   
    
    private Collection<IEdge> getEdgesWithLabel(IEdge e, Graph<INode, IEdge> graph) {
        Collection<IEdge> edges = new ArrayList<>();
        for (IEdge edge : graph.getEdges()) {
            if (edge.getLabel() == e.getLabel())
                edges.add(edge);
        }
        return edges;
    }

    private Solution calculateMaxSimilarity(Map<Integer, List<Solution>> solutionBundle) {
        double maxSimilarity = -1;
        Solution maxSolution = null;
        
        for (Integer key : solutionBundle.keySet()) {
            for (Solution sol : solutionBundle.get(key)) {
                if (sol.getSimilarity() > maxSimilarity) {
                    maxSimilarity = sol.getSimilarity();
                    maxSolution = sol;
                }
            }
        }
        
        return maxSolution;
    }

    /**
     * Counts the number of vertices without any edge as isolated vertices
     * @param graph1
     * @param graph2
     * @param solution 
     */
    private void addIsolatedVertices(Graph<INode, IEdge> graph1, Graph<INode, IEdge> graph2, Solution solution) {
        Collection<INode> vertices = graph1.getVertices();
        List<INode> isolated = new ArrayList<>();
        for (INode vertex : vertices) {
            if (graph1.getInEdges(vertex).isEmpty())
                isolated.add(vertex);
        }
        
        Collection<INode> vertices1 = graph2.getVertices();
        List<INode> isolated1 = new ArrayList<>();
        for (INode vertex : vertices1) {
            if (graph2.getInEdges(vertex).isEmpty())
                isolated1.add(vertex);
        }
        
        int minIsolated = Math.min(isolated.size(), isolated1.size());
        for (int i=0; i<minIsolated; i++) {
            solution.addMapping(isolated.get(i), isolated1.get(i));
        }
    }
}