package tlahcuilo;

import config.Configuration;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGConfig;
import svg.core.SVGRepository;
import svg.elems.ElementLoader;
import svg.engagement.SVGAtomStore;
import svg.eval.*;
import svg.gui.action.CreateDrawingAction;
import svg.gui.action.SaveImageAction;

/**
 * Class to run tlahcuilo visual composer
 * @author Ivan Guerrero
 */
public class TlahcuiloVC {
    public static void main(String[] args) {
        if (args.length >= 2) {
            String filename = args[0];
            filename = filename.replaceFirst("./", "");
            int noImages = Integer.parseInt(args[1]);
            if (args.length == 3 && args[2].equals("on"))
                Logger.getGlobal().setLevel(Level.INFO);
            else
                Logger.getGlobal().setLevel(Level.WARNING);
            createVisualComposition(filename, noImages);
        }
        else {
            System.out.println("Usage: TlahcuiloVC filename number_of_images [on]");
        }
    }

    public static void createVisualComposition(String filename, int noImages) {
        //Loads the general configuration
        SVGConfig.BASIC_ELEMS_PATH = "downloads/";
        Configuration.getInstance().loadConfig();
        //Initializes the repository
        SVGRepository repository = SVGRepository.getInstance();
        repository.generateSVGDocument();
        SVGAtomStore.getInstance().loadAtoms(SVGConfig.AtomsFile);
        //Loads the elements available at the given path
        File basicsDir = new File(SVGConfig.BASIC_ELEMS_PATH);
        Logger.getGlobal().log(Level.INFO, "Retrieving basic elems from {0}", basicsDir);
        ElementLoader loader = new ElementLoader();
        loader.loadElements(basicsDir, filename);
        //Creates a new drawing
        NoveltyEvaluator novelty = new NoveltyEvaluator(repository);
        InterestEvaluator interest = new InterestEvaluator(repository);
        CoherenceEvaluator coherence = new CoherenceEvaluator(repository);
        int best = 0;
        double bestEval = 0;
        for (int i=0; i<noImages; i++) {
            try {
                CreateDrawingAction createAction = new CreateDrawingAction(repository, null);
                createAction.actionPerformed(null);
                //evaluate the output
                double noveltyRes = novelty.evaluate(repository).getResult();
                double interestRes = interest.evaluate(repository).getResult();
                double coherenceRes = coherence.evaluate(repository).getResult();
                double eval = noveltyRes + interestRes + coherenceRes;
                Logger.getGlobal().log(Level.WARNING, "Evaluation of output {0}: Novelty:{1}, Interestingness:{2}, Coherence:{3}", new Object[]{eval, noveltyRes, interestRes, coherenceRes});
                if (eval > bestEval) {
                    best = i;
                    bestEval = eval;
                }
                //Saves the drawing into a file
                SaveImageAction saveAction = new SaveImageAction(repository);
                File file = new File(basicsDir, filename + "_output" + i + ".svg");
                if (file.exists())
                    file.delete();
                saveAction.saveFile(file, false);
                repository.generateSVGDocument();
            } catch (Exception ex) {
                Logger.getGlobal().log(Level.WARNING, ex.getMessage());
                i--;
            }
        }
        File bestOutput = new File(basicsDir, filename + "_output" + best + ".svg");
        File output = new File(basicsDir, filename + "_output.svg");
        bestOutput.renameTo(output);
    }
}