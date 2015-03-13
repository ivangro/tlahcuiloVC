package svg.engagement;

import edu.uci.ics.jung.graph.Graph;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import svg.context.CanvasContext;
import svg.context.SVGEdge;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.gui.IRefreshData;
import svg.reflection.ReflectionAction;
import svg.story.LevelCurveManager;

/**
 * Class to perform an engagement step for creative generation of a graphical composition
 * @author Ivan Guerrero
 */
public class EngagementAction extends AbstractAction {
    private SVGRepository repository;
    private IRefreshData container;
    private double stepSimilarity = 1;
    private Map<SVGAtom, Graph<SVGElement, SVGEdge>> atomContexts;
    private Set<SVGAtom> selectedAtoms;
    private int LEVEL_OFFSET = 0;
    
    private EngagementUtils utils;
    
    public EngagementAction(SVGRepository repository, IRefreshData container) {
        this.repository = repository;
        this.container = container;
        //Obtain the most similar atom to one of the contexts
        atomContexts = SVGAtomStore.getInstance().getAtomContexts();
        selectedAtoms = new HashSet<>();
        utils = new EngagementUtils();
    }
    
    public void resetDrawing() {
        selectedAtoms = new HashSet<>();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Graph<SVGElement, SVGEdge> contextGraph, atomGraph;
        //Obtain the similar atoms to the context on the highest level of the current drawing
        List<SimilarityResult> similarAtoms = utils.obtainSimilarAtoms(repository, repository.getLevels()-1);
        //Obtain the action atoms asociated to the similar atoms
        SimilarityResult maxSimilarity = null;
        for (SimilarityResult res : similarAtoms) {
            SVGAtom actionAtom = SVGAtomStore.getInstance().findActionAtom(res.atom);
            //If the action atom is null, it implies that the action of the atom was applied to an unexistent level in the current drawing
            if (actionAtom != null) {
                atomGraph = atomContexts.get(actionAtom);
                contextGraph = repository.getContext().getContextGraph(actionAtom.getLevel());
                if (atomGraph != null && contextGraph != null) {
                    //Compare the action atoms with the contexts at their same level
                    SimilarityResult similarity = GraphUtils.calculateSimilarity(atomGraph, contextGraph);
                    similarity.atom = actionAtom;
                    if (maxSimilarity == null || maxSimilarity.similarity < similarity.similarity) {
                        if (!selectedAtoms.contains(similarity.atom))
                            maxSimilarity = similarity;
                    }
                }
            }
        }
        
        //Obtain the most similar action atom and apply its action associated
        if (maxSimilarity != null && maxSimilarity.atom != null) {
            utils.applyAction(repository, maxSimilarity.atom);
            selectedAtoms.add(maxSimilarity.atom);
        }
        else {
            new ReflectionAction(repository, container).breakImpasse();
        }
        
        //Refresh the drawing and the system information if this method is called from the GUI
        if (container != null)
            container.refreshData();
        else
            repository.runDetectors();
    }
    
    public void actionPerformed2(ActionEvent ae) {        
        int followingLevel = -1;
        if (SVGConfig.LEVEL_PREDICTION_ENABLED)
            followingLevel = LevelCurveManager.predictFollowingLevel(repository);
        
        SimilarityResult maxSimilarity = null;
        if (followingLevel >= 0) {
            maxSimilarity = obtainSimilarAtom(followingLevel);
        }
        if (maxSimilarity == null || maxSimilarity.similarity < SVGConfig.MIN_SIMILARITY_FOR_LEVEL)
            maxSimilarity = obtainSimilarAtom();
        SVGAtom similarAtom = maxSimilarity.atom;
        
        //Obtain a suitable set of next actions from the atom
        if (similarAtom != null) {
            List<INextAction> nextActions = similarAtom.getNextActions();
            if (nextActions.size() > 0) {
                Logger.getGlobal().log(Level.INFO, "Applying atom {0} with similarity: {1}", 
                                                   new Object[]{similarAtom.getID(), maxSimilarity.similarity});
                int pos = new Random().nextInt(nextActions.size());
                INextAction next = nextActions.get(pos);
                
                //Send the set of next actions to reflection
                int lastLevel = repository.getCurrentLevel();
                repository.setCurrentLevel(similarAtom.getLevel());
                IEngagementActionPerformer actionPerformer = new EngagementActionPerformer(repository);
                actionPerformer.applyActions(next, maxSimilarity.vertexMap);
                repository.setCurrentLevel(lastLevel);
                
                //Update the similarity of the step
                stepSimilarity *= maxSimilarity.similarity;
                selectedAtoms.add(similarAtom);
            }
        }
        else {
            new ReflectionAction(repository, container).breakImpasse();
        }
        
        //Refresh the drawing and the system information
        container.refreshData();
    }
    
    private SimilarityResult obtainSimilarAtom() {
        //Obtain the current drawing contexts
        CanvasContext context = repository.getContext();
        List<Graph<SVGElement, SVGEdge>> contextGraphs = context.getContexts();
        
        List<ContextSimilarity> similarityAnalysis = new ArrayList<>();
        int level = 0;
        for (Graph<SVGElement, SVGEdge> contextGraph : contextGraphs) {
            ContextSimilarity contextSimilarity = new ContextSimilarity();
            contextSimilarity.contextLevel = level;
            
            if (contextGraph.getVertexCount() > 0) {
                for (SVGAtom atom : atomContexts.keySet()) {
                    if (!selectedAtoms.contains(atom)) {
                        SimilarityResult simResult = GraphUtils.calculateSimilarity(atomContexts.get(atom), contextGraph);
                        contextSimilarity.evaluateResult(simResult, atom);
                    }
                }
            }
            similarityAnalysis.add(contextSimilarity);
            level++;
        }
        
        //Analyze the similarities on every level to determine the most suitable
        SimilarityResult maxSimilarity = analyzeMaxSimilarities(similarityAnalysis);
        return maxSimilarity;
    }
    
    private SimilarityResult obtainSimilarAtom(int suggestedLevel) {
        //Obtain the current drawing contexts
        CanvasContext context = repository.getContext();
        List<Graph<SVGElement, SVGEdge>> contextGraphs = context.getContexts();
        
        List<ContextSimilarity> similarityAnalysis = new ArrayList<>();
        int level = 0;
        for (Graph<SVGElement, SVGEdge> contextGraph : contextGraphs) {
            ContextSimilarity contextSimilarity = new ContextSimilarity();
            contextSimilarity.contextLevel = level;
            
            if (contextGraph.getVertexCount() > 0 && Math.abs(level - suggestedLevel) < LEVEL_OFFSET) {
                for (SVGAtom atom : atomContexts.keySet()) {
                    if (!selectedAtoms.contains(atom)) {
                        SimilarityResult simResult = GraphUtils.calculateSimilarity(atomContexts.get(atom), contextGraph);
                        contextSimilarity.evaluateResult(simResult, atom);
                    }
                }
            }
            similarityAnalysis.add(contextSimilarity);
            level++;
        }
        
        //Analyze the similarities on every level to determine the most suitable
        SimilarityResult maxSimilarity = analyzeMaxSimilarities(similarityAnalysis);
        return maxSimilarity;
    }
    
    /**
     * Obtains the similarity of the atoms employed during the step
     * @return 
     */
    public double getStepSimilarity() {
        return stepSimilarity;
    }

    private SimilarityResult analyzeMaxSimilarities(List<ContextSimilarity> maxSimilarities) {
        SimilarityResult resInLevel = new SimilarityResult();
        SimilarityResult resOverall = new SimilarityResult();
        
        for (ContextSimilarity result : maxSimilarities) {
            if (result.inLevel != null && 
                    result.inLevel.similarity > resInLevel.similarity && 
                    result.inLevel.similarity >= SVGConfig.MIN_SIMILARITY_FOR_LEVEL) {
                resInLevel = result.inLevel;
                break;
            }
            if (result.overall != null && 
                    result.overall.similarity > resOverall.similarity && 
                    result.overall.similarity >= SVGConfig.MIN_SIMILARITY_FOR_LEVEL) {
                resOverall = result.overall;
                break;
            }
        }
        
        if (resInLevel.similarity >= SVGConfig.MIN_SIMILARITY_FOR_LEVEL)
            return resInLevel;
        else if (resOverall.similarity >= SVGConfig.MIN_SIMILARITY_FOR_LEVEL)
            return resOverall;
        else
            return new SimilarityResult();
    }
}