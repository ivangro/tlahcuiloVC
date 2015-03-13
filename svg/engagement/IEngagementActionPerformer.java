package svg.engagement;

import java.util.Map;
import svg.core.SVGElement;

/**
 *
 * @author Ivan Guerrero
 */
public interface IEngagementActionPerformer {

    void applyActions(INextAction actions, Map<SVGElement, SVGElement> verticesMap);

}
