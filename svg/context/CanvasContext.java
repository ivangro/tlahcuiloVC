package svg.context;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import java.awt.Dimension;
import java.util.*;
import org.apache.commons.collections15.Transformer;
import svg.core.SVGConfig;
import svg.core.SVGElement;

/**
 * Class to represent the context of the elements in a drawing
 * A context is represented by a set of nodes and a set of edges representing relations between the nodes
 * The nodes are SVGElements and the edges can have one of the types defined in @see(svg.context.ElementRelationType)
 * ElementRelationType
 * @author Ivan Guerrero
 */
public class CanvasContext implements Cloneable {
    private List<Graph<SVGElement, SVGEdge>> contexts;
    
    public CanvasContext() {
        contexts = new ArrayList<>();
        addNewLevel();
    }
    
    public void addElement(SVGElement elem, int level) {
        if (contexts.size() <= level)
            addNewLevel();
        contexts.get(level).addVertex(elem);
    }
    
    private void addNewLevel() {
        Graph<SVGElement, SVGEdge> level = new SparseMultigraph<>();
        contexts.add(level);
    }
    
    public void addRelation(SVGElement source, SVGElement target, ElementRelationType relation, int level) {
        if (!SVGConfig.EMPLOY_SELF_RELATIONS_IN_CONTEXT && source.equals(target))
            return;
        SVGEdge edge = new SVGEdge();
        edge.setType(relation);
        edge.setSource(source);
        edge.setTarget(target);
        Graph<SVGElement, SVGEdge> context = contexts.get(level);
        Collection<SVGEdge> edgeSet = context.findEdgeSet(source, target);
        if (!edgeSet.contains(edge)) {
            context.addEdge(edge, source, target);
        }
    }
    
    public void clearContexts() {
        contexts.clear();
        addNewLevel();
    }
    
    public BasicVisualizationServer<SVGElement, SVGEdge> getVisualizationElement(int level) {
        BasicVisualizationServer<SVGElement, SVGEdge> vv;
        Layout<SVGElement, SVGEdge> layout = new CircleLayout<>(contexts.get(level));
        layout.setSize(new Dimension(300,300));
        vv = new BasicVisualizationServer<>(layout);
        vv.setPreferredSize(new Dimension(350,350));
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<SVGElement, String>() {
            @Override
            public String transform(SVGElement i) {
                return i.getID();
            }            
        });
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<SVGEdge, String>() {
            @Override
            public String transform(SVGEdge i) {
                return i.getType().name();
            }
        });
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        
        return vv;
    }
    
    public Graph<SVGElement, SVGEdge> getContextGraph(int level) {
        return (contexts.size() > level) ? contexts.get(level) : null;
    }
    
    public List<Graph<SVGElement, SVGEdge>> getContexts() {
        return contexts;
    }
    
    public int getLevels() {
        return contexts.size();
    }
    
    @Override
    public Object clone() {
        CanvasContext clone = new CanvasContext();
        for (int i=1; i<contexts.size(); i++) {
            Graph<SVGElement, SVGEdge> graph = contexts.get(i);
            Graph<SVGElement, SVGEdge> newGraph = new SparseMultigraph<>();
            for (SVGElement elem : graph.getVertices()) {
                newGraph.addVertex(elem);
            }
            for (SVGEdge edge : graph.getEdges()) {
                newGraph.addEdge(edge, edge.getSource(), edge.getTarget());
            }
            clone.addContext(newGraph);
        }
        return clone;
    }

    private void addContext(Graph<SVGElement, SVGEdge> graph) {
        contexts.add(graph);
    }
    
    @Override
    public String toString() {
        String desc = "Levels: " + contexts.size() + "\n";
        for (Graph<SVGElement, SVGEdge> graph : contexts) {
            desc += graph.toString() + "\n";
        }
        return desc;
    }
}
