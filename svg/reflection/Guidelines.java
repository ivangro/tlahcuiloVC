package svg.reflection;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set of guidelines activated during the reflection steps and employed during the generation
 * at engagement steps
 * @author Ivan Guerrero
 */
public class Guidelines {
    private Set<Guideline> guidelines;
    
    public Guidelines() {
        guidelines = new HashSet<>();
    }
    
    public void addGuideline(Guideline guideline) {
        guidelines.add(guideline);
    }
    
    public void removeGuideline(Guideline guideline) {
        guidelines.remove(guideline);
    }
    
    public boolean hasGuideline(Guideline guideline) {
        return guidelines.contains(guideline);
    }

    /**
     * Determines if a drawing can be finished.<br>
     * This happens when the density of the images is between the valid ranges
     * and the drawing has some symmetry
     * @return 
     */
    public boolean canTerminate() {
        boolean res = hasGuideline(Guideline.terminateByDensity) && hasGuideline(Guideline.terminateByRhythm) &&
                      (hasGuideline(Guideline.terminateByHBalance) && hasGuideline(Guideline.terminateByVBalance));
                //&& ((hasGuideline(Guideline.terminateByHSymmetry) ||
                 //hasGuideline(Guideline.terminateByVSymmetry) ||
                 //hasGuideline(Guideline.terminateByRSymmetry)));
        
        if (res) {
            Logger.getLogger(Guidelines.class.getName()).log(Level.INFO, "End of drawing because: Rhythm:{4},Density:{0}\nSymmetry H:{1},V:{2},R:{3}\nBalance H:{5},V:{6}", 
                             new Object[] {hasGuideline(Guideline.terminateByDensity),
                                           hasGuideline(Guideline.terminateByHSymmetry),
                                           hasGuideline(Guideline.terminateByVSymmetry),
                                           hasGuideline(Guideline.terminateByRSymmetry),
                                           hasGuideline(Guideline.terminateByRhythm),
                                           hasGuideline(Guideline.terminateByHBalance),
                                           hasGuideline(Guideline.terminateByVBalance)});
        }
        
        return res;
    }
}