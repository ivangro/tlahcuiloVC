package svg.eval;

import edu.uci.ics.jung.graph.Graph;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import svg.context.ElementRelationType;
import svg.context.SVGEdge;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.elems.AbstractUnit;

/**
 * Class to determine the interestingness of a drawing, comparing it with the previous stories
 * @author Ivan Guerrero
 */
public class InterestEvaluator extends AbstractAction implements IEvaluator{
    private SVGRepository repository;
    private NoveltyEvaluator noveltyEvaluator;
    
    public InterestEvaluator(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public IEvaluationResult evaluate(SVGRepository repository) {
        this.repository = repository;
        int insertedSwaps = 0, removedSwaps = 0;
        InterestResult res = new InterestResult();
        for (int i=2; i<repository.getLevels(); i++) {
            Graph<SVGElement, SVGEdge> context1 = repository.getContext().getContextGraph(i-1);
            Graph<SVGElement, SVGEdge> context2 = repository.getContext().getContextGraph(i);
            for (SVGElement elem : context2.getVertices()) {
                //For every element in the new layer, obtain its relationships and compare with the relationships of
                //its elements on the previous layer
                Collection<SVGEdge> inEdges2 = context2.getInEdges(elem);
                Collection<SVGEdge> allEdgesIn1 = new ArrayList<>();
                AbstractUnit unit = (AbstractUnit)elem;
                for (SVGElement simple : unit.getElements()) {
                    Collection<SVGEdge> inEdges1 = context1.getInEdges(simple);
                    if (inEdges1 != null) {
                        allEdgesIn1.addAll(inEdges1);
                        removedSwaps += analyzeRemovedEdges(inEdges2, inEdges1, context2);
                    }
                }
                insertedSwaps += analyzeInsertedEdges(inEdges2, context1, allEdgesIn1);
            }
        }
        res.setInsertedSwaps(insertedSwaps);
        res.setRemovedSwaps(removedSwaps);
        res.setNoveltyEvaluation(getNoveltyEvaluator().getNoveltyResult().getResult());
        return res;
    }
    
    private int analyzeRemovedEdges(Collection<SVGEdge> inEdges2, Collection<SVGEdge> inEdges1, 
                                        Graph<SVGElement, SVGEdge> context2) {
        int swaps = 0;
        
        //Analyze removed symmetries
        for (SVGEdge edge : inEdges1) {
            //Determine if there's an equivalent edge in the new context
            SVGElement target = edge.getTarget();
            SVGElement target2 = findTarget(target, context2.getVertices());
            if (target2 != null && !findEdge(edge.getType(), target2, inEdges2)) {
                swaps++;
            }
            
        }
        
        return swaps;
    }

    private SVGElement findTarget(SVGElement target, Collection<SVGElement> vertices) {
        for (SVGElement elem : vertices) {
            if (elem.containsPoint(target.getCenterX(), target.getCenterY()))
                return elem;
        }
        return null;
    }

    private boolean findEdge(ElementRelationType type, SVGElement target, Collection<SVGEdge> inEdges) {
        for (SVGEdge edge : inEdges) {
            if (edge.getType() == type && edge.getTarget().equals(target))
                return true;
        }
        return false;
    }

    private int analyzeInsertedEdges(Collection<SVGEdge> inEdges2, Graph<SVGElement, SVGEdge> context1, Collection<SVGEdge> allEdgesIn1) {
        int swaps = 0;
        //Determine if there's an edge from any source(edge) -> target(edge) in the previous layer with the same type
        //if no edges found, it's a new edge
        for (SVGEdge edge2 : inEdges2) {
            boolean inserted = false;
            AbstractUnit source = (AbstractUnit)edge2.getSource();
            AbstractUnit target = (AbstractUnit)edge2.getTarget();
            
            checkLayer:
            for (SVGEdge edge1 : allEdgesIn1) {
                if (edge2.getType() == edge1.getType() && 
                    source.containsElement(edge1.getSource()) &&
                    target.containsElement(edge1.getTarget())) {
                    inserted = true;
                    break checkLayer;
                }
            }
            if (!inserted) {
                swaps++;
            }
        }
        return swaps;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        IEvaluationResult result = evaluate(repository);
        JOptionPane.showMessageDialog(null, result, "Evaluation Result", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @param noveltyEvaluator the noveltyEvaluator to set
     */
    public void setNoveltyEvaluator(NoveltyEvaluator noveltyEvaluator) {
        this.noveltyEvaluator = noveltyEvaluator;
    }

    /**
     * @return the noveltyEvaluator
     */
    public NoveltyEvaluator getNoveltyEvaluator() {
        if (noveltyEvaluator == null) {
            noveltyEvaluator = new NoveltyEvaluator(repository);
            noveltyEvaluator.evaluate(repository);
        }
        return noveltyEvaluator;
    }
}