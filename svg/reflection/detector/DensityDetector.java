package svg.reflection.detector;

import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGConfig;
import svg.core.SVGRepository;
import svg.detect.IDetector;
import svg.reflection.Guideline;

/**
 * Detects when the density of the drawing is between the minimum and maximum densities
 * @author Ivan Guerrero
 */
public class DensityDetector implements IReflectionDetector{

    @Override
    public boolean execute(SVGRepository repository) {
        boolean res = false;
        repository.getGuidelines().removeGuideline(Guideline.stopRemoval);
        repository.getGuidelines().removeGuideline(Guideline.stopGeneration);
        repository.getGuidelines().removeGuideline(Guideline.terminateByDensity);
        
        IDetector detector = repository.getDetector(svg.detect.DensityDetector.class);
        detector.detect(repository.getElements(), repository);
        double area = ((svg.detect.DensityDetector)detector).getEmployedAred();
        
        if (area < SVGConfig.MIN_EMPLOYED_AREA) {
            repository.getGuidelines().addGuideline(Guideline.stopRemoval);
            res = true;
        }
        else if (area > SVGConfig.MAX_EMPLOYED_AREA) {
            repository.getGuidelines().addGuideline(Guideline.stopGeneration);
            res = true;
        }
        if (!res) {
            repository.getGuidelines().addGuideline(Guideline.terminateByDensity);
            Logger.getGlobal().log(Level.INFO, "{0} in bounds", detector.getDescription());
        }
        
        return res;
    }

}
