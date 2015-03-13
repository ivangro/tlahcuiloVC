package svg.reflection.detector;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGRepository;
import svg.detect.HSymmetryDetector;
import svg.detect.ISymmetryDetector;
import svg.detect.RadialSymmetryDetector;
import svg.detect.SymmetryResult;
import svg.detect.VSymmetryDetector;
import svg.reflection.Guideline;

/**
 * Detects when the last level of a canvas satisfies with the symmetry criteria
 * to stop drawing
 * @author Ivan Guerrero
 */
public class SymmetryDetector implements IReflectionDetector {

    @Override
    public boolean execute(SVGRepository repository) {
        boolean res = true;
        
        Class[] detectorClasses = {HSymmetryDetector.class, VSymmetryDetector.class, RadialSymmetryDetector.class};
        Guideline[] guidelines = {Guideline.terminateByHSymmetry, Guideline.terminateByVSymmetry, Guideline.terminateByRSymmetry};
        
        for (int i=0; i<detectorClasses.length; i++) {
            repository.getGuidelines().removeGuideline(guidelines[i]);
            ISymmetryDetector detector = (ISymmetryDetector)repository.getDetector(detectorClasses[i]);
            detector.detect(repository.getElements(), repository);
            List<SymmetryResult> results = detector.getResults();
            if (results.size() > 0) {
                SymmetryResult result = results.get(results.size()-1);
                if (result.isMirrored() || result.isSymmetric()) {
                    repository.getGuidelines().addGuideline(guidelines[i]);
                    res = false;
                }
            }
        }
        if (res) {
            Logger.getGlobal().log(Level.INFO, "Missing symmetry");
        }
        return res;
    }

}
