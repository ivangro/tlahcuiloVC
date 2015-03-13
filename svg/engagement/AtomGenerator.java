package svg.engagement;

import config.Configuration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import svg.context.CanvasContext;
import svg.context.ContextAnalyzer;
import svg.context.DifferenceGraph;
import svg.core.SVGAction;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;
import svg.story.StoryLoader;

/**
 * Class to generate atoms based on the differences between contexts of a drawing<br>
 * Procedure:<br>
 * 1. Obtain the contexts for each design action<br>
 * 2. Obtain the differences between each pair of adyacent contexts<br>
 * 3. Obtain the following actions associated to the difference between contexts<br>
 * 4. Create an atom with the initial context and the following actions<br>
 * 5. Add the actions atom to the repository
 * @author Ivan Guerrero
 */
@Deprecated
public class AtomGenerator implements IAtomGenerator {
    @Override
    public void detectAtoms(SVGRepository repository, boolean updateParams) {
        SVGRepository newRepository = SVGRepository.getNewInstance();
        newRepository.generateSVGDocument();
        StoryLoader loader = new StoryLoader();
        List<CanvasContext> contexts = new ArrayList<>();
        List<DifferenceGraph> results = new ArrayList<>();
                
        //Obtains all the parcial contexts
        for (SVGAction action : repository.getActions()) {
            String actionStr = action.getActionForStory();
            loader.parseAction(actionStr, newRepository);
            if (action.isEndOfDesignAction()) {
                newRepository.runDetectors();
                CanvasContext context = (CanvasContext)newRepository.getContext().clone();
                //Logger.getLogger(SaveContextAction.class.getName()).log(Level.INFO, "Context added\n {0}", context.toString());
                contexts.add(context);
            }
        }
        //Updates the parameters of rhythm and employed area for future drawing generation
        if (updateParams) {
            updateConfiguration(newRepository);
        }
        
        //Compares the context i with the context i-1 to obtains their differences
        for (int i=1; i<contexts.size(); i++) {
            CanvasContext context0 = contexts.get(i-1);
            CanvasContext context1 = contexts.get(i);
            int maxLevel = Math.min(context1.getLevels(), context0.getLevels());
            
            for (int j=1; j<maxLevel; j++) {
                DifferenceGraph diff =
                        ContextAnalyzer.getInstance().getDifferenceGraphs(context0.getContextGraph(j), context1.getContextGraph(j));
                diff.setContext(context0.getContextGraph(j));
                diff.setLevel(j);
                results.add(diff);
            }
        }
        
        //Generate the actions with the differences between contexts
        for (DifferenceGraph diff : results) {
            INextAction nextAction = NextActionSetFactory.getInstance().generateNextActionSet(diff);
            SVGAtom atom = new SVGAtom(diff.getContext(), diff.getLevel());
            atom.addNextAction(nextAction);
            SVGAtomStore.getInstance().addAtom(atom);
        }
        
        //Store the atoms with their corresponding next actions
        SVGAtomStore.getInstance().saveAtoms(SVGConfig.AtomsFile);
        JOptionPane.showMessageDialog(null, "Context successfully stored", "Context stored", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateConfiguration(SVGRepository newRepository) {
        Configuration.getInstance().loadConfig();
        List<SVGElement> elements = newRepository.getElements(newRepository.getLevels()-1);
        SVGElement element = elements.get(0);
        int rhythms = element.getRhythms();
        double area = element.getArea();
        double employedArea = area / (SVGConfig.CANVAS_HEIGHT * SVGConfig.CANVAS_WIDTH) * 100;
        if (employedArea > SVGConfig.MAX_EMPLOYED_AREA)
            SVGConfig.MAX_EMPLOYED_AREA = employedArea;
        if (employedArea < SVGConfig.MIN_EMPLOYED_AREA)
            SVGConfig.MIN_EMPLOYED_AREA = employedArea;
        if (rhythms > SVGConfig.MAX_NUMBER_OF_RHYTHMS)
            SVGConfig.MAX_NUMBER_OF_RHYTHMS = rhythms;
        if (rhythms < SVGConfig.MIN_NUMBER_OF_RHYTHMS)
            SVGConfig.MIN_NUMBER_OF_RHYTHMS = rhythms;
        Configuration.getInstance().saveConfig();
    }
}