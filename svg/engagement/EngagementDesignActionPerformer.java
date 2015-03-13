package svg.engagement;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * 
 * @author Ivan Guerrero
 */
public class EngagementDesignActionPerformer implements IEngagementActionPerformer {
    private SVGRepository repository;
    private Map<SVGElement, SVGElement> verticesMap;
    
    public EngagementDesignActionPerformer(SVGRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public void applyActions(INextAction actions, Map<SVGElement, SVGElement> verticesMap) {
        Logger.getGlobal().log(Level.INFO, "Applying actions: {0}", actions.toString());
        this.verticesMap = (verticesMap == null) ? new HashMap<SVGElement, SVGElement>() : verticesMap;        
        
        
    }
}
