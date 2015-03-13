package svg.engagement;

import edu.uci.ics.jung.graph.Graph;
import java.util.*;
import svg.context.DifferenceGraph;
import svg.context.ElementRelationType;
import svg.context.SVGEdge;
import svg.core.SVGElement;

/**
 * Class to parse the next actions from the existing relations and elements in a difference graph
 * @author Ivan Guerrero
 */
@Deprecated
public class NextActionSetFactory {
    private static NextActionSetFactory instance = new NextActionSetFactory();
    
    private NextActionSetFactory() {}
    
    public static NextActionSetFactory getInstance() {
        return instance;
    }
    
    public NextActionSet generateNextActionSet(DifferenceGraph diff) {
        NextActionSet next = new NextActionSet();
        Set<SVGElement> addedVertices = new HashSet<>();
        Set<SVGEdge> edges = new HashSet<>();
        Graph<SVGElement, SVGEdge> context = diff.getContext();
        
        //Fill the edges set with all the available edges
        for (SVGEdge edge : diff.getInsertion().getEdges()) {
            edges.add(edge);
        }
        
        //While edges is not empty, add an action
        while (!edges.isEmpty()) {
            //If the context contains both source and target nodes, add the action duplicated and remove the edge
            List<SVGEdge> forRemoval = checkVerticesWithTwoNodes(edges, context, addedVertices, next);
            if (forRemoval.size() > 0) {
                for (int i=0; i<forRemoval.size(); i++) {
                    edges.remove(forRemoval.get(i));
                }
            }
            //If the context contains one node, source or target, add the action, remove the edge and add the non existing vertex to the addedList
            else {
                forRemoval = checkVerticesWithOneNode(edges, context, addedVertices, next);
                if (forRemoval.size() > 0) {
                    for (int i=0; i<forRemoval.size(); i++) {
                        edges.remove(forRemoval.get(i));
                    }
                }
                else {
                    checkVerticesWithNoNodes(edges, context, addedVertices, next);
                }
            }
        }
        
        for (SVGElement vertex : diff.getInsertion().getVertices()) {
            if (!addedVertices.contains(vertex))
                next.addAction(createInsertionAction(vertex));
        }
        
        for (SVGElement vertex : diff.getRemoval().getVertices()) {
            //if ((diff.getRemoval().getInEdges(vertex).size() + diff.getRemoval().getOutEdges(vertex).size()) == 0)
            next.addAction(createRemovalAction(vertex));
        }

        return next;
    }

    private List<SVGEdge> checkVerticesWithTwoNodes(Set<SVGEdge> edges, Graph<SVGElement, SVGEdge> context, Set<SVGElement> addedVertices, NextActionSet next) {
        List<SVGEdge> forRemoval = new ArrayList<>();
        for (SVGEdge edge : edges) {
            SVGElement source = edge.getSource();
            SVGElement target = edge.getTarget();
            if ((context.containsVertex(source) || addedVertices.contains(source)) && 
                (context.containsVertex(target)) || addedVertices.contains(target)) {
                next.addAction(createNewAction(edge));
                addedVertices.add(source);
                addedVertices.add(target);
                forRemoval.add(edge);
            }
        }
        return forRemoval;
    }

    private List<SVGEdge> checkVerticesWithOneNode(Set<SVGEdge> edges, Graph<SVGElement, SVGEdge> context, Set<SVGElement> addedVertices, NextActionSet next) {
        List<SVGEdge> forRemoval = new ArrayList<>();
        for (SVGEdge edge : edges) {
            SVGElement source = edge.getSource();
            SVGElement target = edge.getTarget();
            if ((context.containsVertex(source) || addedVertices.contains(source)) ^ 
                (context.containsVertex(target)) || addedVertices.contains(target)) {
                next.addAction(createNewAction(edge));
                addedVertices.add(source);
                addedVertices.add(target);
                forRemoval.add(edge);
            }
        }
        return forRemoval;
    }

    private void checkVerticesWithNoNodes(Set<SVGEdge> edges, Graph<SVGElement, SVGEdge> context, Set<SVGElement> addedVertices, NextActionSet next) {
        for (SVGEdge edge : edges) {
            SVGElement source = edge.getSource();
            SVGElement target = edge.getTarget();
            if ((!context.containsVertex(source) && !addedVertices.contains(source)) && 
                (!context.containsVertex(target)) && !addedVertices.contains(target)) {
                if (!context.containsVertex(source) && !addedVertices.contains(source)) {
                    next.addAction(createInsertionAction(source));
                    addedVertices.add(source);
                }
                else {
                    next.addAction(createInsertionAction(target));
                    addedVertices.add(target);
                }
                break;
            }
        }
    }
    
    private NextAction createNewAction(SVGEdge edge) {
        NextAction nextAction = new NextAction();
        nextAction.setRelationType(edge.getType());
        nextAction.setSource(edge.getSource());
        nextAction.setTarget(edge.getTarget());
        nextAction.setInsertion(true);
        return nextAction;
    }

    private NextAction createInsertionAction(SVGElement elem) {
        NextAction nextAction = new NextAction();
        nextAction.setRelationType(ElementRelationType.NEW_ELEM);
        nextAction.setSource(elem);
        nextAction.setInsertion(true);
        return nextAction;
    }

    private NextAction createRemovalAction(SVGElement elem) {
        NextAction nextAction = new NextAction();
        nextAction.setRelationType(ElementRelationType.NEW_ELEM);
        nextAction.setSource(elem);
        nextAction.setInsertion(false);
        nextAction.setRemoval(true);
        return nextAction;
    }
}