package svg.reflection.detector;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGConfig;
import svg.core.SVGRepository;
import svg.detect.design.RhythmResult;
import svg.reflection.Guideline;

/**
 * Class to determine when a drawing can be considered finished depending on 
 * the number of rhythms detected
 * @author Ivan Guerrero
 */
public class RhythmDetector implements IReflectionDetector{
    private int rhythms;
    
    @Override
    public boolean execute(SVGRepository repository) {
        boolean res = true;
        rhythms = 0;
        
        repository.getGuidelines().removeGuideline(Guideline.terminateByRhythm);
        svg.detect.design.RhythmDetector detector = (svg.detect.design.RhythmDetector)repository.getDetector(svg.detect.design.RhythmDetector.class);
        List<RhythmResult> results = detector.getResults();

        if (results.size() > 0) { 
            RhythmResult result = results.get(results.size() -1);
            rhythms = result.patterns.size();

            if (getRhythms() <= SVGConfig.MAX_NUMBER_OF_RHYTHMS && getRhythms() >= SVGConfig.MIN_NUMBER_OF_RHYTHMS) {
                res = false;
                repository.getGuidelines().addGuideline(Guideline.terminateByRhythm);
                Logger.getGlobal().log(Level.INFO, "Number of rhythms in bounds {0}", getRhythms());
            }
            else {
                Logger.getGlobal().log(Level.INFO, "Rhythms out of bounds: {0}", getRhythms());
            }
        }
        
        return res;
    }

    /**
     * @return the rhythms
     */
    public int getRhythms() {
        return rhythms;
    }
}