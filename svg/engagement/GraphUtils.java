package svg.engagement;

import edu.uci.ics.jung.graph.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import subgraph.*;
import svg.context.SVGEdge;
import svg.context.SimpleElement;
import svg.core.SVGElement;
import svg.elems.AbstractUnit;


/**
 * Class to generate a suitable graph for simmilarity comparison
 * @author Ivan Guerrero
 */
public class GraphUtils {    
    private static Pattern vertex1, vertex2;
    
    static {
        vertex1 = Pattern.compile("(\\d+)");
        vertex2 = Pattern.compile("U(\\d+)-(\\d+)");
    }    
    
    /**
     * Transforms the elements inside the graph to simple elements
     * @param graph A graph with elements / groups of elements
     * @param mapping The mapping betwen the elements of the original graph and the elements in the simplified graph
     * @return The simplified graph
     */
    public static Graph<SVGElement, SVGEdge> simplifyElements(Graph<SVGElement, SVGEdge> graph, 
                                                              Map<SVGElement, SVGElement> mapping) {
        if (mapping == null)
            mapping = new HashMap<>();
        Graph<SVGElement, SVGEdge> simple = new SparseMultigraph<>();
        
        for (SVGElement elem : graph.getVertices()) {
            SimpleElement newElem;
            if (!mapping.containsKey(elem)) {
                newElem = new SimpleElement();
                newElem.setID(mapping.size() + "");
                newElem.setOriginalID(elem.getID());
                newElem.setArea(elem.getArea());
                newElem.setCenter(elem.getCenterX(), elem.getCenterY());
                newElem.setDistanceType(elem.getDistanceType());
                newElem.setShape(elem.getShape());
                newElem.setShapeID(elem.getShapeID());
                newElem.setSize(elem.getSize());
                if (elem instanceof AbstractUnit) {
                    AbstractUnit unit = (AbstractUnit)elem;
                    newElem.setNoElements(unit.getElements().size());
                }
                else {
                    newElem.setNoElements(elem.getSimpleElements().size());
                }
                mapping.put(elem, newElem);
            }
            else
                newElem = (SimpleElement)mapping.get(elem);
            simple.addVertex(newElem);
        }
        for (SVGEdge edge : graph.getEdges()) {
            SVGEdge newEdge = new SVGEdge();
            newEdge.setType(edge.getType());
            newEdge.setSource(mapping.get(edge.getSource()));
            newEdge.setTarget(mapping.get(edge.getTarget()));
            simple.addEdge(newEdge, newEdge.getSource(), newEdge.getTarget());
        }
        return simple;
    }
    
    public static SimilarityResult calculateSimilarity(Graph<SVGElement, SVGEdge> atomGraph, Graph<SVGElement, SVGEdge> contextGraph) {
        SimilarityResult result = new SimilarityResult();
        SimilarityEvaluator evaluator = new SimilarityEvaluator();
        Graph<INode, IEdge> simpleAtom = simplify(atomGraph);
        Graph<INode, IEdge> simpleContext = simplify(contextGraph);
        Solution evaluation = evaluator.evaluate(simpleAtom, simpleContext);
        if (evaluation != null) {
            result.similarity = evaluation.getSimilarity();
            result.vertexMap = parseMapping(evaluation, atomGraph, contextGraph);
        }
        return result;
    }
    
    private static Graph<INode, IEdge> simplify(Graph<SVGElement, SVGEdge> graph) {
        Graph<INode, IEdge> simple = new SparseMultigraph<>();
        for (SVGElement elem : graph.getVertices()) {
            INode simpleElem = new Node(elem.getID());
            simple.addVertex(simpleElem);
        }
        for (SVGEdge edge : graph.getEdges()) {
            SVGElement[] vertex;
            vertex = orderVertices(edge);
            IEdge simpleEdge = new Edge(vertex[0].getID(), vertex[1].getID(), edge.getType().ordinal());
            simple.addEdge(simpleEdge, simpleEdge.getSource(), simpleEdge.getTarget());
        }
        return simple;
    }

    private static Map<SVGElement, SVGElement> parseMapping(Solution evaluation, Graph<SVGElement, SVGEdge> atomGraph, Graph<SVGElement, SVGEdge> contextGraph) {
        Map<SVGElement, SVGElement> newMapping = new HashMap<>();
        SVGElement[] atomNodes = atomGraph.getVertices().toArray(new SVGElement[0]);
        SVGElement[] contextNodes = contextGraph.getVertices().toArray(new SVGElement[0]);
        for (INode source : evaluation.getMappingKeys()) {
            INode target = evaluation.getMapping(source);
            int sourcePos, targetPos;
            //TODO: Validate if this part works fine. Its purpose is to deal with the inverted mappings
            try {
                sourcePos = findElementPosition(atomNodes, source);
                targetPos = findElementPosition(contextNodes, target);
                newMapping.put(atomNodes[sourcePos], contextNodes[targetPos]);
            } catch (ArrayIndexOutOfBoundsException e) {
                sourcePos = findElementPosition(contextNodes, source);
                targetPos = findElementPosition(atomNodes, target);
                newMapping.put(atomNodes[targetPos], contextNodes[sourcePos]);
            }
        }
        return newMapping;
    }

    private static int findElementPosition(SVGElement[] contextNodes, INode target) {
        for (int i=0; i<contextNodes.length; i++) {
            SVGElement elem = contextNodes[i];
            if (elem.getID().equals(target.getID()))
                return i;
        }
        return -1;
    }

    /**
     * Orders the vertices of the edge so the source vertex ID is smaller than the target vertex ID
     * @param edge
     * @return 
     */
    private static SVGElement[] orderVertices(SVGEdge edge) {
        SVGElement source = edge.getSource();
        SVGElement target = edge.getTarget();
        SVGElement[] vertex = {source, target};
        Matcher matcher1 = vertex1.matcher(source.getID());
        Matcher matcher2 = vertex1.matcher(target.getID());
        if (matcher1.matches() && matcher2.matches()) {
            int id1, id2;
            id1 = Integer.parseInt(matcher1.group(1));
            id2 = Integer.parseInt(matcher2.group(1));
            vertex = (id1 > id2) ? new SVGElement[]{source, target} : new SVGElement[]{target, source};
        }
        matcher1 = vertex2.matcher(source.getID());
        matcher2 = vertex2.matcher(target.getID());
        if (matcher1.matches() && matcher2.matches()) {
            int elem1, elem2;
            elem1 = Integer.parseInt(matcher1.group(2));
            elem2 = Integer.parseInt(matcher2.group(2));
            vertex = (elem1 > elem2) ? new SVGElement[]{source, target} : new SVGElement[]{target, source};
        }
        return vertex;
    }
}