package svg.detect;

import java.text.DecimalFormat;
import java.util.List;
import svg.core.SVGConfig;
import svg.core.SVGElement;
import svg.core.SVGRepository;

/**
 * Determines the amount of used space in a drawing
 * @author Ivan Guerrero
 */
public class DensityDetector implements IDetector {
    private double usedArea, totalArea;
    
    public DensityDetector() {
        totalArea = SVGConfig.CANVAS_HEIGHT * SVGConfig.CANVAS_WIDTH;
    }
    
    @Override
    public void detect(List<List<SVGElement>> elements, SVGRepository repository) {
        usedArea = 0;
        for (SVGElement elem : elements.get(0)) {
            usedArea += elem.getArea();
        }
    }
    
    /**
     * Obtains the area covered by an element
     * @return 
     */
    public double getUsedArea() {
        return usedArea;
    }
    
    /**
     * Obtains the percentage of area covered by an element respect to the total area
     * @return A decimal number between 0 (no elements found) and 1 (full canvas)
     */
    public double getEmployedAred() {
        return usedArea / totalArea * 100;
    }
    
    @Override
    public String getDescription() {
        return "Employed area: " + new DecimalFormat("0.00").format(getEmployedAred()) + "%";
    }
}
