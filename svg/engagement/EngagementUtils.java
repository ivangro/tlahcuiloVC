package svg.engagement;

import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import svg.context.CanvasContext;
import svg.context.SVGEdge;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Class to store the funcionality employed to manually apply engagement actions.
 * @author Ivan Guerrero
 */
public class EngagementUtils {
    private Map<SVGAtom, Graph<SVGElement, SVGEdge>> atomContexts;
    
    public EngagementUtils() {
        //Obtain the most similar atom to one of the contexts
        atomContexts = SVGAtomStore.getInstance().getAtomContexts();
    }
    
    /**
     * Obtains a list with atoms similar to the context of the drawing at the given level
     * @param level The context level
     * @return A list of similar atoms to the context at the given level
     */
    public List<SimilarityResult> obtainSimilarAtoms(SVGRepository repository, int level) {
        CanvasContext context = repository.getContext();
        Graph<SVGElement, SVGEdge> contextGraph = context.getContexts().get(level);
        
        List<SimilarityResult> similarityAnalysis = new ArrayList<>();
        ContextSimilarity contextSimilarity = new ContextSimilarity();
        contextSimilarity.contextLevel = level;

        if (contextGraph.getVertexCount() > 0) {
            for (SVGAtom atom : atomContexts.keySet()) {
                SimilarityResult simResult = GraphUtils.calculateSimilarity(atomContexts.get(atom), contextGraph);
                simResult.atom = atom;
                if (simResult != null && simResult.similarity >= SVGConfig.MIN_SIMILARITY_FOR_LEVEL)
                    similarityAnalysis.add(simResult);
            }
        }

        return similarityAnalysis;
    }
    
    /**
     * Applies the action of the given atom to the current drawing
     * @param repository 
     * @param actionAtom The selected atom from which the action will be retrieved
     */
    public void applyAction(SVGRepository repository, SVGAtom actionAtom) {
        //Compare the action atom with the context at the same level to obtain the mapping
        Graph<SVGElement, SVGEdge> atomGraph = atomContexts.get(actionAtom);
        Graph<SVGElement, SVGEdge> contextGraph = repository.getContext().getContextGraph(actionAtom.getLevel());
        SimilarityResult similarity = GraphUtils.calculateSimilarity(atomGraph, contextGraph);
        //Based on the mapping obtained on the previous step, apply the action linked with the atom
        IEngagementActionPerformer actionPerformer = new EngagementActionPerformer(repository);
        INextAction nextAction = actionAtom.getNextActions().get(0);
        int lastLevel = repository.getCurrentLevel();
        repository.setCurrentLevel(actionAtom.getLevel());
        actionPerformer.applyActions(nextAction, similarity.vertexMap);
        repository.setCurrentLevel(lastLevel);
    }
}