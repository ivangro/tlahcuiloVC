package svg.detect;

import java.util.List;
import svg.context.ElementRelationType;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Determines if the given image is balanced
 * @author Ivan Guerrero
 */
public class BalanceDetector {
    public enum BalanceType {Horizontal, Vertical};
    private int cx, cy;
    private SVGRepository repository;
    
    public BalanceDetector(SVGRepository repository) {
        this.repository = repository;
        cx = SVGConfig.CANVAS_WIDTH / 2;
        cy = SVGConfig.CANVAS_HEIGHT / 2;
    }
    /**
     * Determines when there's a balance relation between two elements<br>
     * Two elements are balanced if their correspondent momentums are equal<br>
     * The momentum of an element is equal to the distance to the center times its area
     * @param upper
     * @param lower 
     */
    public void detectBalance(List<SVGElement> half1, List<SVGElement> half2, BalanceType type, int level) {
        for (SVGElement elem : half1) {
            double momentum = Math.sqrt(Math.pow(elem.getCenterX() - cx, 2) + Math.pow(elem.getCenterY() - cy, 2));
            momentum *= elem.getArea();
            for (SVGElement elem2 : half2) {
                double momentum2 = Math.sqrt(Math.pow(elem2.getCenterX() - cx, 2) + Math.pow(elem2.getCenterY() - cy, 2));
                momentum2 *= elem2.getArea();
                if (Math.abs(momentum2 - momentum) <= (SVGConfig.MAX_BALANCE_OFFSET * elem2.getArea())) {
                    if (type == BalanceType.Horizontal)
                        repository.getContext().addRelation(elem, elem2, ElementRelationType.HB, level);
                    else
                        repository.getContext().addRelation(elem, elem2, ElementRelationType.VB, level);
                }
            }
        }
    }
    
}
