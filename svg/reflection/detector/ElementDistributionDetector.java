package svg.reflection.detector;

import java.util.List;
import svg.actions.design.DActionDistribute;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Class to run the element distribution action during the reflection phase
 * @author Ivan Guerrero
 */
public class ElementDistributionDetector implements IReflectionDetector {

    /**
     * Identifies the groups conforming linear shapes and homogenously distributes their elements.
     * @param repository
     * @return False if no additional elements' shapes can be distributed
     */
    @Override
    public boolean execute(SVGRepository repository) {
        boolean ans = false;
        int level = 1;
        while (level < repository.getLevels()) {
            List<SVGElement> elements = repository.getElements(level);
            for (SVGElement elem : elements) {
                DActionDistribute action = new DActionDistribute();
                action.applyAction(elem, repository);
            }
            level++;
            //repository.runDetectors();
        }
        
        return ans;
    }
}