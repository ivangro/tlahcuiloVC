package svg.reflection.detector;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGRepository;
import svg.detect.IDetector;
import svg.detect.SymmetryResult;
import svg.reflection.Guideline;

/**
 *
 * @author Ivan Guerrero
 */
public class BalanceDetector implements IReflectionDetector {
    private final double MASS_OFFSET = 0.2;

    @Override
    public boolean execute(SVGRepository repository) {
        boolean res = false;
        repository.getGuidelines().removeGuideline(Guideline.terminateByHBalance);
        repository.getGuidelines().removeGuideline(Guideline.terminateByVBalance);
        
        //Determine if the drawing is horizontally balanced
        IDetector detector = repository.getDetector(svg.detect.HSymmetryDetector.class);
        //detector.detect(repository.getElements(), repository);
        List<SymmetryResult> results = ((svg.detect.HSymmetryDetector)detector).getResults();
        if (!results.isEmpty()) {
            double massBalance = results.get(results.size()-1).getMassBalance();
            if (Math.abs(massBalance - 1) <= MASS_OFFSET * 2) {
                repository.getGuidelines().addGuideline(Guideline.terminateByHBalance);
                Logger.getGlobal().log(Level.INFO, "Horizontal balance detected");
                res = true;
            }
        }
        
        //Determine if the drawing is vertically balanced
        detector = repository.getDetector(svg.detect.VSymmetryDetector.class);
        //detector.detect(repository.getElements(), repository);
        results = ((svg.detect.VSymmetryDetector)detector).getResults();
        if (!results.isEmpty()) {
            double massBalance = results.get(results.size()-1).getMassBalance();
            if (Math.abs(massBalance - 1) <= MASS_OFFSET) {
                repository.getGuidelines().addGuideline(Guideline.terminateByVBalance);
                Logger.getGlobal().log(Level.INFO, "Vertical balance detected");
                res = true;
            }
        }
        
        if (!res) {
            Logger.getGlobal().log(Level.INFO, "No balance detected");
        }
        
        return res;
    }
}
